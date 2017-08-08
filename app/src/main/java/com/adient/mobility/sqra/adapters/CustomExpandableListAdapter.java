package com.adient.mobility.sqra.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.filter.DataItem;
import com.adient.mobility.sqra.global.global;
import com.adient.mobility.sqra.util.MySingleTon;
import com.sawyer.advadapters.widget.RolodexArrayAdapter;


import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CustomExpandableListAdapter extends RolodexArrayAdapter<Integer, DataItem> {


    public CustomExpandableListAdapter(@NonNull Context activity, @NonNull List<DataItem> items) {
        super(activity, items);
    }



   /* public CustomExpandableListAdapter(Context activity, Map<String, List<String>> expandableListDetail) {
        super(activity, expandableListDetail);
    }*/

    /*@Override
    public String createGroupFor(Map<String, List<String>> expandableListDetail) {

        return  this.yearArrayList.get(listPosition);
       // return childItem.year;
    }
*/
@Override
public boolean isChildSelectable(int listPosition, int expandedListPosition) {
    return true;
        }

   /* @Override
    public View getChildView(LayoutInflater inflater, int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expandable_child1, parent, false);
        }
        TextView tv = (TextView) convertView;
        tv.setText(getChild(groupPosition, childPosition).title);
        return convertView;
    }

    @Override
    public View getGroupView(LayoutInflater inflater, int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expandable_group1, parent, false);
        }
        TextView tv = (TextView) convertView;
        tv.setText(getGroup(groupPosition).toString());
        return convertView;
    }*/

    @Override
    public void onGroupCollapsed(int groupPosition) {

        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        int i = getGroup(groupPosition);
    }

    @Override
    public View getChildView(final LayoutInflater inflater, int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
//final String expandedListText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
        //LayoutInflater layoutInflater = (LayoutInflater) this.context
       // .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item, null);
        }
        final TextView expandedListTextView = (TextView) convertView
        .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(getChild(groupPosition, childPosition).title);

        expandedListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.detailVr = expandedListTextView.getText().toString();

                Map<String, String> result = MySingleTon.parseJsonYearFlowDetail(global.rowValue, global.groupVr, global.detailVr);
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
        return convertView;
    }

    @Override
    public View getGroupView(LayoutInflater inflater, int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
       // String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
        //LayoutInflater layoutInflater = (LayoutInflater) this.context.
      //  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_group, null);
        }
        final TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        ImageView icon = (ImageView)convertView.findViewById(R.id.ico_img);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(getGroup(groupPosition).toString());

        if(isExpanded == true){
            icon.setBackgroundResource(R.drawable.down);
        }  else  {
            icon.setBackgroundResource(R.drawable.right);
        }
        listTitleTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                global.groupVr = listTitleTextView.getText().toString();
                return false;
            }
        });

        return convertView;
    }

    @Override
    public boolean hasAutoExpandingGroups() {
        //Auto expand so user can more easily see what's happening
        return false;
    }


    @NonNull
    @Override
    public Integer createGroupFor(DataItem childItem) {
        return childItem.year;
    }

    @Override
    protected boolean isChildFilteredOut(DataItem childItem, @NonNull CharSequence constraint) {
        return !TextUtils.isDigitsOnly(constraint) && !childItem.title.toLowerCase(Locale.US)
                .contains(constraint.toString().toLowerCase(Locale.US));
    }

    @Override
    protected boolean isGroupFilteredOut(Integer year, @NonNull CharSequence constraint) {
        return TextUtils.isDigitsOnly(constraint) && !year.toString().contains(constraint);
    }
}