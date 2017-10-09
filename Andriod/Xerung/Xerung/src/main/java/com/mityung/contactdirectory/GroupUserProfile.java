package com.mityung.contactdirectory;

import org.json.JSONObject;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.views.CircleImageView;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.Dialog;

public class GroupUserProfile extends Activity implements WebServiceInterface{

	private TextView txtApp, txtInternet, txtnetwork, txtUserName;
	private RelativeLayout layBack, relProgress, relInternet, relNetwork;
	private ScrollView scrollMain;
	private TextView txtName, txtEmail, txtMobile, txtCompany, txtProfession, txtBloodGroup,
	txtAddress, txtCity, txtCountry, txtAlternateNumber,txtUserStatus;
	private LinearLayout layMob, layAlt, layEmail;
	private Button txtMakeAdmin;
	private ImageView imgMobCall,imgAltCall, imgEmail,imgSave, imgDelete;
	private SharedPreferanceData shared = null;
	private CircleImageView imagePhoto;
	private Comman comman = null;
	private Context context;
	final String TAG = this.getClass().toString();

    GroupDb dbGroup = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_group_user_profile);
		initialise();
	}
	
	private void initialise(){		
		getRefer();
		findViewids();
		getBundleData();
		setOnClick();
		//Log.e("DADD", "SASASA"+Vars.gMadeByNumber);
	}
	
	private void getRefer(){
		comman  = new Comman();
		shared  = new SharedPreferanceData(GroupUserProfile.this);
		context = this;		
		dbGroup = new GroupDb(GroupUserProfile.this);
	}
	
	String tableName           = "";
	String uid                 = "";
	String index               = "";
	String admin 				= "";
	//String uid = "";
	private void getBundleData(){
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			txtMobile.setText(bundle.get("Number").toString().trim());		
			if(bundle.get("MyPhoneBook").toString().trim().equals("0")){
				txtUserName.setText(bundle.get("Number").toString().trim());
			}else{
				txtUserName.setText(bundle.get("MyPhoneBook").toString().trim());
			}
			txtName.setText(bundle.get("OrignalName").toString().trim());			
		    uid        = bundle.get("Uid").toString().trim();
		    tableName  = bundle.get("TableName").toString().trim();
		    index      = bundle.get("Index").toString().trim();
			admin      = bundle.get("AdminFlag").toString().trim();
			setUserStatus(bundle.get("UserType").toString().trim());
			removeGroupMember(bundle.get("Number").toString().trim());
			if(bundle.getInt("IsMyContact")==0){
				imgSave.setVisibility(View.GONE);
			}else if(!comman.isNumeric(bundle.get("MyPhoneBook").toString().trim())){
				imgSave.setVisibility(View.GONE);
			}else{
				imgSave.setVisibility(View.VISIBLE);
			}
			fetchProfile(uid);
		}
	}

	private void removeGroupMember(String gNum){
		if(Vars.gMadeByNumber.equals(shared.getSharedValue("PMOBILE").trim().equals(gNum))){
			if(Vars.gMadeByNumber.equals(gNum)){
				imgDelete.setVisibility(View.GONE);
			}else{
				imgDelete.setVisibility(View.VISIBLE);
			}
		}else
			imgDelete.setVisibility(View.VISIBLE);
	}

	private void setUserStatus(String userStatus){
		
		if(userStatus.trim().equals("1")){
			txtUserStatus.setText("Admin");
			//imgDelete.setVisibility(View.VISIBLE);
			txtUserStatus.setVisibility(View.VISIBLE);
			txtMakeAdmin.setVisibility(View.GONE);
		}else if(userStatus.trim().equals("2")){
			//imgDelete.setVisibility(View.GONE);
			txtUserStatus.setVisibility(View.GONE);
			if(admin.equalsIgnoreCase("1")){
				txtMakeAdmin.setVisibility(View.VISIBLE);
			}else{
				txtMakeAdmin.setVisibility(View.GONE);
			}
		}
	}
	
	private void findViewids() {
		txtApp = (TextView) findViewById(R.id.txtAppName);
		txtApp.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_android));
		layBack = (RelativeLayout)findViewById(R.id.layBack);
		imagePhoto = (CircleImageView)findViewById(R.id.imgProfilePicSet);
		txtName = (TextView)findViewById(R.id.txtName);
		txtName.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtUserName = (TextView)findViewById(R.id.txtUserName);
		txtUserName.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtEmail = (TextView)findViewById(R.id.txtEmail);
		txtEmail.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		imgEmail = (ImageView)findViewById(R.id.imgEmail);
		layEmail = (LinearLayout)findViewById(R.id.layEmail);
		txtMobile = (TextView)findViewById(R.id.txtMobile);
		txtMobile.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		imgMobCall = (ImageView)findViewById(R.id.imgCallMob);
		layMob   = (LinearLayout)findViewById(R.id.layMob);		
		txtAlternateNumber = (TextView)findViewById(R.id.txtAlternateMobile);
		txtBloodGroup = (TextView)findViewById(R.id.txtBloodGroup);
		txtAlternateNumber.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtBloodGroup.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		imgAltCall   = (ImageView)findViewById(R.id.imgCallAlt);
		layAlt       = (LinearLayout)findViewById(R.id.layAltMob);
		txtProfession = (TextView)findViewById(R.id.txtProfession);
		txtProfession.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtAddress = (TextView)findViewById(R.id.txtAddress);
		txtAddress.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtCity = (TextView)findViewById(R.id.txtCity);
		txtCity.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtCountry = (TextView)findViewById(R.id.txtCountry);
		txtCountry.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtCompany = (TextView)findViewById(R.id.txtCompany);
		txtCompany.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtMakeAdmin = (Button) findViewById(R.id.txtMadeAdmin);
		txtMakeAdmin.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtUserStatus = (TextView)findViewById(R.id.txtUserStatus);
		txtUserStatus.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		imgSave  = (ImageView)findViewById(R.id.imgSave);
		relProgress = (RelativeLayout)findViewById(R.id.layProgressresult);
		relInternet = (RelativeLayout)findViewById(R.id.layinternetresult);
		relNetwork = (RelativeLayout)findViewById(R.id.layNoresultserver);
		txtInternet = (TextView)findViewById(R.id.txtnoinertnettestresult);
		txtInternet.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		txtnetwork = (TextView)findViewById(R.id.txtNoresultnetwork);
		txtnetwork.setTypeface(ManagerTypeface.getTypeface(GroupUserProfile.this, R.string.typeface_roboto_regular));
		scrollMain = (ScrollView)findViewById(R.id.laymain);
		imgDelete   = (ImageView)findViewById(R.id.imgDelete);
	}
	
	Integer cityID = 0;
	private void setOnClick(){
		
		
		layBack.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GroupUserProfile.this.finish();
			}
		});		
		
		imgAltCall.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(Build.VERSION.SDK_INT < 23){
					comman.callPhone(txtAlternateNumber.getText().toString().trim(), GroupUserProfile.this);
				}else {
					if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
						comman.callPhone(txtAlternateNumber.getText().toString().trim(), GroupUserProfile.this);
					}else{
						comman.callPhone(txtAlternateNumber.getText().toString().trim(), GroupUserProfile.this);
					}


				}
			}
		});
		
		imgMobCall.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(Build.VERSION.SDK_INT < 23){
					comman.callPhone(txtMobile.getText().toString().trim(), GroupUserProfile.this);
				}else {
					if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
						comman.callPhone(txtMobile.getText().toString().trim(), GroupUserProfile.this);
					}else{
						//getPermissionToReadUserContacts();
					}


				}

			}
		});
		
		imgEmail.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				comman.callEmail(GroupUserProfile.this, txtEmail.getText().toString().trim());
			}
		});
		
		txtMakeAdmin.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callMakeAdmin();
			}
		});
		
		imgSave.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(Build.VERSION.SDK_INT < 23){
					alertSaveContact("Add Contact", "Do you want to add this contact in you phone.");
				}else {
					if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)== PackageManager.PERMISSION_GRANTED) {
						alertSaveContact("Add Contact", "Do you want to add this contact in you phone.");
					}else{
						//getPermissionToReadUserContacts();
					}
				}
			}
		});

		imgDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callDeleteMember();
			}
		});
	}

	private void offServerResponse() {
		relNetwork.setVisibility(View.VISIBLE);
		scrollMain.setVisibility(View.GONE);
	}

	private void showProgress() {

		relProgress.setVisibility(View.VISIBLE);
		scrollMain.setVisibility(View.GONE);

	}

	private void hideProgress() {

		relProgress.setVisibility(View.GONE);
		scrollMain.setVisibility(View.VISIBLE);

	}

	private void offNetwork() {

		relInternet.setVisibility(View.VISIBLE);
		//scrollMain.setVisibility(View.GONE);

	}

	private void onNetwork() {

		relInternet.setVisibility(View.GONE);
		scrollMain.setVisibility(View.VISIBLE);
	}

	@Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stubs
		hideProgress();
		if(result == null){
			//comman.alertDialog(GroupUserProfile.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			offServerResponse();
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			//comman.alertDialog(GroupUserProfile.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			offServerResponse();
			return;
		}
		
		if(processName.equals(Vars.webMethodName.get(5))){
			parseProfile(result);
		}else if(processName.equals(Vars.webMethodName.get(12))){
			try {
				JSONObject jb = new JSONObject(result);
				if(jb.getString("STATUS").trim().equalsIgnoreCase("success")){
					setUserStatus("1");
					Intent i = new Intent();
					i.putExtra("Index", index);
					setResult(1, i);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}if(processName.equals(Vars.webMethodName.get(15))){
			try {
				JSONObject jb = new JSONObject(result);
				if(jb.getString("STATUS").trim().equalsIgnoreCase("success")){
					Intent i = new Intent();
					i.putExtra("Index", index);
					setResult(2, i);
					GroupUserProfile.this.finish();
				}else{
					Toast.makeText(GroupUserProfile.this, "Only admin has permission to delete members.", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
	
	private void getMemberDetail(){
		
		ContactBean cb = dbGroup.getMemberDetail(Integer.parseInt(uid), tableName);
		if(cb.getMyPhoneBookName().trim().equals("0")){
			txtApp.setText(cb.getNumber());
		}else{
			txtApp.setText(cb.getMyPhoneBookName().trim());
		}
		
		txtName.setText(cb.getOrignalName());	
		
		if(cb.getEmail() == null){
			layEmail.setVisibility(View.GONE);
		}else if(cb.getEmail().equals("")){
			layEmail.setVisibility(View.GONE);
		}else
			txtEmail.setText(cb.getEmail());
		
		if(cb.getNumber() == null)
			layMob.setVisibility(View.GONE);
		else
			txtMobile.setText(cb.getNumber());
		
		if(cb.getAltPhone() == null)
			layAlt.setVisibility(View.GONE);
		else if(cb.getAltPhone().equals("") || cb.getAltPhone().equalsIgnoreCase("0") )
			layAlt.setVisibility(View.GONE);
		else
			txtAlternateNumber.setText(cb.getAltPhone());
		
		if(cb.getAddress() == null)
			txtAddress.setVisibility(View.GONE);
		else if(cb.getAddress().equals(""))
			txtAddress.setVisibility(View.GONE);
		else
			txtAddress.setText(cb.getAddress());
		
		if(cb.getCity() == null )
			txtCity.setVisibility(View.GONE);
		else if(cb.getCity().equals("") || cb.getCity().equalsIgnoreCase("0"))
			txtCity.setVisibility(View.GONE);
		else
			txtCity.setText(cb.getCity());

		if(cb.getmBloodGroup() == null)
			txtBloodGroup.setVisibility(View.GONE);
		else if(cb.getmBloodGroup().equals("") || cb.getmBloodGroup().equalsIgnoreCase("0"))
			txtBloodGroup.setVisibility(View.GONE);
		else
			txtBloodGroup.setText(cb.getmBloodGroup());
		
		if(cb.getCountry() == null)
			txtCountry.setVisibility(View.GONE);
		else if(cb.getCountry().equals(""))
			txtCountry.setVisibility(View.GONE);
		else
			txtCountry.setText(cb.getCountry());
		
		if(cb.getProfession() == null)
			txtProfession.setVisibility(View.GONE);
		else if(cb.getProfession().equals("") || cb.getProfession().equals("0"))
			txtProfession.setVisibility(View.GONE);
		else
			txtProfession.setText(cb.getProfession());
		
		if(cb.getCompany() == null) {
			txtCompany.setVisibility(View.GONE);
		}else if(cb.getCompany().equals("") || cb.getCompany().equals("0") )
			txtCompany.setVisibility(View.GONE);
		else
			txtCompany.setText(cb.getCompany());
		
		String mPhoto = cb.getPhoto();
			// Set image if exists
		
	        try {
	        	
	            if(mPhoto != null ){
					if(mPhoto.length() != 0){
						if(!mPhoto.equalsIgnoreCase("null")  && !mPhoto.equalsIgnoreCase("0")){
							imagePhoto.setImageResource(0);
							imagePhoto.setImageBitmap(comman.decodeBase64(mPhoto));
						}else{
							imagePhoto.setImageBitmap(null);
							imagePhoto.setImageResource(R.drawable.ic_account_circle_white_64dp);
						}
					}else{
						imagePhoto.setImageBitmap(null);
						imagePhoto.setImageResource(R.drawable.ic_account_circle_white_64dp);
					}
				}else{
					imagePhoto.setImageResource(R.drawable.ic_account_circle_white_64dp);
				}
	            // Seting round image
	        } catch (OutOfMemoryError e) {
	            // Add default picture
	        	imagePhoto.setImageResource(R.drawable.ic_account_circle_white_64dp);
	            e.printStackTrace();
	        }

	}
	
	private void parseProfile(String result){
		
		try {
			ContactBean cb = new ContactBean();
			
			JSONObject jSON = new JSONObject(result);
			String uphone = jSON.getString("PHONENUMBER").trim();
			if(uphone.length() == 0){
				layMob.setVisibility(View.GONE);
			}else
				txtMobile.setText(uphone);
			
			cb.setNumber(uphone);
			cb.setUID(uid);
			
			String altNumber = jSON.getString("ALTERNATENO").trim();
			if(altNumber.length() == 0)
				layAlt.setVisibility(View.GONE);
			else if(altNumber.equalsIgnoreCase("0"))
				layAlt.setVisibility(View.GONE);
			else
				txtAlternateNumber.setText(altNumber);
			cb.setAltPhone(altNumber);
			
			String uemail = jSON.getString("EMAIL").trim();
			if(uemail.length() == 0)
				layEmail.setVisibility(View.GONE);
			else if(uemail.equalsIgnoreCase("0"))
				layEmail.setVisibility(View.GONE);
			else
				txtEmail.setText(uemail);
			cb.setEmail(uemail);
	
			String address = jSON.getString("ADDRESS").trim();
			if(address.length() == 0)
				txtAddress.setVisibility(View.GONE);
			else if(address.equalsIgnoreCase("0"))
				txtAddress.setVisibility(View.GONE);
			else
				txtAddress.setText(address);
			cb.setAddress(address);
			
			String coundryIsd = jSON.getString("COUNTRYCODEIDPHONE").trim();		
			String countryid = jSON.getString("COUNTRYCODEID").trim();	
			
			String country = jSON.getString("COUNTRYNAME").trim();
			if(country.length() == 0)
				txtCountry.setVisibility(View.GONE);
			else
				txtCountry.setText(country);
			cb.setCountry(country);
			
			String profession = jSON.getString("PROFESSION").trim();
			if(profession.trim().length() == 0)
				txtProfession.setVisibility(View.GONE);
			else if(profession.equalsIgnoreCase("0"))
				txtProfession.setVisibility(View.GONE);
			else
				txtProfession.setText(profession);
			cb.setProfession(profession);
			
			String companyName = jSON.getString("COMPANYNAME").trim();
			if(companyName.trim().length() == 0)
				txtCompany.setVisibility(View.GONE);
			else if(companyName.equalsIgnoreCase("0"))
				txtCompany.setVisibility(View.GONE);
			else
				txtCompany.setText(companyName);
			cb.setCompany(companyName);
			
			String cityName = jSON.getString("CITYNAME").trim();			
			if(cityName.length() == 0)
				txtCity.setVisibility(View.GONE);
			else
				txtCity.setText(cityName);
			cb.setCity(cityName);

			String blood = jSON.getString("BLOODGROUP").trim();
			if(blood.length() == 0 )
				txtBloodGroup.setVisibility(View.GONE);
			else if(blood.equalsIgnoreCase("0"))
				txtBloodGroup.setVisibility(View.GONE);
			else
				txtBloodGroup.setText(blood);
			cb.setCity(cityName);
			
			String photo = jSON.getString("PHOTO");
		        try {
		            if(photo != null ){
						if(photo.length() != 0){
							if(!photo.equalsIgnoreCase("null")){
								imagePhoto.setImageResource(0);
								imagePhoto.setImageBitmap(comman.decodeBase64(photo));
								cb.setPhoto(photo);    
							}else{
								imagePhoto.setImageBitmap(null);
								imagePhoto.setImageResource(R.drawable.ic_account_circle_white_64dp);
								cb.setPhoto("0");    
							}
						}else{
							imagePhoto.setImageBitmap(null);
							imagePhoto.setImageResource(R.drawable.ic_account_circle_white_64dp);
							cb.setPhoto("0"); 
						}
					}else{
						imagePhoto.setImageResource(R.drawable.ic_account_circle_white_64dp);
						cb.setPhoto("0"); 
					}
		            // Seting round image
		        } catch (OutOfMemoryError e) {
		            // Add default picture
		        	imagePhoto.setImageResource(R.drawable.ic_account_circle_white_64dp);
		        	cb.setPhoto("0"); 
		            e.printStackTrace();
		        }
		        
		    
			dbGroup.addMemberDetail(cb, tableName);
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
			onNetwork();
		} else {
			//comman.alertDialog(GroupUserProfile.this, getString(R.string.no_internet_connection),getString(R.string.you_dont_have_internet));
			offNetwork();
			getMemberDetail();
		}

		return true;
	}
	
	private void fetchProfileWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(5)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(5));
		showProgress();
	}
	
	private void callMakeAdmin(){
		try {
			JSONObject jb = new JSONObject();
			jb.put("PPHONENUMBER", txtMobile.getText().toString().trim());
			jb.put("PPHOTO", "");
			jb.put("PFLAG", "2");
			makeAdmin(jb);
			showProgress();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void makeAdmin(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(12)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(12));
		showProgress();
	}
	
	private void alertSaveContact(String title, String msg){
		
		Dialog dialog = new Dialog(context, title, msg);
		dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Register.this.finish();
				String alt = txtAlternateNumber.getText().toString().trim();
				if(alt.length() < 2){
					alt = null;
				}
				String email = txtEmail.getText().toString().trim();
				if(email.length() < 2){
					email = null;
				}
				String company = txtCompany.getText().toString().trim();
				if(company.length()<2){
					company = null;
				}
				
				String jobTitle = txtProfession.getText().toString().trim();
				if(jobTitle.length() == 0 )
					jobTitle = null;
				comman.addNewContact(GroupUserProfile.this, txtName.getText().toString().trim(), txtMobile.getText().toString().trim(),
						alt, email, company, jobTitle);
			}
		});
		dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Register.this.finish();
			}
		});
		dialog.show();
		
	}

	private boolean callDeleteMember(){
		JSONObject json = new JSONObject();
		try {
			json.put("PGROUPID", Vars.gUID);
			json.put("PUID", shared.getSharedValue("UserID"));
			json.put("PPHONENUMBER", txtMobile.getText().toString().trim());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			callDeleteWebServiceProcess(json);
			onNetwork();
		} else {
			offNetwork();
		}
		return true;
	}

	private void callDeleteWebServiceProcess(JSONObject jbObject) {
		showProgress();
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(15)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(15));
	}


	
}
