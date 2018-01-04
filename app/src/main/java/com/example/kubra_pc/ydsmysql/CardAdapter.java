package com.example.kubra_pc.ydsmysql;

        import android.content.Context;
        import android.media.Image;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import com.android.volley.toolbox.ImageLoader;
        import com.android.volley.toolbox.NetworkImageView;

        import org.w3c.dom.Text;

        import java.util.ArrayList;
        import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {


    private Context context;

    //List to store all superheroes
    List<Skorlar> superHeroes;

    //Constructor of this class
    public CardAdapter(List<Skorlar> superHeroes, Context context){
        super();
        //Getting all superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.skorlar, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        Skorlar superHero =  superHeroes.get(position);

        //Loading image from url


        //Showing data on the views

        holder.dogruSayisi.setText(superHero.getDogruSayisi());
        holder.yanlisSayisi.setText(superHero.getYanlisSayisi());
        holder.sonuc.setText(superHero.getSonuc());
        holder.tarih.setText(superHero.getTarih());

    }

    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views

        public TextView dogruSayisi;
        public TextView yanlisSayisi;
        public TextView sonuc;
        public TextView tarih;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);

            dogruSayisi = (TextView) itemView.findViewById(R.id.dogrusayisi);
            yanlisSayisi = (TextView) itemView.findViewById(R.id.yanlissayisi);
            sonuc = (TextView) itemView.findViewById(R.id.sonuc);
            tarih = (TextView) itemView.findViewById(R.id.tarih);
        }
    }
}
