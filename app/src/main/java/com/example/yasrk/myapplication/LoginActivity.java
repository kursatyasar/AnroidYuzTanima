package com.example.yasrk.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by yasrk on 14/10/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private FaceServiceClient faceServiceClient = new FaceServiceRestClient("https://westcentralus.api.cognitive.microsoft.com/face/v1.0", "c356ed87486b45cbbc78c0d674a3e8b9");
    private final int PICK_IMAGE = 1;
    private ProgressDialog detectionProgressDialog;
    Face[] yuzler = new Face[2];
    int i = 0;
    VerifyResult deneme;
    Bitmap bitmap;
    ImageView imageView;

    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button b1 = (Button)findViewById(R.id.button_photo);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
    }
    public void onButtonRegister(View reg){
        if(reg.getId()==R.id.button_register){
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);

        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap)data.getExtras().get("data");
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        detectAndFrame(bitmap);
    }
    public void onButtonLogin (View z){
        if(z.getId()==R.id.button_login){
            EditText user = (EditText) findViewById(R.id.TFusername);
            String userstr = user.getText().toString();
            EditText pass = (EditText) findViewById(R.id.FTpassword);
            String passstr = pass.getText().toString();


            boolean sonuc= helper.checkUser(userstr,passstr);
            if(sonuc==true){
               /* Intent i = new Intent(LoginActivity.this, Userpage.class);
                i.putExtra("Username",userstr);
                startActivity(i);*/

                Toast tempz = Toast.makeText(LoginActivity.this, "Giris islemi basarili", Toast.LENGTH_SHORT);
                tempz.show();
                //Bitmap bm = helper.getImage(userstr);
                //detectAndFrame(bm);
            }
            else if(sonuc==false){
                Toast temp = Toast.makeText(LoginActivity.this, "Your username or password don't match!!", Toast.LENGTH_SHORT);
                temp.show();
            }

        }


    }


    private void detectAndFrame(final Bitmap imageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());


        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {
            @Override
            protected Face[] doInBackground(InputStream... params) {
                try {
                    publishProgress("Detecting...");
                    Face[] result = faceServiceClient.detect(
                            params[0],
                            true,         // returnFaceId
                            false,        // returnFaceLandmarks
                            null           // returnFaceAttributes: a string like "age, gender"
                    );
                    if (result == null) {
                        publishProgress("Detection Finished. Nothing detected");
                        return null;
                    }
                    //publishProgress(
                    //String.format("Detection Finished. %d face(s) detected", result.length));

                    yuzler[i] = result[0];
                    i++;

                    if (i == 2) {
                        deneme = faceServiceClient.verify(yuzler[0].faceId, yuzler[1].faceId);
                        if (deneme.isIdentical) {
                            //yuzu tanidi
                            detectionProgressDialog.setMessage("tanidi..");

                            /////////////
                        } else
                            detectionProgressDialog.setMessage("HATA..");

                    }
                    return result;
                } catch (Exception e) {
                    publishProgress("Detection failed");
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                if (i >= 1)
                    detectionProgressDialog.show();
            }

            @Override
            protected void onProgressUpdate(String... progress) {

                detectionProgressDialog.setMessage(progress[0]);
            }

            @Override
            protected void onPostExecute(Face[] result) {

                detectionProgressDialog.dismiss();
                if (result == null) return;
                //ImageView imageView = (ImageView)findViewById(R.id.imageView1);
                //imageView.setImageBitmap(drawFaceRectanglesOnBitmap(imageBitmap, result));
                //imageBitmap.recycle();

            }
        };
        detectTask.execute(inputStream);
    }


    private static Bitmap drawFaceRectanglesOnBitmap(Bitmap originalBitmap, Face[] faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        int stokeWidth = 2;
        paint.setStrokeWidth(stokeWidth);
        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }
        return bitmap;
    }
}
