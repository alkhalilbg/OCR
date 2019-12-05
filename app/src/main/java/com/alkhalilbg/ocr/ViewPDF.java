package com.alkhalilbg.ocr;


import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;


import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;


public class ViewPDF extends AppCompatActivity {


    final static String EXTRA_FILE_URI = "FileUri";
    final static String EXTRA_VIEW_TYPE = "ViewType";

    PDFView mPDFView;
    Bitmap mBitmap;

    RelativeLayout mRelativeLayout;
    TextView mTextView;

    String mDate1;
    String mDate2;
    String mSerialNumber1;
    String mSerialNumber1Values;
    String mSerialNumber2;
    String mSerialNumber2Values;
    String mSerialNumber3;
    String mSerialNumber3Values;
    String mSerialNumber4;
    String mSerialNumber4Values;
    String mSerialNumber5;
    String mSerialNumber5Values;
    String mSerialNumber6;
    String mSerialNumber6Values;
    String mCity1;
    String mCity2;
    String mCity3;
    String mAge1;
    String mAge2;
    String mAge3;
    String mAge4;
    String mAge5;
    String mAge6;
    String mAge7;
    String mAge8;
    String mCatName1;
    String mCatName2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        mPDFView = findViewById(R.id.pdf_viewer);

        mRelativeLayout = findViewById(R.id.relative_layout_pdf);

        mTextView = findViewById(R.id.text_view_ocr);
        mTextView.setMovementMethod(new ScrollingMovementMethod());


        final Intent intent = getIntent();

        String uriString = intent.getStringExtra(EXTRA_FILE_URI);

