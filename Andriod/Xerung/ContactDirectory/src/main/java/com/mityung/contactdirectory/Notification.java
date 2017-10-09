package com.mityung.contactdirectory;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.contactplusgroup.adapter.CustomListAdapter;
import com.example.contactplusgroup.adapter.NotificationAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.InviteAccept;
import com.example.contactplusgroup.widgets.InviteAccept.OnAcceptCompInterface;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Notification extends Activity implements WebServiceInterface, OnAcceptCompInterface{
	
	private RelativeLayout mLayback, relProgress, relInternet, relNetwork, layNorecord;
	private TextView txtInternet, txtInternetNote, txtnetwork, txtNoPendingRequest, txtAppName, txtpleasewait, txtNoPendingRequest1;
	private Comman comman = null;
	private SharedPreferanceData shared = null;
	private Context context;
    private ListView listview;
    private LinearLayout layMain;
	private Notification notify = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_notification);
		initialise();
	}
	
	private void initialise(){
		findViewIds();
		getRefer();
		setOnClick();
		createFetchInviteJson();
	}
	
	private void getRefer(){
		comman = new Comman();
		shared = new SharedPreferanceData(this);
	    context =this;
	    notify = this;
	}
	
	private void findViewIds(){
		mLayback = (RelativeLayout)findViewById(R.id.layBack);
		listview = (ListView)findViewById(R.id.lvList);
		relProgress = (RelativeLayout)findViewById(R.id.layProgressresult);
		relInternet = (RelativeLayout)findViewById(R.id.layinternetresult);
		relNetwork = (RelativeLayout)findViewById(R.id.layNoresultserver);
		txtInternet = (TextView)findViewById(R.id.txtcantConnect);
		txtInternet.setTypeface(ManagerTypeface.getTypeface(Notification.this, R.string.typeface_roboto_regular));
		txtInternetNote = (TextView)findViewById(R.id.txtcantConnectNote);
		txtInternetNote.setTypeface(ManagerTypeface.getTypeface(Notification.this, R.string.typeface_roboto_regular));
		txtnetwork = (TextView)findViewById(R.id.txtNoresultnetwork);
		txtnetwork.setTypeface(ManagerTypeface.getTypeface(Notification.this, R.string.typeface_roboto_regular));
		txtNoPendingRequest = (TextView)findViewById(R.id.txtNoPendingRequest);
		txtNoPendingRequest.setTypeface(ManagerTypeface.getTypeface(Notification.this, R.string.typeface_roboto_regular));
		layMain = (LinearLayout)findViewById(R.id.layMain);
		txtAppName = (TextView)findViewById(R.id.txtHead);
		txtAppName.setTypeface(ManagerTypeface.getTypeface(Notification.this, R.string.typeface_android));
		txtpleasewait = (TextView)findViewById(R.id.txtpleasewait);
		txtpleasewait.setTypeface(ManagerTypeface.getTypeface(Notification.this, R.string.typeface_roboto_regular));
		txtNoPendingRequest1 = (TextView)findViewById(R.id.txtNoPendingRequest1);
		txtNoPendingRequest1.setTypeface(ManagerTypeface.getTypeface(Notification.this, R.string.typeface_roboto_regular));
		layNorecord = (RelativeLayout)findViewById(R.id.layNorecord);
		
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
	
	private void setOnClick(){
		
		mLayback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Notification.this.finish();

			}
		});
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ContactBean data = gMemberList.get(i);
                String mGroupName = null;
				String sendby = data.getSento();
				if(data.getInvitationFlag().equalsIgnoreCase("1")){
					mGroupName = getString(R.string.request_received_note)+" "+data.getName()+".";
				}else if(data.getInvitationFlag().equalsIgnoreCase("2")){
					mGroupName = "Do you want to add "+data.getUserName()+ " in directory known as "+data.getName()+".";
				}
                int mGroupId = Integer.parseInt(data.getId());
                new InviteAccept(Notification.this, mGroupId, mGroupName,getString(R.string.request_received),sendby, notify, Notification.this).show();
            }
        });
		
	}

	private boolean createAcceptInviteJson(int mgroupid, int state, String sendby) {
		JSONObject json = new JSONObject();
		try {
			json.put("PPHONENUMBER", shared.getSharedValue("PMOBILE")+"#"+sendby);
			json.put("PGROUPID", mgroupid);
			json.put("PCOUNTRYCODEID", Integer.parseInt(shared.getSharedValue("PCOUNTRYID")));
			json.put("PFLAG", 1);
			json.put("PSTATUSID", state);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			callAcceptInviteWebServiceProcess(json);
			onNetwork();
		} else {
			offNetwork();
		}
		return true;
	}

	private void callAcceptInviteWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(9)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(9));
		showProgress();
	}
	
	private boolean createFetchInviteJson() {

		JSONObject json = new JSONObject();
		try {
			json.put("PUID", Integer.parseInt(shared.getSharedValue("UserID")));
			json.put("PGROUPID", 0);
			json.put("PCOUNTRYCODEID", Integer.parseInt(shared.getSharedValue("PCOUNTRYID")));
			json.put("PFLAG", 2);
			json.put("PSTATUSID", 1);
			json.put("ADMINFLAG", 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			callFetchInviteWebServiceProcess(json);
			onNetwork();
		} else {
			offNetwork();
		}

		return true;
	}

	private void callFetchInviteWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(10)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(10));
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
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			offServerResponse();
			return;
		}if(processName.equals(Vars.webMethodName.get(9))){
			parseJson(result);
		}else if(processName.equals(Vars.webMethodName.get(10))){
			parseJsonfetch(result);
		}
	}
	
	//[{"GROUPNAME":"mityung","INVITATIONSENDBY":"8881670074","NAME":"shivam","GROUPID","2"}]

	ArrayList<ContactBean> gMemberList = new ArrayList<ContactBean>();
	private void parseJsonfetch(String result){
		try {
			JSONArray jArrayPending = new JSONArray(result);
			if(gMemberList.size()>0){
				gMemberList.clear();
			}
			for(int i=0;i<jArrayPending.length()-1;i++){
				ContactBean cb = new ContactBean();
				JSONObject jbPending = jArrayPending.getJSONObject(i);
				if(jbPending.length()==0){
					setNoRecord();
					return;
				}
				cb.setName(jbPending.getString("GROUPNAME").trim());
				cb.setNumber(jbPending.getString("INVITATIONSENDBY").trim() + " (" + jbPending.getString("NAME").trim() + ")");
				cb.setUserName(jbPending.getString("NAME").trim());
				cb.setSento(jbPending.getString("INVITATIONSENDBY").trim());
				cb.setId(jbPending.getString("GROUPID").trim());
				cb.setInvitationFlag(jbPending.getString("INVITATIONFLAGID").trim());
				cb.setRequestFlag("4");
				gMemberList.add(cb);
			}
			for(int i=jArrayPending.length()-1;i<jArrayPending.length();i++){
				JSONObject jbPending = jArrayPending.getJSONObject(i);
				shared.saveSharedData("NCOUNT", "" + jbPending.getString("COUNT").trim());
			}
			setAdapter();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void setNoRecord(){
		layNorecord.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);
	}
	
	private void setRecord(){
		layNorecord.setVisibility(View.GONE);
		layMain.setVisibility(View.VISIBLE);
	}
	
	private void setAdapter(){
		if(gMemberList.size()==0){
			setNoRecord();
			return;
		}
		setRecord();
		NotificationAdapter adapter = new NotificationAdapter(Notification.this, R.layout.act_notification, gMemberList);
        listview.setAdapter(adapter);
	}

	private void parseJson(String result){
		try {
			JSONObject json = new JSONObject(result);
			String status = json.getString("STATUS");
			if(status.equalsIgnoreCase("SUCCESS")){
				createFetchInviteJson();
				Vars.onAdd = true;
				//Notification.this.finish();
			}else{
				comman.alertDialog(Notification.this,"Notification",getString(R.string.something_problem));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void OnAcceptComplete(int result, int id, String sendby) {
		// TODO Auto-generated method stub
		createAcceptInviteJson(result, id, sendby);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

}