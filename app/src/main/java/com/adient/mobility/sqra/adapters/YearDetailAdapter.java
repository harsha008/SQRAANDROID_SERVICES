package com.adient.mobility.sqra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.global.global;

import java.util.ArrayList;

/**
 * Created by Mobile-Tiger on 4/13/2017.
 */

public class YearDetailAdapter extends ArrayAdapter<String>{


    public YearDetailAdapter(Context context, ArrayList<String> countries) {
        super(context, 0, countries);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        if (rowView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }


        TextView name = (TextView)rowView.findViewById(R.id.expandedListItem);

        name.setText(getItem(position));

        return rowView;
    }
}
