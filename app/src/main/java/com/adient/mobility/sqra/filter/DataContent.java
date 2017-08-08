package com.adient.mobility.sqra.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobility on 06/10/16.
 */
public class DataContent {

            /** An array of sample (data) items. */
        public static List<DataItem> ITEM_LIST = new ArrayList<>();



        public static void addItem(DataItem item) {
            ITEM_LIST.add(item);
            //	ITEM_SPARSE.put(item.barcode(), item);
            //	ITEM_JSON.put(item.toJSONObject());
        }

        public static DataItem newMovie(String title, int year, boolean isRecommended) {
            DataItem item = new DataItem();
            item.title = title;
            item.year = year;
           // item.isRecommended = isRecommended;
            return item;
        }


}
