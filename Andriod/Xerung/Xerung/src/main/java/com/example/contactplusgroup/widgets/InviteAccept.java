package com.example.contactplusgroup.widgets;

import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.views.ButtonFlat;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class InviteAccept extends Dialog{
	
	Context context = null;
	Activity activity = null;
	private TextView txtCommon;
	private TextView txtMessage;
	private TextView btnOk, btnCancel, btnReject;
	String inviteSend, groupName = "";
	int gUID = 0;
	private Typeface roboto;
	InviteAccept forgotDilog  = null;
	String sendbyUser;
	Object dashBoardActObjecct  = null;
	public InviteAccept(Context context, int id, String message, String forget, String sendby, Object dashBoardActivity, OnAcceptCompInterface onAccept) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_request);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		setCancelable(false);
		this.context = context;
		roboto = Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
		activity = (Activity) context;
		this.inviteSend= forget;
		this.groupName= message;
		this.sendbyUser= sendby;
		this.gUID= id;
		this.onAcceptInterface = onAccept;
		dashBoardActObjecct = dashBoardActivity;
		forgotDilog = this;
		
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		findView();
		setOnClick();
		if(inviteSend.equals(context.getString(R.string.request_received))){
			txtCommon.setText(context.getString(R.string.request_received));
			
		}
		txtMessage.setText(groupName);
	}
	
	private void setOnClick(){
		btnReject.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				onAcceptInterface.OnAcceptComplete(gUID, 0, sendbyUser);
				InviteAccept.this.dismiss();
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InviteAccept.this.dismiss();
			}
		});
		
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onAcceptInterface.OnAcceptComplete(gUID, 4, sendbyUser);
				InviteAccept.this.dismiss();
			}
		});
		
	}
	
	/**
	 * used to get reference of Xml views
	 */
	private void findView(){
		btnOk    = (TextView)findViewById(R.id.button_accept);
		btnCancel    = (TextView)findViewById(R.id.button_cancel);
		btnReject    = (TextView)findViewById(R.id.button_reject);
		txtMessage = (TextView)findViewById(R.id.message);
		txtCommon    =(TextView)findViewById(R.id.title);
		txtCommon.setTypeface(roboto);
	}
	
	public OnAcceptCompInterface onAcceptInterface = null;
	public interface OnAcceptCompInterface{
		public void OnAcceptComplete(int result, int id, String sendby);
	}
}
