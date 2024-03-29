package com.example.deepsee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.deepsee.accessibility.SpeechReader;

public class ScreenReaderActivity extends Activity {

    // Reads out stuff that's highlighted.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_to_speech);

        // Get the text:
        CharSequence text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        // Read it out:
        SpeechReader s = new SpeechReader(this.getApplicationContext());
        s.speakOut(text.toString());

        s.shutdown();

        // Back button:
        onKeyDown(KeyEvent.KEYCODE_BACK, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Simulate back button press when back is pressed
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
