package com.adient.mobility.sqra.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.SearchView;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.adapters.CustomExpandableListAdapter;
import com.adient.mobility.sqra.filter.DataContent;
import com.adient.mobility.sqra.filter.DataItem;
import com.adient.mobility.sqra.global.global;
import com.adient.mobility.sqra.util.MyAppConstants;
import com.adient.mobility.sqra.util.MySingleTon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ListDetailActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener {

    static final String[] numbers = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private GridView gridView;
    private String jsonResult;
    private SearchView mSearchView;
    private Map<String, String> result;

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
    private ArrayList<String> sqrcArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        gridView = (GridView) findViewById(R.id.ygridView);
        mSearchView = (SearchView)findViewById(R.id.search_yview);

        jsonResult = MySingleTon.json;
        parseSqrcData();

        gridView.setNumColumns(numbers.length);
        gridViewSetting(gridView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, numbers);

        gridView.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            rowValueSticky = (String) bundle.get(MyAppConstants.SQRC_VALUE);
        }

        TextView name = (TextView)findViewById(R.id.detail_text_name);
        name.setText(rowValueSticky);

        parseYearData();
        parseYearDetailData();

        Collections.sort(yearArrayList);

        expandableListView = (ExpandableListView) findViewById(R.id.expandview);
        // expandableListDetail = ExpandableListDataPump.getData();
        // expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        //expandableListAdapter = new CustomExpandableListAdapter(this, yearArrayList, expandableListDetail);
        expandableListAdapter = new CustomExpandableListAdapter(this, DataContent.ITEM_LIST);

        DataContent.ITEM_LIST.clear();

        expandableListAdapter.setOnChildClickListener(this);
        expandableListAdapter.setOnGroupClickListener(this);
        expandableListView.setAdapter(expandableListAdapter);


        setupSearchView();

    }

    private void gridViewSetting(GridView gridview) {

        int size= numbers.length;
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
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> region = new HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>>();
        Gson gson = new Gson();
        Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> sqrcMap = (Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>>) gson.fromJson(jsonResult, region.getClass());
        MySingleTon.sqrcData = sqrcMap;
        sqrcArrayList = new ArrayList<String>(sqrcMap.keySet());
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

        global.detailVr = movie.toString();
        global.groupVr = group;

        parseYearDetailFlowData(group, rowValueSticky, movie.toString());


        Dialog dialog = new Dialog(ListDetailActivity.this);
        dialog.setContentView(R.layout.activity_detail);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView txt_name = (TextView) dialog.findViewById(R.id.textName);
        TextView txt_discrip = (TextView) dialog.findViewById(R.id.textDiscrip);
        TextView txt_status = (TextView) dialog.findViewById(R.id.textStatus);
        TextView textRegion = (TextView) dialog.findViewById(R.id.textRegion);
        TextView txt_country = (TextView) dialog.findViewById(R.id.textCountry);
        TextView txt_sop = (TextView) dialog.findViewById(R.id.textSop);
        TextView txt_eop = (TextView) dialog.findViewById(R.id.textEop);

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

        dialog.show();

        return true;
    }
    private void parseYearDetailFlowData(String group, String value, String movie) {
        result = MySingleTon.parseJsonYearFlowDetail(rowValueSticky, group, movie);

    }

}
