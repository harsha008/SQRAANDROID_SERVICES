package com.adient.mobility.sqra.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.adapters.CustomExpandableListAdapter;
import com.adient.mobility.sqra.filter.DataContent;
import com.adient.mobility.sqra.global.global;
import com.adient.mobility.sqra.util.MySingleTon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mobile-Tiger on 4/15/2017.
 */



public class DetailFragment extends Fragment implements SearchView.OnQueryTextListener {

    private String jsonResult;
    private Map<String, String> result;
    private ExpandableListView expandableListView;
    private String rowValueSticky;
    private Map<String, Map<String, Map<String, Map<String, String>>>> dataYear;
    ArrayList<String> yearArrayList;

    private Map<String, Map<String, Map<String, String>>> dataDetail;

    HashMap<String, List<String>> expandableListDetail;
    CustomExpandableListAdapter expandableListAdapter;
    private ImageView imgExpand;
    private boolean expand;
    private ImageView imgSort;
    private static int sortOrder=0;
    private TextView txtRecycleHeader;
    private SearchView mSearchView;
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            expandAll();
            if(!TextUtils.isEmpty(query)) {
                expandableListAdapter.getFilter().filter(query);
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {

            //  if(!TextUtils.isEmpty(query)) {
            expandAll();
            expandableListAdapter.getFilter().filter(query);
            return true;

        }


    };

    public DetailFragment() {
        expandableListDetail = new HashMap<String, List<String>>();
    }

    public static DetailFragment newInstance() {

        return new DetailFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);


        inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);


        return init(inflater.inflate(R.layout.detail_fragment, container, false));
    }


    private View init(View inflate) {
        expandableListView = (ExpandableListView) inflate.findViewById(R.id.expandview);
        mSearchView        = (SearchView)inflate.findViewById(R.id.search_detail_view);

        rowValueSticky = global.rowValue;

        TextView name = (TextView)inflate.findViewById(R.id.detail_text_name);
        ImageView imgView = (ImageView)inflate.findViewById(R.id.detail_img_flag);

        name.setText(rowValueSticky);

        for(int i = 0; i < global.countries.length; i++) {
            if(rowValueSticky.toLowerCase().contains(global.countries[i])) {

                final int id = name.getContext().getResources().getIdentifier(global.countries[i], "drawable", name.getContext().getPackageName());
                imgView.setImageResource(id);
                break;
            }
        }


        parseYearData();
        parseYearDetailData();

        Collections.sort(yearArrayList);

        expandableListView = (ExpandableListView) inflate.findViewById(R.id.expandview);
        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), DataContent.ITEM_LIST);

        DataContent.ITEM_LIST.clear();

        expandableListView.setAdapter(expandableListAdapter);

        setupSearchView();


        return  inflate;
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

        //  this.secondLevelArrayList = new ArrayList<String>(dataYear.keySet());
    }
    private void expandAll() {
        expand=true;
        int count = expandableListAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.expandGroup(i);
        }
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

}
