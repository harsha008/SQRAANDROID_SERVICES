package com.adient.mobility.sqra.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.fragments.YearListFragment;
import com.adient.mobility.sqra.global.global;
import com.adient.mobility.sqra.util.MyAppConstants;
import com.adient.mobility.sqra.util.MySingleTon;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    static String[] numbers = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private GridView gridView;
    private Button logout;
    private HorizontalScrollView scrollView;
    private String[] adapterNums;
//    private SearchView mSearchView;


    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);


        global.countryVr = "A";

//        ScrollView scrollView = (ScrollView) new HorizontalScrollView(this);

        scrollView = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
        logout = (Button)findViewById(R.id.logout);
        gridView = (GridView) findViewById(R.id.gridView);
//        mSearchView = (SearchView)findViewById(R.id.search_view);

//        loadJSONFromAsset();
        global.jsonResult = MySingleTon.json;
        parseSqrcData();

//        for(int i = 0;i < sqrcArrayList.size(); i++) {
//            String first = sqrcArrayList.get(i).substring(0, 1);
//
//            for(int j = 0;j < numbers.length;j ++) {
//                boolean flag = false;
//                if(first.equals(numbers[j])) {
//                    flag = true;
//                }
//            }
//
//        }

        adapterNums = numbers;
        List<String> numlist = new ArrayList<String>();
        for(int i = 0;i < adapterNums.length; i++) {
            String first = adapterNums[i];
            boolean flag = false;
            for(int j = 0;j < global.sqrcArrayList.size();j ++) {

                if(first.equals(global.sqrcArrayList.get(j).substring(0, 1))) {
                    flag = true;
                }
                if(flag)
                    break;
            }

            if(flag != false){
                numlist.add(first);
            }
        }


        adapterNums = numlist.toArray(new String[numlist.size()]);
        numlist.clear();
        int j = 0;
        while(j < 20) {
            for(int i = 0;i < adapterNums.length;i ++) {
                numlist.add(adapterNums[i]);
            }
            j ++;
        }

        adapterNums = numlist.toArray(new String[numlist.size()]);

        gridView.setNumColumns(adapterNums.length);
        gridViewSetting(gridView);

        numlist.clear();
//
//

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, adapterNums);
        gridView.setAdapter(adapter);

//        int index = gridView.getFirstVisiblePosition();
//        gridView.smoothScrollToPosition(index);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                global.gridIndex = position;

                GradientDrawable gd = new GradientDrawable();
                gd.setColor(Color.GRAY); // Changes this drawbale to use a single color instead of a gradient
                gd.setCornerRadius(100);
                gd.setStroke(1, Color.GRAY);



                int length = adapterNums.length ;
                for(int i = 0;i < length;i ++) {
                    View v = gridView.getChildAt(i);

                    if(i % 14 == position % 14) {

                        ((TextView) v).setBackgroundColor(Color.GRAY);
                        ((TextView) v).setBackgroundDrawable(gd);
                    } else {
                        ((TextView) v).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                global.countryVr = ((TextView) view).getText().toString();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, YearListFragment.newInstance(), "first").commit();
            }
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, YearListFragment.newInstance(), "first").commit();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences(MyAppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
                checkRememberMe(sharedpreferences);

                Intent makerIntent = new Intent(FragmentActivity.this, LoginActivity.class);
                startActivity(makerIntent);
                finish();

            }
        });


        setupSearchView();
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

    private void gridViewSetting(GridView gridview) {

        int size= numbers.length * 20;
        // Calculated single Item Layout Width for each grid element ....
        int width = 50 ;

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(singleItemWidth);
        gridview.setHorizontalSpacing(2);
        gridview.setStretchMode(GridView.STRETCH_SPACING);
        gridview.setNumColumns(size);
    }

    private void parseSqrcData() {

//        StringBuffer sb = new StringBuffer();
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(getApplicationContext().getAssets().open(
//                    MyAppConstants.JSON_FILE)));
//            String temp;
//            while ((temp = br.readLine()) != null)
//                sb.append(temp);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                br.close(); // stop reading
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        String myjsonstring = sb.toString();
//        MySingleTon.json=myjsonstring;

//        global.jsonResult = MySingleTon.json;
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> region = new HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>>();
        Gson gson = new Gson();
        Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> sqrcMap = (Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>>) gson.fromJson(MySingleTon.json, region.getClass());
        MySingleTon.sqrcData = sqrcMap;
        global.sqrcArrayList = new ArrayList<String>(sqrcMap.keySet());

    }

    private void setupSearchView() {
//        mSearchView.setIconifiedByDefault(false);
//        mSearchView.setOnQueryTextListener(this);
//        mSearchView.setSubmitButtonEnabled(true);
//        mSearchView.setQueryHint("Please enter manufacturing site");//MyAppConstants.SEARCH_HINT);
//        mSearchView.clearFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void loadJSONFromAsset() {



    }

}


