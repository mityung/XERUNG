package com.example.contactplusgroup.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by panther on 17/3/17.
 */
public class DetectSms extends BroadcastReceiver {

    private String OTPcode;
    private static final String TAG = "DetectSMS";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String sendername = currentMessage.getOriginatingAddress();
                    Log.d(TAG, "sendername is " + sendername);
                    String servicecntradd = currentMessage.getServiceCenterAddress();
                    Log.d(TAG, "servicecenteraddress is : " + servicecntradd);
                    String senderNum = phoneNumber;
                    Log.d(TAG, "Displayoriginationg address is : " + sendername);
                    String message = currentMessage.getDisplayMessageBody();
                    Log.d(TAG, "senderNum: " + senderNum + "; message: " + message);
                    if (senderNum.equalsIgnoreCase("IM-MEDICO")||senderNum.equalsIgnoreCase("AD-MEDICO")||senderNum.equalsIgnoreCase("DM-MEDICO")||senderNum.equalsIgnoreCase("AM-MEDICO")) {
                        if (!message.isEmpty()) {
                            Pattern intsOnly = Pattern.compile("\\d{5}");
                            Matcher makeMatch = intsOnly.matcher(message);
                            makeMatch.find();
                            OTPcode = makeMatch.group();
                            Intent intentNew = new Intent();
                            intentNew.setAction("SMS_RECEIVED");
                            intentNew.putExtra("otp_code", OTPcode);
                            context.sendBroadcast(intentNew);
                            System.out.println(OTPcode);
                        }
                    } else {
                        //Toast.makeText(context, "didn't identified the number", Toast.LENGTH_LONG).show();
                    }
                }// end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }
}


