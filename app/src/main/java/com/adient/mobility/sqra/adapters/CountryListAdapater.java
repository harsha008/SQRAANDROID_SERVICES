package com.adient.mobility.sqra.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.customview.Country;
import com.adient.mobility.sqra.fragments.DetailFragment;
import com.adient.mobility.sqra.global.global;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Mobile-Tiger on 4/13/2017.
 */

public class CountryListAdapater extends ArrayAdapter<String>{


    public CountryListAdapater(Context context, ArrayList<String> countries) {
        super(context, 0, countries);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        if (rowView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_row, parent, false);
        }


        TextView name = (TextView)rowView.findViewById(R.id.txt_name);
        ImageView flag = (ImageView)rowView.findViewById(R.id.img_flag);



        String imgName = getItem(position);

//        imgName = imgName.toLowerCase();
////        int imageResource = getResourc es().getIdentifier(uri, null, getPackageName());

//        Locale[] locale = Locale.getAvailableLocales();
//        ArrayList<String> countries = new ArrayList<String>();
        String country;
//        for( Locale loc : locale ){
//            country = loc.getDisplayCountry();
//            if( country.length() > 0 && !countries.contains(country) ){
//                countries.add( country );
//            }
//        }
        country = "";
        if(imgName != null) {
            for(int i = 0; i < global.countries.length; i++) {
                if(imgName.toLowerCase().contains(global.countries[i])) {

                    country = global.countries[i];
                    break;
                }
            }
            if(country.isEmpty()) {
//                String ctrName = (imgName.split(";"))[1].toString();
                for(int i = 0; i < global.usa.length; i++) {
                    if(imgName.contains(global.usa[i])) {

                        country = "usa";
                        break;
                    }
                }
                for(int i = 0; i < global.mexico.length; i++) {
                    if(imgName.contains(global.mexico[i])) {

                        country = "mexico";
                        break;
                    }
                }
                for(int i = 0; i < global.canada.length; i++) {
                    if(imgName.contains(global.canada[i])) {

                        country = "canada";
                        break;
                    }
                }
                for(int i = 0; i < global.uk.length; i++) {
                    if(imgName.contains(global.uk[i])) {

                        country = "uk";
                        break;
                    }
                }
                for(int i = 0; i < global.germany.length; i++) {
                    if(imgName.contains(global.germany[i])) {

                        country = "germany";
                        break;
                    }
                }
                for(int i = 0; i < global.southafrica.length; i++) {
                    if(imgName.contains(global.southafrica[i])) {

                        country = "southafrica";
                        break;
                    }
                }
                for(int i = 0; i < global.poland.length; i++) {
                    if(imgName.contains(global.poland[i])) {

                        country = "poland";
                        break;
                    }
                }
                for(int i = 0; i < global.romania.length; i++) {
                    if(imgName.contains(global.romania[i])) {

                        country = "romania";
                        break;
                    }
                }
                for(int i = 0; i < global.spain.length; i++) {
                    if(imgName.contains(global.spain[i])) {

                        country = "spain";
                        break;
                    }
                }
                for(int i = 0; i < global.czech.length; i++) {
                    if(imgName.contains(global.czech[i])) {

                        country = "czech";
                        break;
                    }
                }
                for(int i = 0; i < global.Brazil.length; i++) {
                    if(imgName.contains(global.Brazil[i])) {

                        country = "brazil";
                        break;
                    }
                }
                for(int i = 0; i < global.china.length; i++) {
                    if(imgName.contains(global.china[i])) {

                        country = "china";
                        break;
                    }
                }

            }
        }

        if(country.equals("south africa") )
            country = "southafrica";
        if(country.equals("south korea"))
            country = "southkorea";

        Context context = flag.getContext();
//        imgName.
        final int id = context.getResources().getIdentifier(country, "drawable", context.getPackageName());

        flag.setImageResource(id);

        name.setText(imgName);

        return rowView;
    }
}
