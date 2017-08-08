package com.adient.mobility.sqra.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.adapters.CustomExpandableListAdapter;
import com.adient.mobility.sqra.filter.DataContent;
import com.adient.mobility.sqra.filter.DataItem;
import com.adient.mobility.sqra.util.MyAppConstants;
import com.adient.mobility.sqra.util.MySingleTon;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExpandableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {

    private SearchView mSearchView;

    ExpandableListView expandableListView;
    private String rowValueSticky;
    private Map<String, Map<String, Map<String, Map<String, String>>>> dataYear;
     ArrayList<String> yearArrayList;

    private Map<String, Map<String, Map<String, String>>> dataDetail;

    HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
     CustomExpandableListAdapter expandableListAdapter;
    private ImageView imgExpand;
    private boolean expand;
    private ImageView imgSort;
    private static int sortOrder=0;
    private TextView txtRecycleHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable);
        expand=false;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            rowValueSticky = (String) bundle.get(MyAppConstants.SQRC_VALUE);
        }

        parseYearData();
        parseYearDetailData();

        Collections.sort(yearArrayList);
       // Collections.sort(yearArrayList, DataItem.Comparators.YEAR);



        txtRecycleHeader = (TextView) findViewById(R.id.txt_recycle_header);
        txtRecycleHeader.setText(getResources().getString(R.string.header_expand)+"/"+rowValueSticky);

                expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
               // expandableListDetail = ExpandableListDataPump.getData();
               // expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                //expandableListAdapter = new CustomExpandableListAdapter(this, yearArrayList, expandableListDetail);
                expandableListAdapter = new CustomExpandableListAdapter(this, DataContent.ITEM_LIST);

                 DataContent.ITEM_LIST.clear();





            expandableListAdapter.setOnChildClickListener(this);
            expandableListAdapter.setOnGroupClickListener(this);
                expandableListView.setAdapter(expandableListAdapter);



        mSearchView = (SearchView) findViewById(R.id.search_view);


        imgExpand = (ImageView) findViewById(R.id.img_expand);
        imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expand){
                    collapseAll();
                }else{
                    expandAll();
                }
            }
        });


        imgSort = (ImageView) findViewById(R.id.img_sort);
        imgSort.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
               // expandAll();
                sortOrder++;
                if(sortOrder%2==0){
                    //Collections.sort(yearArrayList,Collections.reverseOrder());
                  //  expandableListAdapter.sortGroup(0);
                }else {
                    // Collections.sort(yearArrayList,Collections.reverseOrder() );
                    //   expandableListAdapter.sortGroup(yearArrayList.size()-1);
                }
               expandableListAdapter.sortAllChildren();
                //expandableListAdapter.sortGroup(0);
            }
        });


        int magId = getResources().getIdentifier("android:id/search_go_btn", null, null);
        ImageView magImage = (ImageView) mSearchView.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        magImage.setVisibility(View.GONE);

        setupSearchView();


        getSupportActionBar().setTitle(getResources().getString(R.string.bread_head)+" / "+rowValueSticky);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }


    private void expandAll() {
        expand=true;
        int count = expandableListAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.expandGroup(i);
        }
    }

    private void collapseAll()
    {
        expand=false;
        int count = expandableListAdapter.getGroupCount();
        for (int i = 0; i < count; i++)
        {
            expandableListView.collapseGroup(i);
        }
    }


    private void parseYearData() {
        dataYear = MySingleTon.parseJson2ndLevel(rowValueSticky);
        yearArrayList = new ArrayList<String>(dataYear.keySet());
    }

    private void parseYearDetailData() {
        for (String data:yearArrayList) {
            dataDetail = MySingleTon.parseJsonYear(data);

            int iData=Integer.parseInt(data);

            ArrayList<String> childDataDetail = new ArrayList<String>(dataDetail.keySet());

           // MySingleTon.yrDetailArrayList = childDataDetail;

            for (String child: childDataDetail) {
                DataContent.addItem(DataContent.newMovie(child, iData, true));
            }

          //  expandableListDetail.put(data, MySingleTon.yrDetailArrayList);
        }

      //  this.yearArrayList = new ArrayList<String>(dataYear.keySet());
    }



    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Please enter manufacturing site");//MyAppConstants.SEARCH_HINT);
        mSearchView.clearFocus();
    }



    @Override
    public boolean onQueryTextChange(String query) {
        expandAll();
        if(!TextUtils.isEmpty(query)) {
            expandableListAdapter.getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        expandAll();
        expandableListAdapter.getFilter().filter(query);
        return true;
    }





    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
        String group = expandableListAdapter.getGroup(groupPosition).toString();
        //ToastHelper.showGroupClicked(this, group);
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        DataItem movie = expandableListAdapter.getChild(groupPosition, childPosition);
        String group = expandableListAdapter.getGroup(groupPosition).toString();
        Toast.makeText(getApplicationContext(),movie+"    "+group,Toast.LENGTH_SHORT);

        Intent intent = new Intent(ExpandableActivity.this, DetailActivity.class);
        intent.putExtra(MyAppConstants.YEAR_VALUE, group);
        intent.putExtra(MyAppConstants.SQRC_VALUE, rowValueSticky);
        intent.putExtra(MyAppConstants.YR_DETAIL_VALUE, movie.toString());
        startActivity(intent);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // go to previous screen when app icon in action bar is clicked
                /*Intent intent = new Intent(this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                this.onBackPressed();
                return true;
            case R.id.logout:

                SharedPreferences sharedpreferences = getSharedPreferences(MyAppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
                checkRememberMe(sharedpreferences);

                Intent makerIntent = new Intent(ExpandableActivity.this, LoginActivity.class);
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

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }



}




