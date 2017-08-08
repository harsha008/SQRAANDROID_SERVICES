package com.adient.mobility.sqra.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.adapters.CountryListAdapater;
import com.adient.mobility.sqra.adapters.StickyAdapter;
import com.adient.mobility.sqra.global.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Mobile-Tiger on 4/14/2017.
 */



public class YearListFragment extends Fragment implements
        AdapterView.OnItemClickListener/*, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener*/ {

    static final String[] numbers = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    ExpandableListView expandableListView;
    private ListView listView;


    private ArrayList<String> availablelists;

    private SearchView mSearchView;
    private CountryListAdapater mAdapter;

    private Map<String, Map<String, Map<String, String>>> dataYear;
    ArrayList<String> yearArrayList;

    private StickyListHeadersListView stickyList;

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {

            //  if(!TextUtils.isEmpty(query)) {
            query = query.toLowerCase();

            final List<String> filteredList = new ArrayList<>();

            for (int i = 0; i < availablelists.size(); i++) {

                final String text = availablelists.get(i).toLowerCase();
                //if (text.contains(query)) {
                if (text.contains(query)) {

                    filteredList.add(availablelists.get(i));
                }
            }


            mAdapter.clear();
            // Set adapter populated with example dummy data


            Collections.sort(filteredList);
            mAdapter.addAll(filteredList);//values);
            listView.setAdapter(mAdapter);

            // Add the sticky headers decoration

           /* }else{
                // Set adapter populated with example dummy data
                mAdapter.clear();
                mAdapter = new SampleArrayHeadersAdapter();
                Collections.sort(sqrcArrayList);
                mAdapter.addAll(sqrcArrayList);//values);
                recyclerView.setAdapter(mAdapter);
            }*/

            return true;

        }


    };

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(listener);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search with site id, city or country ");//MyAppConstants.SEARCH_HINT);
        mSearchView.clearFocus();
    }
    public static YearListFragment newInstance() {

        return new YearListFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_year_list, container, false);

        Activity activity = getActivity();

        String value = global.countryVr;

        listView = (ListView) view.findViewById(R.id.list_country);
        mSearchView = (SearchView) view.findViewById(R.id.search_yview);



//        ArrayList<Country> countries = new ArrayList<Country>();
        availablelists  = new ArrayList<String>();

        for(int i = 0;i < global.sqrcArrayList.size();i ++) {

            String first = global.sqrcArrayList.get(i).substring(0, 1);
            if(first.equals(value)) {
                availablelists.add(global.sqrcArrayList.get(i));
            }
        }

        mAdapter = new CountryListAdapater(getActivity(), availablelists);
        expandableListView = new ExpandableListView(getActivity());

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String title = ((TextView)view.findViewById(R.id.txt_name)).getText().toString();
                ImageView img=(ImageView)view.findViewById(R.id.img_flag);

                img.getImageAlpha();

                global.rowValue = title;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, DetailFragment.newInstance(), "second").commit();

            }
        });

        setupSearchView();

        Collections.sort(availablelists, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        StickyAdapter mStickyAdapter = new StickyAdapter(getContext(), availablelists);

        stickyList = (StickyListHeadersListView) view.findViewById(R.id.list);
        stickyList.setOnItemClickListener(this);
        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);
        stickyList.setAdapter(mStickyAdapter);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getContext(), "Item " + position + " clicked!", Toast.LENGTH_SHORT).show();

        String title = ((TextView)view.findViewById(R.id.txt_name)).getText().toString();
        ImageView img=(ImageView)view.findViewById(R.id.img_flag);

        img.getImageAlpha();

        global.rowValue = title;
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, DetailsFragment.newInstance(), "second").commit();
    }

}
