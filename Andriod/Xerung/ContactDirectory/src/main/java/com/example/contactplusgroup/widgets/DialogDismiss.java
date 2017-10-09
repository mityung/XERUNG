package com.example.contactplusgroup.widgets;

import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.views.ButtonFlat;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Custom Dialog Dismiss (Single button)
 * @author micat
 *
 */
public class DialogDismiss extends android.app.Dialog {

	Context context;
	View view;
	View backView;
	String message;
	TextView messageTextView;
	String title;
	TextView titleTextView;
	Typeface roboto;
	ButtonFlat buttonAccept;

	View.OnClickListener onAcceptButtonClickListener;

	public DialogDismiss(Context context, String title, String message) {
		super(context, android.R.style.Theme_Translucent);
		this.context = context;// init Context
		this.message = message;
		this.title = title;
		roboto       = Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_notdismiss);

		view = (LinearLayout) findViewById(R.id.contentDialog);
		backView = (RelativeLayout) findViewById(R.id.dialog_rootView);
		backView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getX() < view.getLeft()
						|| event.getX() > view.getRight()
						|| event.getY() > view.getBottom()
						|| event.getY() < view.getTop()) {
				}
				return false;
			}
		});

		this.titleTextView = (TextView) findViewById(R.id.title);
		titleTextView.setTypeface(roboto);
		setTitle(title);

		this.messageTextView = (TextView) findViewById(R.id.message);
		messageTextView.setTypeface(roboto);
		setMessage(message);

		this.buttonAccept = (ButtonFlat) findViewById(R.id.btnok);
		buttonAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (onAcceptButtonClickListener != null)
					onAcceptButtonClickListener.onClick(v);
			}
		});
		
	}

	@Override
	public void show() {
		// TODO 自动生成的方法存根
		super.show();
		// set dialog enter animations
	}

	// GETERS & SETTERS

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		messageTextView.setText(message);
	}

	public TextView getMessageTextView() {
		return messageTextView;
	}

	public void setMessageTextView(TextView messageTextView) {
		this.messageTextView = messageTextView;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if (title == null)
			titleTextView.setVisibility(View.GONE);
		else {
			titleTextView.setVisibility(View.VISIBLE);
			titleTextView.setText(title);
		}
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}

	public void setTitleTextView(TextView titleTextView) {
		this.titleTextView = titleTextView;
	}

	public ButtonFlat getButtonAccept() {
		return buttonAccept;
	}

	public void setButtonAccept(ButtonFlat buttonAccept) {
		this.buttonAccept = buttonAccept;
	}

	public void setOnAcceptButtonClickListener(
			View.OnClickListener onAcceptButtonClickListener) {
		this.onAcceptButtonClickListener = onAcceptButtonClickListener;
		if (buttonAccept != null)
			buttonAccept.setOnClickListener(onAcceptButtonClickListener);
	}



	@Override
	public void dismiss() {
		DialogDismiss.super.dismiss();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

}

