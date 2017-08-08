package com.adient.mobility.sqra.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.util.MyAppConstants;
import com.adient.mobility.sqra.util.MySingleTon;

import java.util.Map;


public class DetailActivity extends AppCompatActivity  {

    private SearchView mSearchView;


    private Map<String, String> result;
    private Map<String, String> res_detail;
    private TextView txt_name;
    private TextView txt_discrip;
    private TextView txt_status;
    private TextView textRegion;
    private TextView txt_country;
    private TextView txt_sop;
    private TextView txt_eop;
    private String rowValueSticky;
    private String rowValueYrDetail;
    private String rowValueYr;
    private TextView txtRecycleHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            rowValueYr = (String) bundle.get(MyAppConstants.YEAR_VALUE);
            rowValueSticky = (String) bundle.get(MyAppConstants.SQRC_VALUE);
            rowValueYrDetail = (String) bundle.get(MyAppConstants.YR_DETAIL_VALUE);

        }

        try {
            parseYearDetailFlowData();
        }catch(Exception e){

        }

        txtRecycleHeader = (TextView) findViewById(R.id.txt_recycle_header);
        txtRecycleHeader.setText(rowValueYrDetail);


        txt_name = (TextView) findViewById(R.id.textName);
        txt_discrip = (TextView) findViewById(R.id.textDiscrip);
        txt_status = (TextView) findViewById(R.id.textStatus);
        textRegion = (TextView) findViewById(R.id.textRegion);
        txt_country = (TextView) findViewById(R.id.textCountry);
        txt_sop = (TextView) findViewById(R.id.textSop);
        txt_eop = (TextView) findViewById(R.id.textEop);



       /* for (Map.Entry<String, Map<String, String>> entry : result.entrySet()) {
            Log.v("test 123", String.valueOf(entry.getValue()));
            res_detail= entry.getValue();
        }*/




      if(null != result) {
            for (Map.Entry<String, String> entry : result.entrySet()) {
                if (entry.getKey().equals("country")) {
                    txt_country.setText(entry.getValue());
                } else if (entry.getKey().equals("sop")) {
                    txt_sop.setText(entry.getValue());
                } else if (entry.getKey().equals("eop")) {
                    txt_eop.setText(entry.getValue());
                } else if (entry.getKey().equals("name")) {
                    txt_name.setText(entry.getValue());
                } else if (entry.getKey().equals("description")) {
                    txt_discrip.setText(entry.getValue());
                } else if (entry.getKey().equals("region")) {
                    textRegion.setText(entry.getValue());
                } else if (entry.getKey().equals("status")) {
                    txt_status.setText(entry.getValue());
                }
            }
        }

       /* "country": "Turkey",
                "sop": "7/1/16",
                "eop": "3/28/23",
                "name": "2017 - Renault - Fluence - (LFF) - Seat - Insert Fabrics - Akris  Bali serge fabrics",
                "description": "Akris  Bali serge fabrics",
                "region": "Eastern Europe",
                "status": "Won"


    */

        getSupportActionBar().setTitle(getResources().getString(R.string.bread_head)+"/"+rowValueSticky+"/"+rowValueYr);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }

    private void parseYearDetailFlowData() {
        result = MySingleTon.parseJsonYearFlowDetail(rowValueSticky,rowValueYr,rowValueYrDetail);

    }


  /*  private static List<Country> getCountryList() {
        List<Country> countries = new ArrayList<>();
        List<String> usaCities = Arrays.asList("972483: 2017 Fluence", "965070: 2017 Fluence", "972484: 2017 Fluence", "965069: 2017 Fluence");
        countries.add(new Country("2017", usaCities));


        List<String> frenchCities = Arrays.asList("1193990: 2012 630", "1193989: 2012 630");
        countries.add(new Country("2012", frenchCities));


        List<String> germanCities = Arrays.asList("1057708: 2015 XC60");
        countries.add(new Country("2015 ", germanCities));

        return countries;
    }*/


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                return true;
            case R.id.logout:

                SharedPreferences sharedpreferences = getSharedPreferences(MyAppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
                checkRememberMe(sharedpreferences);
                // SharedPreferences.Editor editor = sharedpreferences.edit();
                // editor.remove(MyAppConstants.SESSION_KEY); // will delete key
                // editor.commit();

                  /*finish();
                System.exit(0);*/
                Intent makerIntent = new Intent(DetailActivity.this, LoginActivity.class);
                startActivity(makerIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void checkRememberMe(SharedPreferences sharedpreferences){



        if(MySingleTon.rememberMe){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(MyAppConstants.SESSION_KEY);
            editor.commit();
        }
        else{
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(MyAppConstants.SESSION_KEY);
            editor.remove(MyAppConstants.USER);
            editor.remove(MyAppConstants.PASSWORD);
            editor.commit();
        }
    }

}
