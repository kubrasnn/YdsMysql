package com.example.kubra_pc.ydsmysql;


        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;

        import android.util.Log;
        import android.view.View;
        import android.widget.ProgressBar;
        import android.widget.Toast;


        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonArrayRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

public class SonucActivity extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {

    //Creating a List of superheroes
    private List<Skorlar> listSuperHeroes;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;

    private String gelenId ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sonuc);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
      //  recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        listSuperHeroes = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        Bundle is=getIntent().getExtras();
         gelenId=is.getString("idim");
        Toast.makeText(SonucActivity.this, gelenId,Toast.LENGTH_LONG).show();

        //Calling method to get data to fetch data

        getDataFromDatabase(gelenId);

        Log.e("sonuctivity.oncreate", listSuperHeroes.toString());

        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(this);

        //initializing our adapter
        adapter = new CardAdapter(listSuperHeroes, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(String kullanciId) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        // JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + String.valueOf(requestCount),
        String url = Config.DATA_URL + kullanciId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseJsonArrayData to parse the json response
                        parseJsonArrayData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("onErrorResponse", error.getMessage());
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(SonucActivity.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get data from the web api
    private void getDataFromDatabase(String kulaniciId) {
        //Adding the method to the queue by calling the method getDataFromServer
        JsonArrayRequest rq = getDataFromServer(kulaniciId);

        // Log.e("getDataFromDatabase", rq.getTag().toString());

        requestQueue.add(rq);
        //Incrementing the request counter
        requestCount++;
    }

    //This method will parse json data
    private void parseJsonArrayData(JSONArray array) {

        Log.e("arraylength", "" + array.length());

        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            Skorlar superHero = new Skorlar();
            JSONObject json = null;

            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object

                superHero.setDogruSayisi(json.getString(Config.TAG_DOGRUSAYISI));
                superHero.setYanlisSayisi(json.getString(Config.TAG_YANLISSAYISI));
                superHero.setSonuc(json.getString(Config.TAG_SONUC));
                superHero.setTarih(json.getString(Config.TAG_DATE));

            } catch (JSONException e) {
                Log.e("parseJsonArrayData()", e.getMessage());
            }
            //Adding the superhero object to the list
            // Log.e("eee", superHero.toString());

            listSuperHeroes.add(superHero);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    //Overriden method to detect scrolling
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
            getDataFromDatabase( gelenId);
        }
    }
}
