package com.example.contactplusgroup.adapter;

import android.content.Context;
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

/**
 * Created by mipanther on 12/3/16.
 */
public class BloodGroupAdapter extends ArrayAdapter<ContactBean> {

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
    public BloodGroupAdapter(Context ncontext, int resource,ArrayList<ContactBean> dataList) {
        super(ncontext, resource,dataList);
        // TODO Auto-generated constructor stub
        context      = ncontext;
        data = dataList;
        comman = new Comman();
    }

    class ViewHolder {
        TextView txtName, txtDesc;
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
            convertView = inflate.inflate(R.layout.act_blood_group_item, null);
            viewHolder.txtName = (TextView)convertView.findViewById(R.id.txtGroupNameList);
            viewHolder.txtDesc = (TextView)convertView.findViewById(R.id.txtTagname);
            viewHolder.txtName.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
            viewHolder.txtDesc.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(tb.getName().toString().trim().equalsIgnoreCase("0")){
            viewHolder.txtName.setText("UNKNOWN");
        }else{
            viewHolder.txtName.setText(tb.getName().trim());
        }

        viewHolder.txtDesc.setText(""+tb.getNumber().trim());

        return convertView;

    }

}
