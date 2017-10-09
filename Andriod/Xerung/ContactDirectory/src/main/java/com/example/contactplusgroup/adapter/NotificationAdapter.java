package com.example.contactplusgroup.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.mityung.contactdirectory.R;
import java.util.ArrayList;

/**
 * Created by panther on 24/3/17.
 */

public class NotificationAdapter extends ArrayAdapter<ContactBean> {


    Context context;
    ArrayList<ContactBean> data = null;
    // declare the color generator and drawable builder
    /**
     * Contracter of this class
     * @param ncontext take input of this class
     * @param resource take input of related layout of this view
     * @param dataList take input of DataList to be show
     */
    public NotificationAdapter(Context ncontext, int resource,ArrayList<ContactBean> dataList) {
        super(ncontext, resource,dataList);
        // TODO Auto-generated constructor stub
        context      = ncontext;
        data = dataList;
    }

    class ViewHolder {
        TextView txtName, txtValue;
    }

    /**
     * Main mathod used to display view in list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        NotificationAdapter.ViewHolder viewHolder = null;
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ContactBean tb = data.get(position);
        if(convertView == null){

            viewHolder = new NotificationAdapter.ViewHolder();
            convertView = inflate.inflate(R.layout.notification_list_item, null);
            viewHolder.txtName = (TextView)convertView.findViewById(R.id.txtContactName);
            viewHolder.txtValue = (TextView)convertView.findViewById(R.id.txtRequestType);
            viewHolder.txtName.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
            viewHolder.txtValue.setTypeface(ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (NotificationAdapter.ViewHolder)convertView.getTag();
        }
        viewHolder.txtName.setText(Html.fromHtml("<b>"+tb.getNumber()+"</b>"+" <font color='grey'><i>sent you request to join</i></font> "+tb.getName()));
        String request = tb.getRequestFlag().trim();
        if(request!=null){
            if(request.length() != 0){
                if(request.equalsIgnoreCase("1")){
                    viewHolder.txtValue.setText("Admin");
                    viewHolder.txtValue.setVisibility(View.VISIBLE);
                    viewHolder.txtValue.setTextColor(context.getResources().getColor(R.color.blue_500));
                }else if(request.equalsIgnoreCase("2")){
                    viewHolder.txtValue.setText("Member");
                    viewHolder.txtValue.setTextColor(context.getResources().getColor(R.color.green));
                    viewHolder.txtValue.setVisibility(View.VISIBLE);
                }else if(request.equalsIgnoreCase("3")){
                    viewHolder.txtValue.setText("Pending");
                    viewHolder.txtValue.setTextColor(context.getResources().getColor(R.color.red_500));
                    viewHolder.txtValue.setVisibility(View.VISIBLE);
                }else if(request.equalsIgnoreCase("4")){
                    viewHolder.txtValue.setText("Request");
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
        return convertView;

    }


}
