package com.mityung.contactdirectory;

import java.io.File;
import org.json.JSONObject;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.crop.Crop;
import com.example.contactplusgroup.edittext.MaterialEditText;
import com.example.contactplusgroup.views.ButtonFlat;
import com.example.contactplusgroup.views.CircleImageView;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.DialogDismiss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GroupSettings extends Activity implements WebServiceInterface{

	private TextView txtApp, txtcantConnect, txtcantConnectNote, txtnetwork, txtGroupName;
	private RelativeLayout layBack, relProgress, relInternet, relNetwork;
	private MaterialEditText edtTAGName, edtDesc;
	private Typeface roboto, androidface;
	private LinearLayout layMain;
	private SharedPreferanceData shared = null;
	private CircleImageView imagePhoto;
	private static final int SELECT_PHOTO = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String STORAGE_DATA = "Userdata";
    Bitmap selectedImage;
	String p;
	private Comman comman = null;
	private Context context;
	private Button btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_edit);
		roboto = Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf");
		androidface = Typeface.createFromAsset(getAssets(), "font/android.ttf");
		initialise();
		//setProfile();
	}
	
	private void initialise(){
		
		getRefer();
		findViewids();
		getBundleData();
		setOnClick();
		addTextChange();
		disableSaveButton();
	}
	
	private void getRefer(){
		comman = new Comman();
		shared = new SharedPreferanceData(GroupSettings.this);
		context = this;
	}
	
	String mGroupTag = null;
	String mGroupDesc = null;
	String mPhoto = null;
	String mGuid = null;
	String mGName = null;
	private void getBundleData(){
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mGName = extras.getString("gName");
			mGroupDesc = extras.getString("gDesc");
			mGroupTag = extras.getString("gTag");
			mPhoto = Vars.gPhoto;
			mGuid = extras.getString("gUID");
			txtGroupName.setText(mGName);
			edtDesc.setText(mGroupDesc);
			edtTAGName.setText(mGroupTag);
			// Set image if exists
	        try {
	            if(mPhoto != null ){
					if(mPhoto.length() != 0){
						if(!mPhoto.equalsIgnoreCase("null")){
							imagePhoto.setImageResource(0);
							imagePhoto.setImageBitmap(comman.decodeBase64(mPhoto));
						}else{
							imagePhoto.setImageBitmap(null);
							imagePhoto.setImageResource(R.drawable.ic_group_circle_white_64dp);
						}
					}else{
						imagePhoto.setImageBitmap(null);
						imagePhoto.setImageResource(R.drawable.ic_group_circle_white_64dp);
					}
				}else{
					imagePhoto.setImageResource(R.drawable.ic_group_circle_white_64dp);
				}
	            // Setting round image
	        } catch (OutOfMemoryError e) {
	            // Add default picture
	        	imagePhoto.setImageResource(R.drawable.ic_group_circle_white_64dp);
	            e.printStackTrace();
	        }
		    // and get whatever type user account id is
			
		}
	}
	
	private void findViewids() {
		txtApp = (TextView) findViewById(R.id.txtAppName);
		txtApp.setTypeface(androidface);
		layBack = (RelativeLayout)findViewById(R.id.layBack);
		imagePhoto = (CircleImageView)findViewById(R.id.imgProfilePicSet);
		edtTAGName = (MaterialEditText)findViewById(R.id.edtTagName);
		edtTAGName.setTypeface(roboto);
		edtDesc = (MaterialEditText)findViewById(R.id.edtDescName);
		edtDesc.setTypeface(roboto);
		relProgress = (RelativeLayout)findViewById(R.id.layProgressresult);
		relInternet = (RelativeLayout)findViewById(R.id.layinternetresult);
		relNetwork = (RelativeLayout)findViewById(R.id.layNoresultserver);
		txtGroupName = (TextView)findViewById(R.id.txtGroupName);
		txtGroupName.setTypeface(roboto);
		txtcantConnect = (TextView)findViewById(R.id.txtcantConnect);
		txtcantConnect.setTypeface(roboto);
		txtcantConnectNote = (TextView)findViewById(R.id.txtcantConnectNote);
		txtcantConnectNote.setTypeface(roboto);
		txtnetwork = (TextView)findViewById(R.id.txtNoresultnetwork);
		txtnetwork.setTypeface(roboto);
		layMain = (LinearLayout)findViewById(R.id.layMain);
		btnSave = (Button)findViewById(R.id.button_accept);
	}
	
	Integer cityID = 0;
	private void setOnClick(){
		
		layBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GroupSettings.this.finish();
			}
		});
		
		imagePhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (comman.isNetworkAvailable(GroupSettings.this)) {
					selectImage();
				} else {
					comman.alertDialog(GroupSettings.this, getString(R.string.no_internet_connection), getString(R.string.you_dont_have_internet));
				}
			}
		});
		
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (comman.isNetworkAvailable(GroupSettings.this)) {
					if (checkNull()) {
						updateGroup();
					}
				} else {
					comman.alertDialog(GroupSettings.this, getString(R.string.no_internet_connection), getString(R.string.you_dont_have_internet));
				}
			}
		});
		
		
	}
	
	/*private void setProfile(){
		if(!mPhoto.equalsIgnoreCase("null")) {
            Bitmap p = comman.decodeBase64(mPhoto);
            imagePhoto.setImageBitmap(p);
        }else{
        	 Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_group_large_white);
        	 imagePhoto.setImageBitmap(icon1);
        }
	}*/
	
	private void selectImage() {

		final CharSequence[] items = { "Take Photo", "Choose from Gallery","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(GroupSettings.this);
		builder.setTitle("Select Photo");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
						startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
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

	String encodeimage = null;
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
        	Uri pp=Crop.getOutput(result);
        	String p1=pp.toString();
            p = comman.compressImage(GroupSettings.this, p1);
            selectedImage = BitmapFactory.decodeFile(p);
            imagePhoto.setImageBitmap(selectedImage);
			encodeimage = comman.encodeTobase64(selectedImage);

			updateGroup();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void offServerResponse() {
		relNetwork.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);
	}

	private void showProgress() {

		relProgress.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);

	}

	private void hideProgress() {

		relProgress.setVisibility(View.GONE);
		layMain.setVisibility(View.VISIBLE);

	}

	private void offNetwork() {

		relInternet.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);

	}

	private void onNetwork() {

		relInternet.setVisibility(View.GONE);
		layMain.setVisibility(View.VISIBLE);
	}
    
    private void disableSaveButton(){
		btnSave.setVisibility(View.GONE);
		btnSave.setEnabled(false);
	}
    
	private void addTextChange() {

		edtTAGName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
				// TODO Auto-generated method stub
				checkTextChange();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

		edtDesc.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
				// TODO Auto-generated method stub
				checkTextChange();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void checkTextChange() {

		boolean status = false;
		if (!edtTAGName.getText().toString().trim().equals("")) {
			if (!mGroupTag.trim().equals(edtTAGName.getText().toString().trim())) {
				status = true;
			}
		}

		if (!edtDesc.getText().toString().trim().equals("")) {
			if (!mGroupDesc.trim().equals(edtDesc.getText().toString().trim())) {
				status = true;
			}
		}
		if (status) {
			btnSave.setVisibility(View.VISIBLE);
			btnSave.setEnabled(true);
		} else {
			btnSave.setVisibility(View.GONE);
			btnSave.setEnabled(false);
		}

	}
	
	private boolean checkNull(){
		if(edtTAGName.getText().toString().trim().equals("")){
			edtTAGName.setError("Tag Name can't be null");
			return false;
		}
		if(edtDesc.getText().toString().trim().equals("")){
			edtDesc.setError("Enter description for the group.");
			return false;
		}
		return true;
	}
	
	private String getEncodeImage(){
    	if(selectedImage==null){
			if(Vars.gPhoto.equals("")){
				Vars.gPhoto= "";
				return "";
			}else{
				return Vars.gPhoto;
			}

    	}else{
			Vars.gPhoto= comman.encodeTobase64(selectedImage);
    		return comman.encodeTobase64(selectedImage);
    	}
    }
	
	private boolean updateGroup() {

		JSONObject json = new JSONObject();
		try {
			json.put("PGROUPID", 			mGuid);
			json.put("PUID", 				shared.getSharedValue("UserID"));
			json.put("PGROUPNAME", 			"");
			json.put("PMADEBY", 			"");
			json.put("PDESC", 				edtDesc.getText().toString());
			json.put("PGROUPTYPEVAR", 		"");
			json.put("PTAGNAME", 			edtTAGName.getText().toString().trim());
			json.put("PGROUPPHOTO", 		getEncodeImage());
			json.put("PCHANGEBYPHONENO", 	shared.getSharedValue("PMOBILE"));               
			json.put("AFLAG", 				1);
			json.put("AGROUPMEMBERLIST", 	"");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			 callWebServiceProcess(json);
			 onNetwork();
		} else {
			comman.alertDialog(GroupSettings.this, getString(R.string.no_internet_connection), getString(R.string.you_dont_have_internet));

		}

		return true;
	}
	
	private void callWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(6)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(6));
		showProgress();
	}

	@Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stub
		hideProgress();
		if(result == null){
			offServerResponse();
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			offServerResponse();
			return;
		}if(processName.equals(Vars.webMethodName.get(6))){
			parseJson(result);
		}
	}
	
	private void parseJson(String result){
		try {
			JSONObject json = new JSONObject(result);
			String status = json.getString("STATUS");
			if(status.equalsIgnoreCase("SUCCESS")){

				alertDialog(GroupSettings.this, getString(R.string.group_already_exist_msg_head), getString(R.string.group_already_exist_msg));

			}else{
				comman.alertDialog(GroupSettings.this,getString(R.string.group_already_exist_head),getString(R.string.failed_to_update));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void alertDialog(Context context, String title, String msg) {
		DialogDismiss dialog = new DialogDismiss(context, title, msg);
		dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i  = new Intent();
				i.putExtra("TagName", edtTAGName.getText().toString().trim());
				i.putExtra("Descrption", edtDesc.getText().toString().trim());
				setResultForResponse(1, i);
				GroupSettings.this.finish();
			}
		});
		dialog.show();
	}
	
	private void setResultForResponse(int resultCode , Intent i){
		setResult(resultCode, i);
	}
}
