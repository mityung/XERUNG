package com.example.contactplusgroup.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.contactplusgroup.utils.ManagerTypeface;
import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.utils.ColorGenerator;
import com.example.contactplusgroup.views.CheckBox;
import com.example.contactplusgroup.views.CircleImageView;
import com.example.contactplusgroup.views.TextDrawable;

public class ContactSearchAdapter extends ArrayAdapter<ContactBean> {

	Context context;
	ArrayList<ContactBean> data = null;
	private List<ContactBean> contactSearchList = null;
	// declare the color generator and drawable builder
	private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
	private TextDrawable.IBuilder mDrawableBuilder;

	/**
	 * Contracter of this class
	 * 
	 * @param ncontext
	 *            take input of this class
	 * @param resource
	 *            take input of related layout of this view
	 * @param dataList
	 *            take input of DataList to be show
	 */
	public ContactSearchAdapter(Context ncontext, int resource,
			ArrayList<ContactBean> dataList) {
		super(ncontext, resource, dataList);
		// TODO Auto-generated constructor stub
		context = ncontext;
		data = dataList;
		contactSearchList = new ArrayList<ContactBean>();
		contactSearchList.addAll(data);
		mDrawableBuilder = TextDrawable.builder().round();
	}

	class ViewHolder {
		private TextView txtName;
		private TextView txtNumber;
		private ImageView image;
		private CircleImageView imageset;
		private CheckBox chselect;

	}

	@Override
	public int getCount() {
		return contactSearchList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Main mathod used to display view in list
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ContactBean tb = contactSearchList.get(position);
		if (convertView == null) {

			viewHolder = new ViewHolder();
			convertView = inflate.inflate(R.layout.contact_list_item_search,
					null);
			viewHolder.txtName = (TextView) convertView
					.findViewById(R.id.txtContactName);
			viewHolder.txtNumber = (TextView) convertView
					.findViewById(R.id.txtContactNumber);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.imgProfilePic);
			viewHolder.imageset = (CircleImageView) convertView
					.findViewById(R.id.imgProfilePicSet);
			viewHolder.txtName.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.txtNumber.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
			viewHolder.chselect = (CheckBox) convertView
					.findViewById(R.id.chselectContact);
			viewHolder.chselect
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton arg0,
													 boolean arg1) {
							// TODO Auto-generated method stub
							int getPosition = (Integer) arg0.getTag(); // Here
							// we get the position that we have set for the checkbox using setTag.
							contactSearchList.get(getPosition).setChecked(
									arg0.isChecked());
						}
					});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.chselect.setTag(position); // This line is important.
		viewHolder.txtName.setText(tb.getName());
		viewHolder.txtNumber.setText(tb.getNumber());
		String text = tb.getName();
		viewHolder.chselect.setChecked(tb.isChecked());
		// Set image if exists
		try {
			if (tb.getThumb() != null) {
				viewHolder.imageset.setVisibility(View.VISIBLE);
				viewHolder.image.setVisibility(View.GONE);
				viewHolder.imageset.setImageBitmap(tb.getThumb());
			} else {
				viewHolder.imageset.setVisibility(View.GONE);
				viewHolder.image.setVisibility(View.VISIBLE);
				TextDrawable drawable = mDrawableBuilder.build(
						String.valueOf(text.toUpperCase().charAt(0)),
						mColorGenerator.getColor(text));
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


	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		try {
			contactSearchList.clear();
			if (charText.length() == 0) {
				contactSearchList.addAll(data);
			} else {
				for (ContactBean cl : data) {
					if (cl.getName().toLowerCase(Locale.getDefault())
							.contains(charText)) {
						contactSearchList.add(cl);
					}
				}
			}
			notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
