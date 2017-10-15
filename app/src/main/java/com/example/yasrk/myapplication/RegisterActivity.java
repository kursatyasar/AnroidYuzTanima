package com.example.yasrk.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by yasrk on 14/10/2017.
 */

public class RegisterActivity extends Activity {
    private final int PICK_IMAGE = 1;
    Bitmap bitmap;
    ImageView imageView;
    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        Button button1=(Button)findViewById(R.id.b_photo);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent gallIntent = new Intent(Intent.ACTION_GET_CONTENT);
                gallIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(gallIntent, "Select Picture"), PICK_IMAGE);*/
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,PICK_IMAGE);


            }
        });
    }
    public void buttonsignin(View k){
        if(k.getId()==R.id.button_Sin){

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }



    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        bitmap = (Bitmap)data.getExtras().get("data");
        imageView = (ImageView) findViewById(R.id.imageView3);
        imageView.setImageBitmap(bitmap);






    }
    public void buttonsginup(View v){
        if(v.getId()==R.id.button_Sup){
            EditText username = (EditText) findViewById(R.id.registerUsername);
            EditText pass1 = (EditText) findViewById(R.id.registerPass);
            EditText pass2 = (EditText) findViewById(R.id.registerCpass);


            String usernamestr = username.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();

            boolean sonuc= helper.checkUser(usernamestr);
            if(!pass1str.equals(pass2str)){

                Toast pass = Toast.makeText(RegisterActivity.this, "Passwords are no same", Toast.LENGTH_SHORT);
                pass.show();
            }
            else if(sonuc==true){
                Toast pass3 = Toast.makeText(RegisterActivity.this, "This Username is used", Toast.LENGTH_SHORT);
                pass3.show();
            }
            else{
                //insert the details to database

                       // Contact a = new Contact();
                        //a.setUsername(usernamestr);
                        //a.setPassword(pass1str);
                        helper.insertContact(usernamestr,pass1str,imageViewToByte(imageView));
                        Toast pass4 = Toast.makeText(RegisterActivity.this, "User is saved", Toast.LENGTH_SHORT);
                        pass4.show();


            }



        }


    }

    private byte[] imageViewToByte(ImageView image) {

        Bitmap bitma = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitma.compress(Bitmap.CompressFormat.PNG, 100,stream);
        byte [] bytearray = stream.toByteArray();
        return bytearray;
    }


}
