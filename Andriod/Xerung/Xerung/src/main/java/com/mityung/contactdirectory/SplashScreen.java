package com.mityung.contactdirectory;

import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.utils.ManagerTypeface;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashScreen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 600;
	private TextView txtcopyright;
	private SharedPreferanceData shared = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		shared = new SharedPreferanceData(SplashScreen.this);
		findViewids();
		if(checkAppVersion())
			startMainActivity();
	}

	private void findViewids() {
		txtcopyright = (TextView) findViewById(R.id.txtcopyright);
		txtcopyright.setTypeface(ManagerTypeface.getTypeface(SplashScreen.this, R.string.typeface_roboto_regular_thin));
	}

	private void startMainActivity() {

		new Handler().postDelayed(new Runnable() { /*Showing splash screen with a timer. This will be useful when you want to show case your app logo / company*/
			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				if (shared.getSharedValue("LoginStatus").equalsIgnoreCase("Success")) {
					startActivity(new Intent(SplashScreen.this, MainDashboard.class));
				} else {
					startActivity(new Intent(SplashScreen.this, SignIn.class));
				}
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

	private boolean checkAppVersion(){


		if(shared.getSharedValue("AppUpdateNoStartStatus").trim().equals(new Comman().getAppVersionName(SplashScreen.this))){
			startActivity(new Intent(SplashScreen.this,AppUpdate.class));
			SplashScreen.this.finish();
			return false;
		}else{
			shared.saveSharedData("UpdateMsg", "");
		}
		return true;
	}

}
