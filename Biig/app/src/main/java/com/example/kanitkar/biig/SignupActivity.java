package com.example.kanitkar.biig;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kanitkar.biig.api.Model.Backend;
import com.example.kanitkar.biig.api.Model.User;
import com.example.kanitkar.biig.api.Model.securityquestion;
import com.example.kanitkar.biig.classes.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity  {


    private UserSignupTask mAuthTask = null;
    private GetSecurityQuestions mGet = null;

    // UI references.
    private EditText firstname, lastname,etemail,etpassword,etcpassword , secans;
    private Spinner mySpinner;
    private Button btnsignup;
    private ProgressDialog pDialog;
    private View mProgressView;
    private View mLoginFormView;
    ArrayList<String> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Set up the login form.
        firstname = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.surname);
        etemail = (EditText) findViewById(R.id.email);
        etpassword = (EditText) findViewById(R.id.password);
        etcpassword = (EditText) findViewById(R.id.confirmpassword);
        secans = (EditText) findViewById(R.id.securityanswer);
        pDialog = new ProgressDialog(SignupActivity.this);
        mGet = new GetSecurityQuestions();
        mGet.execute();

        btnsignup = (Button) findViewById(R.id.email_sign_in_button);
        btnsignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignup() {
        if (mAuthTask != null) {
            return;
        }

        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String email = etemail.getText().toString();
        String password = etpassword.getText().toString();
        String cpassword = etcpassword.getText().toString();
        String securityans = secans.getText().toString();
        String securityques = mySpinner.getSelectedItem().toString();


        if (fname.equals("")) {
            Toast.makeText(this,"Please enter First Name",Toast.LENGTH_LONG).show();
            return;
        }else if (lname.equals("")) {
            Toast.makeText(this,"Please enter Last Name",Toast.LENGTH_LONG).show();
            return;
        }else if (email.equals("")) {
            Toast.makeText(this,"Please enter Email",Toast.LENGTH_LONG).show();
            return;
        }else if (password.equals("")) {
            Toast.makeText(this,"Please enter Password",Toast.LENGTH_LONG).show();
            return;
        }else if (cpassword.equals("")) {
            Toast.makeText(this,"Please enter Confirm Password",Toast.LENGTH_LONG).show();
            return;
        }else if (!cpassword.equals(password)) {
            Toast.makeText(this, "Confirm Password doesn't match", Toast.LENGTH_LONG).show();
            return;
        }else if(securityans.equals(""))
        {
            Toast.makeText(this,"Please enter Security Answer",Toast.LENGTH_LONG).show();
            return;
        }else if(securityques.equals("")) {
            Toast.makeText(this,"Please select Security Question ",Toast.LENGTH_LONG).show();
            return;
        }else {
            showProgress(true);
            mAuthTask = new UserSignupTask(fname, lname ,email,password, cpassword, securityques, securityans);
            mAuthTask.execute((Void) null);
        }
        }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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

    public class GetSecurityQuestions extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            Backend backend = new Backend();
            List<securityquestion> secq = backend.getQuestions();
            questions = new ArrayList<String>();
            for (int i=0;i<secq.size();i++)
            {
                questions.add(secq.get(i).value);
            }
            return questions !=null;
        }

        @Override
        protected void onPostExecute(final Boolean success){
            showProgress(false);
            setQuestions();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void setQuestions(){
        mySpinner = (Spinner) findViewById(R.id.securityques);
        ArrayAdapter myAdapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_expandable_list_item_1, questions){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mySpinner.setAdapter(myAdapter);
    }

    public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFname;
        private final String mLname;
        private final String mEmail;
        private final String mCpassword;
        private final String mPassword;
        private final String mSecurityques;
        private final String mSecurityans;

        UserSignupTask(String fname, String lname ,String email,String password, String cpassword, String securityques, String securityans) {
            mFname = fname;
            mLname = lname;
            mEmail = email;
            mCpassword = cpassword;
            mPassword = password;
            mSecurityques = securityques;
            mSecurityans = securityans;
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
            User user = backendAPI.usersignup(mFname,mLname,mEmail,mPassword,mCpassword,mSecurityques, mSecurityans );
            if (user != null){
                SharedPreferences settings = getSharedPreferences(Session.getInstance().PREFS_NAME, 0);
                Session.getInstance().setUserInfo(user, settings);
            }
            // TODO: register the new account here.
            return user != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(SignupActivity.this,dashboard.class);
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

