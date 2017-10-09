package com.example.contactplusgroup.adapter;

import java.util.ArrayList;

import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.views.SquareImageView;
import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.GroupBean;
import com.example.contactplusgroup.views.CircleImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupSearchAdapter extends ArrayAdapter<GroupBean>{

	Context context;
	ArrayList<GroupBean> data = null;
	Comman comman = null;
	// declare the color generator and drawable builder
	/**
	 * Contracter of this class
	 * @param ncontext take input of this class
	 * @param resource take input of related layout of this view
	 * @param dataList take input of DataList to be show
	 */
	public GroupSearchAdapter(Context ncontext, int resource,ArrayList<GroupBean> dataList) {
		super(ncontext, resource,dataList);
		// TODO Auto-generated constructor stub
		context      = ncontext;
		data = dataList;
		comman = new Comman();
	}

	class ViewHolder {
		TextView txtName, txtDesc, txtTag;
		ImageView imageset;
	}
	
	/**
	 * Main mathod used to display view in list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		GroupBean tb = data.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflate.inflate(R.layout.group_search_list_item, null);
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.txtGroupNameList);
			viewHolder.txtDesc = (TextView)convertView.findViewById(R.id.txtDescription);
			viewHolder.txtTag = (TextView)convertView.findViewById(R.id.txtTagname);
			viewHolder.txtName.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtDesc.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtTag.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.imageset = (ImageView)convertView.findViewById(R.id.imgProfilePicSet);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.txtName.setText(tb.getmGroupName().trim());
		viewHolder.txtTag.setText(tb.getmGroupTag().trim());
		viewHolder.txtDesc.setText(tb.getmGroupDesc().trim());
		String photo = tb.getmPhoto();
		// Set image if exists
		try {
			if(photo != null ){
				if(photo.length() != 0){
					if(!photo.equalsIgnoreCase("null")){
						if(!photo.equalsIgnoreCase("0")){
							viewHolder.imageset.setImageResource(0);
							viewHolder.imageset.setImageBitmap(comman.decodeBase64(photo));
						}else{
							viewHolder.imageset.setImageBitmap(null);
							viewHolder.imageset.setImageResource(R.drawable.ic_group_square_white_64dp);
						}
					}else{
						viewHolder.imageset.setImageBitmap(null);
						viewHolder.imageset.setImageResource(R.drawable.ic_group_square_white_64dp);
					}
				}else{
					viewHolder.imageset.setImageBitmap(null);
					viewHolder.imageset.setImageResource(R.drawable.ic_group_square_white_64dp);
				}
			}else{
				viewHolder.imageset.setImageResource(R.drawable.ic_group_square_white_64dp);
			}
			// Seting round image
		} catch (OutOfMemoryError e) {
			// Add default picture
			viewHolder.imageset.setImageResource(R.drawable.ic_group_square_white_64dp);
			e.printStackTrace();
		}
		return convertView;

	}

}
