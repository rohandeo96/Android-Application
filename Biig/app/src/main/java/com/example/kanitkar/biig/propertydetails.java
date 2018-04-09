package com.example.kanitkar.biig;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kanitkar.biig.api.Model.AdDetail;
import com.example.kanitkar.biig.api.Model.Backend;
import com.example.kanitkar.biig.classes.Session;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class propertydetails extends AppCompatActivity implements OnMapReadyCallback {

    private AdDetailTask mAuthTask = null;
    private ProgressDialog progressDialog;
    public AdDetail dataModel;
    String id;
    List<SliderUtils> sliderImg;
    ImageView propertyImage;
    TextView tvAdDetail, tvAdress, tvAdDescription, tvAdType, tvAdSize,tvAdFstate, tvAdBed, tvAdPublished, tvAdBuilt, tvAgentName, tvAgentDesignation, tvAgentPhone,tvAgentCompany;
    LinearLayout agentContainer,map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propertydetails);

        progressDialog = new ProgressDialog(propertydetails.this);
        id = getIntent().getStringExtra("id");
        sliderImg = new ArrayList<>();

        tvAdDetail = (TextView) findViewById(R.id.tv_ad_title);
        tvAdress = (TextView) findViewById(R.id.tv_address);
        tvAdDescription = (TextView) findViewById(R.id.tv_ad_desc);
        tvAdType = (TextView) findViewById(R.id.tv_ad_typevalue);
        tvAdSize = (TextView) findViewById(R.id.tv_ad_sizevalue);
        tvAdFstate = (TextView) findViewById(R.id.tv_ad_fstatevalue);
        tvAdBed = (TextView) findViewById(R.id.tv_ad_bedvalue);
        tvAdPublished = (TextView) findViewById(R.id.tv_ad_publishedvalue);
        tvAdBuilt = (TextView) findViewById(R.id.tv_ad_builtvalue);
        tvAgentName = (TextView) findViewById(R.id.tv_ad_agent_name);
        tvAgentDesignation = (TextView) findViewById(R.id.tv_ad_agent_designation);
        tvAgentCompany = (TextView) findViewById(R.id.tv_ad_agent_company);
        tvAgentPhone = (TextView) findViewById(R.id.tv_ad_agent_phone);
        propertyImage = (ImageView) findViewById(R.id.imageView);

        agentContainer = (LinearLayout) findViewById(R.id.layout_agent);
        map = (LinearLayout) findViewById(R.id.layout_map);
        manageFormVisibility();
        mAuthTask = new AdDetailTask(id);
        mAuthTask.execute();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        String location = dataModel.housenumber+","+dataModel.street+","+dataModel.city+","+dataModel.state+","+dataModel.country+","+dataModel.plz;
        Log.d("location", location);
        List<Address> addressList =null;
        Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location,1);
                while (addressList.size()==0) {
                    addressList = geocoder.getFromLocationName(location, 1);
                }
                if(addressList.size() > 0){
                    Log.d("Address list", addressList.toString());
                    Address address = addressList.get(0);
                    Log.d("Address", address.toString());
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title("Your Property"));
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 12.0f);
                    map.animateCamera(yourLocation);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    private  void showProgress(boolean show){
        if (show) {
            progressDialog.setMessage("Loading Ads. Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        else{
            progressDialog.dismiss();
        }
    }


    public class AdDetailTask extends AsyncTask<Void, Void, Boolean> {

        private final String mListingId;

        AdDetailTask(String id) {
            mListingId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                dataModel = Backend.getAdDetail(mListingId);
            } catch (Exception e) {
                return false;
            }
            // TODO: register the new account here.
            return dataModel != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                renderAd();
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void renderAd(){
        tvAdDetail.setText(dataModel.title);
        tvAdress.setText(dataModel.housenumber+","+dataModel.street+","+dataModel.city+","+dataModel.state+","+dataModel.country+","+dataModel.plz);
        tvAdDescription.setText(dataModel.description);
        tvAdType.setText(dataModel.typeofproperty);
        tvAdSize.setText(String.valueOf(dataModel.size));
        tvAdFstate.setText(dataModel.furnishingstate);
        tvAdBed.setText(String.valueOf(dataModel.numberofrooms));
        tvAdPublished.setText(dataModel.posted);
        tvAdBuilt.setText(String.valueOf(dataModel.yearbuilt));
        tvAgentName.setText(dataModel.agent_name);
        tvAgentDesignation.setText(dataModel.agent_designation);
        tvAgentPhone.setText(dataModel.agent_phone);
        tvAgentCompany.setText(dataModel.agent_company);
        String imgpath = Backend.baseAddress + dataModel.image1;
        Picasso.with(propertydetails.this).load(imgpath).fit().into(propertyImage);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void manageFormVisibility(){
        if(Session.getInstance().isUserInSession(getSharedPreferences(Session.getInstance().PREFS_NAME, 0))){
            agentContainer.setVisibility(View.VISIBLE);
            map.setVisibility(View.VISIBLE);
        }
    }
}
