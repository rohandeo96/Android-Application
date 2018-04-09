package com.example.kanitkar.biig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class advanced_search extends AppCompatActivity {
    EditText etKeyword;
    Spinner spAccomodation, spFurnishingstate, spSize, spLocation;
    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        etKeyword = (EditText) findViewById(R.id.et_Keyword);
        spLocation = (Spinner) findViewById(R.id.sp_Location);
        spAccomodation = (Spinner) findViewById(R.id.sp_Accomodation);
        spSize = (Spinner) findViewById(R.id.sp_Size);
        spFurnishingstate = (Spinner) findViewById(R.id.sp_Furnishingstate);

        btnSearch = (Button) findViewById(R.id.btn_Search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAd();
            }
        });
    }

    public void searchAd()
    {
        int s,f;
        String location = spLocation.getSelectedItem().toString();
        String key = etKeyword.getText().toString();
        String norooms = spAccomodation.getSelectedItem().toString();
        String size = spSize.getSelectedItem().toString();
        if(size == "Less than 50"){
            s = 241;
        }
        else if(size == "51 - 100"){
            s = 251;
        }
        else if(size == "101 - 200"){
            s = 261;
        }
        else {
            s = 271;
        }
        String state = spFurnishingstate.getSelectedItem().toString();
        if(state == "Unfurnished"){
            f = 161;
        }
        else if (state == "Semi-furnished"){
            f = 171;
        }
        else {
            f = 181;
        }

        Intent intent = new Intent(advanced_search.this,showproperties.class);
        intent.putExtra("stuff","asd");
        intent.putExtra("key",key);
        intent.putExtra("location",location);
        intent.putExtra("size",s);
        intent.putExtra("furnishingstate",f);
        intent.putExtra("numberofrooms",norooms);
        startActivity(intent);
    }
}
