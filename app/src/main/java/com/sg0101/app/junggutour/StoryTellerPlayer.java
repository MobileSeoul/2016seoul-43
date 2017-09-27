package com.sg0101.app.junggutour;

import android.media.MediaPlayer;

/**
 * Created by lee on 2016-08-28.
 */

public class StoryTellerPlayer {

    MediaPlayer mp;
    private static StoryTellerPlayer instance = null;
    private StoryTellerPlayer() { }

    public static synchronized  StoryTellerPlayer getInstance() {
        if (instance == null) {
            instance = new StoryTellerPlayer();
        }
        return instance;
    }

}
