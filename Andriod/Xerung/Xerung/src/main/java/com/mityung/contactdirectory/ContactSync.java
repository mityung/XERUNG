package com.mityung.contactdirectory;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.webservice.Webservice;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class ContactSync extends IntentService implements Webservice.WebServiceInterface{



    public ContactSync() {
        super("com.mityung.callindex.MyIntentService");
    }
    JSONArray jbMainArray = new JSONArray();
    @Override
    protected void onHandleIntent(Intent intent) {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(ContactSync.this);
        mBuilder.setContentTitle("Contact Sync")
                .setContentText("Sync ...")
                .setSmallIcon(R.drawable.ic_custom_notification);
        try {
            ContentResolver cr = getContentResolver();
            Cursor cur         = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

           // Log.e("Sync", "Count = " + cur.getCount());
            int count = 0;
            mBuilder.setProgress(cur.getCount(), 0, false);
            mNotifyManager.notify(notificaitonId, mBuilder.build());
            Vars.onSync = true;
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    //Log.e("Sync","=========================Count================="+count);
                    ++count;
                    String id   = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                       // Log.e("Sync", "name : " + name + ", ID : " + id);
                        JSONObject jbMain = new JSONObject();
                        jbMain.put("name",name);
                        jbMain.put("id",id);
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
                        StringBuffer output = new StringBuffer();

                        JSONObject jb = null;
                        JSONArray jbArray = new JSONArray();
                        while (pCur.moveToNext()) {
                            String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String newPhinr    = phone.replaceAll("\\s+","").replaceAll("\\-", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\#", "").replaceAll("\\_", "").replaceAll("\\?", "");
                            String type = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            // output.append("\n Phone number:" + phone);
                            jb = new JSONObject();
                            jb.put("phone", newPhinr);
                            jb.put("type",type);
                            jbArray.put(jb);
                            //Log.e("Sync", "Phone number " + newPhinr+ "type "+type);
                        }
                        jbMain.put("phone", jbArray);
                        pCur.close();

                        //Log.e("Sync","-----------------Phone-------------");
                        Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",new String[]{id}, null);
                        jbArray = new JSONArray();
                        while (emailCur.moveToNext()) {
                            // This would allow you get several email addresses
                            // if the email addresses were stored in an array
                            String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            String emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                            //Log.e("Sync","Email " + email + " Email Type : " + emailType);
                            jb = new JSONObject();
                            jb.put("email", email);
                            jbArray.put(jb);
                        }
                        jbMain.put("email", jbArray);
                        emailCur.close();
                       // Log.e("Sync", "-----------------Email-------------");

                        // Get note.......
                        String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                        String[] noteWhereParams = new String[]{id,ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                        Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                        jbArray = new JSONArray();
                        if (noteCur.moveToFirst()) {
                            String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                           // Log.e("Sync","Note " + note);
                            jb = new JSONObject();
                            jb.put("note", note);
                            jbArray.put(jb);
                        }
                        jbMain.put("note", jbArray);
                        noteCur.close();
                       // Log.e("Sync", "-----------------Note-------------");

                        final String[] projection = new String[] {
                                ContactsContract.Data.CONTACT_ID,
                                ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,
                                ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                                ContactsContract.CommonDataKinds.StructuredPostal.LABEL,
                                ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                                ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
                                ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,
                                ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                                ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                                ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                                ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        };

                        //Get Postal Address....
                        String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                        String[] addrWhereParams = new String[]{id,ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                        Cursor addrCur = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,projection, addrWhere, addrWhereParams,null );
                        jbArray = new JSONArray();
                        while(addrCur.moveToNext()) {
                            String FORMATTED_ADDRESS = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                            String TYPE = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                            String LABEL = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL));
                            String STREET = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                            String POBOX = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                            String NEIGHBORHOOD = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD));
                            String CITY = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                            String REGION = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                            String POSTCODE = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                            String COUNTRY = addrCur.getString(
                                    addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

                           // Log.e("", "Note " + "poBox: " + FORMATTED_ADDRESS + "street: " + TYPE + "city: " + LABEL+ "type "+POBOX+NEIGHBORHOOD+CITY);
                            jb = new JSONObject();
                            jb.put("address", FORMATTED_ADDRESS);
                            jb.put("street", STREET);
                            jb.put("city", CITY);
                            jb.put("country", COUNTRY);
                            jb.put("postal", POSTCODE);
                            jbArray.put(jb);
                            // Do something with these....

                        }
                        jbMain.put("address", jbArray);
                        addrCur.close();
                        //Log.e("Sync", "-----------------Address-------------");

                        // Get Organizations.........
                        String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                        String[] orgWhereParams = new String[]{id,ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                        Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,null, orgWhere, orgWhereParams, null);
                        jbArray = new JSONArray();
                        if (orgCur.moveToFirst()) {
                            String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                            String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                           // Log.e("Sync","OrgName "+orgName +" title "+title);
                            jb = new JSONObject();
                            jb.put("org", orgName);
                            jb.put("title", title);
                            jbArray.put(jb);
                        }
                        jbMain.put("org", jbArray);
                        orgCur.close();

                        // Get Website.........
                        String webstite = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                        String[] webUrl = new String[]{id,ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE};
                        Cursor webCur = cr.query(ContactsContract.Data.CONTENT_URI,null, webstite, webUrl, null);
                        jbArray = new JSONArray();
                        while(webCur.moveToNext()) {
                           // if (webCur.moveToFirst()) {
                                String orgName = webCur.getString(webCur.getColumnIndex(ContactsContract.CommonDataKinds.Website.DATA));
                                // String title = webCur.getString(webCur.getColumnIndex(ContactsContract.CommonDataKinds.Website.TITLE));
                                //Log.e("Sync", "WEb " + orgName + " title ");
                            jb = new JSONObject();
                            jb.put("web", orgName);
                            jbArray.put(jb);
                           // }
                        }

                        jbMain.put("web", jbArray);
                        webCur.close();
                        //Log.e("Sync", "-----------------Title-------------");
                        jbMainArray.put(jbMain);
                        mBuilder.setProgress(cur.getCount(), count, false);
                        mNotifyManager.notify(notificaitonId, mBuilder.build());
                    }
                }
            }
            //Log.e("JsonArray","Data Array "+jbMainArray);
            mBuilder.setContentText("You may take backup of your contact at any time.");
            // Removes the progress bar
            mBuilder.setProgress(cur.getCount(), cur.getCount(), false);
            mNotifyManager.notify(notificaitonId, mBuilder.build());
            Vars.onSync = false;
            getBackup();
        }catch (Exception e){
            //Log.e("Error in Backup","Backup "+e.getMessage());
            e.printStackTrace();
        }



    }

    private NotificationManager mNotifyManager;
    private Builder mBuilder;
    int notificaitonId = 1;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    private SharedPreferanceData shared = null;
    private boolean sendBackup(String data) {

        shared    = new SharedPreferanceData(getApplicationContext());
        JSONObject json = new JSONObject();
        try {
            json.put("PUID", shared.getSharedValue("UserID"));
            json.put("PBACKUPLIST", data);
            json.put("PFLAG", "1");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        if (new Comman().isNetworkAvailable(getApplicationContext())) {
            sendBackup(json);
        } else {
            //comman.alertDialog(getApplicationContext(),getString(R.string.no_internet_connection),getString(R.string.you_dont_have_internet));
        }

        return true;
    }

    private void sendBackup(JSONObject jbObject) {
        String jsonData = jbObject.toString();
        new Webservice(this, Vars.webMethodName.get(2)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(2));

    }

    private void getBackup() {

        shared    = new SharedPreferanceData(getApplicationContext());
        JSONObject json = new JSONObject();
        try {
            json.put("PUID", shared.getSharedValue("UserID"));
            json.put("PBACKUPLIST", "");
            json.put("PFLAG", "2");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (new Comman().isNetworkAvailable(getApplicationContext())) {
            getBackup(json);
        } else {
            //comman.alertDialog(getApplicationContext(),getString(R.string.no_internet_connection),getString(R.string.you_dont_have_internet));
        }
    }

    private void getBackup(JSONObject jbObject) {
        String jsonData = jbObject.toString();
        new Webservice(this, Vars.webMethodName.get(3)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(3));
    }

    JSONArray jbBackupM = null;
    @Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stub
		if (result == null) {
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			return;
		}
		if(processName.equals(Vars.webMethodName.get(2))){

		}else if(processName.equals(Vars.webMethodName.get(3))){
            try {
                JSONObject jb = new JSONObject(result);
                if(jb.get("BACKUP").toString().trim().equals("0")){
                   sendBackup(jbMainArray.toString());
                }else{
                    // NEW contact added in phone
                    jbBackupM = new JSONArray(jb.get("BACKUP").toString());
                   // Log.e("Data",jbBackupM.length()+"BAcku "+jbMainArray.length());
                    if(jbMainArray.length() > jbBackupM.length()) {
                        boolean newContactFound = false;
                        int count = 0;
                        mBuilder.setContentText("Syncing New Contacts ...");
                        // Removes the progress bar
                        mNotifyManager.notify(notificaitonId, mBuilder.build());
                        JSONArray jBackup = new JSONArray(jb.get("BACKUP").toString());
                        //-----
                        boolean status = false;
                        for (int i = 0; i < jbMainArray.length(); i++) {
                            JSONObject jbM = jbMainArray.getJSONObject(i);
                            JSONArray jbMA = jbM.getJSONArray("phone");
                            for (int ij = 0; ij < jbMA.length(); ij++) {
                                JSONObject jbMAD = jbMA.getJSONObject(ij);
                                String phoneNEW = jbMAD.get("phone").toString().trim();
                                for (int j = 0; j < jBackup.length(); j++) {
                                    JSONObject jbB = jBackup.getJSONObject(j);
                                    JSONArray jbBA = jbB.getJSONArray("phone");
                                    status = false;
                                    for (int ji = 0; ji < jbBA.length(); ji++) {
                                        JSONObject jbBAD = jbBA.getJSONObject(ji);
                                        String phoneBackup = jbBAD.get("phone").toString().trim();
                                        if (phoneBackup.equals(phoneNEW)) {
                                            //Log.e("Compare", "match " + phoneBackup);
                                            status = true;
                                            break;
                                        } else {
                                            status = false;
                                        }
                                    }
                                    if (status == true) {
                                        break;
                                    }

                                }
                                if (status == false) {
                                    jbBackupM.put(jbM);
                                    newContactFound = true;
                                    ++count;
                                   // Log.e("Found", "Found new co" + jbM);
                                } else {
                                   // Log.e("Found", "Not found ");
                                }
                            }
                        }
                        if(newContactFound) {
                            sendBackup(jbBackupM.toString());
                            newContactFound(count);
                        }else{
                            mNotifyManager.cancel(notificaitonId);
                            noNewContactNotif();
                        }
                    }else{ // when contact deleted from phone
                        mBuilder.setContentText("Taking your old phone Contacts ...");
                        // Removes the progress bar
                        mNotifyManager.notify(notificaitonId, mBuilder.build());
                        JSONArray jBackup = new JSONArray(jb.get("BACKUP").toString());
                        jbBackupM = new JSONArray();
                        //jbBackupM = new JSONArray(jb.get("BACKUP").toString());
                        //-----
                        boolean status = false;
                        for (int i = 0; i < jBackup.length(); i++) {
                            JSONObject jbM = jBackup.getJSONObject(i);
                            JSONArray jbMA = jbM.getJSONArray("phone");
                            for (int ij = 0; ij < jbMA.length(); ij++) {
                                JSONObject jbMAD = jbMA.getJSONObject(ij);
                                String phoneNEW = jbMAD.get("phone").toString().trim();
                                for (int j = 0; j < jbMainArray.length(); j++) {
                                    JSONObject jbB = jbMainArray.getJSONObject(j);
                                    JSONArray jbBA = jbB.getJSONArray("phone");
                                    status = false;
                                    for (int ji = 0; ji < jbBA.length(); ji++) {
                                        JSONObject jbBAD = jbBA.getJSONObject(ji);
                                        String phoneBackup = jbBAD.get("phone").toString().trim();

                                        if (phoneBackup.equals(phoneNEW)) {
                                            //Log.e("Compare", "match " + phoneBackup);
                                            status = true;
                                            break;
                                        } else {
                                            status = false;
                                        }
                                    }
                                    if (status == true) {
                                        break;
                                    }
                                }
                                if (status == false) {
                                    jbBackupM.put(jbM);
                                    //Log.e("Found", "Found new co" + jbM);
                                } else {
                                  //  Log.e("Found", "Not found ");
                                }
                            }
                        }
                        mNotifyManager.cancel(notificaitonId);
                        backupFound();
                      /// sendBackup(jbBackupM.toString());
                    }
                }
            }catch (Exception e){
                Log.e("msg", "Error " + e.getMessage());
                e.printStackTrace();
            }
        }
	}

    private void noNewContactNotif(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_custom_notification);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("Contact Sync!!");
        mBuilder.setContentText("No New Contact Found for Backup");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificaitonId, mBuilder.build());
    }

    private void backupFound(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        if(jbBackupM.toString().trim().length()>0){
            Intent dialogIntent = new Intent(this, AddToContactList.class);
            dialogIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            dialogIntent.putExtra("data", jbBackupM.toString());
            PendingIntent intent = PendingIntent.getActivity(this, 0, dialogIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(intent);
        }
        mBuilder.setSmallIcon(R.drawable.ic_custom_notification);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("Contact Sync!!");
        mBuilder.setContentText("You have lost some contact, we have backup");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificaitonId, mBuilder.build());
    }

    private void newContactFound(int count){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_custom_notification);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("Contact Sync!!");
        mBuilder.setContentText("Backup of your new "+count+" Contacts succesfully take.");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificaitonId, mBuilder.build());
    }
}
