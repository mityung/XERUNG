package com.example.contactplusgroup.widgets;

import com.mityung.contactdirectory.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Dialog Box to shows the progress
 * @author micat
 *
 */
public class ProgressDialog{

	PopupWindow paymentAlert =null ;
	TextView txtWaiting;
	@SuppressWarnings("deprecation")
	public void  progressPopUp(final Activity context,String data){
		
		RelativeLayout layoutId = (RelativeLayout)context.findViewById(R.id.dialog_rootView);
		LayoutInflater layoutInflater  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.progresspopup, layoutId);
		txtWaiting = (TextView)layout.findViewById(R.id.txtWaiting);
	
		paymentAlert = new PopupWindow(context);
		paymentAlert.setContentView(layout);		
		paymentAlert.setHeight(WindowManager.LayoutParams.MATCH_PARENT);		
		paymentAlert.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		paymentAlert.setFocusable(true);		
		paymentAlert.setBackgroundDrawable(new BitmapDrawable());
		paymentAlert.showAtLocation(layout, Gravity.CENTER, 0, 0);	
		if(data !=null)
			txtWaiting.setText(data);
    }

	
	public void closeProgressPopUp(){
		try {
			if(paymentAlert != null){
				if(paymentAlert.isShowing())
					paymentAlert.dismiss();
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}


