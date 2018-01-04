package com.example.kubra_pc.ydsmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Essy on 21.03.2017.
 */

public class HomePage extends AppCompatActivity {
   Button kendinidene,cikis,onlineSinav,btnIstatistik;

    private final String TAG = getClass().getSimpleName();

public String gelenIsim;
    public static String kullaniciadi;
    public static Integer kullannicipuan;
    TextView tvdene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anasayfa);

        //Bir AuthStateListener dinleyicisi oluşturuyoruz ve bu dinleyici onAuthStateChanged bölümünü çalıştırır.
        // Buradaki getCurrentUser metodu ile kullanıcı verilerine ulaşabilmekteyiz.

        kendinidene= (Button) findViewById(R.id.btnKendiniDene);
        cikis= (Button) findViewById(R.id.btncikis);
        tvdene=(TextView) findViewById(R.id.tvdene);
        onlineSinav =(Button) findViewById(R.id.btnOnlineSinav);
        btnIstatistik= (Button) findViewById(R.id.btnistatistik);

        Bundle is=getIntent().getExtras();
        gelenIsim=is.getString("isim");

        tvdene.setText(gelenIsim);

        onlineSinav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(HomePage.this,QuizFunActivity.class);
                i.putExtra("idim",gelenIsim.substring(0,gelenIsim.indexOf(" ")));
                startActivity(i);
               // startActivity(new Intent(HomePage.this ,QuizFunActivity.class).putExtra("KR", "0"));

            }
        });
        kendinidene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomePage.this,Bekle.class).putExtra("KR", "1"));

            }
        });
        cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                startActivity(new Intent(HomePage.this, MainActivity.class));
                finish();


            }
        });

       btnIstatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(HomePage.this,SonucActivity.class);
                i.putExtra("idim",gelenIsim.substring(0,gelenIsim.indexOf(" ")));
                startActivity(i);

              //  Intent Intent=new Intent(getApplicationContext(),SonucActivity.class);
               // startActivity(Intent);
            }
        });

    }

}