package com.mityung.contactdirectory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.contactplusgroup.adapter.CustomListAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.webservice.Webservice;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by mipanther on 30/3/16.
 */
public class PendingMemberList extends Activity implements Webservice.WebServiceInterface{

    private RelativeLayout mRelativeLayoutBack, mRelativeLayoutNetwork, mRelativeLayoutInternet, mRelativeLayoutProgress;
    private TextView mTextViewNetwork, mTextViewInternet, mTextViewInternetNote, mTextViewAppName, mTextViewNoResult;
    private LinearLayout mLinearLayoutMain;
    private ListView mListPending;
    private Comman comman = null;
    private SharedPreferanceData shared = null;
    private Context context = null;
    private GroupDb dbContact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pending_request);
        initialise();
    }

    private void initialise(){
        findViewIds();
        getRefer();
        createFetchInviteJson();
        setOnClick();
    }

    private void findViewIds(){
        mRelativeLayoutBack = (RelativeLayout)findViewById(R.id.layBack);
        mTextViewAppName = (TextView)findViewById(R.id.txtAppName);
        mTextViewAppName.setTypeface(ManagerTypeface.getTypeface(PendingMemberList.this, R.string.typeface_android));
        mRelativeLayoutProgress = (RelativeLayout)findViewById(R.id.layProgressresult);
        mRelativeLayoutNetwork = (RelativeLayout)findViewById(R.id.layNoresultserver);
        mRelativeLayoutInternet = (RelativeLayout)findViewById(R.id.layinternetresult);
        mTextViewNetwork = (TextView)findViewById(R.id.txtNoresultnetwork);
        mTextViewNetwork.setTypeface(ManagerTypeface.getTypeface(PendingMemberList.this, R.string.typeface_roboto_regular));
        mTextViewInternet = (TextView)findViewById(R.id.txtcantConnect);
        mTextViewInternet.setTypeface(ManagerTypeface.getTypeface(PendingMemberList.this, R.string.typeface_roboto_regular));
        mTextViewInternetNote = (TextView)findViewById(R.id.txtcantConnectNote);
        mTextViewInternetNote.setTypeface(ManagerTypeface.getTypeface(PendingMemberList.this, R.string.typeface_roboto_regular));
        mLinearLayoutMain = (LinearLayout)findViewById(R.id.layMain);
        mListPending = (ListView)findViewById(R.id.lvList);
        mTextViewNoResult = (TextView)findViewById(R.id.txtnorecord);
        mTextViewNoResult.setTypeface(ManagerTypeface.getTypeface(PendingMemberList.this, R.string.typeface_roboto_regular));
    }

    private void getRefer(){
        dbContact = new GroupDb(this);
        comman = new Comman();
        shared = new SharedPreferanceData(this);
        context =this;

        if(shared.getSharedValue("PR"+Vars.gUID).trim().equals("N/A")){
            //Log.e("Table", "TableCreated");
            shared.saveSharedData("PR"+Vars.gUID, "yes");
            dbContact.createMemberTable("PR"+Vars.gUID);
        }else{
            //Log.e("Table", "TableCreated Not Successs");
        }
    }

    private void setOnClick(){
        mRelativeLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PendingMemberList.this.finish();
            }
        });
    }

    private boolean createFetchInviteJson() {

        JSONObject json = new JSONObject();
        try {
            json.put("PUID", Integer.parseInt(shared.getSharedValue("UserID")));
            json.put("PGROUPID", Integer.parseInt(Vars.gUID));
            json.put("PCOUNTRYCODEID", Integer.parseInt(shared.getSharedValue("PCOUNTRYID")));
            json.put("PFLAG", 1);
            json.put("PSTATUSID", 1);
            json.put("ADMINFLAG", Integer.parseInt(Vars.gAdmin));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        if (comman.isNetworkAvailable(context)) {
            callFetchInviteWebServiceProcess(json);
            onNetwork();
        } else {

            ArrayList<ContactBean> tb = dbContact.getAllMember("PR"+Vars.gUID);
            if(tb.size()>0){
                gMemberList.addAll(tb);
                setAdapter();
            }else
                offNetwork();

        }

        return true;
    }

    private void callFetchInviteWebServiceProcess(JSONObject jbObject) {
        String jsonData = jbObject.toString();
        new Webservice(this, Vars.webMethodName.get(10)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(10));
        showProgress();

    }

    private void offServerResponse() {
        mRelativeLayoutNetwork.setVisibility(View.VISIBLE);
        mLinearLayoutMain.setVisibility(View.GONE);
    }

    private void showProgress() {

        mRelativeLayoutProgress.setVisibility(View.VISIBLE);
        mLinearLayoutMain.setVisibility(View.GONE);

    }

    private void hideProgress() {

        mRelativeLayoutProgress.setVisibility(View.GONE);
        mLinearLayoutMain.setVisibility(View.VISIBLE);

    }

    private void offNetwork() {

        mRelativeLayoutInternet.setVisibility(View.VISIBLE);
        mLinearLayoutMain.setVisibility(View.GONE);

    }

    private void onNetwork() {

        mRelativeLayoutInternet.setVisibility(View.GONE);
        mLinearLayoutMain.setVisibility(View.VISIBLE);
    }

    private void noRecordFound(){

        mTextViewNoResult.setVisibility(View.VISIBLE);
        mLinearLayoutMain.setVisibility(View.GONE);

    }

    private void recordFound(){

        mTextViewNoResult.setVisibility(View.GONE);
        mLinearLayoutMain.setVisibility(View.VISIBLE);

    }

    @Override
    public void onWebCompleteResult(String result, String processName) {
        hideProgress();
        //Log.e("sss", "---RESULT---" + result);
        if (result == null) {
            if(gMemberList.size() == 0) {
                offServerResponse();
            }else{
                // show nointernet message
            }
            return;
        }
        if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
            if(gMemberList.size() == 0) {
                offServerResponse();
            }else{
                // show nointernet message
            }
            return;
        }

        if(processName.equals(Vars.webMethodName.get(10))){
            parseJsonfetch(result);
        }
    }

    private void parseJsonfetch(String result){
        try {
            JSONArray jArrayPending = new JSONArray(result);
            if(jArrayPending.length()==0){
                noRecordFound();
                return;
            }
            dbContact.truncateMemberTable("PR" + Vars.gUID);
            for(int i=0;i<jArrayPending.length();i++){
                ContactBean cb = new ContactBean();
                JSONObject jbPending = jArrayPending.getJSONObject(i);
                cb.setName(dbContact.getContactName(jbPending.getString("INVITATIONSENDTO").trim().trim()));
                cb.setNumber(jbPending.getString("INVITATIONSENDTO").trim());
                cb.setRequestFlag("3");
                cb.setUID("0");
                cb.setSearchKey(cb.getName());
                cb.setOrignalName(cb.getName()); // for pending user
                cb.setMyPhoneBookName(cb.getName());
                cb.setIsMyContact(1);
                cb.setmBloodGroup("-1");
                cb.setCity("-1");
                cb.setProfession("-1");
                gMemberList.add(cb);
                dbContact.addMember(cb,"PR"+ Vars.gUID);
            }
            setAdapter();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    ArrayList<ContactBean> gMemberList = new ArrayList<ContactBean>();

    CustomListAdapter adapter = null;
    private void setAdapter(){
        if(gMemberList.size()==0){
            noRecordFound();
            return;
        }
        recordFound();
        adapter = new CustomListAdapter(PendingMemberList.this, R.layout.act_pending_request, gMemberList);
        mListPending.setAdapter(adapter);
    }
}
