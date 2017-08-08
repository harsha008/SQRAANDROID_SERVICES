package com.adient.mobility.sqra.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.adapters.CustomExpandableListAdapter;
import com.adient.mobility.sqra.adapters.YearDetailAdapter;
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



public class DetailsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private ListView listView;
    private String rowValueSticky;
    private Map<String, Map<String, Map<String, Map<String, String>>>> dataSecondLevel;
    ArrayList<String> yearDetailArrayList;

    private Map<String, Map<String, Map<String, String>>> dataYear;
    private Map<String, Map<String, String>> dataYearDetail;

    HashMap<String, List<String>> expandableListDetail;
    private SearchView mSearchView;
    YearDetailAdapter adapter;


    public DetailsFragment() {
        expandableListDetail = new HashMap<String, List<String>>();
    }

    public static DetailsFragment newInstance() {

        return new DetailsFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);


        return init(inflater.inflate(R.layout.details_fragment, container, false));
    }


    private View init(View inflate) {
        listView = (ListView) inflate.findViewById(R.id.expandview);
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

        parseData();

        Collections.sort(yearDetailArrayList);

        adapter = new YearDetailAdapter(getContext(), yearDetailArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                global.detailVr = yearDetailArrayList.get(position);

//                Map<String, String> result = MySingleTon.parseJsonYearFlowDetail(global.rowValue, global.groupVr, global.detailVr);
                Map<String, String> result = new HashMap<String, String>();

                for (Map.Entry<String, Map<String, String>> entry : MySingleTon.yearDetailData.entrySet()) {
                    String key = entry.getKey();
                    result= entry.getValue();
                }

                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                Dialog dialog = new Dialog(inflater.getContext());
                dialog.setContentView(R.layout.detail);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView txt_header = (TextView)dialog.findViewById(R.id.text_header);
                TextView txt_name = (TextView) dialog.findViewById(R.id.textName);
                TextView txt_discrip = (TextView) dialog.findViewById(R.id.textDiscrip);
                TextView txt_status = (TextView) dialog.findViewById(R.id.textStatus);
                TextView textRegion = (TextView) dialog.findViewById(R.id.textRegion);
                TextView txt_country = (TextView) dialog.findViewById(R.id.textCountry);
                TextView txt_sop = (TextView) dialog.findViewById(R.id.textSop);
                TextView txt_eop = (TextView) dialog.findViewById(R.id.textEop);

                txt_header.setText(global.detailVr);

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
            }
        });

        setupSearchView();


        return  inflate;
    }

    private void parseData() {
        yearDetailArrayList = new ArrayList<String>();
        dataSecondLevel = MySingleTon.parseJson2ndLevel(rowValueSticky);
        ArrayList<String> secondLevelArrayList = new ArrayList<String>(dataSecondLevel.keySet());
        for (String data: secondLevelArrayList) {
            dataYear = MySingleTon.parseJsonYear(data);
            ArrayList<String> yearArrayList = new ArrayList<String>(dataYear.keySet());

            for (String year: yearArrayList) {
                dataYearDetail = MySingleTon.parseJsonYearDetail(year);
                yearDetailArrayList.addAll(new ArrayList<String>(dataYearDetail.keySet()));
            }
        }

        Log.d("yearDetailArrayList","yearDetailArrayList::  "+yearDetailArrayList);
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
        query = query.toLowerCase();

        ArrayList<String> filteredList = new ArrayList<>();

        if(query.length()>0) {

            for (int i = 0; i < yearDetailArrayList.size(); i++) {

                final String text = yearDetailArrayList.get(i).toLowerCase();
                if (text.contains(query)) {
                    filteredList.add(yearDetailArrayList.get(i));
                }
            }
        } else {
            filteredList = yearDetailArrayList;
        }

        adapter.clear();

        Collections.sort(filteredList);
        adapter.addAll(filteredList);
        listView.setAdapter(adapter);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

}
