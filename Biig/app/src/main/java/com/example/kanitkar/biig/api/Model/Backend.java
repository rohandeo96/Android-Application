package com.example.kanitkar.biig.api.Model;

import android.util.Log;

import com.example.kanitkar.biig.util.WebClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kanitkar on 17-03-2018.
 */
public class Backend {
    private static WebClient jParser = new WebClient();
    public static String baseAddress = "https://evening-waters-97508.herokuapp.com/";

    public static User authenticateUser(String userName, String password){
        User userObj = new User();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", userName));
            nameValuePairs.add(new BasicNameValuePair("password", password));

            String url = baseAddress + "user/login";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "POST", nameValuePairs);
            Log.d("User object: ", jsonObj.toString());

            if (jsonObj.getString("id")=="0"){
                userObj = (User)null;
            }
            else{
                JsonElement mJson =  new JsonParser().parse(jsonObj.toString());
                userObj = new Gson().fromJson(mJson, User.class);
            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
            ex.printStackTrace();
        }
        return  userObj;
    }

    public static User usersignup(String fname, String lname ,String email, String password, String cpassword, String securityquestion, String securityanswer){
        User userObj = new User();
        int secq;
        if(securityquestion.equals("What is the name of your hometown?"))
        {
            secq=1;
        }
        else if (securityquestion.equals("What is the name of your first school?"))
        {
            secq=11;
        }
        else
        {
            secq=21;
        }
        Log.d("secq", Integer.toString(secq));
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("firstname", fname));
            nameValuePairs.add(new BasicNameValuePair("lastname", lname));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("securityAnswer", securityanswer));
            nameValuePairs.add(new BasicNameValuePair("securityQuestion", Integer.toString(secq)));
            Log.d("nameValuePairs", nameValuePairs.toString());

            String url = baseAddress + "user/signup";
            Boolean res = jParser.makeHttpRequestBoolean(url, "POST", nameValuePairs);


            if(res == true){

                userObj = authenticateUser(email,password);

            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  userObj;
    }

    public static List<securityquestion> getQuestions(){
        List<securityquestion> sq = new ArrayList<>();
        try{
            String url = baseAddress + "type/SECURITY_QUES/values";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            JSONArray dataArray = jParser.makeHttpRequestByJsonArray(url, "GET", nameValuePairs);
            Log.d("User object: ", dataArray.toString());

            if (dataArray != null){
                Type listType = new TypeToken<ArrayList<securityquestion>>(){}.getType();
                sq = (ArrayList<securityquestion>) new Gson().fromJson(dataArray.toString(), listType);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Log.d("Exception", ex.getMessage());
        }
        return  sq;
    }

    public static ArrayList<PropertyDetails> getRealEstateList(String key, String location, String size, String furnishingstate, String numberofrooms){
        ArrayList<PropertyDetails> adList = new ArrayList<PropertyDetails>();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key", key));
            nameValuePairs.add(new BasicNameValuePair("location", location));
            nameValuePairs.add(new BasicNameValuePair("size", size));
            nameValuePairs.add(new BasicNameValuePair("furnishingstate", furnishingstate));
            nameValuePairs.add(new BasicNameValuePair("numberofrooms", numberofrooms));

            String url = baseAddress + "property/search";
            JSONArray jsonArray = jParser.makeHttpRequestByJsonArray(url, "GET", nameValuePairs);
            Log.d("Ad object: ", jsonArray.toString());

            if(jsonArray!=null){
                Type listType = new TypeToken<ArrayList<PropertyDetails>>(){}.getType();
                adList = (ArrayList<PropertyDetails>) new Gson().fromJson(jsonArray.toString(), listType);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            Log.d("Exception", ex.getMessage());
        }
        return  adList;
    }

    public static AdDetail getAdDetail(String id){
        Log.d("id : ", id);
        AdDetail obj = new AdDetail();
        try{
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            String url = baseAddress + "property/"+id;
            JSONObject jsonObject = jParser.makeHttpRequest(url,"GET",nameValuePairs);
            Log.d("Property object: ", jsonObject.toString());
            if(jsonObject != null){
                JsonElement mJson = new JsonParser().parse(jsonObject.toString());
                obj = new Gson().fromJson(mJson, AdDetail.class);
            }
        }catch (Exception e){
            Log.d("Exception", e.getMessage());
        }
        return obj;
    }

    public static AdDetail addProperty(String employee,String price,String description,String overview,String title,String housenumber,String street,String plz,String city,String state,String country,String image1,String image1extension,String image2,String image3,String size,String numberofrooms,String furnishingstate){
        AdDetail propertyobject = new AdDetail();
        int fstate;
        if(furnishingstate.equals("Unfurnished"))
        {
            fstate=161;
        }
        else if (furnishingstate.equals("Semi-furnished"))
        {
            fstate=171;
        }
        else
        {
            fstate=181;
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("employeeId", employee));
            nameValuePairs.add(new BasicNameValuePair("title", title));
            nameValuePairs.add(new BasicNameValuePair("price", price));
            nameValuePairs.add(new BasicNameValuePair("overview", overview));
            nameValuePairs.add(new BasicNameValuePair("desc", description));
            nameValuePairs.add(new BasicNameValuePair("size", size));
            nameValuePairs.add(new BasicNameValuePair("numberofrooms", numberofrooms));
            nameValuePairs.add(new BasicNameValuePair("furnishingstate", Integer.toString(fstate)));
            nameValuePairs.add(new BasicNameValuePair("streetname", street));
            nameValuePairs.add(new BasicNameValuePair("housenumber", housenumber));
            nameValuePairs.add(new BasicNameValuePair("country", country));
            nameValuePairs.add(new BasicNameValuePair("state", state));
            nameValuePairs.add(new BasicNameValuePair("plz", plz));
            nameValuePairs.add(new BasicNameValuePair("city", city));
            nameValuePairs.add(new BasicNameValuePair("image1", image1));
            nameValuePairs.add(new BasicNameValuePair("image1Format", image1extension));

            String url = baseAddress + "property";
            JSONObject jsonObj = jParser.makeHttpRequest(url, "POST", nameValuePairs);
            Log.d("User object: ", jsonObj.toString());

            if (jsonObj != null){

                JsonElement mJson =  new JsonParser().parse(jsonObj.toString());
                propertyobject = new Gson().fromJson(mJson, AdDetail.class);

            }
        } catch (Exception ex){
            Log.d("Exception", ex.getMessage());
        }
        return  propertyobject;
    }
}
