package com.example.kanitkar.biig;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kanitkar.biig.api.Model.User;
import com.example.kanitkar.biig.classes.Session;

public class dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    EditText searchText;
    ImageView searchButton;
    ImageView imgUser;
    TextView tvUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        imgUser = (ImageView)hView.findViewById(R.id.imageView);
        tvUsername = (TextView) hView.findViewById(R.id.textView);
        searchText = (EditText)findViewById(R.id.et_search);
        searchButton = (ImageView)findViewById(R.id.img_search_ad);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this,showproperties.class);
                String getrec=searchText.getText().toString();
                intent.putExtra("stuff","asd");
                intent.putExtra("key",getrec);
                intent.putExtra("location","");
                intent.putExtra("size","");
                intent.putExtra("furnishingstate","");
                intent.putExtra("numberofrooms","");
                Log.d("getrec", getrec);
                startActivity(intent);
                }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

       // Toast.makeText(this, "onResume: Session: " + Session.getInstance().isUserInSession(getSharedPreferences(PREFS_NAME, 0)), Toast.LENGTH_LONG).show();
        manageNavigationDrawerItems(Session.getInstance().isUserInSession(getSharedPreferences(Session.getInstance().PREFS_NAME, 0)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(dashboard.this,dashboard.class);
            startActivity(intent);
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(dashboard.this,LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(dashboard.this,advanced_search.class);
            startActivity(intent);
        } else if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_favourites) {

        } else if (id == R.id.nav_logout) {
            Session.getInstance().clearSession(getSharedPreferences(Session.getInstance().PREFS_NAME, 0));
            Intent intent = new Intent(dashboard.this,dashboard.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_add) {
            Intent intent = new Intent(dashboard.this,AddProperty.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void manageNavigationDrawerItems(boolean isLoggedIn){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_favourites).setVisible(isLoggedIn);
        nav_Menu.findItem(R.id.nav_profile).setVisible(isLoggedIn);
        nav_Menu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        nav_Menu.findItem(R.id.nav_messages).setVisible(isLoggedIn);
        nav_Menu.findItem(R.id.nav_logout).setVisible(isLoggedIn);
        nav_Menu.findItem(R.id.nav_add).setVisible(false);
        //User userInfo = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0));
        if(isLoggedIn){
            User userInfo = Session.getInstance().getUserInfo(getSharedPreferences(Session.getInstance().PREFS_NAME, 0));
            Log.d("userinfo", userInfo.toString());
            tvUsername.setText(userInfo.email);
            //String imgPath = "http://ec2-54-200-111-60.us-west-2.compute.amazonaws.com:3000/" + userInfo.UserImagePath;
            //Picasso.with(this).load(imgPath).fit().into(imgUser);
            if(userInfo.role == 11){
                nav_Menu.findItem(R.id.nav_add).setVisible(isLoggedIn);
            }
        }

    }
}
