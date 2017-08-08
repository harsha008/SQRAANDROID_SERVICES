package com.adient.mobility.sqra.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.adapters.RecyclerArrayAdapter;
import com.adient.mobility.sqra.adapters.RecyclerItemClickListener;
import com.adient.mobility.sqra.customview.DividerDecoration;
import com.adient.mobility.sqra.util.MyAppConstants;
import com.adient.mobility.sqra.util.MySingleTon;
import com.adient.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.adient.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.adient.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewStickyHeaderActivity extends AppCompatActivity {

    private SampleArrayHeadersAdapter mAdapter;


    private String jsonResult;
    private ArrayList<String> sqrcArrayList;
    private RecyclerView recyclerView;
    private StickyRecyclerHeadersDecoration headersDecor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sticky_header);

        jsonResult = MySingleTon.json;

        parseSqrcData();

//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setTitle(R.string.bread_head);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        // Set adapter populated with example dummy data
        mAdapter = new SampleArrayHeadersAdapter();
        Collections.sort(sqrcArrayList);

        mAdapter.addAll(sqrcArrayList);
        recyclerView.setAdapter(mAdapter);

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
                        Toast.makeText(NewStickyHeaderActivity.this, "Header position: " + position + ", id: " + headerId,
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

                Intent intent = new Intent(NewStickyHeaderActivity.this, ExpandableActivity.class);
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

    }

    private int getLayoutManagerOrientation(int activityOrientation) {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return LinearLayoutManager.VERTICAL;
        } else {
            return LinearLayoutManager.HORIZONTAL;
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
                Intent makerIntent = new Intent(NewStickyHeaderActivity.this, LoginActivity.class);
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
            return getItem(position).charAt(0);
//            return getItem(position).substring(0, getItem(position).indexOf(":"));
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

//            textView.setText(String.valueOf(getItem(position).charAt(0)));
            String text = getItem(position);
            if(getItem(position).indexOf(":")>-1){
                text = getItem(position).substring(0, getItem(position).indexOf(":"));
            }
            textView.setText(text);
        }

   /* private int getRandomColor() {
      SecureRandom rgen = new SecureRandom();
      return Color.HSVToColor(150, new float[]{
          rgen.nextInt(100), 1, 1
      });
    }*/

    }
}
