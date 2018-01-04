package com.example.kubra_pc.ydsmysql;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class QuizFunActivity extends Activity {
    Intent menu = null;
	BufferedReader bReader = null;
	static JSONArray quesList = null;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		Bundle is=getIntent().getExtras();
		final String gelenId=is.getString("idim");
		//Toast.makeText(QuizFunActivity.this,gelenId,Toast.LENGTH_LONG).show();

        Thread thread = new Thread() {
        	public void run() {
		        try {
		        	Thread.sleep(3*1000);
			        finish();
			        loadQuestions();
			        Intent intent = new Intent(QuizFunActivity.this, QuestionActivity.class);
					QuizFunActivity.this.startActivity(intent.putExtra("idim", gelenId));
			        //QuizFunActivity.this.startActivity(intent.putExtra("KR", getIntent().getStringExtra("KR")));
	        	} catch (Exception e) {
	        	}
        	}
        };
        thread.start();

     }
    
    private void loadQuestions() throws Exception {
    	try {
		InputStream questions = this.getBaseContext().getResources()
				.openRawResource(R.raw.questions);
		bReader = new BufferedReader(new InputStreamReader(questions));
		StringBuilder quesString = new StringBuilder();
		String aJsonLine = null;
		while ((aJsonLine = bReader.readLine()) != null) {
			quesString.append(aJsonLine);
		}
		Log.d(this.getClass().toString(), quesString.toString());
		JSONObject quesObj = new JSONObject(quesString.toString());
		quesList = quesObj.getJSONArray("Questions");
		Log.d(this.getClass().getName(),
				"Num Questions " + quesList.length());
    	} catch (Exception e){

    	} finally {
			try {
				bReader.close();
			} catch (Exception e) {
				Log.e("", e.getMessage().toString(), e.getCause());
			}

    	}
    	

    }
    
    public static JSONArray getQuesList() {
    	return quesList;
    }
    
}