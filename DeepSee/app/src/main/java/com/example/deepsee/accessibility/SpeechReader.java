package com.example.deepsee.accessibility;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

// This is a class that provides a method to read any given string out loud
// It may later be expanded to support different types of voices

public class SpeechReader {

    private final TextToSpeech speech;
    Bundle settings;

    Float volume;

    public SpeechReader(Context context) {
        // Can choose to modify volume later
        // Set volume to 140% for now
        volume = 1.4f;
        speech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Text-to-speech initialization successful
                    speech.setLanguage(Locale.getDefault());
                } else {
                    // Text-to-speech initialization failed
                    System.err.println("Text to speech initialization failed");
                }
            }
        });
        // Bundle of settings to pass into the speech engine
        settings = new Bundle();
        settings.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, volume);
    }

    // Method to convert string to audio and read it
    public void speakOut(String text) {
        speech.speak(text, TextToSpeech.QUEUE_FLUSH, settings, null);
    }

    // Releasing the resources of the tts engine
    public void shutdown() {
        if (speech != null) {
            speech.stop();
            speech.shutdown();
        }
    }
}
