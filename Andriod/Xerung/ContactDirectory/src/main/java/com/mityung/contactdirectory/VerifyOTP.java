package com.mityung.contactdirectory;

import java.util.ArrayList;
import java.util.regex.Pattern;
import org.json.JSONObject;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.edittext.MaterialEditText;
import com.example.contactplusgroup.utils.DetectSms;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.views.MaterialSpinner;
import com.example.contactplusgroup.webservice.CallWebService;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.CallWebService.WebServiceInterfaceSMS;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.ProgressDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class VerifyOTP extends Activity implements WebServiceInterface, WebServiceInterfaceSMS{

	private TextView txtOTPNote, txtTimer, txtApp, txtMobile, txtIAgree;
	private Button btnResend, btnCreate;
	private ImageView btnVerifyOTP;
	private RelativeLayout layBack, relAlreadyUser;
	private LinearLayout layNewUser;
	private MaterialSpinner spEmail;
	private CardView layUserDetails;
	private MaterialEditText edtOTP, edtName;

	private CountDownTimer countDownTimer;
	private final long startTime = 60 * 1000;
	private final long interval = 1 * 1000;
	 private SharedPreferanceData shared = null;
		private Context context;
		private Comman comman = null;
		private ProgressDialog pbBar = null;

	public static String mobileNumber = "";
	public static String appVersion = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_otp);
		initialise();
	}
	
	private void initialise(){
		getRefer();
		findViewids();
		getBundleData();
		setOnClick();
	}
	
	private void getRefer(){
		comman = new Comman();
		context = this;
		pbBar = new ProgressDialog();
		appVersion = ""+comman.getAppVersionCode(context);
		shared = new SharedPreferanceData(VerifyOTP.this);
	}

	private void findViewids() {
		txtApp = (TextView) findViewById(R.id.txtAppName);
		txtApp.setTypeface(ManagerTypeface.getTypeface(VerifyOTP.this, R.string.typeface_android));
		txtOTPNote = (TextView) findViewById(R.id.txtvalidateOtp);
		txtOTPNote.setTypeface(ManagerTypeface.getTypeface(VerifyOTP.this, R.string.typeface_roboto_regular));
		layBack = (RelativeLayout)findViewById(R.id.layBack);
		txtTimer = (TextView)findViewById(R.id.txttimer);
		txtTimer.setTypeface(ManagerTypeface.getTypeface(VerifyOTP.this, R.string.typeface_roboto_regular));
		btnResend = (Button)findViewById(R.id.btnResendOTP);
		btnResend.setTypeface(ManagerTypeface.getTypeface(VerifyOTP.this, R.string.typeface_roboto_regular));
		txtMobile = (TextView)findViewById(R.id.txtMobile);
		txtMobile.setTypeface(ManagerTypeface.getTypeface(VerifyOTP.this, R.string.typeface_roboto_regular));
		edtOTP = (MaterialEditText)findViewById(R.id.edtOTP);
		edtOTP.setTypeface(ManagerTypeface.getTypeface(VerifyOTP.this, R.string.typeface_roboto_regular));
		//edtOTP.setText(shared.getSharedValue("mOTP"));
		btnVerifyOTP = (ImageView)findViewById(R.id.btn_verify_otp);
		layUserDetails = (CardView) findViewById(R.id.layUserDetails);
		edtName  =(MaterialEditText)findViewById(R.id.edtName);
		edtName.setTypeface(ManagerTypeface.getTypeface(VerifyOTP.this, R.string.typeface_roboto_regular));
		relAlreadyUser = (RelativeLayout)findViewById(R.id.relAlreadyUser);
		layNewUser = (LinearLayout)findViewById(R.id.layNewUser);
		btnCreate = (Button)findViewById(R.id.btnCreateAccount);
		btnCreate.setTypeface(ManagerTypeface.getTypeface(VerifyOTP.this, R.string.typeface_roboto_regular));
		txtIAgree = (TextView)findViewById(R.id.txtTermsCondt);
		txtIAgree.setTypeface(ManagerTypeface.getTypeface(VerifyOTP.this, R.string.typeface_roboto_regular));
		countDownTimer = new MyCountDownTimer(startTime, interval);
        txtTimer.setText(String.valueOf(startTime / 1000)+"s");
        spEmail = (MaterialSpinner)findViewById(R.id.spEmail);
        countDownTimer.start();
        setAdapter();
	}
	
	private ArrayAdapter<String> emailAdapter = null;
	private void setAdapter() {
		
		ArrayList<String> emailID = getUserEmail();
		emailAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, emailID);
		emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spEmail.setAdapter(emailAdapter);
		try {
			spEmail.setSelection(1);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	String mStatusId = "";
	String mMobileNum = "";
	String mMobilePrefix = "";
	String mCountryName = "";
	private void getBundleData(){
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mStatusId = extras.getString("State");
			mMobileNum = extras.getString("MobileNum");
			mMobilePrefix = extras.getString("CountryCode");
			mCountryName = extras.getString("CountryName");
			txtMobile.setText(mMobileNum);
			mobileNumber = "+"+mMobilePrefix+mMobileNum;
		    // and get whatever type user account id is
			if(mStatusId.equalsIgnoreCase("0")){
				userRegisteredNot();
			}else if(mStatusId.equalsIgnoreCase("2")){
				userRegistered();
			}
		}
	}
	

	private ArrayList<String> getUserEmail(){

		ArrayList<String> email = new ArrayList<String>();

		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(VerifyOTP.this).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				String possibleEmail = account.name;
				if(possibleEmail != null)
					if(possibleEmail.length() !=0 ){
						email.add(possibleEmail);
					}
			}
		}		
		return email;

	}	
	
	private void setOnClick(){
		
		layBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VerifyOTP.this.finish();
			}
		});
		
		txtMobile.setOnTouchListener(new RightDrawableOnTouchListener(txtMobile) {
	        @Override
	        public boolean onDrawableTouch(final MotionEvent event) {
	        	startActivity(new Intent(VerifyOTP.this, SignIn.class));
	        	VerifyOTP.this.finish();
	        	return false;       
	        }
	    });
		
		btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkNullAlready()){
					comman.hideSoftKeyBoard(context, edtOTP);
					if(shared.getSharedValue("mOTP").equalsIgnoreCase(edtOTP.getText().toString().trim())){
						createJson("+"+mMobilePrefix+mMobileNum, "", "");
					}else{
						Toast.makeText(VerifyOTP.this, "OTP doesn't match", Toast.LENGTH_SHORT).show();
					}
						
					
				}
					
			}
		});
		
		btnCreate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkNull()){
					comman.hideSoftKeyBoard(context, edtOTP);
					if(shared.getSharedValue("mOTP").equalsIgnoreCase(edtOTP.getText().toString().trim())){
						createJson("+"+mMobilePrefix+mMobileNum, comman.upperCaseAllFirst(edtName.getText().toString().trim()), spEmail.getSelectedItem().toString().trim());
					}else{
						Toast.makeText(VerifyOTP.this, "OTP doesn't match", Toast.LENGTH_SHORT).show();
					}
				}
					
			}
		});
		
		btnResend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				senOTPJson("+"+mMobilePrefix+mMobileNum);
			}
		});
	}
	
	private void ontimerFinish(){
		txtOTPNote.setText(getString(R.string.enter_otp));
		btnResend.setVisibility(View.VISIBLE);
		txtTimer.setVisibility(View.GONE);
	}
	
	/*private void offtimerFinish(){
		txtOTPNote.setText(getString(R.string.validate_otp));
		btnResend.setVisibility(View.GONE);
		txtTimer.setVisibility(View.VISIBLE);
	}*/
	
	private void userRegistered(){
		
		layUserDetails.setVisibility(View.GONE);
		layNewUser.setVisibility(View.GONE);
		relAlreadyUser.setVisibility(View.VISIBLE);
	}
	
	private void userRegisteredNot(){
		layUserDetails.setVisibility(View.VISIBLE);
		layNewUser.setVisibility(View.VISIBLE);
		relAlreadyUser.setVisibility(View.GONE);
	}
	
	public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
        	ontimerFinish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            txtTimer.setText("" + millisUntilFinished / 1000+"s");
        }
    }
	
	public abstract class RightDrawableOnTouchListener implements OnTouchListener {
	    Drawable drawable;
	    private int fuzz = 10;


	    public RightDrawableOnTouchListener(TextView view) {
	        super();
	        final Drawable[] drawables = view.getCompoundDrawables();
	        if (drawables != null && drawables.length == 4)
	            this.drawable = drawables[2];
	    }

	    /*
	     * (non-Javadoc)
	     * 
	     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	     */
	    @Override
	    public boolean onTouch(final View v, final MotionEvent event) {
	        if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null) {
	            final int x = (int) event.getX();
	            final int y = (int) event.getY();
	            final Rect bounds = drawable.getBounds();
	            if (x >= (v.getRight() - bounds.width() - fuzz) && x <= (v.getRight() - v.getPaddingRight() + fuzz)
	                    && y >= (v.getPaddingTop() - fuzz) && y <= (v.getHeight() - v.getPaddingBottom()) + fuzz) {
	                return onDrawableTouch(event);
	            }
	        }
	        return false;
	    }

	    public abstract boolean onDrawableTouch(final MotionEvent event);

	}

	private boolean createJson(String mobile, String name, String email) {

		JSONObject json = new JSONObject();
		try {
			json.put("PPHONENUMBER", mobile);
			json.put("PEMAIL", email);
			json.put("PNAME", name);
			json.put("PADDRESS", "");
			json.put("PCITYID", 0);
			json.put("PSTATEID", 0);
			json.put("PCOUNTRYCODEID", mMobilePrefix);
			json.put("PCOUNTRYNAME", mCountryName);
			json.put("POTPID", edtOTP.getText().toString().trim());
			json.put("PSTATUSID", 0);
			json.put("PPROFESSION", "");
			json.put("PLOGINFLAG", 0);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			 callWebServiceProcess(json);
		} else {
			comman.alertDialog(VerifyOTP.this, getString(R.string.no_internet_connection),getString(R.string.you_dont_have_internet));
		}

		return true;
	}

	private void callWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(0)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(0));
		pbBar.progressPopUp(VerifyOTP.this, "Loading..."); 
	}

	private boolean checkNullAlready(){
    	
    	if(edtOTP.getText().toString().trim().equals("")){
    		edtOTP.setError("Please enter you OTP.");
    		return false;
    	}
    	return true;
    }
	
	private boolean checkNull(){
    	if(edtName.getText().toString().trim().equals("")){
    		edtName.setError("Please enter your full name.");
    		return false;
    	}
    	if(spEmail.getSelectedItemPosition() == 0){
			Toast.makeText(VerifyOTP.this, "Please select an email Id.", Toast.LENGTH_SHORT).show();
			return false;
		}
    	if(edtOTP.getText().toString().trim().equals("")){
    		edtOTP.setError("Please enter you OTP.");
    		return false;
    	}
    	return true;
    }

	@Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stub
		if(pbBar!=null){
			pbBar.closeProgressPopUp();
		}
		if(result == null){
			comman.alertDialog(VerifyOTP.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			comman.alertDialog(VerifyOTP.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}
		if(processName.equals(Vars.webMethodName.get(0))){
			parseJson(result);
		}else if(processName.equals(Vars.webMethodName.get(5))){
			parseProfile(result);
			addFCM();
		}else if(processName.equals(Vars.webMethodName.get(1))){
			parseJsonOTP(result);
		}
	}
	
	private boolean senOTPJson(String mobile) {

		JSONObject json = new JSONObject();
		try {
			json.put("PPHONENUMBER", mobile);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			sendOTPWebServiceProcess(json);
		} else {
			comman.alertDialog(VerifyOTP.this, getString(R.string.no_internet_connection),getString(R.string.you_dont_have_internet));
		}

		return true;
	}

	private void sendOTPWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(1)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(1));
		pbBar.progressPopUp(VerifyOTP.this, "Loading..."); 
	}
	
	private void parseJsonOTP(String result){
		try {
			String OTP = result;
			shared.saveSharedData("mOTP", OTP);
			sendComposedSms(mMobilePrefix+mMobileNum, OTP+" "+getString(R.string.verify_otp));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private boolean fetchProfile(String uid) {
		JSONObject json = new JSONObject();
		try {
			json.put("PUID", uid);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			fetchProfileWebServiceProcess(json);
		} else {
			comman.alertDialog(VerifyOTP.this, getString(R.string.no_internet_connection),getString(R.string.you_dont_have_internet));
		}

		return true;
	}

	private void fetchProfileWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(5)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(5));
		pbBar.progressPopUp(VerifyOTP.this, "Loading..."); 
	}
	
	String cityName = null;
	private void parseProfile(String result){
		try {
			JSONObject jSON = new JSONObject(result);
			String uphone = jSON.getString("PHONENUMBER");
			shared.saveSharedData("PMOBILE", uphone);
			String altNumber = jSON.getString("ALTERNATENO");
			shared.saveSharedData("PALTERNATEMOBILE", altNumber);
			String uemail = jSON.getString("EMAIL");
			shared.saveSharedData("PEMAIL", uemail);
			String uname = jSON.getString("NAME");
			shared.saveSharedData("PNAME", comman.upperCaseAllFirst(uname));
			String address = jSON.getString("ADDRESS");
			shared.saveSharedData("PADDRESS", comman.upperCaseAllFirst(address));
			String coundryIsd = jSON.getString("COUNTRYCODEIDPHONE");
			shared.saveSharedData("PCOUNTRYISD", coundryIsd);
			String countryid = jSON.getString("COUNTRYCODEID");
			shared.saveSharedData("PCOUNTRYID", countryid);
			String country = jSON.getString("COUNTRYNAME");
			shared.saveSharedData("PCOUNTRY", country);
			String profession = jSON.getString("PROFESSION");
			shared.saveSharedData("PPROFESSION", comman.upperCaseAllFirst(profession));
			String photo = jSON.getString("PHOTO");
			shared.saveSharedData("PPHOTO", photo);
			String company = jSON.getString("COMPANYNAME");
			shared.saveSharedData("PCOMPANYNAME", comman.upperCaseAllFirst(company));
			String bloodgroup = jSON.getString("BLOODGROUP");
			shared.saveSharedData("PBLOODGROUP", bloodgroup);
			cityName = jSON.getString("CITYNAME");
			shared.saveSharedData("PCITYNAME", cityName);
			shared.saveSharedData("LoginStatus", "Success");
			startActivity(new Intent(VerifyOTP.this, MainDashboard.class));
			VerifyOTP.this.finish();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("dadaad", "sddderoor" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void parseJson(String result){
		try {
		
			JSONObject json = new JSONObject(result);
				if(json.getString("STATUS").equalsIgnoreCase("1")){
					String UID = json.getString("UID");
					shared.saveSharedData("UserID", UID);
					fetchProfile(shared.getSharedValue("UserID"));
				}else if(json.getString("STATUS").equalsIgnoreCase("3")){
					//If OTP doesn't match 
					comman.alertDialog(VerifyOTP.this, "Message", getString(R.string.account_email_exist));
					 
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void sendComposedSms(String mob, String otp){
		String data = comman.encodeBase64Text(mob)+"#"+comman.encodeBase64Text(otp);
		new CallWebService(this).execute(data, Vars.URL_SMS);
		pbBar.progressPopUp(VerifyOTP.this, "Loading...");
	}
	@Override
	public void onWebCompleteResult(String result) {
		// TODO Auto-generated method stub
		if(pbBar!=null){
			pbBar.closeProgressPopUp();
		}
		if(result == null){
			comman.alertDialog(VerifyOTP.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			comman.alertDialog(VerifyOTP.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}else{

		}
	}

	//-------------------------------- Gcm--------------------
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String TAG = "VerifyOTP";

	private BroadcastReceiver mRegistrationBroadcastReceiver;
	//private ProgressBar mRegistrationProgressBar;
	//private TextView mInformationTextView;
	private boolean isReceiverRegistered;

	private void addFCM() {

		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				//mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
				SharedPreferences sharedPreferences =
						PreferenceManager.getDefaultSharedPreferences(context);
				boolean sentToken = sharedPreferences
						.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
				if (sentToken) {
					//Toast.makeText(VerifyOTP.this,"Send Token",Toast.LENGTH_LONG).show();
					//mInformationTextView.setText(getString(R.string.gcm_send_message));
				} else {
					//Toast.makeText(VerifyOTP.this,"Send Token Error ",Toast.LENGTH_LONG).show();
					//mInformationTextView.setText(getString(R.string.token_error_message));
				}
			}
		};
		// Registering BroadcastReceiver
		registerReceiver();

		if (checkPlayServices()) {
			// Start IntentService to register this application with GCM.
			Intent intent = new Intent(this, RegistrationIntentService.class);
			startService(intent);
		}
	}

	private void registerReceiver() {
		if (!isReceiverRegistered) {
			LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
					new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
			isReceiverRegistered = true;
		}
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		isReceiverRegistered = false;
		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.moveTaskToFront(getTaskId(), 0);
		super.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("SMS_RECEIVED");
		registerReceiver(broadcastReceiver, filter);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		try {
			unregisterReceiver(broadcastReceiver);
		}catch (IllegalArgumentException e)
		{
			Log.d("XERUNG","exception in onstop is : "+e.toString());
		}
	}

	private String otp;
	BroadcastReceiver broadcastReceiver = new DetectSms() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final Bundle bundle = intent.getExtras();
			edtOTP.setText(bundle.getString("otp_code"));
			otp = edtOTP.getText().toString().trim();
			Log.d("DetectSMS",bundle.getString("otp_code"));
			Handler handler = new Handler();
			handler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					verifyOTP(otp);
				}
			},2500);

		}
	};

	public void verifyOTP(String otp)
	{
		this.otp = otp;
		Log.d("DetectSMS","otp is : "+otp);
		comman.hideSoftKeyBoard(context, edtOTP);
		if(shared.getSharedValue("mOTP").equalsIgnoreCase(edtOTP.getText().toString().trim())){
			createJson("+"+mMobilePrefix+mMobileNum, "", "");
		}else{
			Toast.makeText(VerifyOTP.this, "OTP doesn't match", Toast.LENGTH_SHORT).show();
		}
	}

}
