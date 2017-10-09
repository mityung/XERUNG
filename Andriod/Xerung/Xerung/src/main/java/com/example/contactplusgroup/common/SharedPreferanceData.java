package com.example.contactplusgroup.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * Used to store the data in sharedpreferance
 * @author micat
 *
 */
public class SharedPreferanceData {

	Context context = null;
	SharedPreferences sharedPref =null;
	SharedPreferences.Editor sharedEditor =null;
	
	/**
	 * Constructor of the SharedPreferance class
	 * @param context
	 */
	public SharedPreferanceData(Context context){
		this.context = context;
		getSharedPrefInstance();
	}
	
	/**
	 * get the value with key
	 * @param key
	 * @return
	 */
	public String getSharedValue(String key){
		return sharedPref.getString(key, "N/A");
	}
	
	/**
	 * save the value in key value pair
	 */
	public void saveSharedData(String key , String value){
		 sharedEditor = sharedPref.edit();
		 sharedEditor.putString(key, value);
		 sharedEditor.commit();
	}
	
	/**
	 * Instance of shared preferance to get the value
	 */
	private void getSharedPrefInstance(){
		 sharedPref = context.getSharedPreferences("Comman", Activity.MODE_PRIVATE);
	}
	
	/**
	 * Clear all the data
	 */
	
	public void clearAllCommonSharedData(){
		sharedPref = context.getSharedPreferences("Comman", Context.MODE_PRIVATE);
		sharedPref.edit().clear().commit();	
		SharedPreferences prefs = context.getSharedPreferences(Vars.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
		prefs.edit().clear().commit();
	}
}
