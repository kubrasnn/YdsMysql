package com.example.kubra_pc.ydsmysql;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
/**
 * Created by KUBRA-PC on 9.5.2017.
 */

public class BackgroundTask extends AsyncTask<String,Void,String>{
  //  AlertDialog alertDialog;
    TextView txt;
    Context ctx;
    BackgroundTask()
    {
        this.ctx =ctx;
    }



    BackgroundTask(Context ctx)
    {
        this.ctx =ctx;
    }
    @Override
    protected void onPreExecute() {
   //     alertDialog = new AlertDialog.Builder(ctx).create();
    //    alertDialog.setTitle("Giriş Bilgileri....");

    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        String reg_url = "http://10.0.3.2:8080/yds/register.php";
        String login_url = "http://10.0.3.2:8080/yds/login2.php";
        String skor_url = "http://10.0.3.2:8080/yds/skor.php";
        String method = params[0];
        if (method.equals("register")) {
            String ad = params[1];
            String email = params[2];
            String sifre = params[3];
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("ad", "UTF-8") + "=" + URLEncoder.encode(ad, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("sifre", "UTF-8") + "=" + URLEncoder.encode(sifre, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                //httpURLConnection.connect();
                httpURLConnection.disconnect();
                return "Kayıt Başarılı...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       else if (method.equals("skor")) {

            String dogruSayisi = params[1];
            String yanlisSayisi = params[2];
            String puan = params[3];
            String tarih = params[4];
             String kullanici_ID = params[5];
            try {
                URL url = new URL(skor_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("dogruSayisi", "UTF-8") + "=" + URLEncoder.encode(dogruSayisi.toString(), "UTF-8") + "&" +
                        URLEncoder.encode("yanliSayisi", "UTF-8") + "=" + URLEncoder.encode(yanlisSayisi.toString(), "UTF-8") + "&" +
                        URLEncoder.encode("sonuc", "UTF-8") + "=" + URLEncoder.encode(puan.toString(), "UTF-8")+ "&" +
                        URLEncoder.encode("tarih", "UTF-8") + "=" + URLEncoder.encode(tarih.toString(), "UTF-8")+"&" +
                        URLEncoder.encode("kullanici_ID", "UTF-8") + "=" + URLEncoder.encode(kullanici_ID.toString(), "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                //httpURLConnection.connect();
                httpURLConnection.disconnect();
                return "Skor Kaydedildi.";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("login"))
        {

            String email = params[1];
            String sifre = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("sifre","UTF-8")+"="+URLEncoder.encode(sifre,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
               response=response.substring(3);
                return response;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return  response;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Kayıt Başarılı..."))
        {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();

        }


        else if (result.trim().length()>0)
        {

        //    alertDialog.dismiss();
            Log.e("asdsf",result);

        }
        else
        {
          //  alertDialog.setMessage(result);
           // alertDialog.show();
        }

    }



}

