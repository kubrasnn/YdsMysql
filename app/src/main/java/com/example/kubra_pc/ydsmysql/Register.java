package com.example.kubra_pc.ydsmysql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by KUBRA-PC on 9.5.2017.
 */

public class Register extends Activity {
    EditText ET_NAME,ET_EMAIL,ET_PASS;
    String ad,email,sifre;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        ET_NAME = (EditText)findViewById(R.id.ad);
        ET_EMAIL = (EditText)findViewById(R.id.email);
        ET_PASS = (EditText)findViewById(R.id.sifre);

    }

    public void userReg(View view)
    {
        ad = ET_NAME.getText().toString();
        email = ET_EMAIL.getText().toString();
        sifre = ET_PASS.getText().toString();

        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,ad,email,sifre);

        finish();

    }
}
