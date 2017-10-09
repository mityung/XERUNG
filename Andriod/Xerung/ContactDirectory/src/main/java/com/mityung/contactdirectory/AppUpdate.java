package com.mityung.contactdirectory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.utils.ManagerTypeface;

/**
 * Created by mipanther on 29/3/16.
 */
public class AppUpdate extends Activity {

    TextView txtThisVersion,txtDeviceDate, txtAppUpdate;
    Context context = null;
    Button btnDownload;
    Typeface fontBauhus;
    Bundle extras;
    Comman commanMethod = null;
    private SharedPreferanceData shared = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_update);
       // fontBauhus       = Typeface.createFromAsset(getAssets(),"fonts/BAUHAUSM_0.TTF");
        context = this;
        commanMethod = new Comman();
        shared = new SharedPreferanceData(this);
        findView();
        setOnClick();
        getBundleData();
        checkUpdateMessge();
    }

    private void checkUpdateMessge(){

        if(shared.getSharedValue("UpdateMsg").toString().trim().length() != 0){
            txtThisVersion.setText(shared.getSharedValue("UpdateMsg").toString());
        }
    }

    private void getBundleData(){

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            if(bundle.get("msg") != null){

                if(bundle.get("msg").toString().trim().length() != 0){

                    txtThisVersion.setText(bundle.get("msg").toString().trim());
                    shared.saveSharedData("UpdateMsg", bundle.get("msg").toString().trim());

                }
            }
        }

    }

    private void setOnClick(){
        btnDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(commanMethod.isNetworkAvailable(AppUpdate.this))
                    openPlayStore();
            }
        });
    }
    private void findView(){
        txtThisVersion = (TextView)findViewById(R.id.txtThisVersionOf);
        txtThisVersion.setTypeface(ManagerTypeface.getTypeface(AppUpdate.this, R.string.typeface_roboto_regular));
        btnDownload    = (Button)findViewById(R.id.btnDownloadApp);
        btnDownload.setTypeface(ManagerTypeface.getTypeface(AppUpdate.this, R.string.typeface_roboto_regular));
        txtAppUpdate = (TextView)findViewById(R.id.txtAppUpdateMainHead);

    }

    private void openPlayStore(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}

