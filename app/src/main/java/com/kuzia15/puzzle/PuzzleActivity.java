package com.kuzia15.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PuzzleActivity extends Activity implements PuzzleView.Listener {

    public static String EXTRA_DIFFICULTY = "difficulty";
    public static int DIFF_EASY   = 10;
    public static int DIFF_MEDIUM = 15;
    public static int DIFF_HARD   = 20;

    int TOTAL_ROUNDS = DIFF_EASY;

    PuzzleView puzzleView;
    TextView levelText;
    ConstraintLayout questionPanel;
    TextView factText;
    Button nextButton;

    Button a1, a2, a3, back;
    Button[] answers;

    int stars;
    int rounds;
    int correctAnswers;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        SoundManager.get(this).startBackgroundMusic();

        TOTAL_ROUNDS = getIntent().getIntExtra(EXTRA_DIFFICULTY, DIFF_EASY);

        FrameLayout container = findViewById(R.id.gameContainer);
        levelText = findViewById(R.id.levelText);
        questionPanel = findViewById(R.id.questionPanel);
        a1 = findViewById(R.id.answer1);
        a2 = findViewById(R.id.answer2);
        a3 = findViewById(R.id.answer3);
        factText = findViewById(R.id.factText);
        nextButton = findViewById(R.id.nextButton);

        answers = new Button[]{a1, a2, a3};

        puzzleView = new PuzzleView(this);
        puzzleView.setDifficulty(TOTAL_ROUNDS);
        puzzleView.setListener(this);
        container.addView(puzzleView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        View.OnClickListener click = v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            checkAnswer((Button) v);
        };
        for (Button button : answers) {
            button.setOnClickListener(click);
            SoundManager.get(this).playClick();
        }

        nextButton.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            SoundManager.get(this).playClick();
            nextRound();
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            SoundManager.get(this).playClick();
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });

        updateHeader();
    }

    void updateHeader() {
        levelText.setText(getString(R.string.game_level,
                puzzleView.getLevelNumber(), TOTAL_ROUNDS));
    }

    @Override
    public void onPuzzleSolved() {
        stars++;
        updateHeader();
        showQuestion();
    }

    void showQuestion() {
        question = puzzleView.getQuestion();

        List<String> options = new ArrayList<>(Arrays.asList(question.options));
        Collections.shuffle(options);

        for (int i = 0; i < answers.length; i++) {
            Button button = answers[i];
            button.setText(options.get(i));
            button.setEnabled(true);
            button.setBackgroundResource(R.drawable.btn_answer);
            button.setTextColor(getResources().getColor(R.color.text_primary, null));
        }

        factText.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        questionPanel.setVisibility(View.VISIBLE);
    }

    void checkAnswer(Button b) {
        if (question == null)
            return;

        for (Button button : answers)
            button.setEnabled(false);

        rounds++;

        int textColor = getResources().getColor(R.color.text_on_color, null);

        if (b.getText().toString().equals(question.correct)) {
            correctAnswers++;
            SoundManager.get(this).playCorrect();

            b.setBackgroundResource(R.drawable.btn_answer_right);
            b.setTextColor(textColor);

            Toast.makeText(this, getString(R.string.answer_correct), Toast.LENGTH_SHORT).show();
            questionPanel.postDelayed(this::nextRound, 700);
        } else {
            SoundManager.get(this).playWrong();
            b.setBackgroundResource(R.drawable.btn_answer_wrong);
            b.setTextColor(textColor);

            for (Button button : answers) {
                if (button.getText().toString().equals(question.correct)) {
                    button.setBackgroundResource(R.drawable.btn_answer_right);
                    button.setTextColor(textColor);
                    break;
                }
            }

            factText.setText(question.fact);
            factText.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    void nextRound() {
        questionPanel.setVisibility(View.GONE);
        question = null;

        if (rounds >= TOTAL_ROUNDS) {
            finishGame();
            return;
        }

        puzzleView.nextLevel();
        updateHeader();
    }

    void finishGame() {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra(ResultActivity.EXTRA_CORRECT, correctAnswers);
        i.putExtra(ResultActivity.EXTRA_TOTAL, TOTAL_ROUNDS);
        startActivity(i);
        finish();
    }
}