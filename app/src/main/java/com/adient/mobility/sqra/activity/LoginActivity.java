package com.adient.mobility.sqra.activity;

/**
 * Created by mobility on 09/09/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;


import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.util.MyAppConstants;
import com.adient.mobility.sqra.util.MySingleTon;
import com.crashlytics.android.Crashlytics;
import com.eggheadgames.siren.Siren;
import com.eggheadgames.siren.SirenAlertType;
import com.eggheadgames.siren.SirenVersionCheckType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;


public class LoginActivity extends AppCompatActivity {


    private static String json = "";
    private Toolbar toolbar;
    private EditText inputName, inputPassword;

    private Button btnLogin;
    private String user;
    private String password;
    private SharedPreferences sharedpreferences;
    private TextInputLayout inputLayoutName, inputLayoutPassword;
    private boolean networkState;
    private String localJson;
    private Switch switchRemember;
    private ImageView imgHeader;
    private CoordinatorLayout coLayout;
    private static final String SIREN_JSON_DOCUMENT_URL = "https://ag.adient.com/mobile/android/SQRA/manifest.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //networkState=MySingleTon.isNetworkOnline(getApplicationContext());
        sharedpreferences = getSharedPreferences(MyAppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
        networkState = MySingleTon.isNetworkOnline(getApplicationContext());

        // toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
       // imgHeader = (ImageView) findViewById(R.id.img_header);

        coLayout = (CoordinatorLayout) findViewById(R.id.co_layout);
        checkOrientation();


        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);

        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputName = (EditText) findViewById(R.id.input_name);

        inputPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        String user1 = sharedpreferences.getString(MyAppConstants.USER, null);
        String password1 = sharedpreferences.getString(MyAppConstants.PASSWORD, null);

        inputName.setText(user1);
        inputPassword.setText(password1);

        switchRemember = (Switch) findViewById(R.id.switchRemember);
        switchRemember.setChecked(true);
        MySingleTon.rememberMe=true;
        if (MySingleTon.rememberMe) {
            switchRemember.setChecked(true);
        } else {
            switchRemember.setChecked(false);
        }


        //attach a listener to check for changes in state
        switchRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    MySingleTon.rememberMe = true;

                } else {
                    MySingleTon.rememberMe = false;

                }

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkState) {
                    login();

                }else{
                    onLoginFailedNetwork();
                }
            }
        });
        Siren siren = Siren.getInstance(getApplicationContext());
        siren.setVersionCodeUpdateAlertType(SirenAlertType.FORCE);
        siren.checkVersion(this, SirenVersionCheckType.IMMEDIATELY, SIREN_JSON_DOCUMENT_URL);

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


    private void checkOrientation() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //code for portrait mode
            coLayout.setBackgroundResource(R.drawable.bg);
        } else {
            //code for landscape mode
            coLayout.setBackgroundResource(R.drawable.bg);
        }
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        user = inputName.getText().toString();
        password = inputPassword.getText().toString();

        /* user = "akanags";//inputName.getText().toString()       athullh;
         password = "S@e7_8Yy";//inputPassword.getText().toString();    Janaki123*/
        new LoginAsync().execute();
    }

    /**
     * onLoginSuccess method - When login is success
     * 1. Enable Login Button
     * 2. Initiate Welcome Activity
     */
    public void onLoginSuccess() {
        checkRememberMe();
        logUser();
        Intent regionIntent = new Intent(LoginActivity.this, StartUpActivity.class);
        startActivity(regionIntent);
        finish();
    }

    /**
     * If 'Remember me' Switch is ON -> Save credentials & SM Key
     */
    public void checkRememberMe() {

        if (switchRemember.isChecked()) {
            MySingleTon.rememberMe = true;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(MyAppConstants.SESSION_KEY, MySingleTon.smKey);
            editor.putString(MyAppConstants.USER, user);
            editor.putString(MyAppConstants.PASSWORD, password);
            editor.putBoolean(MyAppConstants.FIRST_LOGIN, true);
            editor.putLong(MyAppConstants.LAST_ACTIVITY_TIME, new Date().getTime());
            editor.commit();
        } else {
            MySingleTon.rememberMe = false;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(MyAppConstants.SESSION_KEY);
            editor.remove(MyAppConstants.USER);
            editor.remove(MyAppConstants.PASSWORD);
            editor.putBoolean(MyAppConstants.FIRST_LOGIN, false);
            editor.commit();
        }
    }

    /**
     * onLoginFailed method - When login is fail
     * 1. Call Alert Builder
     * 2. Set Failure message and throw
     */
    public void onLoginFailed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title));
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.err_login))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        btnLogin.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        user = inputName.getText().toString();
        password = inputPassword.getText().toString();
