package com.mityung.contactdirectory;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactplusgroup.adapter.AddToContactAdapter;
import com.example.contactplusgroup.adapter.ContactAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.utils.HanziToPinyin;
import com.example.contactplusgroup.widgets.Dialog;
import com.example.contactplusgroup.widgets.DialogDismiss;
import com.example.contactplusgroup.widgets.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AddToContactList extends Activity {


	private Comman comman = null;
	private SharedPreferanceData shared = null;
	private Typeface roboto, android;
    GroupDb groupdb = null;
	LinearLayout layMain;
	private RelativeLayout relProgress;
	RelativeLayout mLayback = null;
	TextView mTxtHead,mBtnDone;
	ListView mListSearch;
	Context context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtocontactlist);
		roboto = Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf");
		android = Typeface.createFromAsset(getAssets(), "font/android.ttf");
		initialise();
	}
	
	private void initialise(){
		findViewIds();
		getRefer();
		setOnClick();
		getBundleData();
	}

	private void showProgress() {

		relProgress.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);

	}

	private void hideProgress() {

		relProgress.setVisibility(View.GONE);
		layMain.setVisibility(View.VISIBLE);

	}

	private void getBundleData(){

		try {
			Bundle bundle = getIntent().getExtras();
			if(bundle != null) {
				String data = bundle.get("data").toString();
				JSONArray jbArr = new JSONArray(data);

				for(int i=0 ; i < jbArr.length() ;i++){

					JSONObject jb = jbArr.getJSONObject(i);

					ContactBean cb = new ContactBean();
					cb.setName(jb.get("name").toString());

					JSONArray jPhone = jb.getJSONArray("phone");
					String phone = "";
					for(int j = 0; j < jPhone.length() ;j++ ){
						JSONObject jp = jPhone.getJSONObject(j);
						if(phone.length() == 0){
							phone = jp.get("phone").toString();
						}else{
							phone = phone+"#"+jp.get("phone").toString();
						}
					}

					cb.setNumber(phone);

					JSONArray jEmail = jb.getJSONArray("email");
					String email = "";
					for(int j = 0; j < jEmail.length() ;j++ ){
						JSONObject jp = jEmail.getJSONObject(j);
						if(email.length() == 0){
							email = jp.get("email").toString();
						}else{
							email = email+"#"+jp.get("email").toString();
						}
					}
					cb.setEmail(email);

					selectUsers.add(cb);
				}
			}
			setAdapter();

		}catch (Exception e){
			e.printStackTrace();;
		}

	}

	private void setAdapter(){
		//Collections.sort(selectUsers);
		AddToContactAdapter atc = new AddToContactAdapter(AddToContactList.this,  0,selectUsers );
		mListSearch.setAdapter(atc);
	}

	ArrayList<ContactBean> selectUsers = null;
	private void getRefer(){
		groupdb = new GroupDb(AddToContactList.this);
		comman = new Comman();
		shared = new SharedPreferanceData(this);
		selectUsers = new ArrayList<ContactBean>();
		context = this;
	}
	
	private void findViewIds(){
		mLayback = (RelativeLayout)findViewById(R.id.layBack);
		mTxtHead = (TextView)findViewById(R.id.txtHead);
		mTxtHead.setTypeface(android);
		layMain   = (LinearLayout)findViewById(R.id.laymain);
		relProgress = (RelativeLayout)findViewById(R.id.layProgressresult);
		mBtnDone = (TextView)findViewById(R.id.btnDone);
		mBtnDone.setTypeface(roboto);
		mListSearch = (ListView)findViewById(R.id.lvContactListSearch);

	}
	
	private void setOnClick(){
		mLayback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddToContactList.this.finish();
			}
		});
		
		mBtnDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog(AddToContactList.this, "Message", "Do you want to add this contacts in you phone.");

			}
		});
	}

	public void alertDialog(final Context context, String title, String msg) {
		Dialog dialog = new Dialog(context, title, msg);
		dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AddContact addContact = new AddContact();
				addContact.execute();

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


	// Load data on background
	class AddContact extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				showProgress();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		@Override
		protected Void doInBackground(Void... voids) {
			// Get Contact list from Phone
			for(ContactBean cb : selectUsers){
				String name = cb.getName().toString().trim();
				String number = cb.getNumber().toString().trim();
				String email = cb.getEmail().toString().trim();
				ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

				ops.add(ContentProviderOperation.newInsert(
						ContactsContract.RawContacts.CONTENT_URI)
						.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
						.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
						.build());

				//------------------------------------------------------ Names
				if (name != null) {
					ops.add(ContentProviderOperation.newInsert(
							ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
									name).build());
				}

				if(number != null ){
					if(number.length() != 0){
						if(!number.equalsIgnoreCase("null")){
							ArrayList<String> num = comman.splitString(number, "\\#");
							for (String numberuser : num){
								ops.add(ContentProviderOperation.
										newInsert(ContactsContract.Data.CONTENT_URI)
										.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
										.withValue(ContactsContract.Data.MIMETYPE,
												ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
										.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, numberuser)
										.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
												ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
										.build());
							}

						}else{

						}
					}else{

					}
				}else{

				}

				if(email != null ){
					if(email.length() != 0){
						if(!email.equalsIgnoreCase("null")){
							ArrayList<String> emailID = comman.splitString(email, "\\#");
							for (String emailuser : emailID){
								ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
										.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
										.withValue(ContactsContract.Data.MIMETYPE,
												ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
										.withValue(ContactsContract.CommonDataKinds.Email.DATA, emailuser)
										.withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
										.build());
							}

						}else{

						}
					}else{

					}
				}else{

				}
				// Asking the Contact provider to create a new contact
				try {
					context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			hideProgress();
			alertDialogDismiss(context, "Message", "All contacts saved succesfully.");

		}
	}

	public void alertDialogDismiss(Context context, String title, String msg) {
		DialogDismiss dialog = new DialogDismiss(context, title, msg);
		dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AddToContactList.this.finish();
			}
		});
		dialog.show();
	}

}
