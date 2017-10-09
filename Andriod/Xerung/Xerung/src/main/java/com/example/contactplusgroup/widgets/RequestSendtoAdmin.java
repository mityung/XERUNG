package com.example.contactplusgroup.widgets;

import org.json.JSONObject;
import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.views.ButtonFlat;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RequestSendtoAdmin extends Dialog implements WebServiceInterface{
	
	Context context = null;
	Activity activity = null;
	private TextView txtCommon;
	private TextView txtMessage;
	private ButtonFlat btnOk, btnCancel;
	String requestSend, groupName, groupNumber = "";
	int gUID = 0;
	private Typeface roboto;
	RequestSendtoAdmin requestDilog  = null;
	private RelativeLayout relProgress;
	private LinearLayout layMain;
	private Comman comman = null;
	SharedPreferanceData shared = null;
	
	
	Object dashBoardActObjecct  = null;
	public RequestSendtoAdmin(Context context, int id, String number,String name, String request, Object dashBoardActivity) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_send_request_admin);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		setCancelable(false);
		this.context = context;
		roboto = Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
		activity = (Activity) context;
		this.requestSend= request;
		this.groupName= name;
		this.groupNumber= number;
		this.gUID= id;
		dashBoardActObjecct = dashBoardActivity;
		shared = new SharedPreferanceData(context);
		requestDilog = this;
		comman = new Comman();
		
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		findView();
		setOnClick();
		if(requestSend.equals(context.getString(R.string.send_request))){
			txtCommon.setText(context.getString(R.string.send_request));
			
		}
		txtMessage.setText( "Join directory("+ groupName+") by sending request to admin.");
	}
	
	private void setOnClick(){
		
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RequestSendtoAdmin.this.dismiss();
			}
		});
		
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addNewUserJson(groupNumber, gUID);
			}
		});
		
	}
	
	private void showProgress() {

		relProgress.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);

	}

	private void hideProgress() {

		relProgress.setVisibility(View.GONE);
		layMain.setVisibility(View.VISIBLE);

	}
	
	/**
	 * used to get reference of Xml views
	 */
	private void findView(){
		btnOk    = (ButtonFlat)findViewById(R.id.button_accept);
		btnCancel    = (ButtonFlat)findViewById(R.id.button_cancel);
		txtMessage = (TextView)findViewById(R.id.message);
		relProgress = (RelativeLayout)findViewById(R.id.layProgressresult);
		layMain = (LinearLayout)findViewById(R.id.layMain);
		txtCommon    =(TextView)findViewById(R.id.title);
		txtCommon.setTypeface(roboto);
	}
	
	 private boolean addNewUserJson(String members, int gUID) {

			JSONObject json = new JSONObject();
			try {
				json.put("PSENDBYNO", 			shared.getSharedValue("PMOBILE"));
				json.put("PSENDTONO", 			members);
				json.put("PGROUPID", 			gUID);
				json.put("PINVITATIONFLAGID", 	2);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			if (comman.isNetworkAvailable(context)) {
				sendOTPWebServiceProcess(json);
			} else {
				comman.alertDialog(context, context.getString(R.string.no_internet_connection),context.getString(R.string.you_dont_have_internet));
			}

			return true;
		}

	    private void sendOTPWebServiceProcess(JSONObject jbObject) {
			String jsonData = jbObject.toString();
			new Webservice(this, Vars.webMethodName.get(8)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(8));
			showProgress();
		}
	
	@Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stub
		hideProgress();
		if (result == null) {
			comman.alertDialog(activity,context.getString(R.string.network_problem_head),context.getString(R.string.network_problem));
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			comman.alertDialog(activity,context.getString(R.string.network_problem_head),context.getString(R.string.network_problem));
			return;
		}if(processName.equals(Vars.webMethodName.get(8))){
			parseStatus(result);
		}else if(processName.equals(Vars.webMethodName.get(14))){
			RequestSendtoAdmin.this.dismiss();
		}

	}

	private void parseStatus(String result){
		try {
			JSONObject json = new JSONObject(result);
			String status = json.getString("STATUS");
			if(status.equalsIgnoreCase("SUCCESS")){
				sendNotification();
			}else{
				comman.alertDialog(activity, context.getString(R.string.network_problem_head), context.getString(R.string.network_problem));
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	// Send Notificaiton
	private boolean sendNotification() {

		JSONObject json = new JSONObject();
		try {
			json.put("PPHONENUMBER", shared.getSharedValue("PMOBILE"));
			json.put("PFLAG", "1");
			json.put("PPHONETYPE", 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			sendN(json);
		} else {
			comman.alertDialog(activity, context.getString(R.string.no_internet_connection), context.getString(R.string.you_dont_have_internet));
		}

		return true;
	}

	private void sendN(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(14)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(14));
		showProgress();
	}
}
