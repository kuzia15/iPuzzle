package com.kuzia15.puzzle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SoundManager.get(this).startBackgroundMusic();

        ImageView play = findViewById(R.id.playButton);
        play.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            SoundManager.get(this).playClick();
            showDifficultyDialog();
        });

        ImageView more = findViewById(R.id.moreButton);
        more.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            SoundManager.get(this).playClick();
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/kuzia15"));
            startActivity(intent);
        });
    }

    void showDifficultyDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_selectlevel);
        dialog.setCancelable(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.findViewById(R.id.btnEasy).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            dialog.dismiss();
            startGame(PuzzleActivity.DIFF_EASY);
        });

        dialog.findViewById(R.id.btnMedium).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            dialog.dismiss();
            startGame(PuzzleActivity.DIFF_MEDIUM);
        });

        dialog.findViewById(R.id.btnHard).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            dialog.dismiss();
            startGame(PuzzleActivity.DIFF_HARD);
        });

        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
            dialog.dismiss();
        });

        dialog.show();
    }

    void startGame(int difficulty) {
        Intent i = new Intent(this, PuzzleActivity.class);
        i.putExtra(PuzzleActivity.EXTRA_DIFFICULTY, difficulty);
        startActivity(i);
    }
}