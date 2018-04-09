package com.example.kanitkar.biig;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kanitkar.biig.api.Model.AdDetail;
import com.example.kanitkar.biig.api.Model.Backend;
import com.example.kanitkar.biig.api.Model.User;
import com.example.kanitkar.biig.classes.Session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddProperty extends AppCompatActivity {

    int SELECT_PICTURE = 101;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri selectedImageUri;
    String extension;

    Button getImage;
    String encodedImage;
    private CreateProperty mAuthTask = null;
    EditText etTitle, etDescription, etSize, etNoofrooms, etStreet, etHouse, etCountry, etState, etPlz, etCity, etPrice, etOverview;
    Spinner spFstate;
    Button addproperty;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestStoragePermission();
        pDialog = new ProgressDialog(AddProperty.this);
        setContentView(R.layout.activity_add_property);
        etTitle = (EditText) findViewById(R.id.et_title);
        etDescription = (EditText) findViewById(R.id.et_description);
        etSize = (EditText) findViewById(R.id.et_size);
        etNoofrooms = (EditText) findViewById(R.id.et_noofrooms);
        etStreet = (EditText) findViewById(R.id.et_street);
        etHouse = (EditText) findViewById(R.id.et_house);
        etCountry = (EditText) findViewById(R.id.et_country);
        etState = (EditText) findViewById(R.id.et_state);
        etPlz = (EditText) findViewById(R.id.et_plz);
        etCity = (EditText) findViewById(R.id.et_city);
        etPrice = (EditText) findViewById(R.id.et_price);
        etOverview = (EditText) findViewById(R.id.et_overview);
        spFstate = (Spinner) findViewById(R.id.sp_fstate);
        getImage = (Button) findViewById(R.id.btn_choose);

        getImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

            }
        });


        addproperty = (Button) findViewById(R.id.btn_add);
        addproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_PICTURE) {
            // Make sure the request was successful

            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                String uri = selectedImageUri.toString();
                Log.d("URI" , uri);
                Bitmap selectedImageBitmap = null;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
                Log.d("ByteArray", byteArrayImage.toString());
                encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

                Log.d("encodedimage", encodedImage.toString());
            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void add() {
        if (mAuthTask != null) {
            return;
        }

        String title = etTitle.getText().toString();
        String desc = etDescription.getText().toString();
        String size = etSize.getText().toString();
        String nor = etNoofrooms.getText().toString();
        String hno = etHouse.getText().toString();
        String country = etCountry.getText().toString();
        String state = etState.getText().toString();
        String plz = etPlz.getText().toString();
        String city = etCity.getText().toString();
        String price = etPrice.getText().toString();
        String str = etStreet.getText().toString();
        String overview = etOverview.getText().toString();
        String furn = spFstate.getSelectedItem().toString();


        if (title.equals("")) {
            Toast.makeText(this,"Please enter Title",Toast.LENGTH_LONG).show();
            return;
        }else if (desc.equals("")) {
            Toast.makeText(this,"Please enter Description",Toast.LENGTH_LONG).show();
            return;
        }else if (size.equals("")) {
            Toast.makeText(this,"Please enter Size",Toast.LENGTH_LONG).show();
            return;
        }else if (nor.equals("")) {
            Toast.makeText(this,"Please enter Number of Rooms",Toast.LENGTH_LONG).show();
            return;
        }else if (hno.equals("")) {
            Toast.makeText(this,"Please enter House Number",Toast.LENGTH_LONG).show();
            return;
        }else if(country.equals(""))
        {
            Toast.makeText(this,"Please enter Country",Toast.LENGTH_LONG).show();
            return;
        }else if(state.equals("")) {
            Toast.makeText(this,"Please enter State ",Toast.LENGTH_LONG).show();
            return;
        }else if(city.equals("")) {
            Toast.makeText(this,"Please enter City ",Toast.LENGTH_LONG).show();
            return;
        }else if(plz.equals("")) {
            Toast.makeText(this,"Please enter PLZ ",Toast.LENGTH_LONG).show();
            return;
        }else if(price.equals("")) {
            Toast.makeText(this,"Please enter Price ",Toast.LENGTH_LONG).show();
            return;
        }else {
            showProgress(true);
            String path = getPath(selectedImageUri);
            extension = path.substring(path.lastIndexOf(".")+1);
            Log.d("Extension", extension);
            mAuthTask = new CreateProperty(title,price ,desc,overview,size,nor,furn,str,hno,country,state,plz,city,encodedImage,extension);
            mAuthTask.execute((Void) null);
            if (mAuthTask != null){
                Toast.makeText(AddProperty.this, "Property Added Successfully",Toast.LENGTH_LONG).show();
            }
        }
    }


    private void showProgress(boolean show) {
        if (show) {
            pDialog.setMessage("Please Wait");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        else{
            pDialog.dismiss();
        }
    }


    public class CreateProperty extends AsyncTask<Void, Void, Boolean> {

        private final String mTitle;
        private final String mPrice;
        private final String mDescription;
        private final String mOverview;
        private final String mSize;
        private final String mNor;
        private final String mFurn;
        private final String mStr;
        private final String mHno;
        private final String mCountry;
        private final String mState;
        private final String mPlz;
        private final String mCity;
        private final String mImage;
        private final String mImageExtension;
        User userInfo = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0));
        String employee_id = Integer.toString(userInfo.employee);
        CreateProperty(String title, String price ,String desc, String overview,String size, String nor, String furn, String str, String hno, String country, String state, String plz, String city, String image, String extension) {
            mTitle = title;
            mPrice = price;
            mDescription = desc;
            mOverview = overview;
            mSize = size;
            mNor = nor;
            mFurn = furn;
            mStr = str;
            mHno = hno;
            mCountry = country;
            mState = state;
            mPlz = plz;
            mCity = city;
            mImage = image;
            mImageExtension = extension;
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
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            Backend backendAPI = new Backend();
            AdDetail obj = backendAPI.addProperty(employee_id,mPrice,mDescription,mOverview,mTitle,mHno,mStr,mPlz,mCity,mState,mCountry,mImage,mImageExtension,"","",mSize,mNor,mFurn);
            Log.d("Object", obj.toString());
            // TODO: register the new account here.
            return obj != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(AddProperty.this,dashboard.class);
                startActivity(intent);
                finish();

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
