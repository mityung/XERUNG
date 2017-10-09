package com.example.contactplusgroup.adapter;

import java.util.ArrayList;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.mityung.contactdirectory.R;
import com.mityung.contactdirectory.SearchListView;
import com.example.contactplusgroup.common.ContactBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListSearchAdapter extends ArrayAdapter<ContactBean> {
	
	
	Context context;
	ArrayList<ContactBean> data = null;
	SearchListView search;
	// declare the color generator and drawable builder
	/**
	 * Contracter of this class
	 * @param ncontext take input of this class
	 * @param resource take input of related layout of this view
	 * @param dataList take input of DataList to be show
	 */
	public CustomListSearchAdapter(Context ncontext, int resource,ArrayList<ContactBean> dataList, SearchListView searchView) {
		super(ncontext, resource,dataList);
		// TODO Auto-generated constructor stub
		context      = ncontext;
		data = dataList;
		this.search = searchView;
	}

	class ViewHolder {
		TextView txtName, txtNum, txtValue, txtAddress, txtcity;
	}
	
	/**
	 * Main mathod used to display view in list
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ContactBean tb = data.get(position);
		if(convertView == null){

			viewHolder = new ViewHolder();
			convertView = inflate.inflate(R.layout.snippet_item1, null);
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.txtContactName);
			viewHolder.txtNum = (TextView)convertView.findViewById(R.id.txtContactNumber);
			viewHolder.txtValue = (TextView)convertView.findViewById(R.id.txtRequestType);
			viewHolder.txtAddress = (TextView)convertView.findViewById(R.id.txtContactAddress);
			viewHolder.txtcity = (TextView)convertView.findViewById(R.id.txtContactcity);
			viewHolder.txtName.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtNum.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtValue.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtAddress.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtcity.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.txtName.setText(tb.getName());
		//viewHolder.txtNum.setText(tb.getNumber());
		//Log.e("ssss", "ssss"+tb.getNumber()+tb.getAdminFlag());
		String access = tb.getAdminFlag().trim();
		if(access!=null){
			if(access.length() != 0){
				if(!access.equalsIgnoreCase("1")){
					viewHolder.txtNum.setText(tb.getNumber());
					viewHolder.txtNum.setVisibility(View.VISIBLE);
				}else {

					viewHolder.txtNum.setVisibility(View.GONE);
				}
			}else{
				viewHolder.txtNum.setVisibility(View.GONE);
			}
		}else{
			viewHolder.txtNum.setVisibility(View.GONE);
		}

		if(!access.equalsIgnoreCase("1")){
			String address = tb.getAddress().trim();
			String city = tb.getCity().trim();
			if(address!=null){
				if(address.length() != 0){
					viewHolder.txtAddress.setText(tb.getAddress().trim());
					viewHolder.txtAddress.setVisibility(View.VISIBLE);
				}else{
					viewHolder.txtAddress.setVisibility(View.GONE);
				}
			}
			else{
				viewHolder.txtAddress.setVisibility(View.GONE);
			}
			if(city!=null){
				if(city.length() != 0){
					viewHolder.txtcity.setText(tb.getCity().trim());
					viewHolder.txtcity.setVisibility(View.VISIBLE);
				}else{
					viewHolder.txtcity.setVisibility(View.GONE);
				}
			}
			else{
				viewHolder.txtcity.setVisibility(View.GONE);
			}
		}
		String request = tb.getRequestFlag().trim();

		if(request!=null){
			if(request.length() != 0){
				if(request.equalsIgnoreCase("1")){
					viewHolder.txtValue.setText("Send Request");
					viewHolder.txtValue.setVisibility(View.VISIBLE);
				}else{
					viewHolder.txtValue.setVisibility(View.GONE);
				}
			}else{
				viewHolder.txtValue.setVisibility(View.GONE);
			}
		}else{
			viewHolder.txtValue.setVisibility(View.GONE);
		}

		viewHolder.txtValue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search.sendRequest(position);
			}
		});
		return convertView;

	}
   
    
}