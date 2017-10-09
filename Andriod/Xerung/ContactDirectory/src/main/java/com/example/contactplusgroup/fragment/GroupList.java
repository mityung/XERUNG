package com.example.contactplusgroup.fragment;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.contactplusgroup.adapter.PrivateDirectoryAdapter;
import com.example.contactplusgroup.views.XListView;
import com.mityung.contactdirectory.AddNewGroup;
import com.mityung.contactdirectory.GroupView;
import com.mityung.contactdirectory.R;
import com.example.contactplusgroup.adapter.GroupAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.GroupBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.example.contactplusgroup.widgets.SnackBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupList extends Fragment implements WebServiceInterface, XListView.IXListViewListener{

	@SuppressWarnings("unused")
	private int position;
	private Button btnCreate;
    // Contact List
	XListView listView;
    private LinearLayout layMain;
    private RelativeLayout relNoGroup , relProgress, relNetwork;
	private TextView txtnetwork;
	private SharedPreferanceData shared = null;
	private Context context;
	private Comman comman = null;
	int pageInitial = 1;
	int pageFinal = 0;
	int pageData =10;
	GroupDb dbGroup = null;
	boolean onLoadCheck = false;

	/**
	 * Static instance of the Profile
	 * @return
	 */
	public static GroupList newInstance() {
		GroupList f = new GroupList();
		Bundle b = new Bundle();
		f.setArguments(b);
		return f;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_group, container,false);
		findViewids(rootView);
		initialise();
		return rootView;
	}

	private void findViewids(View view) {
		btnCreate = (Button)view.findViewById(R.id.btnCreate);
		listView = (XListView) view.findViewById(R.id.lvGroupList);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		relProgress = (RelativeLayout)view.findViewById(R.id.layProgressresult);
		relNetwork = (RelativeLayout)view.findViewById(R.id.layNoresultserver);
		txtnetwork = (TextView)view.findViewById(R.id.txtNoresultnetwork);
		txtnetwork.setTypeface(ManagerTypeface.getTypeface(getActivity(), R.string.typeface_roboto_regular));
		layMain = (LinearLayout)view.findViewById(R.id.laymain);
		relNoGroup = (RelativeLayout)view.findViewById(R.id.noContact);
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

	private void noRecordFound(){

		relNoGroup.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);

	}

	private void recordFound(){

		relNoGroup.setVisibility(View.GONE);
		layMain.setVisibility(View.VISIBLE);

	}

	int mGroupCount = 0;
	private void initialise(){
		getRefer();
		setOnClick();
		mGroupCount = (int)dbGroup.countTest();
		if(mGroupCount>0){
			if(mGroupCount>10){
				pageFinal = getPageCount(mGroupCount);
			}else{
				onLoadCheck = true;
			}
			int skipvalue = (pageInitial-1)*10;
			ArrayList<GroupBean> gbLimit = dbGroup.getGroupLimit(skipvalue, 10);
			tbList.addAll(gbLimit);
			setAdapter();
		}else{
			createJson("");
		}
	}


	private void getRefer(){
		comman  = new Comman();
		context = getActivity();
		shared  = new SharedPreferanceData(getActivity());
		if(!shared.getSharedValue("mLASTTIMESTAMP").toString().equalsIgnoreCase("N/A"))
			timeStamp = shared.getSharedValue("mLASTTIMESTAMP").toString().trim();
		dbGroup = new GroupDb(getActivity());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (resultCode == 1) { // test complete
				if (comman.isNetworkAvailable(context)) {
					if(shared.getSharedValue("mLASTTIMESTAMP").toString().equalsIgnoreCase("N/A")){
						if (comman.isNetworkAvailable(context)) {
							//createJson("");
							createJsonRefresh(timeStamp);
						}else{
							onLoad();
						}

					}else{
						if (comman.isNetworkAvailable(context)) {
							createJsonRefresh(shared.getSharedValue("mLASTTIMESTAMP").toString());
						}else{
							onLoad();
						}
					}
				}
			} else { // test uncomplete

			}
		}
	}



	private void setOnClick(){

		btnCreate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), AddNewGroup.class);
				startActivityForResult(i, 100);
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				//Toast.makeText(context, "position  "+i, Toast.LENGTH_SHORT).show();
				GroupBean gb = tbList.get(i-1);
				String gAccess = gb.getmGroupAccessType();
				String gName = gb.getmGroupName();
				Vars.gUID = gb.getmGroupId();
				String gDesc = gb.getmGroupDesc();
				String gTag = gb.getmGroupTag();
				Vars.gAdmin = gb.getmGroupAdmin();
				Vars.gPhoto = gb.getmPhoto();
				Vars.gMCount = gb.getmGroupSize();
				Vars.gMadeByNumber = gb.getmGroupMadeByNum();
				if(gAccess.equalsIgnoreCase("1")){
					Intent intent = new Intent(getActivity(), GroupView.class);
					intent.putExtra("gName", gName);
					intent.putExtra("gDesc", gDesc);
					intent.putExtra("gTag", gTag);
					shared.saveSharedData("mGroupCount" + Vars.gUID,String.valueOf(Vars.gMCount));
					startActivity(intent);
				}

			}
		});

	}

	boolean webserviceState = false;
	private boolean createJson(String timestamp) {

		JSONObject json = new JSONObject();
		try {
			json.put("PUID", shared.getSharedValue("UserID"));
			json.put("PGROUPID", 0);
			json.put("PTIMESTAMP", timestamp);
			json.put("PPAGENUMBER", pageInitial);
			json.put("PRECORDCOUNT", pageData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			//Log.e("dadad", "---JSON---" + json.toString());
			webserviceState = true;
			callWebServiceProcess(json, Vars.webMethodName.get(7));

		} else {
			noInternet("No internet connection.", "");
			webserviceState = false;
		}

		return true;
	}

	private boolean createJsonRefresh(String timestamp) {

		JSONObject json = new JSONObject();
		try {
			json.put("PUID", shared.getSharedValue("UserID"));
			json.put("PGROUPID", 0);
			json.put("PTIMESTAMP", timestamp);
			json.put("PPAGENUMBER", "1");
			json.put("PRECORDCOUNT", "10");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (comman.isNetworkAvailable(context)) {
			//Log.e("dadad", "---JSON 1111---" + json.toString());
			webserviceState = true;
			callWebServiceProcess(json, "Refreshdata");

		} else {
			webserviceState = false;
			noInternet("No internet connection.", "");
		}

		return true;
	}

	private void callWebServiceProcess(JSONObject jbObject, String process) {
		String jsonData = jbObject.toString();
		new Webservice(this, process).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(7));
		if(!loadmore)
			showProgress();
	}


	@Override
	public void onWebCompleteResult(String result, String processName) {
		// TODO Auto-generated method stub
		hideProgress();
		//Log.e("ss", "ssss" + result);
		if (result == null) {
			if(tbList.size() == 0) {
				offServerResponse();
			}else{
				// show nointernet message
			}
			return;
		}
		if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
			if(tbList.size() == 0) {
				offServerResponse();
			}else{
				// show nointernet message
			}
			return;
		}
		if(processName.equals(Vars.webMethodName.get(7))){

			parseJson(result);
			webserviceState = false;
		}else if(processName.equals("Refreshdata")){
			parseJsonRefresh(result);
		}
	}

	//[{"MADEBYPHONENO":"8475068367","MADEBYNAME":"rakeshsharma"
	//,"GROUPID":"1","TAGNAME":"mygroup","DESCRITION":"hdjdjdjdjjj"
	//	,"GROUPPHOTO":"","MEMBERCOUNT":"2","ADMINFLAG":"0"}]
	private void parseJsonRefresh(String result){
		ArrayList<GroupBean> tempTbList = new ArrayList<GroupBean>();
		try {
			ArrayList<String> gdata = comman.splitString(result, "\\~");
			shared.saveSharedData("mGroupCount", gdata.get(0).trim());
		//	pageFinal = getPageCount(Integer.parseInt(gdata.get(0).trim()));
			JSONArray jArray = new JSONArray(gdata.get(1));
			if(jArray.length()==0){
				onLoad();
				return;
			}
			GroupBean gb = null;
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json = jArray.getJSONObject(i);
				gb = new GroupBean();
				gb.setmGroupMadeByName(json.getString("MADEBYNAME"));
				gb.setmGroupMadeByNum(json.getString("MADEBYPHONENO"));
				gb.setmGroupName(json.getString("GROUPNAME"));
				gb.setmGroupId(json.getString("GROUPID"));
				gb.setmGroupTag(json.getString("TAGNAME"));
				gb.setmGroupDesc(json.getString("DESCRITION"));
				gb.setmPhoto(json.getString("GROUPPHOTO"));
				gb.setmGroupSize(Integer.parseInt(json.getString("MEMBERCOUNT")));
				gb.setmGroupAdmin(json.getString("ADMINFLAG"));
				gb.setmGroupAccessType(json.getString("GROUPACCESSTYPE"));
				gb.setmGroupCreatedDate(json.getString("CREATEUPDATETS"));
				//Log.e("Date, ===", "-----------Date-----" + gb.getmGroupCreatedDate());
				if(timeStamp.equals("")){
					timeStamp =gb.getmGroupCreatedDate(); // first
				}else{
					timeStamp = comman.getLargerTimeStamp(timeStamp, gb.getmGroupCreatedDate());
				}
				shared.saveSharedData("mLASTTIMESTAMP", timeStamp);
				//dbGroup.addGroup(gb); // Save Group to db
				dbGroup.checkGroupExist(Integer.parseInt(gb.getmGroupId().trim()), gb);
				tempTbList.add(gb);
				if(tbList.size()>0){
					checkForExist(gb);
				}

			}

			tbList.addAll(tempTbList);
			adapter.notifyDataSetChanged();
			onLoad();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Errror", "error" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void checkForExist(GroupBean gb){

		ArrayList<GroupBean> clon = (ArrayList<GroupBean>)tbList.clone();

		int count = 0;
		for(GroupBean g : clon) {
			if (g.getmGroupId().equals(gb.getmGroupId())) {
				tbList.remove(count);
			}
			++ count;
		}

		try{
			adapter.notifyDataSetChanged();
		}catch(Exception e){
			Log.e("Errror", "error"+e.getMessage());
			e.printStackTrace();
		}

	}

	//[{"MADEBYPHONENO":"8475068367","MADEBYNAME":"rakeshsharma"
	//,"GROUPID":"1","TAGNAME":"mygroup","DESCRITION":"hdjdjdjdjjj"
	//	,"GROUPPHOTO":"","MEMBERCOUNT":"2","ADMINFLAG":"0"}]
	ArrayList<GroupBean> tbList = new ArrayList<GroupBean>();
	String timeStamp = "";
	private void parseJson(String result){
		ArrayList<GroupBean> tempTbList = new ArrayList<GroupBean>();
		try {
			ArrayList<String> gdata = comman.splitString(result, "\\~");
			shared.saveSharedData("mGroupCount", gdata.get(0).trim());
			if(Integer.parseInt(gdata.get(0).trim())<10){
				onLoadCheck = true;
			}
			pageFinal = getPageCount(Integer.parseInt(gdata.get(0).trim()));
			JSONArray jArray = new JSONArray(gdata.get(1));
			if(jArray.length()==0){
				if(pageInitial==1){
					if(tbList.size()>0)
						tbList.clear();
					noRecordFound();
					return;
				}else{
					listView.removeLoad();
				}


			}
			GroupBean gb = null;
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json = jArray.getJSONObject(i);
				gb = new GroupBean();
				gb.setmGroupMadeByName(json.getString("MADEBYNAME"));
				gb.setmGroupMadeByNum(json.getString("MADEBYPHONENO"));
				gb.setmGroupName(json.getString("GROUPNAME"));
				gb.setmGroupId(json.getString("GROUPID"));
				gb.setmGroupTag(json.getString("TAGNAME"));
				gb.setmGroupDesc(json.getString("DESCRITION"));
				gb.setmPhoto(json.getString("GROUPPHOTO"));
				gb.setmGroupSize(Integer.parseInt(json.getString("MEMBERCOUNT")));
				gb.setmGroupAdmin(json.getString("ADMINFLAG"));
				gb.setmGroupAccessType(json.getString("GROUPACCESSTYPE"));
				gb.setmGroupCreatedDate(json.getString("CREATEUPDATETS"));
				//Log.e("Date, ===", "-----------Date-----" + gb.getmGroupCreatedDate());
				if(timeStamp.equals("")){
					timeStamp =gb.getmGroupCreatedDate(); // first
				}else{
					timeStamp = comman.getLargerTimeStamp(timeStamp, gb.getmGroupCreatedDate());
				}
				shared.saveSharedData("mLASTTIMESTAMP", timeStamp);
				//dbGroup.addGroup(gb); // Save Group to db
				if(dbGroup.checkGroupExist(Integer.parseInt(gb.getmGroupId().trim()), gb)!=0);
					tempTbList.add(gb);
			}
			tbList.addAll(tempTbList);
			if(loadmore){
				adapter.notifyDataSetChanged();
				scrollMyListViewToBottom();
				onLoad();
				loadmore = false;
				return;
			}
			setAdapter();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Errror", "error"+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Vars.onAdd){
			//createJsonRefresh(shared.getSharedValue("mLASTTIMESTAMP").toString());
			if(shared.getSharedValue("mLASTTIMESTAMP").toString().equalsIgnoreCase("N/A")){
				if (comman.isNetworkAvailable(context)) {
					createJsonRefresh(timeStamp);

				}else{
					onLoad();
				}

			}else{
				if (comman.isNetworkAvailable(context)) {
					createJsonRefresh(shared.getSharedValue("mLASTTIMESTAMP").toString());
				}else{
					onLoad();
				}
			}
			Vars.onAdd= false;
		}
	}

	PrivateDirectoryAdapter adapter =null;
	private void setAdapter(){
		if(tbList.size()==0){
			noRecordFound();
			return;
		}
		recordFound();

		adapter = new PrivateDirectoryAdapter(getActivity(), R.layout.fragment_group, tbList);

        listView.setAdapter(adapter);
		if(onLoadCheck){
			listView.removeLoad();
			listView.deferNotifyDataSetChanged();
			adapter.notifyDataSetChanged();
		}
	}

	private void noInternet(String title, String btntext){
		new SnackBar(getActivity(), title, btntext, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		}).show();
	}

	int saveIndexPos=0;
	int saveTopPos = 0;
	private void saveListViewIndexPosition(){
		saveIndexPos = listView.getLastVisiblePosition();
		View v  = listView.getChildAt(0);
		saveTopPos = (v==null)?0:v.getTop();
	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("Just now");
		isServiceRunning = false;
	}

	@Override
	public void onRefresh() {
		loadmore = true;
		if(isServiceRunning)
			return;
		isServiceRunning = true;
		if(shared.getSharedValue("mLASTTIMESTAMP").toString().equalsIgnoreCase("N/A")){
			if (comman.isNetworkAvailable(context)) {
				createJsonRefresh(timeStamp);
			}else{
				onLoad();
			}

		}else{
			if (comman.isNetworkAvailable(context)) {
				createJsonRefresh(shared.getSharedValue("mLASTTIMESTAMP").toString());
			}else{
				onLoad();
			}
		}
	}

	boolean loadmore = false;
	boolean isServiceRunning = false;
	boolean flag = false;

	@Override
	public void onLoadMore() {
		if (isServiceRunning)
			return;
		isServiceRunning = true;
		loadmore = true;
		if (pageFinal > pageInitial) {
			++pageInitial;
			saveListViewIndexPosition();
			if (mGroupCount > tbList.size()) {
				int skipvalue = (pageInitial - 1) * 10;
				ArrayList<GroupBean> gbLimit = dbGroup.getGroupLimit(skipvalue, 10);
				tbList.addAll(gbLimit);
				adapter.notifyDataSetChanged();
				scrollMyListViewToBottom();
				onLoad();
				loadmore = false;
			} else {
				if (comman.isNetworkAvailable(context)) {
					createJson("");
				} else {
					listView.removeLoad();
					onLoad();
				}
			}
		}else{
			if (flag == true) {
				return;
			} else {
				if (!shared.getSharedValue("mGroupCount").equalsIgnoreCase("N/A")) {
					if (Integer.parseInt(shared.getSharedValue("mGroupCount")) > tbList.size()) {
						++pageInitial;
						if (comman.isNetworkAvailable(context)) {
							createJson("");
						} else {
							listView.removeLoad();
							onLoad();
						}
					} else {
						isServiceRunning = false;
						listView.removeLoad();
						onLoad();
					}
				}
			}
		}
		onLoad();
	}


	private void scrollMyListViewToBottom() {
		listView.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				listView.setSelection(adapter.getCount() - 1);
			}
		});
	}

	private int getPageCount(int size){
		int page = 0;
		if(size>10){
			int pageCount = size%10;
			if(pageCount!=0){
				page = 1+(size/10);
			}else{

				page = (size/10);
			}
		}
		return page;
	}

}
