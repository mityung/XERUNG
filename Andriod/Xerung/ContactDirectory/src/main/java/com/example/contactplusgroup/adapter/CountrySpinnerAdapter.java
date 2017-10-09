package com.example.contactplusgroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.countrymaster.Country;
import com.example.contactplusgroup.countrymaster.CountryMaster;

/**
 * Created by Claude on 15. 8. 5..
 */
public class CountrySpinnerAdapter extends ArrayAdapter<Country> {

    @SuppressWarnings("unused")
	private Context context;
    private ArrayList<Country> itemList;
    private LayoutInflater layoutInflater;
    private int textViewResourceId;
    CountryMaster country = null;
    public CountrySpinnerAdapter(Context context, int textViewResourceId, ArrayList<Country> itemList) {

        super(context, textViewResourceId,itemList);
        this.context=context;
        this.itemList=itemList;
        layoutInflater=LayoutInflater.from(context);
        this.textViewResourceId=textViewResourceId;
        country = CountryMaster.getInstance(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position,convertView,parent);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return getCustomViewDropDown(position,convertView,parent);
    }

    private View getCustomViewDropDown(int position, View convertView, ViewGroup parent)
    {
        View root=layoutInflater.inflate(R.layout.phone_code_spinner_item,parent,false);
        ((TextView)root.findViewById(R.id.countryNameTxt)).setText(itemList.get(position).mCountryName);
        ((TextView)root.findViewById(R.id.phoneCodeTxt)).setText(itemList.get(position).mDialPrefix);
        return root;
    }
    
    private View getCustomView(int position, View convertView, ViewGroup parent)
    {
        View root=layoutInflater.inflate(textViewResourceId,parent,false);

        ((TextView)root.findViewById(R.id.countryCodePrefix)).setText(itemList.get(position).mCountryName);


        return root;
    }

}