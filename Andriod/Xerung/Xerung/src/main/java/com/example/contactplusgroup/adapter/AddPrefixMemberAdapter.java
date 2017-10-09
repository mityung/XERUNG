package com.example.contactplusgroup.adapter;

import java.util.ArrayList;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.views.MaterialSpinner;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AddPrefixMemberAdapter extends ArrayAdapter<ContactBean> {

	Context context;
	Typeface fontBauhus;
	ArrayList<ContactBean> data = null;
	/**
	 * Contracter of this class
	 * @param ncontext take input of this class
	 * @param resource take input of related layout of this view
	 * @param dataList take input of DataList to be show
	 */
	public AddPrefixMemberAdapter(Context ncontext, int resource,ArrayList<ContactBean> dataList) {
		super(ncontext, resource,dataList);
		// TODO Auto-generated constructor stub
		context      = ncontext;
		fontBauhus   = Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
		data         = dataList;
	}

	class ViewHolder {
		TextView txtName;
		MaterialSpinner spCountry;
	}
	
	/**
	 * Main mathod used to display view in list
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ContactBean tb = data.get(position);
		if(convertView == null){

			viewHolder = new ViewHolder();
			convertView = inflate.inflate(R.layout.add_prefix_member_list_item, null);
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.txtContactName);
			viewHolder.spCountry = (MaterialSpinner)convertView.findViewById(R.id.spCountry);
			viewHolder.txtName.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			convertView.setTag(viewHolder);
			convertView.setTag(R.id.txtContactName, viewHolder.txtName);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.txtName.setTypeface(fontBauhus);
		viewHolder.txtName.setText(tb.getName());
		return convertView;

	}

}

