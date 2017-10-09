package com.mityung.contactdirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;
import com.example.contactplusgroup.adapter.ContactSearchAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.crop.Crop;
import com.example.contactplusgroup.edittext.MaterialEditText;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.views.ButtonFlat;
import com.example.contactplusgroup.views.CircleImageView;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.ProgressDialog;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class AddNewGroup extends Activity implements WebServiceInterface{
	
	private RelativeLayout mLayback;
	private TextView mTxtHead, mTxtNote;
	private ButtonFlat mBtnNext;
	private TextView mBtnDone;
	private MaterialEditText mEdtGroupName,mEdtTagLine, mEdtGroupDesc, mEdtSerachContact;
	private CircleImageView mImgGroupPhoto;
	private Comman comman = null;
	private SharedPreferanceData shared = null;
	private ProgressDialog pbBar = null;
	private ViewFlipper vw1, vw2;
	private ListView mListSearch;
	private static final int SELECT_PHOTO = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String STORAGE_DATA = "Userdata";
    Bitmap selectedImage;
	String p;
	ArrayList<ContactBean> selectUsers;
    List<ContactBean> temp;
    TextView txtNoData;
    Cursor phones, email;
    private Typeface roboto, android;
    // Pop up
    ContentResolver resolver;
    ContactSearchAdapter adapter;
    static Context context;
    GroupDb groupdb = null;
	AddNewGroup adGroup = null;
	final String TAG = this.getClass().toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_create_new_group);
		roboto = Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf");
		android = Typeface.createFromAsset(getAssets(), "font/android.ttf");
		initialise();
	}
	
	private void initialise(){
		findViewIds();
		getRefer();
		setOnClick();
	}
	
	private void getRefer(){
		groupdb = new GroupDb(AddNewGroup.this);
		comman = new Comman();
		shared = new SharedPreferanceData(this);
		selectUsers = new ArrayList<ContactBean>();
		pbBar = new ProgressDialog();
		adGroup = this;
	    resolver = getContentResolver();
	    phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
	    LoadContact loadContact = new LoadContact();
	    loadContact.execute();
	    context =this;
		
	}
	
	private void findViewIds(){
		mLayback = (RelativeLayout)findViewById(R.id.layBack);
		mTxtHead = (TextView)findViewById(R.id.txtHead);
		mTxtHead.setTypeface(android);
		mTxtNote = (TextView)findViewById(R.id.txtNote);
		mTxtNote.setTypeface(roboto);
		mBtnNext = (ButtonFlat)findViewById(R.id.btnNextOk);
		mBtnDone = (TextView)findViewById(R.id.btnDone);
		mBtnDone.setTypeface(roboto);
		mEdtGroupName = (MaterialEditText)findViewById(R.id.edtGroupName);
		mEdtGroupName.setTypeface(roboto);
		mEdtTagLine = (MaterialEditText)findViewById(R.id.edtTagName);
		mEdtTagLine.setTypeface(roboto);
		mEdtGroupDesc = (MaterialEditText)findViewById(R.id.edtDescName);
		mEdtGroupDesc.setTypeface(roboto);
		mImgGroupPhoto = (CircleImageView)findViewById(R.id.imgProfilePicSet);
		mEdtSerachContact = (MaterialEditText)findViewById(R.id.edtContactSearch);
		mEdtSerachContact.setTypeface(roboto);
		mListSearch = (ListView)findViewById(R.id.lvContactListSearch);
		vw1 = (ViewFlipper)findViewById(R.id.vw1);
		vw2 = (ViewFlipper)findViewById(R.id.vw2);
		txtNoData = (TextView)findViewById(R.id.txtNoSearchresult);
		txtNoData.setTypeface(roboto);
	}
	
	private void setOnClick(){
		mLayback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(vw2.isShown()){
					onViewOnShow();
					return;
				}else if(vw1.isShown()){
					AddNewGroup.this.finish();
				}
			}
		});
		
		mBtnDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String data  = null;
				int count = 3;
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
									data = cb.getName() + ":" + number;

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
					Toast.makeText(AddNewGroup.this , "Please select contacts you want to add in directory.", Toast.LENGTH_LONG).show();
					return;
				}
				String finalSt = count+":"+data;
				addMembersJson(finalSt);
				
			}
		});
		
		mBtnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkGroupNull()){
					onViewOnHide();
				}
			}
		});
		
		mImgGroupPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectImage();
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

	private boolean checkGroupNull(){
		if(mEdtGroupName.getText().toString().trim().equals("")) {
			mEdtGroupName.setError("Please enter directory name.");
			return false;
		}
		if(mEdtGroupName.getText().toString().trim().length() < 4) {
			mEdtGroupName.setError("Directory name must be greater than 3 characters.");
			return false;
		}
		if(mEdtTagLine.getText().toString().trim().equals("")) {
			mEdtTagLine.setError("Please enter directory members tag.");
			return false;
		}
		if(mEdtGroupDesc.getText().toString().trim().equals("")) {
			mEdtGroupDesc.setError("Please write something about directory.");
			return false;
		}
		return true;
	}
	
	private void onViewOnHide() {
		vw1.setVisibility(View.GONE);
		vw2.setVisibility(View.VISIBLE);
		mTxtHead.setText("Add Contacts");
		mBtnNext.setVisibility(View.GONE);
		mBtnDone.setVisibility(View.VISIBLE);
	}

	private void onViewOnShow() {
		vw1.setVisibility(View.VISIBLE);
		vw2.setVisibility(View.GONE);
		mTxtHead.setText(getString(R.string.action_group_create));
		mBtnDone.setVisibility(View.GONE);
		mBtnNext.setVisibility(View.VISIBLE);
	}
	
	private void selectImage() {

		final CharSequence[] items = { "Take Photo", "Choose from Gallery","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(AddNewGroup.this);
		builder.setTitle("Select Photo");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
						startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
					}
				} else if (items[item].equals("Choose from Gallery")) {
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					startActivityForResult(photoPickerIntent, SELECT_PHOTO);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            beginCrop(imageReturnedIntent.getData());
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        	 Bundle extras = imageReturnedIntent.getExtras();
             selectedImage = (Bitmap) extras.get("data");
             Uri pp=comman.getImageUri(getApplicationContext(), selectedImage);
             beginCrop(pp);
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, imageReturnedIntent);
        }

    }
	
	private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
        	Uri pp=Crop.getOutput(result);
        	String p1=pp.toString();
            p = comman.compressImage(AddNewGroup.this, p1);
            selectedImage = BitmapFactory.decodeFile(p);
            mImgGroupPhoto.setImageBitmap(selectedImage);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    
    private String getEncodeImage(){
    	if(selectedImage==null){
    		return "";
    	}else{
    		return comman.encodeTobase64(selectedImage);
    	}
    }
	
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
    	if(vw2.isShown()){
    		onViewOnShow();
			return;
		}else if(vw1.isShown()){
			AddNewGroup.this.finish();
		}
		super.onBackPressed();
	}

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
            adapter = new ContactSearchAdapter(AddNewGroup.this, R.layout.act_create_new_group, selectUsers);
            mListSearch.setAdapter(adapter);

            // Select item on listclick
            mListSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }
    }
    
    private boolean addMembersJson(String members) {

		JSONObject json = new JSONObject();
		try {
			json.put("PGROUPID", 			0);
			json.put("PUID", 				shared.getSharedValue("UserID"));
			json.put("PGROUPNAME", 			mEdtGroupName.getText().toString());
			json.put("PMADEBY", 			shared.getSharedValue("PMOBILE"));
			json.put("PDESC", 				mEdtGroupDesc.getText().toString());
			json.put("PGROUPTYPEVAR", 		"");
			json.put("PTAGNAME", 			mEdtTagLine.getText().toString().trim());
			json.put("PGROUPPHOTO", 		getEncodeImage());
			json.put("PCHANGEBYPHONENO", 	0);               
			json.put("AFLAG", 				0);
			json.put("AGROUPMEMBERLIST", 	members+":"+shared.getSharedValue("PNAME")+":"+shared.getSharedValue("PMOBILE"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			sendOTPWebServiceProcess(json);
		} else {
			comman.alertDialog(AddNewGroup.this, getString(R.string.cant_connect),getString(R.string.cant_connect_please_check));
		}

		return true;
	}

    private void sendOTPWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(6)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(6));
		pbBar.progressPopUp(AddNewGroup.this, "Loading..."); 
	}

	@Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stub
		if(pbBar!=null){
			pbBar.closeProgressPopUp();
		}
		if(result == null){
			comman.alertDialog(AddNewGroup.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			comman.alertDialog(AddNewGroup.this,getString(R.string.network_problem_head),getString(R.string.network_problem));
			return;
		}
		if(processName.equals(Vars.webMethodName.get(6))){
			parseStatus(result);
		}else if(processName.equals(Vars.webMethodName.get(14))){
			AddNewGroup.this.finish();
		}
	}
	
	
	private void parseStatus(String result){
		try {
			JSONObject json = new JSONObject(result);
			String status = json.getString("STATUS");
			if(status.equalsIgnoreCase("SUCCESS")){
				Intent i  = new Intent();
				i.putExtra("RESULT", "1");
				setResultForResponse(1, i);
				sendNotification();
			}else if(status.equalsIgnoreCase("Already exist")){
				comman.alertDialog(AddNewGroup.this,getString(R.string.group_already_exist_head),getString(R.string.group_already_exist));
				return;
			}else{
				comman.alertDialog(AddNewGroup.this,getString(R.string.group_already_exist_head),getString(R.string.group_already_exist_error));
				return;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void setResultForResponse(int resultCode , Intent i){
		
		setResult(resultCode, i);
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
			comman.alertDialog(AddNewGroup.this, getString(R.string.no_internet_connection), getString(R.string.you_dont_have_internet));
		}

		return true;
	}

	private void sendN(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(14)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(14));
		pbBar.progressPopUp(AddNewGroup.this, "Loading...");
	}
    
}
