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
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    EditText ET_EMAIL,ET_PASS;
    TextView txt;
    String email,sifre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ET_EMAIL = (EditText)findViewById(R.id.email);
        ET_EMAIL.setText("kubra@gmail.com");
                ET_PASS = (EditText)findViewById(R.id.sifre);
        ET_PASS.setText("kubra@gmail.com");
         txt = (TextView)findViewById(R.id.tvdene);
    }

    public void userReg(View view)
    {

        startActivity(new Intent(this,Register.class));
    }
    public void userLogin(View view) throws ExecutionException, InterruptedException {
        email = ET_EMAIL.getText().toString();
        sifre = ET_PASS.getText().toString();

        String method = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);

            String deneme=backgroundTask.execute(method,email,sifre).get();
        //    Toast.makeText(MainActivity.this,"Veri:"+data,Toast.LENGTH_LONG).show();
gec(deneme);


        // startActivity(new Intent(this,HomePage.class));

    }
    public void gec(String gelenIsım){
        Intent i=new Intent(MainActivity.this,HomePage.class);
        i.putExtra("isim",gelenIsım);
        startActivity(i);
       // Toast.makeText(MainActivity.this,gelenIsım,Toast.LENGTH_LONG).show();
    }
}