        if (intent != null) {
            String viewType = intent.getStringExtra(EXTRA_VIEW_TYPE);
            if (viewType != null || TextUtils.isEmpty(viewType)) {
                Uri pdfFileUri = Uri.parse(uriString);
                mPDFView.fromUri(pdfFileUri)
                        .password(null)
                        .defaultPage(1)
                        .enableSwipe(true)
                        .swipeHorizontal(true)
                        .enableDoubletap(false)
                        .onDraw(new OnDrawListener() {
                            @Override
                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                            }
                        })
                        .onDrawAll(new OnDrawListener() {
                            @Override
                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                            }
                        })
                        .onPageError(new OnPageErrorListener() {
                            @Override
                            public void onPageError(int page, Throwable t) {
                                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                            }
                        })
                        .onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {

                            }
                        })
                        .onTap(new OnTapListener() {
                            @Override
                            public boolean onTap(MotionEvent e) {
                                return false;
                            }
                        })
                        .onRender(new OnRenderListener() {
                            @Override
                            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {

                            }
                        })
                        .enableAnnotationRendering(true)
                        .invalidPageColor(Color.WHITE)
                        .load();


            }
        }

        Button b = findViewById(R.id.ocr_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTextView.setText("");
                mBitmap = Bitmap.createBitmap(mRelativeLayout.getWidth(), mRelativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mBitmap);
                mRelativeLayout.draw(canvas);
                runTextRecognizer(mBitmap);
                Log.d("ocr", "Text: " + mDate1);

            }
        });

    }

    private void runTextRecognizer(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTextRecognitionResult(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(this, "No text found", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements1 = lines.get(0).getElements();
                mDate1 = elements1.get(0).getText();


                try {
                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(mDate1);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                    mDate1 = dateFormat.format(date1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<FirebaseVisionText.Element> elements2 = lines.get(lines.size() - 1).getElements();
                mDate2 = elements2.get(elements2.size() - 1).getText();

                try {
                    Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(mDate2);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                    mDate2 = dateFormat.format(date2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<FirebaseVisionText.Element> elements3 = lines.get(1).getElements();
                mSerialNumber1 = elements3.get(elements3.size() - 1).getText();

                mSerialNumber1Values = "Value 1:" + mSerialNumber1.charAt(0);
                mSerialNumber1Values += mSerialNumber1.charAt(1);
                mSerialNumber1Values += mSerialNumber1.charAt(2);

                mSerialNumber1Values += " , Value 2:" + mSerialNumber1.charAt(4);
                mSerialNumber1Values += mSerialNumber1.charAt(5);
                mSerialNumber1Values += mSerialNumber1.charAt(6);

                mSerialNumber1Values += " , Value 3:" + mSerialNumber1.charAt(8);
                mSerialNumber1Values += mSerialNumber1.charAt(9);
                mSerialNumber1Values += mSerialNumber1.charAt(10);

                mSerialNumber1Values += " , Value 4:" + mSerialNumber1.charAt(12);
                mSerialNumber1Values += mSerialNumber1.charAt(13);
                mSerialNumber1Values += mSerialNumber1.charAt(14);


                List<FirebaseVisionText.Element> elements4 = lines.get(4).getElements();
                mSerialNumber2 = elements4.get(0).getText();

                mSerialNumber2Values = "Value 1:" + mSerialNumber2.charAt(0);
                mSerialNumber2Values += mSerialNumber2.charAt(1);
                mSerialNumber2Values += mSerialNumber2.charAt(2);

                mSerialNumber2Values += " , Value 2:" + mSerialNumber2.charAt(4);
                mSerialNumber2Values += mSerialNumber2.charAt(5);
                mSerialNumber2Values += mSerialNumber2.charAt(6);

                mSerialNumber2Values += " , Value 3:" + mSerialNumber2.charAt(8);
                mSerialNumber2Values += mSerialNumber2.charAt(9);
                mSerialNumber2Values += mSerialNumber2.charAt(10);

                mSerialNumber2Values += " , Value 4:" + mSerialNumber2.charAt(12);
                mSerialNumber2Values += mSerialNumber2.charAt(13);
                mSerialNumber2Values += mSerialNumber2.charAt(14);


                List<FirebaseVisionText.Element> elements5 = lines.get(5).getElements();
                mSerialNumber3 = elements5.get(0).getText();

                mSerialNumber3Values = "Value 1:" + mSerialNumber3.charAt(0);
                mSerialNumber3Values += mSerialNumber3.charAt(1);
                mSerialNumber3Values += mSerialNumber3.charAt(2);

                mSerialNumber3Values += " , Value 2:" + mSerialNumber3.charAt(4);
                mSerialNumber3Values += mSerialNumber3.charAt(5);
                mSerialNumber3Values += mSerialNumber3.charAt(6);

                mSerialNumber3Values += " , Value 3:" + mSerialNumber3.charAt(8);
                mSerialNumber3Values += mSerialNumber3.charAt(9);
                mSerialNumber3Values += mSerialNumber3.charAt(10);

                mSerialNumber3Values += " , Value 4:" + mSerialNumber3.charAt(12);
                mSerialNumber3Values += mSerialNumber3.charAt(13);
                mSerialNumber3Values += mSerialNumber3.charAt(14);

                List<FirebaseVisionText.Element> elements6 = lines.get(6).getElements();
                mSerialNumber4 = elements6.get(0).getText();

                mSerialNumber4Values = "Value 1:" + mSerialNumber4.charAt(0);
                mSerialNumber4Values += mSerialNumber4.charAt(1);
                mSerialNumber4Values += mSerialNumber4.charAt(2);

                mSerialNumber4Values += " , Value 2:" + mSerialNumber4.charAt(4);
                mSerialNumber4Values += mSerialNumber4.charAt(5);
                mSerialNumber4Values += mSerialNumber4.charAt(6);

                mSerialNumber4Values += " , Value 3:" + mSerialNumber4.charAt(8);
                mSerialNumber4Values += mSerialNumber4.charAt(9);
                mSerialNumber4Values += mSerialNumber4.charAt(10);

                mSerialNumber4Values += " , Value 4:" + mSerialNumber4.charAt(12);
                mSerialNumber4Values += mSerialNumber4.charAt(13);
                mSerialNumber4Values += mSerialNumber4.charAt(14);

                List<FirebaseVisionText.Element> elements7 = lines.get(7).getElements();
                mSerialNumber5 = elements7.get(0).getText();

                mSerialNumber5Values = "Value 1:" + mSerialNumber5.charAt(0);
                mSerialNumber5Values += mSerialNumber5.charAt(1);
                mSerialNumber5Values += mSerialNumber5.charAt(2);

                mSerialNumber5Values += " , Value 2:" + mSerialNumber5.charAt(4);
                mSerialNumber5Values += mSerialNumber5.charAt(5);
                mSerialNumber5Values += mSerialNumber5.charAt(6);

                mSerialNumber5Values += " , Value 3:" + mSerialNumber5.charAt(8);
                mSerialNumber5Values += mSerialNumber5.charAt(9);
                mSerialNumber5Values += mSerialNumber5.charAt(10);

                mSerialNumber5Values += " , Value 4:" + mSerialNumber5.charAt(12);
                mSerialNumber5Values += mSerialNumber5.charAt(13);
                mSerialNumber5Values += mSerialNumber5.charAt(14);

                List<FirebaseVisionText.Element> elements8 = lines.get(8).getElements();
                mSerialNumber6 = elements8.get(0).getText();

                mSerialNumber6Values = "Value 1:" + mSerialNumber6.charAt(0);
                mSerialNumber6Values += mSerialNumber6.charAt(1);
                mSerialNumber6Values += mSerialNumber6.charAt(2);

                mSerialNumber6Values += " , Value 2:" + mSerialNumber6.charAt(4);
                mSerialNumber6Values += mSerialNumber6.charAt(5);
                mSerialNumber6Values += mSerialNumber6.charAt(6);

                mSerialNumber6Values += " , Value 3:" + mSerialNumber6.charAt(8);
                mSerialNumber6Values += mSerialNumber6.charAt(9);
                mSerialNumber6Values += mSerialNumber6.charAt(10);

                mSerialNumber6Values += " , Value 4:" + mSerialNumber6.charAt(12);
                mSerialNumber6Values += mSerialNumber6.charAt(13);
                mSerialNumber6Values += mSerialNumber6.charAt(14);

                List<FirebaseVisionText.Element> elements9 = lines.get(1).getElements();
                mCity1 = elements9.get(5).getText();

                List<FirebaseVisionText.Element> elements10 = lines.get(2).getElements();
                mCity2 = elements10.get(elements10.size() - 7).getText();

                List<FirebaseVisionText.Element> elements11 = lines.get(lines.size() - 1).getElements();
                mCity3 = elements11.get(elements11.size() - 3).getText();

                List<FirebaseVisionText.Element> elements12 = lines.get(0).getElements();
                mAge1 = elements12.get(elements12.size() - 4).getText();
                mAge1 += " " + elements12.get(elements12.size() - 3).getText();
                mAge1 += " " + elements12.get(elements12.size() - 2).getText();

                List<FirebaseVisionText.Element> elements13 = lines.get(2).getElements();
                mAge2 = elements13.get(8).getText();
                mAge2 += " " + elements13.get(9).getText();
                mAge2 += " " + elements13.get(10).getText();

                List<FirebaseVisionText.Element> elements14 = lines.get(10).getElements();
                mAge3 = elements14.get(0).getText();
                mAge3 += " " + elements14.get(1).getText();
                mAge3 += " " + elements14.get(2).getText();

                List<FirebaseVisionText.Element> elements15 = lines.get(11).getElements();
                mAge4 = elements15.get(0).getText();
                mAge4 += " " + elements15.get(1).getText();
                mAge4 += " " + elements15.get(2).getText();

                List<FirebaseVisionText.Element> elements16 = lines.get(11).getElements();
                mAge5 = elements16.get(3).getText();
                mAge5 += " " + elements16.get(4).getText();
                mAge5 += " " + elements16.get(5).getText();

                List<FirebaseVisionText.Element> elements17 = lines.get(12).getElements();
                mAge6 = elements17.get(0).getText();
                mAge6 += " " + elements17.get(1).getText();
                mAge6 += " " + elements17.get(2).getText();

                List<FirebaseVisionText.Element> elements18 = lines.get(13).getElements();
                mAge7 = elements18.get(0).getText();
                mAge7 += " " + elements18.get(1).getText();
                mAge7 += " " + elements18.get(2).getText();

                List<FirebaseVisionText.Element> elements19 = lines.get(14).getElements();
                mAge8 = elements19.get(0).getText();
                mAge8 += " " + elements19.get(1).getText();
                mAge8 += " " + elements19.get(2).getText();

                List<FirebaseVisionText.Element> elements20 = lines.get(0).getElements();
                mCatName1 = elements20.get(elements20.size() - 7).getText();

                List<FirebaseVisionText.Element> elements21 = lines.get(2).getElements();
                mCatName2 = elements21.get(5).getText();

            }
        }


        mTextView.setText("  a. Dates: " + "\n\n  1- " + mDate1 + "\n  2- " + mDate2 + "\n\n\n"
                + "  b. Serial Numbers:" + "\n\n  1- " + mSerialNumber1Values + "\n  2- "
                + mSerialNumber2Values + "\n  3- "
                + mSerialNumber3Values + "\n  4- "
                + mSerialNumber4Values + "\n  5- "
                + mSerialNumber5Values + "\n  6- "
                + mSerialNumber6Values + "\n\n\n"
                + "  c. Cities:" + "\n\n  1- " + mCity1 + "\n  2- "
                + mCity2 + "\n  3- "
                + mCity3 + "\n\n\n"
                + "  d. Ages:" + "\n\n  1- " + mAge1 + "\n  2- "
                + mAge2 + "\n  3- "
                + mAge3 + "\n  4- "
                + mAge4 + "\n  5- "
                + mAge5 + "\n  6- "
                + mAge6 + "\n  7- "
                + mAge7 + "\n  8- "
                + mAge8 + "\n\n\n"
                + "  e. Cat Names:" + "\n\n  1- " + mCatName1 + "\n  2- "
                + mCatName2);


    }


}
