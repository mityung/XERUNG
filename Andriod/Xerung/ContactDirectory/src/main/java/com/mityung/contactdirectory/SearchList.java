package com.mityung.contactdirectory;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.contactplusgroup.adapter.GroupSearchAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.GroupBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.ProgressDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchList extends Activity implements WebServiceInterface{

	private RelativeLayout layBack;
	private Typeface roboto;
	private EditText edtSearch;
	private ProgressDialog pbbar = null;
	private RelativeLayout relProgress,relNoNetwork, relType;
	private TextView txtNoNetwork, txtType;
	private LinearLayout layMain; 
	private Comman comman = null;
	Button btnSearch;
	ListView lvList;
	Context context = null;
	GroupDb dbContact = null;
	SharedPreferanceData shared = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search_list);
		roboto = Typeface.createFromAsset(getAssets(),"font/Roboto-Regular.ttf");
		initialise();
	}
	
	public void onResume(){
		super.onResume();
		
	}
	
	private void findViews() {
		edtSearch   = (EditText) findViewById(R.id.edtsearch);
		lvList      = (ListView)findViewById(R.id.lvSearchList);
		relProgress = (RelativeLayout)findViewById(R.id.layProgressresult);
		relNoNetwork = (RelativeLayout)findViewById(R.id.layNoresultserver);
		txtNoNetwork = (TextView)findViewById(R.id.txtNoresultnetwork);
		txtNoNetwork.setTypeface(roboto);
		relType = (RelativeLayout)findViewById(R.id.layTypeSearch);
		txtType = (TextView)findViewById(R.id.txtTypeSearch);
		txtType.setTypeface(roboto);
		layMain = (LinearLayout)findViewById(R.id.laymain);
		layBack = (RelativeLayout)findViewById(R.id.layBack);
		btnSearch = (Button)findViewById(R.id.btnsearchgo);
	}	
	
	private void initialise() {
		findViews();
		getRefer();
		setOnClick();
		showMainScreen();
		
	}
	
	private void showMainScreen(){
		layMain.setVisibility(View.GONE);
		relType.setVisibility(View.VISIBLE);
	}
	
	private void hideMainScreen(){
		layMain.setVisibility(View.VISIBLE);
		relType.setVisibility(View.GONE);
	}
	
	private void showServerResult(String msg){		
		txtNoNetwork.setText(msg);
		relNoNetwork.setVisibility(View.VISIBLE);
	}
	
	private void hideServerNoResult(){
		relNoNetwork.setVisibility(View.GONE);
	}
	
	private void showSearchProgress(){
		hideServerNoResult();
		relProgress.setVisibility(View.VISIBLE);
	}
	
	private void hideSearchProgress(){
		relProgress.setVisibility(View.GONE);
	}
	
	private void hideLoadSearch(){
		try {
			if(pbbar != null){
				pbbar.closeProgressPopUp();
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void getRefer(){
		comman = new Comman();
		dbContact = new GroupDb(this);
		pbbar  = new ProgressDialog();
		shared = new SharedPreferanceData(this);
		context = this;
	}
	
	private String startTxt = "", endTxt = "";
	private void setOnClick(){	
		layBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SearchList.this.finish();
			}
		});
		
		edtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				startTxt = s.toString().trim();
				if (startTxt.trim().equals("") || startTxt.equals(endTxt)) {
					return;
				}
				endTxt = startTxt;
				if (endTxt.length() % 2 == 0) {
					showSearchProgress();

					createSearchJson(0, edtSearch.getText().toString().trim(), 0);
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
				if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
					showSearchProgress();
					comman.hideSoftKeyBoard(context, edtSearch);
					createSearchJson(0, edtSearch.getText().toString().trim(), 0);
					return true;
				}
				return false;
			}
		});	
		
		
		lvList.setOnItemClickListener(new OnItemClickListener() {
			// type 1 for access all , type 2 for only view detail(SubsCribe by voucher)
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GroupBean gb = tbList.get(position);
				String gUId = gb.getmGroupId();
				String gAccess = gb.getmGroupAccessType();
				String gName = gb.getmGroupName();
				Vars.gAdmin = gb.getmGroupAdmin();
				Vars.gMCount = gb.getmGroupSize();
				Vars.gUID = gb.getmGroupId();
				if(comman.isNetworkAvailable(context)){
					Intent i = new Intent(SearchList.this, SearchListView.class);
					i.putExtra("gUID", gUId);
					i.putExtra("gAccess", gAccess);
					i.putExtra("gName", gName);
					startActivity(i);
				}else{
					Intent intent = new Intent(SearchList.this, GroupView.class);
					intent.putExtra("gName", gName);
					intent.putExtra("gDesc", gb.getmGroupDesc());
					intent.putExtra("gTag", gb.getmGroupTag());
					shared.saveSharedData("mGroupCount" + Vars.gUID, String.valueOf(Vars.gMCount));
					startActivity(intent);
				}
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSearchProgress();
				comman.hideSoftKeyBoard(context, edtSearch);
				createSearchJson(0, edtSearch.getText().toString().trim(), 0);
			}
		});
	}
	
	private boolean createSearchJson(int guid, String search, int access) {

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
		} else {
			new SearchDBGroup().execute("");
			//comman.alertDialog(SearchList.this, getString(R.string.no_internet_connection), getString(R.string.you_dont_have_internet));
		}

		return true;
	}

	private void callSearchWebServiceProcess(JSONObject jbObject) {
		String jsonData = jbObject.toString();
		new Webservice(this, Vars.webMethodName.get(11)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(11));
	}
	
	
	
	@Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stub
		if (result == null) {	
			hideLoadSearch();
			hideSearchProgress();
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			hideLoadSearch();
			hideSearchProgress();
			return;
		}
		if(processName.equals(Vars.webMethodName.get(11))){
			parseJson(result);
		}
		
	}
	
	//[{"MADEBYPHONENO":"8475068367","MADEBYNAME":"rakeshsharma"
		//,"GROUPID":"1","TAGNAME":"mygroup","DESCRITION":"hdjdjdjdjjj"
		//	,"GROUPPHOTO":"","MEMBERCOUNT":"2","ADMINFLAG":"0"}]	
		
		ArrayList<GroupBean> tbList = new ArrayList<GroupBean>();
		private void parseJson(String result){	
			try {
				hideMainScreen();
				hideSearchProgress();
				clearPreviousData();
				JSONArray jArray = new JSONArray(result);
				GroupBean gb = null;
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json = jArray.getJSONObject(i);
					if(json.length()==0){
						showServerResult("No record found.");
						return;
					}
					gb = new GroupBean();
					gb.setmGroupName(json.getString("GROUPNAME"));
					gb.setmGroupId(json.getString("GROUPID"));
					gb.setmGroupTag(json.getString("TAGNAME"));
					gb.setmGroupDesc(json.getString("DESCRITION"));
					gb.setmPhoto(json.getString("GROUPPHOTO"));
					gb.setmGroupAccessType(json.getString("GROUPACCESSTYPE"));
					tbList.add(gb);
				}
				setAdapter();
			} catch (Exception e) {
				// TODO: handle exception
				//Log.e("Errror", "error"+e.getMessage());
				e.printStackTrace();
			}
		}
	
	@SuppressLint("NewApi")
	private void clearPreviousData(){
		if(tbList.size() > 0)
			tbList.clear();		
		lvList.deferNotifyDataSetChanged();
	}
	
	private void setAdapter() {
		
		if (tbList.size() == 0) {
			showServerResult("No record found.");
			return;
		}
		hideServerNoResult();
		GroupSearchAdapter adapter = new GroupSearchAdapter(SearchList.this, R.layout.act_search_list, tbList);
		lvList.setAdapter(adapter);
	}

	class SearchDBGroup extends AsyncTask<String, String, String> {
		ArrayList<GroupBean> tb = new ArrayList<GroupBean>();
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<GroupBean> db = dbContact.searchGroup(edtSearch.getText().toString().trim());
			tb.addAll(db);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			hideMainScreen();
			hideSearchProgress();
			clearPreviousData();
			tbList.addAll(tb);
			setAdapter();
		}


	}
	
	
}
