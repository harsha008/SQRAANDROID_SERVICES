package com.adient.mobility.sqra.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.adient.mobility.sqra.customview.DividerDecoration;
import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.adapters.RecyclerArrayAdapter;
import com.adient.mobility.sqra.adapters.RecyclerItemClickListener;
import com.adient.mobility.sqra.util.MyAppConstants;
import com.adient.mobility.sqra.util.MySingleTon;
import com.adient.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.adient.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.adient.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickyHeaderActivity extends AppCompatActivity implements View.OnClickListener {//implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private SampleArrayHeadersAdapter mAdapter;


    private String jsonResult;
    private ArrayList<String> sqrcArrayList;
    private RecyclerView recyclerView;
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {

          //  if(!TextUtils.isEmpty(query)) {
                query = query.toLowerCase();

                final List<String> filteredList = new ArrayList<>();

                for (int i = 0; i < sqrcArrayList.size(); i++) {

                    final String text = sqrcArrayList.get(i).toLowerCase();
                    //if (text.contains(query)) {
                    if (text.contains(query)) {

                        filteredList.add(sqrcArrayList.get(i));
                    }
                }


                mAdapter.clear();
                // Set adapter populated with example dummy data
                mAdapter = new SampleArrayHeadersAdapter();

                Collections.sort(filteredList);
                mAdapter.addAll(filteredList);//values);
                recyclerView.setAdapter(mAdapter);

            // Add the sticky headers decoration
            headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
            recyclerView.addItemDecoration(headersDecor);
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

        public boolean onQueryTextSubmit(String query) {
            txtRecycleHeader.setText(getResources().getString(R.string.header_expand)+": "+query);
            getSupportActionBar().setTitle(getResources().getString(R.string.bread_head)+" / "+query);
            return false;
        }
    };
    private ImageView imgAscend;
    private boolean ascendSortingOrder = false;
    private StickyRecyclerHeadersDecoration headersDecor;
    private static int sortOrder=0;
    private TextView txtRecycleHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //jsonResult = loadJSONFromAsset();
        jsonResult = MySingleTon.json;

        parseSqrcData();


        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.bread_head);


        txtRecycleHeader = (TextView) findViewById(R.id.txt_recycle_header);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        imgAscend = (ImageView) findViewById(R.id.img_ascend);
        imgAscend.setOnClickListener(this);

        // Set adapter populated with example dummy data
        mAdapter = new SampleArrayHeadersAdapter();
//ascendSortingOrder=true;
        Collections.sort(sqrcArrayList);
        //System.out.println(Arrays.toString(sqrcArrayList.toArray()));
    /*Collections.sort(colours, Collections.reverseOrder());
    System.out.println(Arrays.toString(colours.toArray()));*/

        mAdapter.addAll(sqrcArrayList);//values);
        recyclerView.setAdapter(mAdapter);

        // Set SearchView
        mSearchView = (SearchView) findViewById(R.id.search_view);

        int magId = getResources().getIdentifier("android:id/search_go_btn", null, null);
        ImageView magImage = (ImageView) mSearchView.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        magImage.setVisibility(View.GONE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Add the sticky headers decoration
        headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        recyclerView.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
        recyclerView.addItemDecoration(new DividerDecoration(this));

        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        Toast.makeText(StickyHeaderActivity.this, "Header position: " + position + ", id: " + headerId,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //mAdapter.remove(mAdapter.getItem(position));


                TextView textView = (TextView) view.findViewById(R.id.txt_item);
                String text = textView.getText().toString();

                Intent intent = new Intent(StickyHeaderActivity.this, ExpandableActivity.class);
                intent.putExtra(MyAppConstants.SQRC_VALUE, text);
                startActivity(intent);

            }
        }));
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });


        setupSearchView();


    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(listener);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Please enter manufacturing site");//MyAppConstants.SEARCH_HINT);
        mSearchView.clearFocus();
    }

    private int getLayoutManagerOrientation(int activityOrientation) {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return LinearLayoutManager.VERTICAL;
        } else {
            return LinearLayoutManager.HORIZONTAL;
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.img_ascend) {

            sortOrder++;
            if(sortOrder%2==0){
                Collections.sort(sqrcArrayList);
            }else {
                Collections.sort(sqrcArrayList, Collections.reverseOrder());
            }
            mAdapter.clear();
            mAdapter = new SampleArrayHeadersAdapter();
            mAdapter.addAll(sqrcArrayList);

            headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
            headersDecor.invalidateHeaders();
            recyclerView.addItemDecoration(headersDecor);

            recyclerView.setAdapter(mAdapter);

            /*mAdapter = null;
            mAdapter = new SampleArrayHeadersAdapter();
            Collections.sort(sqrcArrayList, Collections.reverseOrder());
            Log.v("tssts", sqrcArrayList + "");
            //    Collections.reverse(sqrcArrayList);
            mAdapter.addAll(sqrcArrayList);//values);

            recyclerView.setAdapter(mAdapter);
            recyclerView.invalidate();

            headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
            recyclerView.addItemDecoration(headersDecor);
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    headersDecor.invalidateHeaders();
                }
            });


            mAdapter.notifyDataSetChanged();
            recyclerView.invalidate();*/

        }
    }

    private void parseSqrcData() {
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>> region = new HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>>();
        Gson gson = new Gson();
        Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> sqrcMap = (Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>>) gson.fromJson(jsonResult, region.getClass());
        MySingleTon.sqrcData = sqrcMap;
        sqrcArrayList = new ArrayList<String>(sqrcMap.keySet());
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
                // SharedPreferences.Editor editor = sharedpreferences.edit();
                // editor.remove(MyAppConstants.SESSION_KEY); // will delete key
                // editor.commit();

                  /*finish();
                System.exit(0);*/
                Intent makerIntent = new Intent(StickyHeaderActivity.this, LoginActivity.class);
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



    /*public String loadJSONFromAsset() {

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
        return myjsonstring;
    }*/

    private class SampleArrayHeadersAdapter extends RecyclerArrayAdapter<String, RecyclerView.ViewHolder>
            implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(getItem(position));

        }

        @Override
        public long getHeaderId(int position) {
     /* if (position == 0) {
        return -1;
      } else {*/
            return getItem(position).charAt(0);
            //}
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_header, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;

            textView.setText(String.valueOf(getItem(position).charAt(0)));
            //  holder.itemView.setBackgroundColor(getRandomColor());
        }

   /* private int getRandomColor() {
      SecureRandom rgen = new SecureRandom();
      return Color.HSVToColor(150, new float[]{
          rgen.nextInt(100), 1, 1
      });
    }*/

    }
}
