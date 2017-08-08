package com.adient.mobility.sqra.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.util.MyAppConstants;
import com.adient.mobility.sqra.util.MySingleTon;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class SplashActivity extends AppCompatActivity {


    private static String json = "";
    private SharedPreferences sharedpreferences;
    public static String user="";
    public static String password="";
    private boolean networkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        networkState = MySingleTon.isNetworkOnline(getApplicationContext());

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
*/
        if(networkState) {
            login();

        }else{
            onLoginFailedNetwork();
        }



       /*   new Handler().postDelayed(new Runnable() {

            *//*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             *//*

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent regionIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(regionIntent);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT); }*/


    }

    public  void onLoginFailedNetwork() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title));
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.err_login_network))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        // btnLogin.setEnabled(true);
    }


    public void login(){
        sharedpreferences = getSharedPreferences(MyAppConstants.MyPREFERENCES, Context.MODE_PRIVATE);

        Long lastActivityTime = sharedpreferences.getLong(MyAppConstants.LAST_ACTIVITY_TIME, 0L);
        Long currentTime = new Date().getTime();
        Long timeDiff = currentTime-lastActivityTime;

        Long smKeyTimeout = MyAppConstants.SM_KEY_TIMEOUT;
        Long userCredentialTimeout =MyAppConstants.CREDENTIAL_TIMEOUT;

        if(timeDiff < smKeyTimeout){// SM Key is still valid
            String smKey = sharedpreferences.getString(MyAppConstants.SESSION_KEY, null);
            if (smKey != null) {
                MySingleTon.smKey=smKey;
                logUser();
                new DirectAsync().execute();
            }else{ // Session Key is Null. Assume that user credential is also Null. Hence dispatch to Login Activity
                callLoginActivity();
            }
        }
        else{// SM Key is invalid. Check the User credential validity
            timeDiff=currentTime-userCredentialTimeout;
            logUser();
            if(timeDiff < userCredentialTimeout){// User Credential is still valid
                user = sharedpreferences.getString(MyAppConstants.USER, null);
                password = sharedpreferences.getString(MyAppConstants.PASSWORD, null);
                if(user !=null && user !="" && password!=null && password!=""){
                    new LoginAsync().execute();
                }
                else{// Either user or password is null, dispatch to Login Activity
                    callLoginActivity();
                }
            }
            else{// SM Key and User credentials are not valid. Hence dispatch to Login screen
                callLoginActivity();
            }
        }
    }

    /**
     * onLoginSuccess method - When login is success
     * 1. Enable Login Button
     * 2. Initiate Welcome Activity
     */
    public void onLoginSuccess() {

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(MyAppConstants.LAST_ACTIVITY_TIME,new Date().getTime());
        editor.putString(MyAppConstants.SESSION_KEY,MySingleTon.smKey);
        editor.commit();

        Intent regionIntent = new Intent(SplashActivity.this, FragmentActivity.class);
        startActivity(regionIntent);
        finish();
    }
    /**
     * onLoginFailed method - When login is fail
     * 1. Dispatch to Login Activity
     */
    public void onLoginFailed() {
        callLoginActivity();
    }


    public void callLoginActivity(){
        Intent regionIntent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(regionIntent);
        finish();
    }


    /**
     *DirectAsync Class
     * 1. PreExecute
     *      1. Start Progress with 'Authenticating...' message
     * 2. doInBackground
     *      1. set username, password in JSON and call smAuthenticate method
     *      2. Save the credentials and SMKey in SharedPreference If the 'Remember me' is enabled
     *      3. Return the JSON file
     * 3. PostExecute
     *      1. If JSON and SMKey is not null/empty, call LoginSuccess method
     *      2. Else Call LoginFailed method
     */
    private class DirectAsync extends AsyncTask<String, Integer, String> {
        private ProgressDialog proDialog;

        protected void onPreExecute() {
            proDialog = new ProgressDialog(SplashActivity.this);
            proDialog.setIndeterminate(true);
            proDialog.setMessage("Authenticating...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        protected String doInBackground(String... strings) {
            //MySingleTon.retrieveRegions();
            loadJSONFromAsset();
            json = MySingleTon.json;
            return json;
        }

        protected void onPostExecute(String result) {
            proDialog.dismiss();
            if(result!=null) {
                Intent regionIntent = new Intent(SplashActivity.this, FragmentActivity.class);
                startActivity(regionIntent);
            }else{
                callLoginActivity();
            }
            finish();
        }
    }

    public String loadJSONFromAsset() {

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getApplicationContext().getAssets().open(
                    MyAppConstants.JSON_FILE)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String myjsonstring = sb.toString();
        MySingleTon.json=myjsonstring;
        return myjsonstring;
    }


    /**
     * onLoginFailed method - When login is fail
     * 1. Call Alert Builder
     * 2. Set Failure message and throw
     */
    public void onConnectionFailed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_connection_title));
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.err_connection))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return;
    }

    /**
     * onLoginFailed method - When login is fail
     * 1. Call Alert Builder
     * 2. Set Failure message and throw
     */
    public void wrongCredntials() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title));
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.err_login))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return;
    }

    /**
     *LoginAsync Class
     * 1. PreExecute
     *      1. Start Progress with 'Authenticating...' message
     * 2. doInBackground
     *      1. set username, password in JSON and call smAuthenticate method
     *      2. Save the credentials and SMKey in SharedPreference If the 'Remember me' is enabled
     *      3. Return the JSON file
     * 3. PostExecute
     *      1. If JSON and SMKey is not null/empty, call LoginSuccess method
     *      2. Else Call LoginFailed method
     */
    class LoginAsync extends AsyncTask<Void, Integer, String> {

        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SplashActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(MyAppConstants.MSG_AUNTHENTICATION);
            progressDialog.show();
        }

        protected String doInBackground(Void... arg0) {

            // try{
            MySingleTon.smAuthenticate(user, password);
            // MySingleTon.retrieveRegions();

            loadJSONFromAsset();

           /* }catch(SocketTimeoutException e) {
                Log.d("LoginActivitiy:", "Fail in doInBackground: " + e.getMessage());
                onConnectionFailed();*/

           /* } catch (IOException e) {
                Log.d("LoginActivitiy:", "Fail in doInBackground: " + e.getMessage());
                wrongCredntials();
            }*/

            if(MySingleTon.json !=null){
                return MySingleTon.json;
            }
            else{
                return null;
            }

        }

        protected void onPostExecute(String result) {
            if (result != null && result != "" && MySingleTon.smKey != "") {
                onLoginSuccess();
                //progressDialog.dismiss();
            } else {
                onLoginFailed();
            }
            progressDialog.dismiss();
        }
    }
    private void logUser() {
        // You can call any combination of these three methods
        user = sharedpreferences.getString(MyAppConstants.USER, null);
        Crashlytics.setUserIdentifier(user);
        Crashlytics.setUserEmail(user + "@adient.com");
        Crashlytics.setUserName(user);
    }

}



