package com.mityung.contactdirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ObjectSerializer;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.crop.Crop;
import com.example.contactplusgroup.edittext.MaterialEditText;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.views.ButtonFlat;
import com.example.contactplusgroup.views.CircleImageView;
import com.example.contactplusgroup.views.MaterialSpinner;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.DialogDismiss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ProfileSetting extends Activity implements WebServiceInterface {

    private TextView txtApp, txtcantConnect, txtcantConnectNote, txtNoresultnetwork, txtpleasewait, txtName, txtEmail, txtMobile;
    private RelativeLayout layBack, relProgress, relInternet, relNetwork;
    private ScrollView scrollMain;
    private MaterialEditText edtProfession, edtAddress, edtCountry, edtAlternateNumber, edtCompany;
    private MaterialSpinner spCity, spBloodGroup;
    //private SearchableSpinner spCitySelect;
    private SharedPreferanceData shared = null;
    private CircleImageView imagePhoto;
    private static final int SELECT_PHOTO = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String STORAGE_DATA = "Userdata";
    Bitmap selectedImage;
    String p;
    private Comman comman = null;
    private Context context;
    private ButtonFlat btnSave;
    final String TAG = this.getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        initialise();
        setProfile();
    }

    private void initialise() {

        getRefer();
        findViewids();
        getCity(shared.getSharedValue("PCOUNTRYID"));
        setOnClick();
        addTextChange();
        disableSaveButton();
    }

    private void getRefer() {
        comman = new Comman();
        shared = new SharedPreferanceData(ProfileSetting.this);
        context = this;
    }

    private void findViewids() {
        txtApp = (TextView) findViewById(R.id.txtAppName);
        txtApp.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_android));
        layBack = (RelativeLayout) findViewById(R.id.layBack);
        imagePhoto = (CircleImageView) findViewById(R.id.imgProfilePicSet);
        txtName = (TextView) findViewById(R.id.txtUserName);
        txtName.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        txtName.setText(shared.getSharedValue("PNAME"));
        txtMobile = (TextView) findViewById(R.id.txtUserMobile);
        txtMobile.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        txtMobile.setText(shared.getSharedValue("PMOBILE"));
        txtEmail = (TextView) findViewById(R.id.txtUserEmail);
        txtEmail.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        txtEmail.setText(shared.getSharedValue("PEMAIL"));
        edtProfession = (MaterialEditText) findViewById(R.id.edtProfession);
        edtProfession.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        edtProfession.setText(shared.getSharedValue("PPROFESSION"));
        edtAddress = (MaterialEditText) findViewById(R.id.edtAddress);
        edtAddress.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        edtAddress.setText(shared.getSharedValue("PADDRESS"));
        spCity = (MaterialSpinner) findViewById(R.id.spCity);
        //spCitySelect = (SearchableSpinner)findViewById(R.id.spCitySelect);
        spBloodGroup = (MaterialSpinner) findViewById(R.id.spBloodGroup);

        edtCountry = (MaterialEditText) findViewById(R.id.edtCountry);
        edtCountry.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        edtCountry.setText(shared.getSharedValue("PCOUNTRY"));
        edtCountry.setEnabled(false);
        relProgress = (RelativeLayout) findViewById(R.id.layProgressresult);
        relInternet = (RelativeLayout) findViewById(R.id.layinternetresult);
        relNetwork = (RelativeLayout) findViewById(R.id.layNoresultserver);
        txtcantConnect = (TextView) findViewById(R.id.txtcantConnect);
        txtcantConnect.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        txtcantConnectNote = (TextView) findViewById(R.id.txtcantConnectNote);
        txtcantConnectNote.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        txtNoresultnetwork = (TextView) findViewById(R.id.txtNoresultnetwork);
        txtNoresultnetwork.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        txtpleasewait = (TextView) findViewById(R.id.txtpleasewait);
        txtpleasewait.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        scrollMain = (ScrollView) findViewById(R.id.laymain);
        btnSave = (ButtonFlat) findViewById(R.id.button_accept);
        cityName = shared.getSharedValue("PCITYNAME");
        bloodGroup = shared.getSharedValue("PBLOODGROUP");
        edtAlternateNumber = (MaterialEditText) findViewById(R.id.edtAlternateMobile);
        edtAlternateNumber.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));
        edtAlternateNumber.setText(shared.getSharedValue("PALTERNATEMOBILE"));
        edtCompany = (MaterialEditText) findViewById(R.id.edtCompany);
        edtCompany.setTypeface(ManagerTypeface.getTypeface(ProfileSetting.this, R.string.typeface_roboto_regular));

        if (!shared.getSharedValue("PCOMPANYNAME").equalsIgnoreCase("0"))
            edtCompany.setText(shared.getSharedValue("PCOMPANYNAME"));
        setBloodAdapter();
    }


    private void setBloodAdapter() {
        final String[] choices = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> badapter = new ArrayAdapter<String>(ProfileSetting.this, android.R.layout.simple_spinner_item, choices);
        badapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBloodGroup.setAdapter(badapter);
        try {
            if (!bloodGroup.equals(null) && !bloodGroup.equals("0")) {
                int spinnerPosition = badapter.getPosition(bloodGroup);
                spBloodGroup.setSelection(spinnerPosition + 1);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    Integer cityID = 0;

    private void setOnClick() {


        layBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ProfileSetting.this.finish();
            }
        });

        imagePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (comman.isNetworkAvailable(ProfileSetting.this)) {
                    selectImage();
                } else {
                    comman.alertDialog(ProfileSetting.this, getString(R.string.no_internet_connection), getString(R.string.you_dont_have_internet));
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (comman.isNetworkAvailable(ProfileSetting.this)) {
                    if (checkNull()) {
                        updateProfile();
                    }
                } else {
                    comman.alertDialog(ProfileSetting.this, getString(R.string.no_internet_connection), getString(R.string.you_dont_have_internet));
                }
            }
        });

        spCity.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String sys = spCity.getSelectedItem().toString().trim();
                try {
                    cityID = Integer.parseInt(getCityId(sys));
                } catch (Exception e) {
                    // TODO: handle exception
                    cityID = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //spCategory.setBackgroundResource(R.color.red);
            }
        });
    }

    private void setProfile() {

        String imagePreferance = shared.getSharedValue("PPHOTO");
        if (!shared.getSharedValue("PPHOTO").equalsIgnoreCase("N/A")) {
            try {
                if (imagePreferance != null) {
                    if (imagePreferance.length() != 0) {
                        if (!imagePreferance.equalsIgnoreCase("null")) {
                            Bitmap p = comman.decodeBase64(imagePreferance);
                            imagePhoto.setImageBitmap(p);
                        } else {
                            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_white_64dp);
                            imagePhoto.setImageBitmap(icon1);
                        }
                    } else {
                        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_white_64dp);
                        imagePhoto.setImageBitmap(icon1);
                    }
                } else {
                    Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_white_64dp);
                    imagePhoto.setImageBitmap(icon1);
                }
            } catch (OutOfMemoryError e) {
                // Add default picture
                Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_white_64dp);
                imagePhoto.setImageBitmap(icon1);
            }
        }
    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSetting.this);
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
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = imageReturnedIntent.getExtras();
            selectedImage = (Bitmap) extras.get("data");
            Uri pp = comman.getImageUri(getApplicationContext(), selectedImage);
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
            Uri pp = Crop.getOutput(result);
            String p1 = pp.toString();

            p = comman.compressImage(ProfileSetting.this, p1);
            selectedImage = BitmapFactory.decodeFile(p);
            imagePhoto.setImageBitmap(selectedImage);
            encodeimage = comman.encodeTobase64(selectedImage);

            callImagechange(encodeimage);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getCity(String countryId) {

        JSONObject json = new JSONObject();
        try {
            json.put("PCOUNTRYID", countryId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        if (comman.isNetworkAvailable(context)) {
            getCityWebServiceProcess(json);
            onNetwork();
        } else {
            dserializeCity();
            if (cityNameId.size() == 0) {
                //offNetwork();
            }
            setAdapter();
        }
        return true;
    }

    private void getCityWebServiceProcess(JSONObject jbObject) {
        String jsonData = jbObject.toString();
        new Webservice(this, Vars.webMethodName.get(4)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(4));
        showProgress();
    }

    @Override
    public void onWebCompleteResult(String result, String processName) {
        // TODO Auto-generated method stub
        hideProgress();
        if (result == null) {
            offServerResponse();
            return;
        }
        if (result.trim().equalsIgnoreCase("null") || result.trim().equalsIgnoreCase("0")) {
            offServerResponse();
            return;
        } else if (processName.equals(Vars.webMethodName.get(4))) {
            parseCity(result);
        } else if (processName.equals(Vars.webMethodName.get(0))) {
            parseUpadeProfile(result);

        } else if (processName.equals(Vars.webMethodName.get(12))) {
            parsePhotoProfile(result);

        }

    }

    private void parsePhotoProfile(String result) {
        try {
            JSONObject jSON = new JSONObject(result);
            String status = jSON.getString("STATUS");
            if (status.trim().equals("SUCCESS")) {
                shared.saveSharedData("PPHOTO", encodeimage);
                comman.alertDialog(ProfileSetting.this, "Profile ", getString(R.string.profile_updated_msg_photo));
            } else {
                comman.alertDialog(ProfileSetting.this, "Update Profile ", "Profile Updation failed. Please try after some time !!");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void parseUpadeProfile(String result) {
        try {
            JSONObject jSON = new JSONObject(result);
            String status = jSON.getString("STATUS");
            if (status.trim().equals("1")) {
                alertDialog(ProfileSetting.this, "Profile ", getString(R.string.profile_updated_msg));

            } else if (status.trim().equals("3")) {
                comman.alertDialog(ProfileSetting.this, "Profile ", getString(R.string.account_email_exist));
            } else {
                comman.alertDialog(ProfileSetting.this, "Profile ", "Profile Updation failed. Please try after some time !!");
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
                setUserdetails();
                ProfileSetting.this.finish();
            }
        });
        dialog.show();
    }

    private String getPhoneNumber(String email1) {
        if (email1.equals("")) {
            return txtMobile.getText().toString().trim() + "#" + 0;
        } else {
            return txtMobile.getText().toString().trim() + "#" + comman.trimEnd(email1);
        }
    }

    private String getProfessionDetails(String profession, String companyName) {
        String prof = comman.trimEnd(profession);
        String company = comman.trimEnd(companyName);
        if (prof.equals("") && company.equals("")) {
            return "0" + "#" + "0";
        } else if (prof.equals("") && !company.equals("")) {
            return company + "#" + "0";
        } else if (!prof.equals("") && company.equals("")) {
            return "0" + "#" + prof;
        } else {
            return company + "#" + prof;
        }
    }

    private void setUserdetails() {

        shared.saveSharedData("PMOBILE", txtMobile.getText().toString().trim());
        shared.saveSharedData("PALTERNATEMOBILE", edtAlternateNumber.getText().toString().trim());
        shared.saveSharedData("PNAME", txtName.getText().toString().trim());
        shared.saveSharedData("PEMAIL", txtEmail.getText().toString().trim());
        shared.saveSharedData("PCOUNTRY", edtCountry.getText().toString().trim());
        shared.saveSharedData("PPROFESSION", edtProfession.getText().toString().trim());
        shared.saveSharedData("PCITYNAME", spCity.getSelectedItem().toString().trim());
        shared.saveSharedData("PADDRESS", edtAddress.getText().toString().trim());
        shared.saveSharedData("PCOMPANYNAME", edtCompany.getText().toString());
        shared.saveSharedData("PBLOODGROUP", spBloodGroup.getSelectedItem().toString().trim());

    }

    private void callImagechange(String img) {
        JSONObject jb = new JSONObject();
        try {

            jb.put("PPHONENUMBER", shared.getSharedValue("PMOBILE"));
            jb.put("PPHOTO", img);
            jb.put("PFLAG", "1");
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (comman.isNetworkAvailable(context)) {
            callimagechange(jb);
        } else {

        }
    }

    private void callimagechange(JSONObject jbObject) {
        String jsonData = jbObject.toString();
        new Webservice(this, Vars.webMethodName.get(12)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(12));
        showProgress();
    }


    String cityName = null;
    String bloodGroup = null;

    LinkedHashMap<String, String> cityNameId = new LinkedHashMap<String, String>();

    private void parseCity(String result) {
        try {
            JSONArray jArray = new JSONArray(result);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jb = jArray.getJSONObject(i);
                String cityId = jb.getString("CITYID");
                String cityName = jb.getString("CITYNAME");
                cityNameId.put(cityName, cityId);
            }
            serializeCity(cityNameId);
            setAdapter();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private String getCityId(String name) {
        if (spCity.getSelectedItemPosition() == 0) {
            return "0";
        } else {
            try {
                return cityNameId.get(name);
            } catch (Exception e) {
                // TODO: handle exception
                return "0";
            }
        }
    }

    private String getBloodGroup() {
        if (spBloodGroup.getSelectedItemPosition() == 0) {
            return "0";
        } else {
            return spBloodGroup.getSelectedItem().toString().trim();
        }
    }

    private ArrayAdapter<String> dataAdapter = null;

    private void setAdapter() {

        ArrayList<String> ar = new ArrayList<String>();
        //ar.add("Select City");
        for (String key : cityNameId.keySet()) {
            ar.add(key);
        }
        Collections.sort(ar);
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ar);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(dataAdapter);
        //spCitySelect.setAdapter(dataAdapter);
        try {
            if (!cityName.equals(null)) {
                int spinnerPosition = dataAdapter.getPosition(cityName);
                //Log.e("Profile", "CITY---ID--"+cityName+"---position---"+spinnerPosition);
                spCity.setSelection(spinnerPosition + 1);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
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
        scrollMain.setVisibility(View.GONE);

    }

    private void onNetwork() {

        relInternet.setVisibility(View.GONE);
        scrollMain.setVisibility(View.VISIBLE);
    }


    private boolean updateProfile() {

        JSONObject json = new JSONObject();
        try {
            json.put("PPHONENUMBER", getPhoneNumber(edtAlternateNumber.getText().toString().trim()));
            json.put("PEMAIL", txtEmail.getText().toString().trim());
            json.put("PNAME", txtName.getText().toString().trim());
            json.put("PADDRESS", comman.trimEnd(comman.upperCaseAllFirst(edtAddress.getText().toString().trim())));
            json.put("PCITYID", getCityId(spCity.getSelectedItem().toString().trim()));
            json.put("PSTATEID", 0);
            json.put("PCOUNTRYCODEID", shared.getSharedValue("PCOUNTRYISD") + "#" + getBloodGroup());
            json.put("PCOUNTRYNAME", edtCountry.getText().toString().trim());
            json.put("POTPID", 0);
            json.put("PSTATUSID", 0);
            String p = getProfessionDetails(comman.upperCaseAllFirst(edtProfession.getText().toString()), comman.upperCaseAllFirst(edtCompany.getText().toString().trim()));
            json.put("PPROFESSION", p);
            json.put("PLOGINFLAG", 1);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        if (comman.isNetworkAvailable(context)) {
            callWebServiceProcess(json);
            onNetwork();
        } else {
            comman.alertDialog(ProfileSetting.this, getString(R.string.no_internet_connection), getString(R.string.you_dont_have_internet));

        }
        return true;
    }

    private void disableSaveButton() {
        btnSave.setVisibility(View.GONE);
        btnSave.setBackgroundColor(getResources().getColor(R.color.theme_primary));
        btnSave.setTextColor(getResources().getColor(R.color.white));
        btnSave.setEnabled(false);
    }

    private void callWebServiceProcess(JSONObject jbObject) {
        String jsonData = jbObject.toString();
        new Webservice(this, Vars.webMethodName.get(0)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(0));
        showProgress();
    }

    private void addTextChange() {
        edtAlternateNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                checkTextChange();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                checkTextChange();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        edtProfession.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                checkTextChange();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        edtCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                checkTextChange();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        spCity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                checkTextChange();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spBloodGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                checkTextChange();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    private void checkTextChange() {
        boolean status = false;
        if (!edtAlternateNumber.getText().toString().trim().equals("")) {
            if (!shared.getSharedValue("PALTERNATEMOBILE").trim().equals(edtAlternateNumber.getText().toString().trim())) {
                status = true;
            }
        }

        if (!edtProfession.getText().toString().trim().equals("")) {
            if (!shared.getSharedValue("PPROFESSION").trim().equals(edtProfession.getText().toString().trim())) {
                status = true;
            }
        }

        if (!edtCompany.getText().toString().trim().equals("")) {
            if (!shared.getSharedValue("PCOMPANYNAME").trim().equals(edtCompany.getText().toString().trim())) {
                status = true;
            }
        }
        if (!edtAddress.getText().toString().trim().equals("")) {
            if (!shared.getSharedValue("PADDRESS").trim().equals(edtAddress.getText().toString().trim())) {
                status = true;
            }
        }
        if (spCity.getSelectedItemPosition() != 0) {
            if (!shared.getSharedValue("PCITYNAME").trim().equals(spCity.getSelectedItem().toString())) {
                status = true;
            }
        }

        if (spBloodGroup.getSelectedItemPosition() != 0) {
            if (!shared.getSharedValue("PBLOODGROUP").trim().equals(spBloodGroup.getSelectedItem().toString())) {
                status = true;
            }
        }

        if (status) {
            btnSave.setVisibility(View.VISIBLE);
            btnSave.setBackgroundColor(getResources().getColor(R.color.white));
            btnSave.setTextColor(getResources().getColor(R.color.theme_primary));
            btnSave.setEnabled(true);
        } else {
            btnSave.setBackgroundColor(getResources().getColor(R.color.theme_primary));
            btnSave.setTextColor(getResources().getColor(R.color.white));
            btnSave.setVisibility(View.GONE);
            btnSave.setEnabled(false);
        }

    }

    private boolean checkNull() {

        if (spCity.getSelectedItemPosition() == 0) {
            //spCity.setError("Please select city");
        }
        if (spBloodGroup.getSelectedItemPosition() == 0) {
            //spBloodGroup.setError("Please select blood group");
        }
        if (edtProfession.getText().toString().trim().equals("")) {
            //edtProfession.setError("Please enter profession.");
        }
        if (edtCompany.getText().toString().trim().equals("")) {
            //edtCompany.setError("Please enter company name.");
        }
        if (edtAddress.getText().toString().trim().equals("")) {
            //edtAddress.setError("Please enter address.");
        }
        return true;
    }


    private void serializeCity(LinkedHashMap<String, String> tblist) {

        SharedPreferences prefs = context.getSharedPreferences(Vars.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString("fetchCity", new ObjectSerializer().serialize(tblist));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }


    @SuppressWarnings("unchecked")
    private void dserializeCity() {
        SharedPreferences prefs = context.getSharedPreferences(Vars.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        LinkedHashMap<String, String> currentTasks = null;
        try {
            currentTasks = (LinkedHashMap<String, String>) new ObjectSerializer().deserialize(prefs.getString("fetchCity", new ObjectSerializer().serialize(new LinkedHashMap<String, String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (currentTasks != null) {
            cityNameId = currentTasks;
        }

    }

}
