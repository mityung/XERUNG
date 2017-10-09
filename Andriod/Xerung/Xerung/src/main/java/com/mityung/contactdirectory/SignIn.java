package com.mityung.contactdirectory;

import java.util.ArrayList;
import org.json.JSONObject;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.example.contactplusgroup.adapter.CountrySpinnerAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.countrymaster.Country;
import com.example.contactplusgroup.countrymaster.CountryMaster;
import com.example.contactplusgroup.edittext.MaterialEditText;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.views.MaterialSpinner;
import com.example.contactplusgroup.webservice.CallWebService;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.CallWebService.WebServiceInterfaceSMS;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.ProgressDialog;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class SignIn extends Activity implements WebServiceInterface, WebServiceInterfaceSMS{

	private TextView txtOTPNote, txtApp;
	private ImageView btnSendOTP;
	private RelativeLayout layBack;
	private MaterialEditText edtMobile;
	private MaterialSpinner spCountry;
	private Context context;
	private Comman comman = null;
	private ProgressDialog pbBar = null;
	private SharedPreferanceData shared = null;
	final String TAG = this.getClass().toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		initialise();
	}
	
	private void initialise(){
		getRefer();
		findViewids();
		setOnClick();
	}
	
	private void getRefer(){
		comman = new Comman();
		context = this;
		pbBar = new ProgressDialog();
		shared = new SharedPreferanceData(SignIn.this);
	}

	private void findViewids() {
		txtApp = (TextView) findViewById(R.id.txtAppName);
		txtApp.setTypeface(ManagerTypeface.getTypeface(SignIn.this, R.string.typeface_android));
		txtOTPNote = (TextView) findViewById(R.id.txtNote);
		txtOTPNote.setTypeface(ManagerTypeface.getTypeface(SignIn.this, R.string.typeface_roboto_regular));
		layBack = (RelativeLayout)findViewById(R.id.layBack);
		
		edtMobile = (MaterialEditText)findViewById(R.id.inputMobile);
		edtMobile.setTypeface(ManagerTypeface.getTypeface(SignIn.this, R.string.typeface_roboto_regular));
		btnSendOTP = (ImageView)findViewById(R.id.btn_request_sms);
		
		spCountry = (MaterialSpinner)findViewById(R.id.spCountry);
		CountryMaster cm = CountryMaster.getInstance(context);
		ArrayList<Country> countries = cm.getCountries();
		CountrySpinnerAdapter adapter = new CountrySpinnerAdapter(context, R.layout.phone_country_image, countries);
		spCountry.setAdapter(adapter);
		try {
			spCountry.setSelection(79);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	String countryPrefix = null;
	String countryName = null;
	String countryIso = null;
	private void setOnClick(){
		btnSendOTP.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Build.VERSION.SDK_INT < 23){
					sendOTPNumber();
				}else {
					if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS)== PackageManager.PERMISSION_GRANTED) {
						sendOTPNumber();
					}else{
						getPermissionToReadUserContacts();
					}
				}
			}
		});
		
		layBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SignIn.this.finish();
			}
		});
		
		spCountry.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				CountryMaster cm = CountryMaster.getInstance(context);
				Country country = cm.getCountryByPosition(arg2);
				countryPrefix = country.mDialPrefix;
				countryName = country.mCountryName;
				countryIso = country.mCountryIso;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				//spCategory.setBackgroundResource(R.color.red);
			}
		});
		
	}

	private void sendOTPNumber(){
		if(checkNull()){
			PhoneNumberUtil util = PhoneNumberUtil.getInstance();
			// assuming you only a button in your layout...
			boolean isAuthentic = false;
			try {
				PhoneNumber number = util.parse(countryPrefix + edtMobile.getText().toString().trim(), countryIso);
				isAuthentic = true;
			} catch (NumberParseException e) {
				e.printStackTrace();
			}
			if (isAuthentic) {
				comman.hideSoftKeyBoard(context, edtMobile);
				createJson(edtMobile.getText().toString().trim(), countryPrefix, countryName);
			}
		}
	}
	
	private boolean checkNull(){
    	if(edtMobile.getText().toString().trim().equals("")){
    		edtMobile.setError("Please enter mobile number.");
    		return false;
    	}
    	return true;
    }
	
	private boolean createJson(String mobile, String countryCode, String countryName) {

		JSONObject json = new JSONObject();
		try {
			json.put("PPHONENUMBER", "+"+countryCode+mobile);
			json.put("PEMAIL", "");
			json.put("PNAME", "");
			json.put("PADDRESS", "");
			json.put("PCITYID", 0);
			json.put("PSTATEID", 0);
			json.put("PCOUNTRYCODEID", "+"+countryCode);
			json.put("PCOUNTRYNAME", countryName);
			json.put("POTPID", 0);
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
			comman.alertDialog(SignIn.this, getString(R.string.no_internet_connection),getString(R.string.you_dont_have_internet));
		}

		return true;
	}

	private void callWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(0)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(0));
		pbBar.progressPopUp(SignIn.this, "Loading..."); 
	}
	
	@Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stub
		if(pbBar!=null){
			pbBar.closeProgressPopUp();
		}
		if(result == null){
			comman.alertDialog(SignIn.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			comman.alertDialog(SignIn.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}
		if(processName.equals(Vars.webMethodName.get(0))){
			parseJson(result);
		}
	}
	
	String StatusId = null;
	private void parseJson(String result){
		try {
			JSONObject json = new JSONObject(result);
			StatusId = json.getString("STATUS");
			shared.saveSharedData("State", StatusId);
			String mOTP = json.getString("OTP");
			shared.saveSharedData("mOTP", mOTP);
			if(StatusId.equalsIgnoreCase("0")){
				//If phone number not exist.fill all the parameter manually
				 //senOTPJson("+"+countryPrefix+edtMobile.getText().toString().trim());
				sendComposedSms(countryPrefix+edtMobile.getText().toString().trim(), mOTP+" "+getString(R.string.verify_otp));
				/*Intent i = new Intent(SignIn.this, VerifyOTP.class);
				i.putExtra("State", StatusId);
				i.putExtra("MobileNum", edtMobile.getText().toString().trim());
				i.putExtra("CountryCode", countryPrefix);
				i.putExtra("CountryName", countryName);
				startActivity(i);*/
				//SignIn.this.finish();
			}else if(StatusId.equalsIgnoreCase("2")){
				//If user already registered login  & enter OTP
				sendComposedSms(countryPrefix+edtMobile.getText().toString().trim(), mOTP+" "+getString(R.string.verify_otp));
				/*Intent i = new Intent(SignIn.this, VerifyOTP.class);
				i.putExtra("State", StatusId);
				i.putExtra("MobileNum", edtMobile.getText().toString().trim());
				i.putExtra("CountryCode", countryPrefix);
				i.putExtra("CountryName", countryName);
				startActivity(i);
				SignIn.this.finish();*/
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	private void sendComposedSms(String mob, String otp){
		String data = comman.encodeBase64Text(mob)+"#"+comman.encodeBase64Text(otp);
		new CallWebService(this).execute(data, Vars.URL_SMS);
		pbBar.progressPopUp(SignIn.this, "Loading...");
	}

	@Override
	public void onWebCompleteResult(String result) {
		// TODO Auto-generated method stub
		if(pbBar!=null){
			pbBar.closeProgressPopUp();
		}
		if(result == null){
			comman.alertDialog(SignIn.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			comman.alertDialog(SignIn.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}else{
			Intent i = new Intent(SignIn.this, VerifyOTP.class);
			i.putExtra("State", StatusId);
			i.putExtra("MobileNum", edtMobile.getText().toString().trim());
			i.putExtra("CountryCode", countryPrefix);
			i.putExtra("CountryName", countryName);
			startActivity(i);
			SignIn.this.finish();
		}
	}

	// Identifier for the permission request
	private static final int GET_ACCOUNTS_PERMISSIONS_REQUEST = 1;

	// Called when the user is performing an action which requires the app to read the
	// user's contacts
	public void getPermissionToReadUserContacts() {
		// 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
		// checking the build version since Context.checkSelfPermission(...) is only available
		// in Marshmallow
		// 2) Always check for permission (even if permission has already been granted)
		// since the user can revoke permissions at any time through Settings
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS)
				!= PackageManager.PERMISSION_GRANTED) {

			// The permission is NOT already granted.
			// Check if the user has been asked about this permission already and denied
			// it. If so, we want to give more explanation about why the permission is needed.
			if (shouldShowRequestPermissionRationale(
					android.Manifest.permission.GET_ACCOUNTS)) {
				// Show our own UI to explain to the user why we need to read the contacts
				// before actually requesting the permission and showing the default UI
			}

			// Fire off an async request to actually get the permission
			// This will show the standard permission request dialog UI
			requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS},
					GET_ACCOUNTS_PERMISSIONS_REQUEST);
		}
	}

	// Callback with the request from calling requestPermissions(...)
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String permissions[],
										   @NonNull int[] grantResults) {
		// Make sure it's our original READ_CONTACTS request
		if (requestCode == GET_ACCOUNTS_PERMISSIONS_REQUEST) {
			if (grantResults.length == 1 &&
					grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(context, "Get Accounts permission granted.", Toast.LENGTH_SHORT).show();
				sendOTPNumber();
			} else {
				Toast.makeText(context, "Get Accounts permission denied.", Toast.LENGTH_SHORT).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}


	

}
