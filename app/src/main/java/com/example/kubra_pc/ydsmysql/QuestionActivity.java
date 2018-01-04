package com.example.kubra_pc.ydsmysql;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;


public class QuestionActivity extends AppCompatActivity{
	private InventorModel inventorModel;
	private Map<String, Object> postValues;
	EditText question = null;
	DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
	String date = df.format(Calendar.getInstance().getTime());
	String keysonuc;
	String dogruSayisi ;
	String yanlisSayisi ;
	String sonuc ;
	String kullanici_ID ;


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

	boolean review = false;
	Button prev, next = null;
	//Kronometre icin tanimlamalar
	private ProgressBar progressBar;
	private TextView textView;
	private Handler handler = new Handler();
	private Button baslat;
	private Button durdur;
	private int score = 0;
	private int progressStatus = 0;
	private boolean suspended = false;//Durdur butonuna basıldığında bu değeri true yapacağız.
	private boolean stopped = false;//Sıfırla butonuna basıldığında bu değeri true yapacağız.

	String veri;
	String key;
	String gelenId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.soru);


		Bundle is=getIntent().getExtras();
		 gelenId=is.getString("idim");
		Toast.makeText(QuestionActivity.this,gelenId,Toast.LENGTH_LONG).show();




		//Toast.makeText(QuestionActivity.this,"Veri:"+gelenIsim,Toast.LENGTH_LONG).show();

		setKronometre().start();
		stopped = false;
		//  baslat.setEnabled(false);
		// durdur.setEnabled(true);

		String newString = null;

		if (getIntent().hasExtra("KR")) {
			newString = getIntent().getStringExtra("KR");
			Log.e("lNKRONOMETRE", "KR exists.");
		} else
			Log.e("lNKRONOMETRE", "KR doesn't exist.");

		if (newString == null) {
			Log.e("lNKRONOMETRE: ", "value is null");
		}

		LinearLayout linearkronometre = (LinearLayout) findViewById(R.id.linearKronometre);

		if (newString != null) {
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
		//baslat = (Button) findViewById(R.id.button1);
		//durdur = (Button) findViewById(R.id.button2);


		progressBar.setMax(300); //ProgressBar'ın Max değerini belirliyoruz.
		progressBar.setIndeterminate(false); //ProgressBar'ın tekrar eden bir animasyon ile çalışmasını önlüyoruz.

		initValues();

	/*	baslat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});
		durdur.setOnClickListener(new OnClickListener() {
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
		});
*/

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


			selected = new int[QuizFunActivity.getQuesList().length()];
			Arrays.fill(selected, -1); //diziyi -1 ile dolduruyor
			correctAns = new int[QuizFunActivity.getQuesList().length()];
			Arrays.fill(correctAns, -1); //diziyi -1 ile dolduruyor

			// indexi sıfır gönderiyor gözden geçirme->false
			this.showQuestion(0, review);

			quizLayout.setVisibility(View.VISIBLE);

		} catch (Exception e) {
			Log.e("Hata nedir", e.getMessage().toString(), e.getCause());
		}

	}


	private void initValues() { //Başlangıç değerlerini set ediyoruz.
		progressStatus = 0;
		progressBar.setProgress(progressStatus);
		textView.setText("0sn / 300sn");
		//baslat.setEnabled(true);
		//durdur.setEnabled(false);


//		durdur.setText("Durdur");
		suspended = false;
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

	private void showQuestion(int qIndex, boolean review) {
		try {
			JSONObject aQues = QuizFunActivity.getQuesList().getJSONObject(qIndex);
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

			//seçenek renkleri siyah
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
			Log.d("", selected[qIndex] + "");
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
			if (quesIndex == (QuizFunActivity.getQuesList().length() - 1))
				next.setEnabled(false);

			if (quesIndex == 0)
				prev.setEnabled(false);

			if (quesIndex > 0)
				prev.setEnabled(true);

			if (quesIndex < (QuizFunActivity.getQuesList().length() - 1))
				next.setEnabled(true);


			// gözden geçirileceği zaman çalışıyor
			if (review) {
				Log.d("review", selected[qIndex] + "" + correctAns[qIndex]);
				;
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


	private OnClickListener finishListener = new OnClickListener() {
		public void onClick(View v) {
			setAnswer();
			stopped = true;
			initValues();
			//Calculate Score


			// correctAns[i] != -1 yani soruya cevap verilmiş ise ve,
			// doğru cevap verilmişse->correctAns[i] == selected[i] , skoru 1 arttır.
			for (int i = 0; i < correctAns.length; i++) {
				if ((correctAns[i] != -1) && (correctAns[i] == selected[i]))
					score++;

			}

			AlertDialog alertDialog;

			alertDialog = new Builder(QuestionActivity.this).create();
			alertDialog.setTitle("Skor");
			alertDialog.setMessage((QuizFunActivity.getQuesList().length()) + "  soruda  " + (score) + "  soru bildin!");


			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cevapları Gör", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					//gözden geçirilecekse  review değişkenine true değeri veriliyor
					review = true;
					quesIndex = 0;
					QuestionActivity.this.showQuestion(0, review);
					score = 0;
					//.setEnabled(false);
					//durdur.setEnabled(false);
				}
			});

			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Kaydet ve Çık", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					//uygulama kapatılıyor

					review = false;
					date=date+"";
					dogruSayisi=score+"";
					yanlisSayisi=((selected.length-(Integer.parseInt(dogruSayisi))) +"");
					sonuc=((Integer.parseInt(dogruSayisi)*10))+"";
					kullanici_ID=gelenId+"";



					finish();
					//Toast.makeText(QuestionActivity.this,"dogru:"+dogruSayisi+"tarih "+date+"yanlis "+yanlisSayisi+"sonuc "+sonuc+"id "+kullanici_ID,Toast.LENGTH_LONG).show();
					skoruKaydet(dogruSayisi,yanlisSayisi,sonuc,date,kullanici_ID);


				}
			});

			alertDialog.show();

		}
	};


	public  void skoruKaydet(String dogruSayisi,String yanlisSayisi,String sonuc,String date,String kullanici_ID){


		String method="skor";
		BackgroundTask backgroundTask = new BackgroundTask(this);
		backgroundTask.execute(method,dogruSayisi,yanlisSayisi,sonuc,date,kullanici_ID);
		finish();
	}


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

	private OnClickListener nextListener = new OnClickListener() {
		public void onClick(View v) {
			setAnswer();
			quesIndex++;

			//Son sorudan sonra değişiklik olmuyor
			if (quesIndex >= QuizFunActivity.getQuesList().length())
				quesIndex = QuizFunActivity.getQuesList().length() - 1;

			showQuestion(quesIndex, review);
		}
	};

	private OnClickListener prevListener = new OnClickListener() {
		public void onClick(View v) {
			setAnswer();

			//ilk soru ise değişiklik olmayacak
			quesIndex--;
			if (quesIndex < 0)
				quesIndex = 0;

			showQuestion(quesIndex, review);
		}
	};

	private void setScoreTitle() {
		this.setTitle("Online Sınav     " + (quesIndex + 1) + "/" + QuizFunActivity.getQuesList().length());
	}


}