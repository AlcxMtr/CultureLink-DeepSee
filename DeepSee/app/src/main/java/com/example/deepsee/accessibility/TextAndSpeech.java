package com.example.deepsee.accessibility;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deepsee.R;

// This activity is a test of the text-to-speech feature
// It also serves as an example for how to use the SpeechReader class
public class TextAndSpeech extends AppCompatActivity {

    private SpeechReader speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_to_speech);

        speech = new SpeechReader(this);
    }

    @Override
    protected void onDestroy() {
        // Need to shutdown the TextToSpeech engine within Speech object
        speech.shutdown();
        super.onDestroy();
    }

    public String saveText(View view) {
        EditText editText = findViewById(R.id.message);
        return editText.getText().toString();
    }

    public void readText(View view) {
        String userInput = saveText(view);
        speech.speakOut(userInput);
    }
}
