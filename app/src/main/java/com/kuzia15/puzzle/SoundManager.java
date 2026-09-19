package com.kuzia15.puzzle;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;

// thanks opensrc
public class SoundManager {

    static SoundManager instance;

    Context context;
    AudioAttributes musicAttributes;
    AudioAttributes effectAttributes;

    MediaPlayer musicPlayer;
    MediaPlayer effectPlayer;

    SoundManager(Context context) {
        this.context = context.getApplicationContext();

        musicAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        effectAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
    }

    public static synchronized SoundManager get(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }
    public synchronized void startBackgroundMusic() {
        if (musicPlayer == null) {
            musicPlayer = MediaPlayer.create(context, R.raw.bg_music, musicAttributes, 0);
            if (musicPlayer == null) {
                return;
            }

            musicPlayer.setLooping(true);
            musicPlayer.setVolume(0.22f, 0.22f);
            musicPlayer.setOnErrorListener((mp, what, extra) -> {
                releaseMusicPlayer();
                return true;
            });
        }

        if (!musicPlayer.isPlaying()) {
            musicPlayer.start();
        }

        setMusicVolume(effectPlayer == null ? 0.22f : 0.07f);
    }

    public void playCorrect() {
        playEffect(R.raw.correct);
    }

    public void playWrong() {
        playEffect(R.raw.wrong);
    }

    public void playClick() {
        playEffect(R.raw.btn_click);
    }

    public void playVictory() {
        startBackgroundMusic();
        playEffect(R.raw.victory);
    }

    synchronized void playEffect(int resourceId) {
        stopCurrentEffect();

        duckMusic();

        MediaPlayer player;
        try {
            player = MediaPlayer.create(context, resourceId, effectAttributes, 0);
        } catch (RuntimeException e) {
            restoreMusic();
            return;
        }

        if (player == null) {
            restoreMusic();
            return;
        }

        effectPlayer = player;
        player.setVolume(1.0f, 1.0f);

        player.setOnCompletionListener(mp -> finishEffect(mp));
        player.setOnErrorListener((mp, what, extra) -> {
            finishEffect(mp);
            return true;
        });

        try {
            player.start();
        } catch (RuntimeException e) {
            finishEffect(player);
        }
    }

    synchronized void finishEffect(MediaPlayer player) {
        if (effectPlayer != player) {
            try {
                player.release();
            } catch (RuntimeException ignored) {
            }
            return;
        }

        try {
            player.release();
        } catch (RuntimeException ignored) {
        }
        effectPlayer = null;
        restoreMusic();
    }

    synchronized void stopCurrentEffect() {
        if (effectPlayer != null) {
            try {
                effectPlayer.stop();
            } catch (RuntimeException ignored) {
            }
            try {
                effectPlayer.release();
            } catch (RuntimeException ignored) {
            }
            effectPlayer = null;
        }

        restoreMusic();
    }

    synchronized void duckMusic() {
        if (musicPlayer != null) {
            try {
                musicPlayer.setVolume(0.07f, 0.07f);
            } catch (RuntimeException ignored) {
            }
        }
    }

    synchronized void restoreMusic() {
        setMusicVolume(0.22f);
    }

    synchronized void setMusicVolume(float volume) {
        if (musicPlayer != null) {
            try {
                musicPlayer.setVolume(volume, volume);
            } catch (RuntimeException ignored) {
            }
        }
    }

    synchronized void releaseMusicPlayer() {
        if (musicPlayer != null) {
            try {
                musicPlayer.release();
            } catch (RuntimeException ignored) {
            }
            musicPlayer = null;
        }
    }

    public synchronized void release() {
        stopCurrentEffect();
        releaseMusicPlayer();
        instance = null;
    }
}
