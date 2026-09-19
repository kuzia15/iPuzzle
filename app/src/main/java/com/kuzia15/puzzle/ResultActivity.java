package com.kuzia15.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {

    static String EXTRA_CORRECT = "correct";
    static String EXTRA_TOTAL = "total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        SoundManager.get(this).playVictory();

        int correct = getIntent().getIntExtra(EXTRA_CORRECT, 0);
        int total   = getIntent().getIntExtra(EXTRA_TOTAL, 10);

        TextView scoreText   = findViewById(R.id.scoreText);
        TextView answersText = findViewById(R.id.answersText);

        ImageView starNearL = findViewById(R.id.starnearl);
        ImageView starNearR = findViewById(R.id.starnearr);

        int starss;

        if (total == 10) {
            if (correct >= 8)      starss = 3;
            else if (correct >= 5) starss = 2;
            else                   starss = 1;
        } else if (total == 15) {
            if (correct >= 12)     starss = 3;
            else if (correct >= 8) starss = 2;
            else                   starss = 1;
        } else {
            if (correct >= 16)     starss = 3;
            else if (correct >= 10) starss = 2;
            else                    starss = 1;
        }

        scoreText.setText(getString(R.string.result_score, starss));
        answersText.setText(getString(R.string.result_answers, correct, total));

        starNearR.setVisibility(starss >= 2 ? View.VISIBLE : View.GONE);
        starNearL.setVisibility(starss == 3 ? View.VISIBLE : View.GONE);

        findViewById(R.id.againButton).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            Intent i = new Intent(this, PuzzleActivity.class);
            i.putExtra(PuzzleActivity.EXTRA_DIFFICULTY, total);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });
    }
}