/*
 * Copyright 2014 Jay Soyer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adient.mobility.sqra.filter;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class DataItem implements Comparable<DataItem>, Parcelable {
	public static final Creator<DataItem> CREATOR = new DataCreator();
	//For use when working with a DataItem converted to a JSONObject
	/*public static final String JSON_TITLE = "title";
	public static final String JSON_YEAR = "year";
	public static final String JSON_IS_RECOMMENDED = "recommended";
	public static final String JSON_BARCODE = "barcode";

	//For use when working with a DataItem converted to a Map<String, String>
	public static final String KEY_TITLE = JSON_TITLE;
	public static final String KEY_YEAR = JSON_YEAR;
	public static final String KEY_IS_RECOMMENDED = JSON_IS_RECOMMENDED;
	public static final String KEY_BARCODE = JSON_BARCODE;*/

	//public static final String KEY_BARCODE = "barcode";
	//public static final String[] MAP_KEYS = {JSON_TITLE, JSON_YEAR, JSON_IS_RECOMMENDED, JSON_BARCODE};

	//private static Random sRand = new Random();

	public static final String KEY_TITLE = "title";
	public static final String KEY_YEAR = "year";

	public String title;
	public int year;
	//public boolean isRecommended;

	//private int mBarcode;
	//private long mBarcodeLong;

/*
	public DataItem() {
		mBarcode = sRand.nextInt();
		mBarcodeLong = mBarcode;
	}

	public DataItem(int barcode) {
		mBarcode = barcode;
		mBarcodeLong = barcode;
	}
*/

	private static String getStringToCompare(String text) {
		if (TextUtils.isEmpty(text)) return "";

		String compare = text.toLowerCase(Locale.US);
		compare = compare.replace("the ", "").replace("a ", "").replace("an ", "");
		return compare;
	}

	/*public int barcode() {
		return mBarcode;
	}

	public long barcodeLong() {
		return mBarcodeLong;
	}*/

	@Override
	public int compareTo(DataItem another) {
		if (TextUtils.isEmpty(title)) {
			return (TextUtils.isEmpty(another.title)) ? Integer.valueOf(year).compareTo(year) : 1;
		}

		return getStringToCompare(title).compareTo(getStringToCompare(another.title));
	}


	@Override
	public int describeContents() {
		return 0;
	}

	/*@Override
	public boolean equals(Object o) {
		if (!(o instanceof DataItem)) return false;
		DataItem other = (DataItem) o;
		return title == other.title;

	}*/


	public boolean equals(Object o){
		if(o == null)                return false;
		if(!(o instanceof DataItem)) return false;

		DataItem other = (DataItem) o;
		if(this.title != other.title)      return false;
		//if(! this.firstName.equals(other.firstName)) return false;
		//if(! this.lastName.equals(other.lastName))   return false;

		return true;
	}

	/*@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return i == ((MyWrapper) o).i;
	}*/

	/*@Override
	public int hashCode() {
		return i;
	}
*/
	@Override
	public int hashCode() {
		//return Integer.valueOf(title).hashCode();
		return (int) title.hashCode();
	}

	/*public JSONObject toJSONObject() {
		JSONObject object = new JSONObject();
		try {
			object.put(JSON_TITLE, title);
			object.put(JSON_YEAR, year);
			//object.put(JSON_IS_RECOMMENDED, isRecommended);
			//object.put(JSON_BARCODE, mBarcode);
		} catch (JSONException e) {
			Log.e(DataItem.class.getSimpleName(), "Error converting to JSON", e);
		}

		return object;
	}
*/
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		map.put(KEY_TITLE, title);
		map.put(KEY_YEAR, String.valueOf(year));
		//map.put(KEY_IS_RECOMMENDED, String.valueOf(isRecommended));
		//map.put(KEY_BARCODE, String.valueOf(mBarcode));
		return map;
	}

	@Override
	public String toString() {
		return (title == null ? "" : title);// + " " + Integer.toString(year);// + " " +
			   //Boolean.toString(isRecommended);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeInt(year);
		//dest.writeInt(mBarcode);
		//dest.writeString(Boolean.toString(isRecommended));
	}

	private static class DataCreator implements Creator<DataItem> {
		@Override
		public DataItem createFromParcel(Parcel source) {
			DataItem item = new DataItem();
			item.title = source.readString();
			item.year = source.readInt();
			//item.mBarcode = source.readInt();
			//item.mBarcodeLong = item.mBarcode;
			//item.isRecommended = Boolean.getBoolean(source.readString());

			return item;
		}

		@Override
		public DataItem[] newArray(int size) {
			return new DataItem[size];
		}
	}
}
