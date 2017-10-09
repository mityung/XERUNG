package com.mityung.contactdirectory;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.contactplusgroup.adapter.CustomListAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.utils.ManagerTypeface;

public class SearchGroupMember extends Activity {
	
	private RelativeLayout mLayback, relProgress ;	
	private EditText edtSearch;
	private Comman comman = null;
	private SharedPreferanceData shared = null;
    Context context;
    private ListView listview;
    private LinearLayout layMain;
    TextView txtType;
    GroupDb dbContact = null;   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_group_member);
		initialise();
	}
	
	private void initialise(){
		findViewIds();
		getRefer();
		getBundleData();	
		setOnClick();
		setAdapterData();
	}
	
	String mGroupName = "";
	String mGroupId = "";	
	String mGroupAdmin = "";
	private void getBundleData(){
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mGroupName = extras.get("gName").toString();
			mGroupId   = extras.get("gID").toString();
			mGroupAdmin   = extras.get("gAdmin").toString();
			//"MG"+mGroupId		    	
		}
	}
	
	
	private void getRefer(){
		comman    = new Comman();
		shared    = new SharedPreferanceData(this);
	    context   = this;
		dbContact = new GroupDb(SearchGroupMember.this);	
	}
	
	ArrayList<ContactBean> gMemberList = new ArrayList<ContactBean>();
	
	private void setAdapter(){
		if(gMemberList.size()==0){
			hidelist();
			return;
		}
		showLocation();
		CustomListAdapter adapter = new CustomListAdapter(SearchGroupMember.this, R.layout.act_group_member, gMemberList);
        listview.setAdapter(adapter);
	}
	
	private void findViewIds(){
		mLayback    = (RelativeLayout)findViewById(R.id.layBack);		
		listview    = (ListView)findViewById(R.id.lvList);		
		relProgress = (RelativeLayout)findViewById(R.id.layProgressresult);		
		layMain     = (LinearLayout)findViewById(R.id.layMain);	
		edtSearch   = (EditText)findViewById(R.id.edtGroupSearch);
		txtType = (TextView)findViewById(R.id.txtTypeSearch);
		txtType.setTypeface(ManagerTypeface.getTypeface(SearchGroupMember.this, R.string.typeface_roboto_regular));
	}
	
	
	private void showProgress() {

		relProgress.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);

	}

	private void hideProgress() {

		relProgress.setVisibility(View.GONE);
		layMain.setVisibility(View.VISIBLE);

	}

	private void hidelist() {
		layMain.setVisibility(View.GONE);
		txtType.setVisibility(View.VISIBLE);
		txtType.setText("No Record Found.");
	}

	private void showLocation() {
		layMain.setVisibility(View.VISIBLE);
		txtType.setVisibility(View.GONE);
	}
	
	
	private void setOnClick(){
		
		mLayback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SearchGroupMember.this.finish();
			}
		});
		
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            	ContactBean cb = gMemberList.get(i);
            	
            	if(cb.getUID().trim().equals("0")){
            		comman.alertDialog(SearchGroupMember.this,"Pending Request",cb.getName()+" does not accept your request.");
            		return;
            	}
                Intent intent  = new Intent(SearchGroupMember.this, GroupUserProfile.class);
                intent.putExtra("Name", cb.getName());                
                intent.putExtra("OrignalName", cb.getOrignalName());
                intent.putExtra("MyPhoneBook", cb.getMyPhoneBookName());
                intent.putExtra("Number", cb.getNumber());
                intent.putExtra("Uid", cb.getUID());
                intent.putExtra("UserType", cb.getRequestFlag());
                intent.putExtra("Index", "-1");
                intent.putExtra("TableName","MG"+mGroupId);
				intent.putExtra("AdminFlag",mGroupAdmin);
                intent.putExtra("IsMyContact", cb.getIsMyContact());
                startActivity(intent);
            }
        });		
		
		edtSearch.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(!edtSearch.getText().toString().trim().equals(""))
					new SearchMember().execute("");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private void setAdapterData(){
		showProgress();
		if(gMemberList.size() > 0)
			gMemberList.clear();
		ArrayList<ContactBean> tb = dbContact.getMemberLimit("MG"+mGroupId);
		gMemberList.addAll(tb);	
		setAdapter();
		hideProgress();
	}
	
	class SearchMember extends AsyncTask<String, String, String> {
		ArrayList<ContactBean> tb = new ArrayList<ContactBean>();
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<ContactBean> db = dbContact.searchMember("MG"+mGroupId, edtSearch.getText().toString().trim());
			tb.addAll(db);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(gMemberList.size() > 0)
				gMemberList.clear();
			gMemberList.addAll(tb);
			setAdapter();
		}
		
		
	}
}
