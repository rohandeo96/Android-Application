package com.example.kanitkar.biig.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kanitkar.biig.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kanitkar on 04-04-2018.
 */
public class RealEstateAdapter extends ArrayAdapter<RealEstateModel> implements View.OnClickListener {
    private ArrayList<RealEstateModel> dataSet;
    Activity mContext;

    public RealEstateAdapter(ArrayList<RealEstateModel> data, Activity context) {
        super(context, R.layout.activity_propertylist, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        RealEstateModel dataModel=(RealEstateModel)object;

        switch (v.getId())
        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        View rowView;
        try {
            RealEstateModel dataModel = getItem(position);
            //LayoutInflater inflater = mContext.getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.activity_propertylist,null);

            String imgPath = "https://evening-waters-97508.herokuapp.com/" + dataModel.getAdImage();

            TextView txtTitle = (TextView) rowView.findViewById(R.id.tv_ad_title);
            TextView txtPrice = (TextView) rowView.findViewById(R.id.tv_ad_price);
            TextView txtArea = (TextView) rowView.findViewById(R.id.tv_ad_area);
            TextView txtSize = (TextView) rowView.findViewById(R.id.tv_ad_size);
            ImageView imgAd = (ImageView) rowView.findViewById(R.id.img_ad);
            txtTitle.setText(dataModel.getTitle());
            txtPrice.setText(Double.toString(dataModel.getPrice()));
            txtArea.setText(dataModel.getArea());
            txtSize.setText(Integer.toString(dataModel.getSize()));


            Picasso.with(getContext()).load(imgPath).fit().into(imgAd);
            return rowView;

        } catch (Exception ex){
            ex.printStackTrace();
            rowView = null;
        }
        return null;
    }
}
