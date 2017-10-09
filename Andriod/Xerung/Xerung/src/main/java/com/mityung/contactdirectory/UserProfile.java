package com.mityung.contactdirectory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.views.CircleImageView;

/**
 * Created by panther on 21/3/17.
 */

public class UserProfile extends Activity {

    private TextView txtAppName, txtFullName, txtProfession, txtBloodGroup, txtEmail, txtContactNumber1, txtContactNumber2
            ,txtCompany, txtAddress;
    private RelativeLayout layBack;
    private SharedPreferanceData shared = null;
    private CircleImageView imagePhoto;
    private Comman comman = null;
    private Context context;
    private Button btneditProfile;
    final String TAG = this.getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getRefer();
        findViewIds();
        setOnClick();
        findViewIds();
        setProfile();
    }

    private void getRefer(){
        shared = new SharedPreferanceData(this);
        context = this;
        comman =  new Comman();
    }

    private void findViewIds(){
        layBack = (RelativeLayout)findViewById(R.id.layBack);
        txtAppName = (TextView)findViewById(R.id.txtAppName);
        txtAppName.setTypeface(ManagerTypeface.getTypeface(UserProfile.this, R.string.typeface_android));
        btneditProfile = (Button)findViewById(R.id.btneditProfile);
        txtFullName = (TextView)findViewById(R.id.txtFullName);
        txtProfession = (TextView)findViewById(R.id.txtProfession);
        imagePhoto = (CircleImageView)findViewById(R.id.imgProfilePicSet);
        txtBloodGroup = (TextView)findViewById(R.id.txtBloodGroup);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtContactNumber1 = (TextView)findViewById(R.id.txtContactNumber1);
        txtContactNumber2 = (TextView)findViewById(R.id.txtContactNumber2);
        txtCompany = (TextView)findViewById(R.id.txtCompany);
        txtAddress = (TextView)findViewById(R.id.txtAddress);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfile();
    }

    private void setOnClick(){
        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile.this.finish();
            }
        });

        btneditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, ProfileSetting.class));
            }
        });
    }

    private void setProfile() {

        txtFullName.setText(shared.getSharedValue("PNAME"));
        txtContactNumber1.setText(shared.getSharedValue("PMOBILE"));
        if(shared.getSharedValue("PALTERNATEMOBILE").equals("") || shared.getSharedValue("PALTERNATEMOBILE").equalsIgnoreCase("N/A")||shared.getSharedValue("PALTERNATEMOBILE").trim().equalsIgnoreCase("0")){
            txtContactNumber2.setText("Not Addded");
        }else{
            txtContactNumber2.setText(shared.getSharedValue("PALTERNATEMOBILE"));
        }
        txtEmail.setText(shared.getSharedValue("PEMAIL"));
        if(shared.getSharedValue("PPROFESSION").trim().equals("") || shared.getSharedValue("PPROFESSION").trim().equalsIgnoreCase("N/A")||shared.getSharedValue("PPROFESSION").trim().equalsIgnoreCase("0")){
            txtProfession.setText("Not Addded");
        }else{
            txtProfession.setText(shared.getSharedValue("PPROFESSION"));
        }
        if(shared.getSharedValue("PBLOODGROUP").equals("") || shared.getSharedValue("PBLOODGROUP").equalsIgnoreCase("N/A")|| shared.getSharedValue("PBLOODGROUP").equalsIgnoreCase("0")){
            txtBloodGroup.setText("Not Addded");
        }else{
            txtBloodGroup.setText(shared.getSharedValue("PBLOODGROUP"));
        }
        String mAddess = "";
        if(shared.getSharedValue("PADDRESS").trim().equals("") || shared.getSharedValue("PADDRESS").equalsIgnoreCase("N/A")){
            mAddess = "";
        }else{
            mAddess = shared.getSharedValue("PADDRESS").trim().toString()+", ";
        }
        if(shared.getSharedValue("PCITYNAME").trim().equalsIgnoreCase("") || shared.getSharedValue("PCITYNAME").equalsIgnoreCase("N/A")){
            mAddess += "";
        }else{
            mAddess += shared.getSharedValue("PCITYNAME").trim().toString()+", ";
        }
        if(shared.getSharedValue("PCOUNTRY").equalsIgnoreCase("") || shared.getSharedValue("PCOUNTRY").equalsIgnoreCase("N/A")){
            mAddess += "";
        }else{
            mAddess += shared.getSharedValue("PCOUNTRY").trim().toString();
        }
        txtAddress.setText(mAddess);
        if(shared.getSharedValue("PCOMPANYNAME").trim().equalsIgnoreCase("0")|| shared.getSharedValue("PCOMPANYNAME").equalsIgnoreCase("")|| shared.getSharedValue("PCOMPANYNAME").equalsIgnoreCase("N/A"))
            txtCompany.setText("Not Addded");
        else
            txtCompany.setText(shared.getSharedValue("PCOMPANYNAME"));

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
}
