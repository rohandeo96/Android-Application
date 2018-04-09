package com.example.kanitkar.biig;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.kanitkar.biig.Adapters.RealEstateAdapter;
import com.example.kanitkar.biig.Adapters.RealEstateModel;
import com.example.kanitkar.biig.api.Model.Backend;
import com.example.kanitkar.biig.api.Model.PropertyDetails;

import java.util.ArrayList;

public class showproperties extends AppCompatActivity {
    EditText etSearchTxt;
    ImageView searchBtn;
    ListView listView;
    private AdRenderingTask mAuthTask = null;
    private ProgressDialog pDialog;
    ArrayList<PropertyDetails> dataModels;
    private static RealEstateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showproperties);
        etSearchTxt = (EditText) findViewById(R.id.et_search);
        pDialog = new ProgressDialog(showproperties.this);
        listView=(ListView)findViewById(R.id.list_ads);
        if (getIntent().getStringExtra("stuff") != "") {
            String key = getIntent().getStringExtra("key");
            String location = getIntent().getStringExtra("location");
            String size = getIntent().getStringExtra("size");
            String furnishingstate = getIntent().getStringExtra("furnishingstate");
            String numberofrooms = getIntent().getStringExtra("numberofrooms");
            Log.d("key1",key);
            fetchAdList(key, location, size, furnishingstate, numberofrooms);
        } else {
            fetchAdList("", "", "", "", "");
        }
        searchBtn = (ImageView)findViewById(R.id.img_search_ad);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModels.clear();
                fetchAdList(etSearchTxt.getText().toString(),"","","","");
            }
        });
    }


    public void fetchAdList(String key, String location, String size, String furnishingstate, String numberofrooms) {
        showProgress(true);
        mAuthTask = new AdRenderingTask(key, location, size, furnishingstate, numberofrooms);
        mAuthTask.execute((Void) null);
    }


    private  void showProgress(boolean show){
        if (show) {
            pDialog.setMessage("Loading Properties. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        else{
            pDialog.dismiss();
        }
    }

    public class AdRenderingTask extends AsyncTask<Void, Void, Boolean> {

        private final String mKey;
        private final String mLocation;
        private final String mSize;
        private final String mFurnishingstate;
        private final String mNumberofrooms;

        AdRenderingTask(String key, String location, String size, String furnishingstate, String numberofrooms) {
            mKey = key;
            mLocation = location;
            mSize = size;
            mFurnishingstate = furnishingstate;
            mNumberofrooms = numberofrooms;
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
                Backend backendAPI = new Backend();
                dataModels = backendAPI.getRealEstateList(mKey,mLocation,mSize,mFurnishingstate,mNumberofrooms);
            } catch (Exception e) {
                return false;
            }
            // TODO: register the new account here.
            return dataModels != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            renderList();
            if (success) {

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void renderList(){
        ArrayList<RealEstateModel> adModel = new ArrayList<RealEstateModel>();
        for (PropertyDetails ad : dataModels){
            adModel.add(new RealEstateModel(ad.id,ad.title,ad.price,ad.city + " ," +ad.state+ " ," +ad.plz,ad.size,ad.image1));
        }
        adapter= new RealEstateAdapter(adModel,showproperties.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RealEstateModel obj = (RealEstateModel) parent.getAdapter().getItem(position);
                Intent intent = new Intent(showproperties.this, propertydetails.class);
                intent.putExtra("id", Integer.toString(obj.getId()));
                startActivity(intent);
            }
        });

    }
}