/*

    private class DirectAsync extends AsyncTask<String, Integer, String> {


        private ProgressDialog proDialog;


        protected void onPreExecute() {
           proDialog = new ProgressDialog(SplashActivity.this);
            proDialog.setIndeterminate(true);
            proDialog.setMessage("Authenticating...");
            proDialog.setCancelable(false);
            proDialog.show();

        }


        protected String doInBackground(String... strings) {

            try {
                MySingleTon.json= checkSessionValidity();
            } catch (IOException e) {

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            json = MySingleTon.json;
            return json;
        }


        protected void onPostExecute(String result) {

            proDialog.dismiss();

            if(result!=null) {
                Intent regionIntent = new Intent(SplashActivity.this, RegionActivity.class);
                startActivity(regionIntent);
            }else{
                Intent regionIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(regionIntent);
            }
            finish();

        }
    }*/


    /*public String checkSessionValidity() throws ClientProtocolException, IOException, URISyntaxException
    {

       // networkState=MySingleTon.isNetworkOnline(getApplicationContext());
       // if(networkState==true) {
            HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
            String finalURL = MyAppConstants.PRE_LOGIN_URL_1;
            URL url = new URL(finalURL);
            HttpPost get = new HttpPost(url.toURI());
            get.addHeader("Cookie", session);
            HttpResponse response = client.execute(get);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();

        }else{
            Toast.makeText(getApplicationContext(),"Check your network connection!!!",Toast.LENGTH_SHORT).show();
            return null;
        }
    //}
*/


//}
