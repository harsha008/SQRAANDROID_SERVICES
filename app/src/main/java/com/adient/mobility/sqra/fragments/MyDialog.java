package com.adient.mobility.sqra.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.global.global;
import com.adient.mobility.sqra.util.MySingleTon;

import java.util.Map;

/**
 * Created by Mobile-Tiger on 4/15/2017.
 */

public class MyDialog extends DialogFragment {

    public static MyDialog newInstance() {

        return new MyDialog();
    }

    private Map<String, String> result;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInsttanceState) {

        View view = inflater.inflate(R.layout.detail, container, false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView txt_header = (TextView)view.findViewById(R.id.text_header);
        TextView txt_name = (TextView) view.findViewById(R.id.textName);
        TextView txt_discrip = (TextView) view.findViewById(R.id.textDiscrip);
        TextView txt_status = (TextView) view.findViewById(R.id.textStatus);
        TextView textRegion = (TextView) view.findViewById(R.id.textRegion);
        TextView txt_country = (TextView) view.findViewById(R.id.textCountry);
        TextView txt_sop = (TextView) view.findViewById(R.id.textSop);
        TextView txt_eop = (TextView) view.findViewById(R.id.textEop);


        result = MySingleTon.parseJsonYearFlowDetail(global.rowValue,global.groupVr, global.detailVr);

        if(null != result) {

            txt_header.setText(global.detailVr);
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

        return view;
    }

    public void show(FragmentManager fm, String s) {

        this.show(fm, s);
    }

}


