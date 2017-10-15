package com.example.yasrk.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by yasrk on 14/10/2017.
 */

public class Userpage extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpage);
        String username = getIntent().getStringExtra("Username");

        TextView tv =(TextView) findViewById(R.id.textView3);
        tv.setText(username);
    }


}
