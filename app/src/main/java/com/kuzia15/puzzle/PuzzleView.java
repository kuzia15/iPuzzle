package com.kuzia15.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PuzzleView extends View {

    interface Listener {
        void onPuzzleSolved();
    }

    int N = 4;
    int PIECE = 2;
    int TRAY_CELL = 60;
    int HINT_MS = 200;
    int PLACE_TOLERANCE = 1;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint hintForStupid = new Paint(Paint.ANTI_ALIAS_FLAG);

    ArrayList<Part> parts = new ArrayList<>();
    boolean[][] placed = new boolean[N][N];
    ArrayList<Level> levels = LevelManager.createLevels();

    Level level;
    Listener listener;
    int levelIndex;
    int totalLevels = 10;

    float boardLeft, boardTop, cellSize;
    Part dragging;
    Part hint;
    float touchX, touchY;

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable hideHint = () -> {
        hint = null;
        invalidate();
    };

    Bitmap picture;

    public PuzzleView(Context context) {
        super(context);

        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(2f);
        gridPaint.setColor(0x66000044);

        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setStrokeWidth(2f);
        edgePaint.setColor(0x00000055);

        hintForStupid.setStyle(Paint.Style.FILL);
        hintForStupid.setColor(0x6681C784);

        loadLevel();
    }
    public void setDifficulty(int totalLevels) {
        this.totalLevels = totalLevels;
        this.levelIndex = 0;
        this.levels = LevelManager.pickLevels(totalLevels);
        loadLevel();
    }

    void loadLevel() {
        level = levels.get(levelIndex);
        picture = BitmapFactory.decodeResource(getResources(), level.image);
        buildParts();
    }

    void nextLevel() {
        levelIndex++;

        if (levelIndex >= levels.size())
            levelIndex = 0;

        loadLevel();
    }

    Question getQuestion() {
        return new Question(level.answer, level.fact, level.variants);
    }

    int getLevelNumber() {
        return levelIndex + 1;
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    void buildParts() {
        parts.clear();

        for (int y = 0; y < N; y++)
            Arrays.fill(placed[y], false);

        hint = null;

        for (int y = 0; y < N; y += PIECE) {
            for (int x = 0; x < N; x += PIECE)
                parts.add(new Part(x, y));
        }

        Collections.shuffle(parts);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float size = Math.min(getWidth() - 40f, getHeight() * 0.55f);
        cellSize = size / N;
        boardLeft = (getWidth() - size) / 2f;
        boardTop = 20f;

        drawBoard(canvas);
        drawTray(canvas);
        drawDragging(canvas);
    }

    void drawBoard(Canvas canvas) {
        if (hint != null) {
            float left = boardLeft + hint.originX * cellSize;
            float top = boardTop + hint.originY * cellSize;
            RectF rect = new RectF(
                    left,
                    top,
                    left + PIECE * cellSize,
                    top + PIECE * cellSize);
            canvas.drawRect(rect, hintForStupid);
        }

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                float left = boardLeft + x * cellSize;
                float top = boardTop + y * cellSize;
                RectF rect = new RectF(left, top, left + cellSize, top + cellSize);

                if (placed[y][x])
                    drawPictureCell(canvas, x, y, rect);

                canvas.drawRect(rect, gridPaint);
            }
        }
    }

    void drawTray(Canvas canvas) {
        layoutTray();

        for (Part p : parts) {
            if (p == dragging)
                continue;

            drawPart(canvas, p, p.trayX, p.trayY, TRAY_CELL);
        }
    }

    void drawDragging(Canvas canvas) {
        if (dragging == null)
            return;

        float s = cellSize;
        float px = touchX - PIECE * s / 2f;
        float py = touchY - PIECE * s / 2f;

        paint.setAlpha(220);
        drawPart(canvas, dragging, px, py, s);
        paint.setAlpha(255);
    }

    void drawPart(Canvas canvas, Part p, float x, float y, float cell) {
        for (int yy = 0; yy < PIECE; yy++) {
            for (int xx = 0; xx < PIECE; xx++) {
                RectF dst = new RectF(
                        x + xx * cell,
                        y + yy * cell,
                        x + (xx + 1) * cell,
                        y + (yy + 1) * cell);
                drawPictureCell(canvas, p.originX + xx, p.originY + yy, dst);
            }
        }

        canvas.drawRect(x, y, x + PIECE * cell, y + PIECE * cell, edgePaint);
    }

    void drawPictureCell(Canvas canvas, int srcX, int srcY, RectF dst) {
        if (picture == null) {
            paint.setColor(0xff4caf50);
            canvas.drawRect(dst, paint);
            return;
        }

        int bw = picture.getWidth();
        int bh = picture.getHeight();

        Rect src = new Rect(
                srcX * bw / N,
                srcY * bh / N,
                (srcX + 1) * bw / N,
                (srcY + 1) * bh / N);

        canvas.drawBitmap(picture, src, dst, paint);
    }

    void layoutTray() {
        float x = 16f;
        float y = boardTop + N * cellSize + 20f;
        float w = PIECE * TRAY_CELL;

        int i = 0;

        for (Part p : parts) {
            if (p == dragging)
                continue;

            p.trayX = x;
            p.trayY = y;
            x += w + 12f;

            if (++i % 2 == 0) {
                x = 16f;
                y += w + 10f;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            layoutTray();

            for (int i = parts.size() - 1; i >= 0; i--) {
                Part p = parts.get(i);

                if (p.hit(x, y)) {
                    dragging = p;
                    touchX = x;
                    touchY = y;
                    showHint(p);
                    invalidate();
                    return true;
                }
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE && dragging != null) {
            touchX = x;
            touchY = y;
            invalidate();
            return true;
        } else if (e.getAction() == MotionEvent.ACTION_UP && dragging != null) {
            fixPlace();
            return true;
        }

        return true;
    }

    void showHint(Part p) {
        hint = p;
        handler.removeCallbacks(hideHint);
        handler.postDelayed(hideHint, HINT_MS);
    }

    void fixPlace() {
        Part p = dragging;

        float w = PIECE * cellSize;
        float h = PIECE * cellSize;
        int gx = Math.round((touchX - w / 2f - boardLeft) / cellSize);
        int gy = Math.round((touchY - h / 2f - boardTop) / cellSize);

        if (Math.abs(gx - p.originX) <= PLACE_TOLERANCE &&
                Math.abs(gy - p.originY) <= PLACE_TOLERANCE) {
            for (int yy = 0; yy < PIECE; yy++) {
                for (int xx = 0; xx < PIECE; xx++)
                    placed[p.originY + yy][p.originX + xx] = true;
            }

            parts.remove(p);

            if (parts.isEmpty() && listener != null)
                listener.onPuzzleSolved();
        }

        dragging = null;
        hint = null;
        handler.removeCallbacks(hideHint);
        invalidate();
    }

    class Part {
        final int originX;
        final int originY;

        float trayX;
        float trayY;

        Part(int x, int y) {
            originX = x;
            originY = y;
        }

        boolean hit(float x, float y) {
            return x >= trayX
                    && x <= trayX + PIECE * TRAY_CELL
                    && y >= trayY
                    && y <= trayY + PIECE * TRAY_CELL;
        }
    }
}