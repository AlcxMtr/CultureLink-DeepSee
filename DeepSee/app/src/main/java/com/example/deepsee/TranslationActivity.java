package com.example.deepsee;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TranslationActivity extends AppCompatActivity {

    private TextView highlightedTextView;
    private TextView translatedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_translation);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        highlightedTextView = findViewById(R.id.HighlightedText);
        translatedTextView = findViewById(R.id.TranslationText);

        // Gets the text from highlighted context menu
        CharSequence highlightedText = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        highlightedTextView.setText(highlightedText);

        // Process the highlighted text
        String translatedText = translateText(highlightedText.toString());

        // Set the translated text to the bottom TextView
        translatedTextView.setText(translatedText);

        // Enable drawing over other apps
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
          // TODO: REQUEST PERMISSION FOR DRAWING OVER OTHER APPS!!
        } else {
            // Start the service to draw over other apps
            startDrawOverlaysService();
        }
    }

    // Function to translate text TODO: ADD IN GOOGLE API STUFF!!
    private String translateText(String text) {
        //// Need to connect to Google Translation APIS:
        return "Unimplemented";
    }

    // Method to start the service to draw over other apps
    private void startDrawOverlaysService() {
        // Start the service to draw over other apps
        Intent serviceIntent = new Intent(this, DrawOverlaysService.class);
        serviceIntent.putExtra("activityClass", TranslationActivity.class);
        startService(serviceIntent);
    }
}
