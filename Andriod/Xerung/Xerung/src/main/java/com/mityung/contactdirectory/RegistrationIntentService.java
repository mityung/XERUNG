package com.mityung.contactdirectory;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.webservice.Webservice;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

public class RegistrationIntentService extends IntentService implements Webservice.WebServiceInterface {

    private static final String TAG = "RegIntentService";
    SharedPreferanceData shared = null;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        shared = new SharedPreferanceData(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            //Log.e(TAG, "GCM Registration Token: yuyu" );
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.i(TAG, "FCM Registration Token: " + token);
            // [END get_token]
            //Log.e(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            callForSendingTokenIdServices(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            //Log.e(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void callForSendingTokenIdServices(String token){
        try {
            JSONObject jb = new JSONObject();
            jb.put("MOBILENO", VerifyOTP.mobileNumber);
            jb.put("PPHONETYPE", "1");
            jb.put("ID", token);
            jb.put("VERSION", VerifyOTP.appVersion);
            sendIDTOG(jb);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void sendIDTOG(JSONObject jbObject) {
        //Log.e("sE ","Seddd "+jbObject);
        String jsonData = jbObject.toString();
        new Webservice(this, Vars.webMethodName.get(13)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(13));

    }
    @Override
    public void onWebCompleteResult(String result, String processName) {
        // TODO Auto-generated method stub

        if(result == null){

            return;
        }
        if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {

            return;
        }
        if(processName.equals(Vars.webMethodName.get(13))){

        }
    }

}