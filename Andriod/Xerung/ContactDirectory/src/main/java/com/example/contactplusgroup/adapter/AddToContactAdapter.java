package com.example.contactplusgroup.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.mityung.contactdirectory.R;

import java.util.ArrayList;

public class AddToContactAdapter extends ArrayAdapter<ContactBean> {

	Context context;
	ArrayList<ContactBean> data = null;
	Comman comman = null;
	// declare the color generator and drawable builder
    /**
	 * Contracter of this class
	 * @param ncontext take input of this class
	 * @param resource take input of related layout of this view
	 * @param dataList take input of DataList to be show
	 */
	public AddToContactAdapter(Context ncontext, int resource, ArrayList<ContactBean> dataList) {
		super(ncontext, resource,dataList);
		// TODO Auto-generated constructor stub
		context      = ncontext;
		data = dataList;
		comman = new Comman();
	}

	class ViewHolder {
		TextView txtName, txtNum, txtemail;
	}
	
	/**
	 * Main mathod used to display view in list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ContactBean tb = data.get(position);
		if(convertView == null){

			viewHolder = new ViewHolder();
			convertView = inflate.inflate(R.layout.addtocontact_item, null);
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.txtContactName);
			viewHolder.txtNum = (TextView)convertView.findViewById(R.id.txtContactNumber);
			viewHolder.txtemail = (TextView)convertView.findViewById(R.id.txtContactEmail);
			viewHolder.txtName.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtNum.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtemail.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		viewHolder.txtName.setText(Html.fromHtml(tb.getName()));
		String number = tb.getNumber();
		StringBuffer sbNum  = new StringBuffer();
		if(number != null ){
			if(number.length() != 0){
				if(!number.equalsIgnoreCase("null")){
					ArrayList<String> num = comman.splitString(number, "\\#");
					for (String numberuser : num){
						sbNum.append("\nNumber :"+numberuser);
					}
					viewHolder.txtNum.setText(sbNum);
				}else{
					viewHolder.txtNum.setVisibility(View.GONE);
				}
			}else{
				viewHolder.txtNum.setVisibility(View.GONE);
			}
		}else{
			viewHolder.txtNum.setVisibility(View.GONE);
		}
		String emailId = tb.getEmail();
		StringBuffer sb  = new StringBuffer();
		if(emailId != null ){
			if(emailId.length() != 0){
				if(!emailId.equalsIgnoreCase("null")){
					ArrayList<String> email = comman.splitString(emailId, "\\#");
					for (String emailID : email){
						sb.append("\nEmail :"+emailID);
					}
					viewHolder.txtemail.setText(sb);
				}else{
					viewHolder.txtemail.setVisibility(View.GONE);
				}
			}else{
				viewHolder.txtemail.setVisibility(View.GONE);
			}
		}else{
			viewHolder.txtemail.setVisibility(View.GONE);
		}
		return convertView;

	}

    
    
}

