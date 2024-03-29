package com.example.deepsee;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

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


        String text = (String) highlightedText;

        if(text == null) {
            Toast.makeText(TranslationActivity.this, "Please highlight some text.", Toast.LENGTH_SHORT).show();
            this.finish();
        }


        String[] langOptions = {"French", "Spanish", "Chinese", "Hindi", "Arabic", "German", "Italian", "Russian", "Japanese"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TranslationActivity.this);
        builder.setTitle("Choose Language to Translate To");
        builder.setItems(langOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int l) {

                translatedTextView.setText("Loading...");

                if (l == 0) {
                    translateText(text, 17);
                } else if (l == 1) {
                    translateText(text, 13);
                } else if (l == 2) {
                    translateText(text, 58);
                } else if (l == 3) {
                    translateText(text, 22);
                } else if (l == 4) {
                    translateText(text, 1);
                } else if (l == 5) {
                    translateText(text, 9);
                } else if (l == 6) {
                    translateText(text, 28);
                } else if (l == 7) {
                    translateText(text, 44);
                } else if (l == 8) {
                    translateText(text, 29);
                }
            }
        });
        builder.show();






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
    private void translateText(String text, int lang) {
        //// Need to connect to Google Translation APIS:


        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(lang)
                        .build();
        final FirebaseTranslator englishGermanTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);


        //Toast.makeText(TranslationActivity.this, "Loading...", Toast.LENGTH_SHORT).show();

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)

                                englishGermanTranslator.translate(text)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<String>() {
                                                    @Override
                                                    public void onSuccess(@NonNull String translatedT) {
                                                        // Translation successful.
                                                        translatedTextView.setText(translatedT);
                                                    }
                                                })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Error.
                                                        // ...
                                                        Toast.makeText(TranslationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                });




                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                                Toast.makeText(TranslationActivity.this, "Model could not be downloaded, please try again.", Toast.LENGTH_SHORT).show();

                            }
                        });

        //return "error";
    }

    // Method to start the service to draw over other apps
    private void startDrawOverlaysService() {
        // Start the service to draw over other apps
        Intent serviceIntent = new Intent(this, DrawOverlaysService.class);
        serviceIntent.putExtra("activityClass", TranslationActivity.class);
        startService(serviceIntent);
    }
}
