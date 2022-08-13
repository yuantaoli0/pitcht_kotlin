package com.example.test.audio;

import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;

/**
 * Created by Charle on 2018-06-22.
 */

public class MediaManager {
    public static final String TAG = "MediaManager";
    private static MediaPlayer mMediaPlayer;
    private static boolean isPause;
    private static String currentFilePath;

    public static void playSound(String filePath, final MediaPlayer.OnCompletionListener onCompletionListener) {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mMediaPlayer.reset();
                        return false;
                    }
                });
            } else {
                mMediaPlayer.reset();
            }
            if (!TextUtils.isEmpty(currentFilePath) && !currentFilePath.equals(filePath)) {
                mMediaPlayer.reset();
            }
            currentFilePath = filePath;
            mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            release();
            e.printStackTrace();
        }
    }

    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    public static void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public static MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }
}
