package com.example.contactplusgroup.widgets;

import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.views.ButtonFlat;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Dialog box Custom
 * @author micat
 *
 */
public class Dialog extends android.app.Dialog {

	Context context;
	View view;
	View backView;
	String message;
	TextView messageTextView;
	String title;
	TextView titleTextView;
	Typeface roboto;
	ButtonFlat buttonAccept;
	ButtonFlat buttonCancel;

	View.OnClickListener onAcceptButtonClickListener;
	View.OnClickListener onCancelButtonClickListener;

	public Dialog(Context context, String title, String message) {
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
		setContentView(R.layout.dialog_alert);

		view = (LinearLayout) findViewById(R.id.contentDialog);
		backView = (RelativeLayout) findViewById(R.id.dialog_rootView);
		backView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getX() < view.getLeft()
						|| event.getX() > view.getRight()
						|| event.getY() > view.getBottom()
						|| event.getY() < view.getTop()) {
					dismiss();
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

		this.buttonAccept = (ButtonFlat) findViewById(R.id.button_accept);
		buttonAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (onAcceptButtonClickListener != null)
					onAcceptButtonClickListener.onClick(v);
			}
		});
		this.buttonCancel = (ButtonFlat) findViewById(R.id.button_cancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (onCancelButtonClickListener != null)
					onCancelButtonClickListener.onClick(v);
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

	public ButtonFlat getButtonCancel() {
		return buttonCancel;
	}

	public void setButtonCancel(ButtonFlat buttonCancel) {
		this.buttonCancel = buttonCancel;
	}

	public void setOnAcceptButtonClickListener(
			View.OnClickListener onAcceptButtonClickListener) {
		this.onAcceptButtonClickListener = onAcceptButtonClickListener;
		if (buttonAccept != null)
			buttonAccept.setOnClickListener(onAcceptButtonClickListener);
	}

	public void setOnCancelButtonClickListener(
			View.OnClickListener onCancelButtonClickListener) {
		this.onCancelButtonClickListener = onCancelButtonClickListener;
		if (buttonCancel != null)
			buttonCancel.setOnClickListener(onAcceptButtonClickListener);
	}

	@Override
	public void dismiss() {
		Dialog.super.dismiss();
	}

}
