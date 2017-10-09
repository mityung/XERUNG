package com.mityung.contactdirectory;

import android.util.Log;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private SharedPreferanceData shared = null;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        shared=new SharedPreferanceData(getApplicationContext());
        shared.saveSharedData(Vars.DEVICE_TOKEN_ID,token);

    }
}
