package com.adient.mobility.sqra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.adient.mobility.sqra.R;
import com.adient.mobility.sqra.global.global;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class StickyAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
//    private String[] mCountries;
    private int[] mSectionIndices;
    private String[] mSectionLetters;
    private LayoutInflater mInflater;
    ArrayList<String> availableLists;

    public StickyAdapter(Context context, ArrayList<String> availableLists) {
        mContext = context;
        this.availableLists = availableLists;
        mInflater = LayoutInflater.from(context);
//        mCountries = context.getResources().getStringArray(R.array.countries);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        String lastHeader = availableLists.get(0);
        if(availableLists.get(0).indexOf(":")>0) {
            lastHeader = availableLists.get(0).substring(0, availableLists.get(0).indexOf(":"));
        }
        sectionIndices.add(0);
        for (int i = 1; i < availableLists.size(); i++) {
            String currentHeader = availableLists.get(i);
            if(availableLists.get(i).indexOf(":")>0) {
                currentHeader = availableLists.get(i).substring(0, availableLists.get(i).indexOf(":"));
            }
            if (!currentHeader.equalsIgnoreCase(lastHeader)) {
                lastHeader = currentHeader;
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private String[] getSectionLetters() {
        String[] letters = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = availableLists.get(mSectionIndices[i]);
            if(availableLists.get(mSectionIndices[i]).indexOf(":")>0) {
                letters[i] = availableLists.get(mSectionIndices[i]).substring(0, availableLists.get(mSectionIndices[i]).indexOf(":"));
            }
        }
        return letters;
    }

    @Override
    public int getCount() {
        return availableLists.size();
    }

    @Override
    public Object getItem(int position) {
        return availableLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_row, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.txt_name);
            holder.flag = (ImageView)convertView.findViewById(R.id.img_flag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.text.setText(availableLists.get(position));





        String imgName = availableLists.get(position);

        String country;
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

        Context context = holder.flag.getContext();
//        imgName.
        final int id = context.getResources().getIdentifier(country, "drawable", context.getPackageName());

        holder.flag.setImageResource(id);

        holder.text.setText(imgName);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        String headerChar = availableLists.get(position);
        if(availableLists.get(position).indexOf(":")>0) {
            headerChar = availableLists.get(position).substring(0, availableLists.get(position).indexOf(":"));
        }
        holder.text.setText(headerChar);

        return convertView;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public String getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        String headerChar = availableLists.get(position);
        if(availableLists.get(position).indexOf(":")>0) {
            headerChar = availableLists.get(position).substring(0, availableLists.get(position).indexOf(":"));
        }
        return headerChar;
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }
        
        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    /*public void clear() {
        mCountries = new String[0];
        mSectionIndices = new int[0];
        mSectionLetters = new String[0];
        notifyDataSetChanged();
    }

    public void restore() {
        mCountries = mContext.getResources().getStringArray(R.array.countries);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }*/

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
        ImageView flag;
    }

}
