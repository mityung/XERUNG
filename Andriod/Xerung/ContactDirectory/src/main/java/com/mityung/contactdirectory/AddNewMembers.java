package com.mityung.contactdirectory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.contactplusgroup.adapter.ContactSearchAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.edittext.MaterialEditText;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.DialogDismiss;
import com.example.contactplusgroup.widgets.ProgressDialog;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class AddNewMembers extends Activity implements WebServiceInterface{
	
	private RelativeLayout mLayback;
	private TextView mTxtHead;
	private TextView mBtnDone;
	private MaterialEditText mEdtSerachContact;
	private Comman comman = null;
	private SharedPreferanceData shared = null;
	private ProgressDialog pbBar = null;
	private ListView mListSearch;
    Bitmap selectedImage;
	String p;
	ArrayList<ContactBean> selectUsers;
    List<ContactBean> temp;
    TextView txtNoData;
	AddNewMembers addMembers;
//    Cursor phones, email;
    private Typeface roboto, android;
    // Pop up
//    ContentResolver resolver;
    ContactSearchAdapter adapter;
    static Context context;
    GroupDb groupdb = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_new_member);
		roboto = Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf");
		android = Typeface.createFromAsset(getAssets(), "font/android.ttf");
		initialise();
	}
	
	private void initialise(){
		findViewIds();
		getRefer();
		getBundleData();
		setOnClick();
	}
	
	
	String mGroupId = "";
	String mGroupName = "";
	private void getBundleData(){
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mGroupName = extras.getString("gName");
			mGroupId = extras.getString("gID");
			mTxtHead.setText(mGroupName);
		    // and get whatever type user account id is
			
		}
	}
	
	private void getRefer(){
		groupdb = new GroupDb(AddNewMembers.this);
		comman = new Comman();
		shared = new SharedPreferanceData(this);
		selectUsers = new ArrayList<ContactBean>();
		pbBar = new ProgressDialog();
		addMembers = this;
	  //  resolver = getContentResolver();
	   // phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
	    LoadContact loadContact = new LoadContact();
	    loadContact.execute();
	    context =this;
		
	}
	
	private void findViewIds(){
		mLayback = (RelativeLayout)findViewById(R.id.layBack);
		mTxtHead = (TextView)findViewById(R.id.txtHead);
		mTxtHead.setTypeface(android);
		mBtnDone = (TextView)findViewById(R.id.btnDone);
		mBtnDone.setTypeface(roboto);
		mEdtSerachContact = (MaterialEditText)findViewById(R.id.edtContactSearch);
		mEdtSerachContact.setTypeface(roboto);
		mListSearch = (ListView)findViewById(R.id.lvContactListSearch);
		txtNoData = (TextView)findViewById(R.id.txtNoSearchresult);
		txtNoData.setTypeface(roboto);
	}
	
	private void setOnClick(){
		mLayback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddNewMembers.this.finish();
			}
		});
		
		mBtnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String data  = null;
				int count = 1;
				for(ContactBean cb : selectUsers){
					if(cb.isChecked()){
						count = count + 2;
						String number = cb.getNumber();
							if(data == null){
								if(comman.checkValidNumber(number)==0){
									data = cb.getName()+":"+number;
								}else{
									PhoneNumberUtil util = PhoneNumberUtil.getInstance();
									try {
										Phonenumber.PhoneNumber numberCheck = util.parse(number, "IN");
										number = "+"+numberCheck.getCountryCode()+numberCheck.getNationalNumber();
									} catch (NumberParseException e) {
										e.printStackTrace();
									}
									//if(!number.equalsIgnoreCase(shared.getSharedValue("PMOBILE"))) {
										data = cb.getName() + ":" + number;
									//}
								}

							}else{

								if(comman.checkValidNumber(number)==0){
									data = data +":"+cb.getName()+":"+number;
								}else{
									PhoneNumberUtil util = PhoneNumberUtil.getInstance();
									try {
										Phonenumber.PhoneNumber numberCheck = util.parse(number, "IN");
										number = "+"+numberCheck.getCountryCode()+numberCheck.getNationalNumber();
									} catch (NumberParseException e) {
										e.printStackTrace();
									}
									//if(!number.equalsIgnoreCase(shared.getSharedValue("PMOBILE"))) {
									data = data + ":" + cb.getName() + ":" + number;
									//}
								}
							}
						}



				}
				if(data == null){
					Toast.makeText(AddNewMembers.this , "Please select contacts you want to add in directory.", Toast.LENGTH_LONG).show();
					return;
				}
				String finalSt = count+":"+data;
				addNewUserJson(finalSt);

			}
		});
		
		mEdtSerachContact.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

				String text = mEdtSerachContact.getText().toString().toLowerCase(Locale.getDefault());
				if (adapter != null)
					adapter.filter(text);
				if(adapter.getCount()>0){
					txtNoData.setVisibility(View.GONE);
					mListSearch.setVisibility(View.VISIBLE);
				}else{
					txtNoData.setVisibility(View.VISIBLE);
					mListSearch.setVisibility(View.GONE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		
		
	}

    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	/*String data = null;
	int count = 1;
	@Override
	public void OnPrefixAddComplete(String number, String  name) {
		// TODO Auto-generated method stub
		count = count + 2;
		if(data == null){
			data = name+":"+number;
		}else{
			data = data +":"+name+":"+number;
		}
	}*/

	// Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
			selectUsers = groupdb.getAllContacts();
        return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ContactSearchAdapter(AddNewMembers.this, R.layout.act_create_new_group, selectUsers);
			mListSearch.setAdapter(adapter);

            // Select item on listclick
            mListSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					/*ContactBean cb = selectUsers.get(i);
					String name = cb.getName();
					String number = cb.getNumber();
					//ImageView img  = (ImageView) view.findViewById(R.id.ivSelcteUser);
					//img.setImageResource(R.drawable.ic_action_add_user_blue);
					if(comman.checkValidNumber(number)==0){
						count = count + 2;
						if(data == null){
							data = name+":"+number;
						}else{
							data = data +":"+name+":"+number;
						}*//*
						selectUsers.remove(i);
						adapter.notifyDataSetChanged();
						mListSearch.deferNotifyDataSetChanged();*//*
					}else{
						new AddPrefixNumber(AddNewMembers.this, getString(R.string.choose_country),name, number, addMembers, AddNewMembers.this).show();
						return;
					}*/
				}
            });
        }
    }
    
    private boolean addNewUserJson(String members) {

		JSONObject json = new JSONObject();
		try {
			json.put("PSENDBYNO", 			shared.getSharedValue("PMOBILE"));
			json.put("PSENDTONO", 			members);
			json.put("PGROUPID", 			mGroupId);
			json.put("PINVITATIONFLAGID", 	1);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			sendOTPWebServiceProcess(json);
		} else {
			comman.alertDialog(AddNewMembers.this, getString(R.string.no_internet_connection),getString(R.string.you_dont_have_internet));
		}

		return true;
	}

    private void sendOTPWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(8)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(8));
		pbBar.progressPopUp(AddNewMembers.this, "Loading..."); 
	}

	String rejected = null;
	@Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stub
		if(pbBar!=null){
			pbBar.closeProgressPopUp();
		}
		if(result == null){
			comman.alertDialog(AddNewMembers.this, getString(R.string.network_problem_head), getString(R.string.network_problem));
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			comman.alertDialog(AddNewMembers.this, getString(R.string.network_problem_head), getString(R.string.network_problem));
			return;
		}
		if(processName.equals(Vars.webMethodName.get(8))){
			parseStatus(result);
		}else if(processName.equals(Vars.webMethodName.get(14))){
			if (rejected.length()>0){
				ArrayList<String> rejectlist = comman.splitString(rejected ,"\\~");
				StringBuffer userRejected = new StringBuffer("List of user rejected your request : ");
				for (String key : rejectlist){
					userRejected.append("\n "+key);
				}
				alertDialogReject(AddNewMembers.this, getString(R.string.group_already_exist_head), userRejected.toString());
				return;
			}else{
				AddNewMembers.this.finish();
			}
		}
	}

	private void parseStatus(String result){
		try {
			JSONObject json = new JSONObject(result);
			String status = json.getString("STATUS");
			rejected = json.getString("REJECTNO");
			if(status.equalsIgnoreCase("SUCCESS")){
				setResult(1001);
				sendNotification();
			}else if(status.equalsIgnoreCase("Already exist")){
				comman.alertDialog(AddNewMembers.this,getString(R.string.group_already_exist_head),getString(R.string.group_already_exist));
				return;
			}else{
				comman.alertDialog(AddNewMembers.this,getString(R.string.group_already_exist_head),getString(R.string.group_already_exist_error));
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	//Send Notification
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
			comman.alertDialog(AddNewMembers.this, getString(R.string.no_internet_connection), getString(R.string.you_dont_have_internet));
		}
		return true;
	}

	private void sendN(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(14)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(14));
		pbBar.progressPopUp(AddNewMembers.this, "Loading...");
	}

	private void alertDialogReject(Context context, String title, String msg) {
		DialogDismiss dialog = new DialogDismiss(context, title, msg);
		dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AddNewMembers.this.finish();
			}
		});
		dialog.show();
	}
    
}
