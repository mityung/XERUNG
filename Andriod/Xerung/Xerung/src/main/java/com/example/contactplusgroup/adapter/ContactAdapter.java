package com.example.contactplusgroup.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.contactplusgroup.utils.ManagerTypeface;
import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.utils.ColorGenerator;
import com.example.contactplusgroup.views.CircleImageView;
import com.example.contactplusgroup.views.TextDrawable;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<ContactBean> implements SectionIndexer{

	Context context;
	ArrayList<ContactBean> data = null;
	// declare the color generator and drawable builder
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
	/**
	 * Contracter of this class
	 * @param ncontext take input of this class
	 * @param resource take input of related layout of this view
	 * @param dataList take input of DataList to be show
	 */
	public ContactAdapter(Context ncontext, int resource,ArrayList<ContactBean> dataList) {
		super(ncontext, resource,dataList);
		// TODO Auto-generated constructor stub
		context      = ncontext;
		data = dataList;
		if (data == null) {
			data = new ArrayList<>();
        }
        Collections.sort(data);
		mDrawableBuilder = TextDrawable.builder().round();
	}

	class ViewHolder {
		TextView txtName, txtNum;
		ImageView image;
		CircleImageView imageset;
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
			convertView = inflate.inflate(R.layout.contact_list_item, null);
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.txtContactName);
			viewHolder.txtNum = (TextView)convertView.findViewById(R.id.txtContactNumber);
			viewHolder.image = (ImageView)convertView.findViewById(R.id.imgProfilePic);
			viewHolder.imageset = (CircleImageView)convertView.findViewById(R.id.imgProfilePicSet);
			viewHolder.txtName.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtNum.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.txtName.setText(tb.getName());
		viewHolder.txtNum.setText(tb.getNumber());
		String text = tb.getName();
		
		// Set image if exists
        try {

            if (tb.getThumb() != null) {
            	viewHolder.imageset.setVisibility(View.VISIBLE);
            	viewHolder.image.setVisibility(View.GONE);
            	viewHolder.imageset.setImageBitmap(tb.getThumb());
            } else {
            	viewHolder.imageset.setVisibility(View.GONE);
            	viewHolder.image.setVisibility(View.VISIBLE);
            	TextDrawable drawable = mDrawableBuilder.build(String.valueOf(text.toUpperCase().charAt(0)), mColorGenerator.getColor(text));
            	viewHolder.image.setImageDrawable(drawable);
            }
            // Seting round image
        } catch (OutOfMemoryError e) {
            // Add default picture
        	viewHolder.image.setImageResource(R.drawable.ic_account_circle_white_64dp);
            e.printStackTrace();
        }
		return convertView;

	}

	public int getSectionForPosition(int position) {
        ContactBean item = data.get(position);
        return item.getFirstChar();
    }

    /**
     * 
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            char firstChar = data.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    
    
}

