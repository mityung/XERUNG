package com.mityung.contactdirectory;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

import com.example.contactplusgroup.adapter.CustomListSearchAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.RequestSendtoAdmin;

public class SearchListView extends Activity implements WebServiceInterface{

	private RelativeLayout layBack, laySearchBack;
	private EditText edtSearch;
	private RelativeLayout relProgress,relNoNetwork, relInternet;
	private TextView txtInternet, txtInternetNote, txtNoNetwork, txtType, txtAppHeading;
	private LinearLayout layMain,lay1, lay2; 
	private Comman comman = null;
	ListView lvList;
	Context context = null;
	ImageView search;
	SearchListView searchView;
	SharedPreferanceData shared= null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search_list_details);
		initialise();
	}
	
	private void findViews() {
		edtSearch   = (EditText) findViewById(R.id.edtsearch);
		lvList      = (ListView)findViewById(R.id.lvSearchList);
		relProgress = (RelativeLayout)findViewById(R.id.layProgressresult);
		relNoNetwork = (RelativeLayout)findViewById(R.id.layNoresultserver);
		txtNoNetwork = (TextView)findViewById(R.id.txtNoresultnetwork);
		txtNoNetwork.setTypeface(ManagerTypeface.getTypeface(SearchListView.this, R.string.typeface_roboto_regular));
		txtType = (TextView)findViewById(R.id.txtTypeSearch);
		txtType.setTypeface(ManagerTypeface.getTypeface(SearchListView.this, R.string.typeface_roboto_regular));
		layMain = (LinearLayout)findViewById(R.id.laymain);
		layBack = (RelativeLayout)findViewById(R.id.layBack);
		lay1 = (LinearLayout)findViewById(R.id.lay1);
		lay2 = (LinearLayout)findViewById(R.id.lay2);
		laySearchBack = (RelativeLayout)findViewById(R.id.layBack1);
		txtAppHeading = (TextView) findViewById(R.id.txtHead);
		search = (ImageView)findViewById(R.id.imgSearch);
		relInternet = (RelativeLayout)findViewById(R.id.layinternetresult);
		txtInternet = (TextView)findViewById(R.id.txtcantConnect);
		txtInternet.setTypeface(ManagerTypeface.getTypeface(SearchListView.this, R.string.typeface_roboto_regular));
		txtInternetNote = (TextView)findViewById(R.id.txtcantConnectNote);
		txtInternetNote.setTypeface(ManagerTypeface.getTypeface(SearchListView.this, R.string.typeface_roboto_regular));
		txtAppHeading.setTypeface(ManagerTypeface.getTypeface(SearchListView.this, R.string.typeface_roboto_regular));
	}	
	
	private void initialise() {
		findViews();
		getBundle();
		getRefer();
		setOnClick();
		searchView = this;
		createJson(Integer.parseInt(gid), "", Integer.parseInt(gAccess));
	}
	
	String gname = "";
	String gid = "";
	String gAccess = "";
	private void getBundle(){
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gname = extras.getString("gName");
			gid = extras.getString("gUID");
			gAccess = extras.getString("gAccess");
			txtAppHeading.setText(gname);
		    // and get whatever type user account id is
			if(gAccess.equalsIgnoreCase("1")){
				search.setVisibility(View.GONE);
			}else{
				search.setVisibility(View.VISIBLE);
			}
			
		}
	}

	private void showServerResult(String msg){
		txtNoNetwork.setText(msg);
		relNoNetwork.setVisibility(View.VISIBLE);
	}
	
	
	private void offServerResponse() {
		relNoNetwork.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);
	}

	private void showProgress() {

		relProgress.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);

	}

	private void hideProgress() {

		layMain.setVisibility(View.VISIBLE);
		relProgress.setVisibility(View.GONE);

	}
	
	private void offNetwork() {

		relInternet.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.VISIBLE);

	}

	private void onNetwork() {

		layMain.setVisibility(View.VISIBLE);
		relInternet.setVisibility(View.GONE);

	}

	
	private void getRefer(){
		comman = new Comman();
		context = this;
		shared = new SharedPreferanceData(SearchListView.this);
	}
	
	public boolean flagSearch = false;
	
	private String startTxt = "", endTxt = "";
	private void setOnClick(){	
		
		layBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SearchListView.this.finish();
			}
		});
		
		edtSearch.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		    }

		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		        startTxt = s.toString().trim();		        
		        if(startTxt.trim().equals("") || startTxt.equals(endTxt)){
		        	return;
		        }
		        endTxt = startTxt;
		        if(endTxt.length()%2 == 0){
		        	flagSearch = true;
					createJson(Integer.parseInt(gid), edtSearch.getText().toString().trim(), Integer.parseInt(gAccess));
		        }       
		        
		    }

		    @Override
		    public void afterTextChanged(Editable s) {
		    }
		});
		
		edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if(arg1 == EditorInfo.IME_ACTION_SEARCH){
					comman.hideSoftKeyBoard(context, edtSearch);
					flagSearch = true;
					createJson(Integer.parseInt(gid), edtSearch.getText().toString().trim(), Integer.parseInt(gAccess));
					return true;
				}
				return false;
			}
		});	
		
		
		lvList.setOnItemClickListener(new OnItemClickListener() {
			// type 1 for access all , type 2 for only view detail(SubsCribe by voucher)
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {				
					ContactBean cb = gMemberList.get(position);
					String number = cb.getNumber();
					//comman.callPhone(number, SearchListView.this);
			}
		});
		
		laySearchBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SearchListView.this.finish();
				
			}
		});
		
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					getSearchstatus();
					//showLocationSearch();

			}
		});
		
	}
	
	private boolean isExpand = false;
	
	private void getSearchstatus(){
		
		if (isExpand) {
			lay1.setVisibility(View.VISIBLE);
			lay2.setVisibility(View.GONE);
			//showLocation();
			isExpand = false;

		} else {
			lay1.setVisibility(View.GONE);
			lay2.setVisibility(View.VISIBLE);
			isExpand = true;
		}
	}

	private void clearPreviousData(){
		if(gMemberList.size() > 0)
			gMemberList.clear();		
		lvList.deferNotifyDataSetChanged();
	}
	
	private boolean createJson(int guid, String search, int access) {

		JSONObject json = new JSONObject();
		try {
			json.put("PGROUPID", guid);
			json.put("PSERACHTEXT", search);
			json.put("PGROUPACCESSTYPE", access);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			callSearchWebServiceProcess(json);
			onNetwork();
		} else {
			offNetwork();
		}

		return true;
	}

	private void callSearchWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(11)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(11));
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
			hidelist();
			return;
		}
		if(processName.equals(Vars.webMethodName.get(11))){
			parseJsonGroup(result);
		}
		
	}
	
	
	private void hidelist() {
		lvList.setVisibility(View.GONE);
		txtType.setVisibility(View.VISIBLE);
		txtType.setText("No record found.");
	}

	private void showLocation() {
		lvList.setVisibility(View.VISIBLE);
		txtType.setVisibility(View.GONE);
	}
	
	
		ArrayList<ContactBean> gMemberList = new ArrayList<ContactBean>();
		private void parseJsonGroup(String result){
			try {
				clearPreviousData();
				JSONArray jArray = new JSONArray(result);
				if(jArray.length()==0){
					hidelist();
					return;
				}
				ContactBean gb = null;
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json = jArray.getJSONObject(i);

					gb = new ContactBean();
					gb.setName(json.getString("NAME"));
					gb.setNumber(json.getString("PHONENUMBER"));
					gb.setAdminFlag(gAccess);
					if(json.length()>2){
						gb.setAddress(json.getString("ADDRESS"));
					}

					if(json.length()>3){
						gb.setCity(json.getString("CITYNAME"));

					}

					if(gAccess.equalsIgnoreCase("1")){
						if(!json.getString("PHONENUMBER").equalsIgnoreCase(shared.getSharedValue("PMOBILE"))){
							gb.setRequestFlag("1");
						}else{
							gb.setRequestFlag("2");
						}

					}else
						gb.setRequestFlag("2");
					gMemberList.add(gb);
				}
				setAdapter();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();;
				Log.e("ERrr", "Error " + e.getMessage());

			}
			
		}
	

	private void setAdapter(){
		if(gMemberList.size()==0){
			return;
		}
		showLocation();
		CustomListSearchAdapter adapter = new CustomListSearchAdapter(SearchListView.this, R.layout.act_search_list_details, gMemberList, SearchListView.this);
		lvList.setAdapter(adapter);
	}
	
	public void sendRequest(int position){ // 1 for title click and 2 for image click.
		ContactBean cb = gMemberList.get(position);
		String gNumber = cb.getNumber();
		String gName = cb.getName();
		String member = "3:"+gName+":"+gNumber;
		new RequestSendtoAdmin(SearchListView.this, Integer.parseInt(gid), member, gname, getString(R.string.send_request), searchView).show();
		
	}
	
	
}