/*
         user = "akanags";//inputName.getText().toString();
          password = "S@e7_8Yy";//inputPassword.getText().toString();*/

        if (user.isEmpty()) {
            inputName.setError(MyAppConstants.ID_ERROR);
            valid = false;
        } else {
            inputName.setError(null);
        }

        if (password.isEmpty()) {
            inputPassword.setError(MyAppConstants.PASSWORD_ERROR);
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }

    class LoginAsync extends AsyncTask<Void, Integer, String> {

        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            //progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(MyAppConstants.MSG_AUNTHENTICATION);
            progressDialog.show();

        }

        protected String doInBackground(Void... arg0) {
            try {
                MySingleTon.smAuthenticate(user, password);
                //MySingleTon.retrieveRegions();
                localJson = loadJSONFromAsset();

                if (localJson != null && localJson != "") {
                    MySingleTon.json   = localJson;
                }
            } catch (NullPointerException e) {
               /* runOnUiThread(new Runnable() {
                    public void run() {
                        // Write your code here to run in UI thread
                       // onLoginFailed();
                    }
                });*/
            }
            return localJson;

           /* SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(MyAppConstants.SESSION_KEY, sessionKey);
            editor.commit();

            json = MySingleTon.json;

            if(json!=null) {

                return json;
            }else{
                return null;
            }*/
        }

        protected void onPostExecute(String result) {

            if (result != null && result != "" && MySingleTon.smKey != "") {
                btnLogin.setEnabled(true);
                onLoginSuccess();
                //progressDialog.dismiss();
            } else {
                onLoginFailed();

            }
            progressDialog.dismiss();
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
        //MySingleTon.json=myjsonstring;
        return myjsonstring;
    }

    private void logUser() {
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(user);
        Crashlytics.setUserEmail(user + "@adient.com");
        Crashlytics.setUserName(user);
    }

/*    public  String smAuthenticate(String user, String password) {


        HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();

        HttpPost post = new HttpPost(MyAppConstants.PRE_LOGIN_URL);
        post.setHeader(MyAppConstants.USER_AGENT, MyAppConstants.NEW_USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair(MyAppConstants.USER, user));
        urlParameters.add(new BasicNameValuePair(MyAppConstants.PASSWORD, password));

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            Header[] headerArray = response.getAllHeaders();
            for (Header val : headerArray) {
                if (val.getName().equalsIgnoreCase(MyAppConstants.SET_COOKIE)) {
                    sessionKey = val.getValue();
                }
            }

            Log.v("test", response.getEntity().getContentType().toString());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            if (sessionKey.contains("expires")) {


               *//* String data = "{ ... }";
                Object json = new JSONTokener(data).nextValue();
                if (json instanceof JSONObject)
                //you have an object
                else if (json instanceof JSONArray)*//*



               *//* //String jsonText = readAll(br);
               // JSONObject json = new JSONObject(jsonText);
               // JSONObject resp = json.getJSONObject("Response");*//*
                // if(response.getEntity().getContentType().toString().equalsIgnoreCase("text/html; charset=iso-8859-1")){
                //text/html; charset=iso-8859-1
                //text/plain
                return null;
            } else {
                return result.toString();
            }
        } catch (Exception e) {
        } finally {
            client.getConnectionManager().shutdown();
        }
        return "";
    }else {
    return "";
}*/


}


