package com.alkhalilbg.ocr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    final static int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openFromStorage = findViewById(R.id.open_storage);
        openFromStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browseStorage = new Intent(Intent.ACTION_GET_CONTENT);
                browseStorage.setType("application/pdf");
                browseStorage.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(browseStorage, "Select PDF"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedPDF = data.getData();
            Intent intent = new Intent(this, ViewPDF.class);
            intent.putExtra(ViewPDF.EXTRA_FILE_URI, selectedPDF.toString());
            startActivity(intent);
            Log.d("OCR", "FileUri: " + selectedPDF.toString());
        }
    }
}
