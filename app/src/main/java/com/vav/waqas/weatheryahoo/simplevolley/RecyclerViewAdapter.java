package com.vav.waqas.weatheryahoo.simplevolley;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.vav.waqas.weatheryahoo.R;

import java.util.List;

/**
 * Created by haslina on 11/13/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;

    List<ForecastData> dataList;

    ImageLoader imageLoader1;

    public RecyclerViewAdapter(List<ForecastData> forecastData, Context context){

        super();
        this.dataList = forecastData;
        String item = forecastData.get(2).fDate;
        Log.e("getDataAdapter",item);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder Viewholder, int position) {

        ForecastData forecastData1 =  dataList.get(position);
        Log.e("item", forecastData1.getfDate());
        Log.e("item", forecastData1.getfDay());

        Viewholder.tvDayDate.setText(forecastData1.getfDay()+", "+ forecastData1.getfDate());
        Viewholder.textViewCond.setText(forecastData1.getfText());
        Viewholder.tvHigh.setText(forecastData1.getfHigh());
        Viewholder.tvLow.setText(forecastData1.getfLow());
        int resourceID = context.getResources().
                getIdentifier("drawable/icon_" + forecastData1.getfCode(), null, context.getPackageName());
        //noinspection deprecation
        Drawable weatherIcon = context.getResources().getDrawable(resourceID);
        Viewholder.imIcon.setImageDrawable(weatherIcon);

        Viewholder.imIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ImageViewClicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewCond;
        public TextView tvDayDate;
        public TextView tvHigh;
        public TextView tvLow;
        public ImageView imIcon;

        // public NetworkImageView networkImageView ;

        public ViewHolder(View itemView) {

            super(itemView);

            tvDayDate = (TextView) itemView.findViewById(R.id.textViewDayDate) ;
            textViewCond = (TextView) itemView.findViewById(R.id.textViewCondition) ;
            tvHigh = (TextView) itemView.findViewById(R.id.textViewHigh) ;
            tvLow = (TextView) itemView.findViewById(R.id.textViewlow) ;
            imIcon = (ImageView) itemView.findViewById(R.id.VollyNetworkImageView1) ;
            //networkImageView = (NetworkImageView) itemView.findViewById(R.id.VollyNetworkImageView1) ;

        }
    }
}