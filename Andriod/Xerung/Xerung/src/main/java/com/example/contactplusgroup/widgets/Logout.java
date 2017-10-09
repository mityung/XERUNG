package com.example.contactplusgroup.widgets;

import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.webservice.Webservice;
import com.mityung.contactdirectory.MainDashboard;
import com.mityung.contactdirectory.R;
import com.mityung.contactdirectory.SignIn;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.mityung.contactdirectory.VerifyOTP;
import android.app.Dialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONObject;

public class Logout extends Dialog implements Webservice.WebServiceInterface{
	
	Context context = null;
	Activity activity = null;
	GroupDb db = null;
	private TextView txtCommon, heading;
	private TextView btnOk, btnCancel;
	ProgressDialog pbbar = null;
	String email = "";
	private Typeface roboto;
	
	Object dashBoardActObjecct  = null;
	public Logout(Context context, String emailID, Object dashBoardActivity) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_logout);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		setCancelable(false);
		this.context = context;
		roboto = Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
		activity = (Activity) context;
		pbbar = new ProgressDialog();
		db = new GroupDb(context);
		this.email= emailID;
		dashBoardActObjecct = dashBoardActivity;
		
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		findView();
		setOnClick();
		if(email.equals(context.getString(R.string.action_logout))){
			txtCommon.setText(context.getString(R.string.are_you_sure_you_want));
			
		}
	}
	
	private void setOnClick(){
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Logout.this.dismiss();
			}
		});
		
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(email.equals(context.getString(R.string.action_logout))){

					new SharedPreferanceData(context).clearAllCommonSharedData();
					if(db!=null)
						db.deleteUsers();
					if(dashBoardActObjecct instanceof MainDashboard){
						MainDashboard d = (MainDashboard) dashBoardActObjecct;
						d.finish();
						callMakeAdmin("0");
						context.startActivity(new Intent(context,SignIn.class));
						Logout.this.dismiss();
					}
				}
				
			}
		});
		
	}

	private void callMakeAdmin(String id){
		try {
			JSONObject jb = new JSONObject();
			jb.put("MOBILENO", VerifyOTP.mobileNumber);
			jb.put("PPHONETYPE", "1");
			jb.put("ID", id);
			jb.put("VERSION", VerifyOTP.appVersion);
			sendIDTOG(jb);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void sendIDTOG(JSONObject jbObject) {
		//Log.e("sE ","Seddd "+jbObject);
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(13)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(13));

	}
	
	
	/**
	 * used to get reference of Xml views
	 */
	private void findView(){
		btnOk    = (TextView)findViewById(R.id.button_accept);
		btnCancel    = (TextView)findViewById(R.id.button_cancel);
		txtCommon    =(TextView)findViewById(R.id.message);
		txtCommon.setTypeface(roboto);
		heading    =(TextView)findViewById(R.id.heading);
		heading.setTypeface(roboto);
	}

	@Override
	public void onWebCompleteResult(String result, String processName) {

		if(result == null){
			//comman.alertDialog(VerifyOTP.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			//comman.alertDialog(VerifyOTP.this, getString(R.string.network_problem_head), getString(R.string.network_problem));
			return;
		}
	}
}
