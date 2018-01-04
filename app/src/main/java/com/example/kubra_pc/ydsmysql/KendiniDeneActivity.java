package com.example.kubra_pc.ydsmysql;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Essy on 27.04.2017.
 */

public class KendiniDeneActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    EditText question = null;
    RadioButton answer1 = null;
    RadioButton answer2 = null;
    RadioButton answer3 = null;
    RadioButton answer4 = null;
    RadioButton answer5 = null;
    RadioGroup answers = null;
    Button finish = null;
    int quesIndex = 0;
    int selected[] = null;
    int correctAns[] = null;
    int questionId[]=null;
    boolean review =false;
    Button prev, next = null;

    private ProgressBar progressBar;
    private TextView textView;
    private Handler handler = new Handler();
    private Button baslat;
    private Button durdur;
    private int score = 0;
    private int progressStatus = 0;
    private boolean suspended = false;//Durdur butonuna basıldığında bu değeri true yapacağız.
    private boolean stopped = false;//Sıfırla butonuna basıldığında bu değeri true yapacağız.




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soru);

        String newString = null;

        if(getIntent().hasExtra("KR"))
        {
            newString = getIntent().getStringExtra("KR");
            Log.e("lNKRONOMETRE", "KR exists.");
        }
        else
            Log.e("lNKRONOMETRE", "KR doesn't exist.");

        if(newString == null)
        {
            Log.e("lNKRONOMETRE: ", "value is null");
        }

        LinearLayout linearkronometre=(LinearLayout) findViewById(R.id.linearKronometre);

        if(newString != null) {
            if (newString.equals("1")) {
                linearkronometre.setVisibility(View.GONE);
            } else {
                linearkronometre.setVisibility(View.VISIBLE);
            }
        }

        TableLayout quizLayout = (TableLayout) findViewById(R.id.quizLayout);
        quizLayout.setVisibility(View.INVISIBLE);


        // Bileşenleri eşleştiriyoruz.
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        textView = (TextView) findViewById(R.id.textView2);



        progressBar.setMax(300); //ProgressBar'ın Max değerini belirliyoruz.
        progressBar.setIndeterminate(false); //ProgressBar'ın tekrar eden bir animasyon ile çalışmasını önlüyoruz.

        initValues();

      /*  baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setKronometre().start();
                stopped=false;
                baslat.setEnabled(false);
                durdur.setEnabled(true);

            }
        });
        durdur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (suspended) {
                    suspended = false;
                    durdur.setText("Durdur");
                    prev.setEnabled(true);
                    next.setEnabled(true);
                } else {
                    suspended = true;
                    durdur.setText("Devam Et");
                    prev.setEnabled(false);
                    next.setEnabled(false);
                }
            }
        });*/


        try {
            question = (EditText) findViewById(R.id.question);
            answer1 = (RadioButton) findViewById(R.id.a0);
            answer2 = (RadioButton) findViewById(R.id.a1);
            answer3 = (RadioButton) findViewById(R.id.a2);
            answer4 = (RadioButton) findViewById(R.id.a3);
            answer5 = (RadioButton) findViewById(R.id.a4);
            answers = (RadioGroup) findViewById(R.id.answers);

            finish = (Button) findViewById(R.id.finish);
            finish.setOnClickListener(finishListener);
            prev = (Button) findViewById(R.id.Prev);
            prev.setOnClickListener(prevListener);
            next = (Button) findViewById(R.id.Next);
            next.setOnClickListener(nextListener);

            questionId = new int[Bekle.getQuesList().length()];
            Arrays.fill(questionId, -1); //diziyi -1 ile dolduruyor
            selected = new int[Bekle.getQuesList().length()];
            Arrays.fill(selected, -1); //diziyi -1 ile dolduruyor
            correctAns = new int[Bekle.getQuesList().length()];
            Arrays.fill(correctAns, -1); //diziyi -1 ile dolduruyor

            // indexi sıfır gönderiyor gözden geçirme->false
            this.showQuestion(0, review);

            quizLayout.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Log.e("Hata nedir", e.getMessage().toString(), e.getCause());
        }

    }
    private void initValues() { //Başlangıç değerlerini set ediyoruz.
        progressStatus=0;
        progressBar.setProgress(progressStatus);
        textView.setText("0sn / 300sn");
      //  baslat.setEnabled(true);
        //durdur.setEnabled(false);


       // durdur.setText("Durdur");
        suspended=false;
    }

    private Thread setKronometre() {
        return new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 60) {
                    while (suspended) { //Eğer kronometre durdurulduysa bekle
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (stopped) // Eğer kronometre sıfırlandıysa işlemi sonlandır
                        break;
                    progressStatus += 1;
                    // yeni değeri ekranda göster ve progressBar'a set et.
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(progressStatus + "sn / "
                                    + progressBar.getMax() + "sn");
                        }
                    });

                    try {
                        // Sleep for 1 second.
                        // Just to display the progress slowly
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showQuestion(int qIndex,boolean review) {
        try {
            JSONObject aQues = Bekle.getQuesList().getJSONObject(qIndex);

            String quesId = aQues.getString("Questionid");

            questionId[qIndex] = Integer.parseInt(quesId);

         /*
  Random rnd=new Random();
            questionId[qIndex] = rnd.nextInt(Integer.parseInt(quesId));

            int randomObjectIndex = rnd.nextInt(3-0) + 0;

            JSONObject selectedRandomObject = quesId.getJSONObject(randomObjectIndex);*/
            Toast.makeText(getApplicationContext(), quesId, Toast.LENGTH_LONG).show();
            //soru alınıyor


            String quesValue = aQues.getString("Question");
            if (correctAns[qIndex] == -1) {
                // dogru cevabın indeksi
                String correctAnsStr = aQues.getString("CorrectAnswer");
                //doğru cevapların bulunduğu diziye aktarılıyor.
                correctAns[qIndex] = Integer.parseInt(correctAnsStr);
            }
            //soru set ediliyor
            question.setText(quesValue.toCharArray(), 0, quesValue.length());
            answers.check(-1); // radio butonların seçimi siliniyor
            //answers.clearCheck(); yukardaki ile aynı işi görür

            //seçenek renkleri beyaz
            answer1.setTextColor(Color.BLACK);
            answer2.setTextColor(Color.BLACK);
            answer3.setTextColor(Color.BLACK);
            answer4.setTextColor(Color.BLACK);
            answer5.setTextColor(Color.BLACK);




            //Json dizisi alınıyor (seçeneklerin olduğu)
            JSONArray ansList = aQues.getJSONArray("Answers");

            //sırayla seçenekler set ediliyor
            String aAns = ansList.getJSONObject(0).getString("Answer");
            answer1.setText(aAns.toCharArray(), 0, aAns.length());
            aAns = ansList.getJSONObject(1).getString("Answer");
            answer2.setText(aAns.toCharArray(), 0, aAns.length());
            aAns = ansList.getJSONObject(2).getString("Answer");
            answer3.setText(aAns.toCharArray(), 0, aAns.length());
            aAns = ansList.getJSONObject(3).getString("Answer");
            answer4.setText(aAns.toCharArray(), 0, aAns.length());
            aAns = ansList.getJSONObject(4).getString("Answer");
            answer5.setText(aAns.toCharArray(), 0, aAns.length());

            //????? normal soruda -1 oluyor -> selected[qIndex]
            Log.d("",selected[qIndex]+"");
            if (selected[qIndex] == 0)
                answers.check(R.id.a0);
            if (selected[qIndex] == 1)
                answers.check(R.id.a1);
            if (selected[qIndex] == 2)
                answers.check(R.id.a2);
            if (selected[qIndex] == 3)
                answers.check(R.id.a3);
            if (selected[qIndex] == 4)
                answers.check(R.id.a4);

            setScoreTitle();

            //ileri ve geri butonların etkin olup olmaması belirleniyor.
            if (quesIndex == (Bekle.getQuesList().length()-1))
                next.setEnabled(false);

            if (quesIndex == 0)
                prev.setEnabled(false);

            if (quesIndex > 0)
                prev.setEnabled(true);

            if (quesIndex < (Bekle.getQuesList().length()-1))
                next.setEnabled(true);


            // gözden geçirileceği zaman çalışıyor
            if (review) {
                Log.d("review",selected[qIndex]+""+correctAns[qIndex]);;
                if (selected[qIndex] != correctAns[qIndex]) {
                    if (selected[qIndex] == 0)
                        answer1.setTextColor(Color.RED);
                    if (selected[qIndex] == 1)
                        answer2.setTextColor(Color.RED);
                    if (selected[qIndex] == 2)
                        answer3.setTextColor(Color.RED);
                    if (selected[qIndex] == 3)
                        answer4.setTextColor(Color.RED);
                    if (selected[qIndex] == 4)
                        answer5.setTextColor(Color.RED);
                }
                if (correctAns[qIndex] == 0)
                    answer1.setTextColor(Color.GREEN);
                if (correctAns[qIndex] == 1)
                    answer2.setTextColor(Color.GREEN);
                if (correctAns[qIndex] == 2)
                    answer3.setTextColor(Color.GREEN);
                if (correctAns[qIndex] == 3)
                    answer4.setTextColor(Color.GREEN);
                if (correctAns[qIndex] == 4)
                    answer5.setTextColor(Color.GREEN);
            }
        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.getMessage(), e.getCause());
        }
    }


    private View.OnClickListener finishListener = new View.OnClickListener() {
        public void onClick(View v) {
            setAnswer();
            stopped=true;
            initValues();
            //Calculate Score


            // correctAns[i] != -1 yani soruya cevap verilmiş ise ve,
            // doğru cevap verilmişse->correctAns[i] == selected[i] , skoru 1 arttır.
            for(int i=0; i<correctAns.length; i++){
                if ((correctAns[i] != -1) && (correctAns[i] == selected[i]))
                    score++;
            }

            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(KendiniDeneActivity.this).create();
            alertDialog.setTitle("Skor");
            alertDialog.setMessage((Bekle.getQuesList().length())+"  soruda  "+(score) +"  soru bildin!");

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Tekrar Dene", new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialog, int which) {
                    //tekrar edilecekse  review değişkenine false değeri veriliyor
                    score=0;
                    review = false;
                    quesIndex=0;
                    KendiniDeneActivity.this.showQuestion(0, review);
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cevapları Gör", new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialog, int which) {
                    //gözden geçirilecekse  review değişkenine true değeri veriliyor
                    review = true;
                    quesIndex=0;
                    KendiniDeneActivity.this.showQuestion(0, review);
                    score=0;
                   // baslat.setEnabled(false);
                    //durdur.setEnabled(false);
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Çık", new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialog, int which) {
                    //uygulama kapatılıyor

                    review = false;

                 //   skoruKaydet(score);

                    finish();
                }
            });

            alertDialog.show();

        }
    };

 /*   private void skoruKaydet(int skor){

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String uid = firebaseAuth.getCurrentUser().getUid();

        Log.e("skoruKaydet: ", "UID: " + uid);

        mDatabase.child("users").child(uid).child("skor").setValue(skor);
    }*/

    private void setAnswer() {
        if (answer1.isChecked())
            selected[quesIndex] = 0;
        if (answer2.isChecked())
            selected[quesIndex] = 1;
        if (answer3.isChecked())
            selected[quesIndex] = 2;
        if (answer4.isChecked())
            selected[quesIndex] = 3;
        if (answer5.isChecked())
            selected[quesIndex] = 4;

        Log.d("", Arrays.toString(selected));
        Log.d("", Arrays.toString(correctAns));

    }

    private View.OnClickListener nextListener = new View.OnClickListener() {
        public void onClick(View v) {

            setAnswer();
            quesIndex++;

            //Son sorudan sonra değişiklik olmuyor
            if (quesIndex >= Bekle.getQuesList().length())
                quesIndex = Bekle.getQuesList().length() - 1;

            showQuestion(quesIndex,review);
        }
    };

    private View.OnClickListener prevListener = new View.OnClickListener() {
        public void onClick(View v) {
            setAnswer();

            //ilk soru ise değişiklik olmayacak
            quesIndex--;
            if (quesIndex < 0)
                quesIndex = 0;

            showQuestion(quesIndex,review);
        }
    };

    private void setScoreTitle() {
        this.setTitle("Online Sınav     " + (quesIndex+1)+ "/" + Bekle.getQuesList().length());
    }


}
