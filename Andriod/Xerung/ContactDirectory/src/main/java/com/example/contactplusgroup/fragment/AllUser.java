package com.example.contactplusgroup.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.contactplusgroup.adapter.CustomListAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.views.XListView;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.mityung.contactdirectory.GroupUserProfile;
import com.mityung.contactdirectory.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mipanther on 11/3/16.
 */
public class AllUser extends Fragment implements WebServiceInterface, XListView.IXListViewListener{

    private RelativeLayout relProgress, relInternet, relNetwork;
    private TextView txtInternet, txtnetwork;
    private Comman comman = null;
    private SharedPreferanceData shared = null;
    Context context;
    private XListView listview;
    private LinearLayout layMain;
    int pageInitial = 1;
    int pageFinal = 0;
    int pageData =50;
    boolean onLoadCheck = false;

    @SuppressWarnings("unused")
    private int position;
    private static final String ARG_POSITION = "position";
    GroupDb dbContact = null;
    /**
     * Static instance of the Profile
     * @param position
     * @return
     */
    public static AllUser newInstance(int position) {
        AllUser f = new AllUser();
        Bundle b      = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position      = getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.fragment_alluser, container,false);
        findViewids(rootView);
        initialise();
        return rootView;
    }

    int mGroupCount = 0;
    private void initialise(){
        getRefer();
        setOnClick();
        createJson("");
        mGroupCount = (int)dbContact.countMemberTest("MG" + Vars.gUID);
        if(mGroupCount>0){
            if(mGroupCount>50){
                pageFinal = getPageCount(Vars.gMCount);

            }else{
                onLoadCheck = true;
            }
            int skipvalue = (pageInitial-1)*50;
            ArrayList<ContactBean> gbLimit = dbContact.getGroupMemberLimit(skipvalue, 50, "MG" + Vars.gUID);
            gMemberList.addAll(gbLimit);
            setAdapter();
        }else{
            createJson("");
        }

    }

    private void findViewids(View view) {
        listview = (XListView)view.findViewById(R.id.lvList);
        listview.setPullLoadEnable(true);
        listview.setXListViewListener(this);
        relProgress = (RelativeLayout)view.findViewById(R.id.layProgressresult);
        relInternet = (RelativeLayout)view.findViewById(R.id.layinternetresult);
        relNetwork = (RelativeLayout)view.findViewById(R.id.layNoresultserver);
        txtInternet = (TextView)view.findViewById(R.id.txtnoinertnettestresult);
        txtInternet.setTypeface(ManagerTypeface.getTypeface(getActivity(), R.string.typeface_roboto_regular));
        txtnetwork = (TextView)view.findViewById(R.id.txtNoresultnetwork);
        txtnetwork.setTypeface(ManagerTypeface.getTypeface(getActivity(), R.string.typeface_roboto_regular));
        layMain = (LinearLayout)view.findViewById(R.id.layMain);

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
        //layMain.setVisibility(View.GONE);

    }

    private void onNetwork() {

        relInternet.setVisibility(View.GONE);
        layMain.setVisibility(View.VISIBLE);
    }



    private void getRefer(){

        dbContact = new GroupDb(getActivity());
        comman    = new Comman();
        shared    = new SharedPreferanceData(getActivity());
        if(shared.getSharedValue("MG"+Vars.gUID).trim().equals("N/A")){
            //Log.e("Table", "TableCreated");
            shared.saveSharedData("MG"+Vars.gUID, "yes");
            dbContact.createMemberTable("MG"+Vars.gUID);
        }else{
            //Log.e("Table", "TableCreated Not Successs");
        }
        if (!shared.getSharedValue("mGroupCount" + Vars.gUID).equalsIgnoreCase("N/A")) {
            if(Integer.parseInt(shared.getSharedValue("mGroupCount" + Vars.gUID))<50){
                onLoadCheck = true;
            }
        }
        context   = getActivity();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(Vars.onUpadte){
            if(shared.getSharedValue("mLASTTIMESTAMP"+Vars.gUID).toString().equalsIgnoreCase("N/A")){
                if (comman.isNetworkAvailable(context)) {
                    createJsonRefresh(timeStamp);
                }else{
                    onLoad();
                }
            }else{
                if (comman.isNetworkAvailable(context)) {
                    createJsonRefresh(shared.getSharedValue("mLASTTIMESTAMP"+Vars.gUID).toString());
                }else{
                    onLoad();
                }
            }
            Vars.onUpadte= false;
        }
    }

    public static int gUPostion =0;
    public static String gUNumber = null;
    private void setOnClick(){

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gUPostion = i-1;
                ContactBean cb = gMemberList.get(i-1);
                if (cb.getUID().trim().equals("0")) {
                    comman.alertDialog(getActivity(), "Pending Request", cb.getName() + " has not accepted yet.");
                    return;
                }
                gUNumber = cb.getNumber();
                Intent intent = new Intent(getActivity(), GroupUserProfile.class);
                intent.putExtra("Name", cb.getName());
                intent.putExtra("OrignalName", cb.getOrignalName());
                intent.putExtra("MyPhoneBook", cb.getMyPhoneBookName());
                intent.putExtra("Number", cb.getNumber());
                intent.putExtra("Uid", cb.getUID());
                intent.putExtra("UserType", cb.getRequestFlag());
                intent.putExtra("TableName", "MG" + Vars.gUID);
                intent.putExtra("Index", "" + i);
                intent.putExtra("IsMyContact", cb.getIsMyContact());
                intent.putExtra("AdminFlag", cb.getAdminFlag());
                startActivityForResult(intent, 10001);
            }
        });
    }

    boolean webserviceState = false;
    private boolean createJson(String timestamp) {
        JSONObject json = new JSONObject();
        try {
            json.put("PUID", shared.getSharedValue("UserID"));
            json.put("PGROUPID", Vars.gUID);
            json.put("PTIMESTAMP", timestamp);
            json.put("PPAGENUMBER", pageInitial);
            json.put("PRECORDCOUNT", pageData);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        if (comman.isNetworkAvailable(context)) {
            callWebServiceProcess(json, Vars.webMethodName.get(7));
            webserviceState = true;
            onNetwork();
        } else {
            offNetwork();
            webserviceState = false;
        }

        return true;
    }

    private boolean createJsonRefresh(String timestamp) {

        JSONObject json = new JSONObject();
        try {
            json.put("PUID", shared.getSharedValue("UserID"));
            json.put("PGROUPID", Vars.gUID);
            json.put("PTIMESTAMP", timestamp);
            json.put("PPAGENUMBER", pageInitial);
            json.put("PRECORDCOUNT", pageData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        if (comman.isNetworkAvailable(context)) {
            webserviceState = true;
            callWebServiceProcess(json, "Refreshdata");

        } else {
            webserviceState = false;
            offNetwork();
        }

        return true;
    }

    private void callWebServiceProcess(JSONObject jbObject, String process) {
        String jsonData = jbObject.toString();
        //Log.e("sedd ","send json"+jsonData);
        new Webservice(this, process).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(7));
        if(!loadmore)
            showProgress();
    }


    @Override
    public void onWebCompleteResult(String result, String processName) {
        // TODO Auto-generated method stub
        hideProgress();
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

        if(processName.equals(Vars.webMethodName.get(7))){
            parseJson(result);

        }else if(processName.equals("Refreshdata")){
            parseJsonRefresh(result);
        }
    }

    //String timeStamp = "";
    private void parseJson(String result){
        ArrayList<ContactBean> cbList = new ArrayList<ContactBean>();
        try {
            JSONArray jArray = new JSONArray(result);
            if(jArray.length()==0){
                if(pageInitial==1){
                    if(gMemberList.size()>0)
                        gMemberList.clear();
                    onLoad();
                    // noRecordFound();
                    return;
                }else {
                    flag=true;
                    listview.removeLoad();
                }

            }
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                String members = json.getString("MEMBER").trim();

                pageFinal = getPageCount(Vars.gMCount);
                ArrayList<String> ar = comman.splitString(members, "\\#");
                for(String a :ar){
                    ContactBean cb = new ContactBean();
                    try {
                        ArrayList<String> data = comman.splitString(a, "\\|");
                        cb.setNumber(data.get(1).trim());
                        // Name Logic
                        final String name = dbContact.getContactName(cb.getNumber());
                        cb.setOrignalName(data.get(0).trim());
                        if(name == null){
                            cb.setIsMyContact(1);
                            cb.setName(data.get(0).trim());
                            cb.setMyPhoneBookName(cb.getNumber().trim());
                        }else if(name.trim().length() == 0){
                            cb.setIsMyContact(1);
                            cb.setName(comman.upperCaseAllFirst(data.get(0).trim()));
                            cb.setMyPhoneBookName(cb.getNumber().trim());
                        }else{
                            cb.setIsMyContact(1);
                            cb.setName(name.trim());
                            cb.setMyPhoneBookName(name.trim());
                        }

                        cb.setRequestFlag(data.get(2).trim());
                        cb.setSearchKey(data.get(3).trim());
                        if(shared.getSharedValue("UserID").equals(data.get(4).trim())){
                            cb.setMyPhoneBookName("You");
                            cb.setName("You");
                            cb.setIsMyContact(0);
                        }
                        cb.setUID(data.get(4).trim());
                        cb.setAdminFlag(Vars.gAdmin);
                        if(data.size() > 5){
                            if(!data.get(5).trim().equalsIgnoreCase(""))
                                cb.setmBloodGroup(data.get(5).trim());
                            else cb.setmBloodGroup("0");

                        }

                        else cb.setmBloodGroup("0");

                        if(data.size() > 6)
                        {
                            if(!data.get(6).trim().equalsIgnoreCase(""))
                                cb.setCity(data.get(6).trim());
                            else cb.setCity("0");

                        }

                        else cb.setCity("0");

                        if(data.size() > 7)
                        {
                            if(!data.get(7).trim().equalsIgnoreCase(""))
                                cb.setProfession(comman.upperCaseAllFirst(data.get(7).trim()));
                            else cb.setProfession("0");

                        }
                        else cb.setProfession("0");
                        if(data.size() > 8)
                            cb.setmCreatedDate(data.get(8).trim());

                        //Log.e("Date, ===", "-----------Date-----" + cb.getmCreatedDate());
                        if(timeStamp.equals("")){
                            timeStamp =cb.getmCreatedDate(); // first
                        }else{
                            timeStamp = comman.getLargerTimeStamp(timeStamp, cb.getmCreatedDate());
                        }
                        shared.saveSharedData("mLASTTIMESTAMP" + Vars.gUID, timeStamp);
                        dbContact.checkGroupMemberExist(Integer.parseInt(cb.getUID()), cb, "MG" + Vars.gUID);
                        cbList.add(cb);
                        checkForExist(cb);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }


                gMemberList.addAll(cbList);
                if(loadmore){
                    adapter.notifyDataSetChanged();
                    scrollMyListViewToBottom();
                    onLoad();
                    loadmore = false;
                    return;
                }
                setAdapter();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    String timeStamp = "";
    private void parseJsonRefresh(String result){
        ArrayList<ContactBean> cbList = new ArrayList<ContactBean>();
        try {
            JSONArray jArray = new JSONArray(result);
            if(jArray.length()==0){
                onLoad();
                return;
            }
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                String members = json.getString("MEMBER").trim();
                ArrayList<String> ar = comman.splitString(members, "\\#");
                for(String a :ar){
                    ContactBean cb = new ContactBean();
                    try {
                        ArrayList<String> data = comman.splitString(a, "\\|");
                        cb.setNumber(data.get(1).trim());
                        // Name Logic
                        final String name = dbContact.getContactName(cb.getNumber());
                        cb.setOrignalName(data.get(0).trim());
                        if(name == null){
                            cb.setIsMyContact(1);
                            cb.setName(data.get(0).trim());
                            cb.setMyPhoneBookName(cb.getNumber().trim());
                        }else if(name.trim().length() == 0){
                            cb.setIsMyContact(1);
                            cb.setName(comman.upperCaseAllFirst(data.get(0).trim()));
                            cb.setMyPhoneBookName(cb.getNumber().trim());
                        }else{
                            cb.setIsMyContact(1);
                            cb.setName(name.trim());
                            cb.setMyPhoneBookName(name.trim());
                        }

                        cb.setRequestFlag(data.get(2).trim());
                        cb.setSearchKey(data.get(3).trim());
                        if(shared.getSharedValue("UserID").equals(data.get(4).trim())){
                            cb.setMyPhoneBookName("You");
                            cb.setName("You");
                            cb.setIsMyContact(0);
                        }
                        cb.setUID(data.get(4).trim());
                        cb.setAdminFlag(Vars.gAdmin);
                        if(data.size() > 5){
                            if(!data.get(5).trim().equalsIgnoreCase(""))
                                cb.setmBloodGroup(data.get(5).trim());
                            else cb.setmBloodGroup("0");

                        }

                        else cb.setmBloodGroup("0");

                        if(data.size() > 6)
                        {
                            if(!data.get(6).trim().equalsIgnoreCase(""))
                                cb.setCity(data.get(6).trim());
                            else cb.setCity("0");

                        }

                        else cb.setCity("0");

                        if(data.size() > 7)
                        {
                            if(!data.get(7).trim().equalsIgnoreCase(""))
                                cb.setProfession(comman.upperCaseAllFirst(data.get(7).trim()));
                            else cb.setProfession("0");

                        }
                        else
                            cb.setProfession("0");
                        if(data.size() > 8)
                            cb.setmCreatedDate(data.get(8).trim());

                        //Log.e("Date, ===", "-----------Date-----" + cb.getmCreatedDate());
                        if(timeStamp.equals("")){
                            timeStamp =cb.getmCreatedDate(); // first
                        }else{
                            timeStamp = comman.getLargerTimeStamp(timeStamp, cb.getmCreatedDate());
                        }
                        shared.saveSharedData("mLASTTIMESTAMP" + Vars.gUID, timeStamp);
                        //dbContact.addMember(cb, "MG" + Vars.gUID);
                        dbContact.checkGroupMemberExist(Integer.parseInt(cb.getUID()), cb, "MG" + Vars.gUID);
                        cbList.add(cb);
                        checkForExist(cb);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

                gMemberList.addAll(cbList);
                adapter.notifyDataSetChanged();
                onLoad();
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Errror", "error" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkForExist(ContactBean gb){

        ArrayList<ContactBean> clone = (ArrayList<ContactBean>)gMemberList.clone();
        int count = 0;
        for(ContactBean g : clone) {
            if (g.getUID().equals(gb.getUID())) {
                gMemberList.remove(count);
            }
            ++ count;
        }
        adapter.notifyDataSetChanged();

    }

    ArrayList<ContactBean> gMemberList = new ArrayList<ContactBean>();

    CustomListAdapter adapter = null;
    private void setAdapter(){
        if(gMemberList.size()==0){
            return;
        }
        Collections.sort(gMemberList, new Comparator<ContactBean>(){
            public int compare(ContactBean p1, ContactBean p2) {
                return p1.getRequestFlag().compareTo(p2.getRequestFlag());
            }
        });
        adapter = new CustomListAdapter(getActivity(), R.layout.fragment_alluser, gMemberList);
        listview.setAdapter(adapter);
        if(onLoadCheck){
            listview.removeLoad();
            listview.deferNotifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10001){
            if(resultCode == 1){
                if(shared.getSharedValue("mLASTTIMESTAMP"+Vars.gUID).toString().equalsIgnoreCase("N/A")){
                    if (comman.isNetworkAvailable(context)) {
                        createJsonRefresh(timeStamp);
                    }else{
                        onLoad();
                    }
                }else{
                    if (comman.isNetworkAvailable(context)) {
                        createJsonRefresh(shared.getSharedValue("mLASTTIMESTAMP"+Vars.gUID).toString());
                    }else{
                        onLoad();
                    }
                }
            }if(resultCode == 2){
                delUploadTemp(gUPostion, gUNumber);
            }
        }
    }

    int saveIndexPos=0;
    int saveTopPos = 0;
    private void saveListViewIndexPosition(){
        saveIndexPos = listview.getLastVisiblePosition();
        View v  = listview.getChildAt(0);
        saveTopPos = (v==null)?0:v.getTop();
    }

    private void onLoad() {
        listview.stopRefresh();
        listview.stopLoadMore();
        listview.setRefreshTime("Just now");
        isServiceRunning = false;
    }

    @Override
    public void onRefresh() {
        loadmore = true;
        if(isServiceRunning)
            return;
        isServiceRunning = true;
        if(shared.getSharedValue("mLASTTIMESTAMP"+Vars.gUID).toString().equalsIgnoreCase("N/A")){
            if (comman.isNetworkAvailable(context)) {
                createJsonRefresh(timeStamp);
            }else{
                onLoad();
            }
        }else{
            if (comman.isNetworkAvailable(context)) {
                createJsonRefresh(shared.getSharedValue("mLASTTIMESTAMP"+Vars.gUID).toString());
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

        if(isServiceRunning)
            return;
        isServiceRunning = true;
        loadmore = true;
        if(pageFinal>=pageInitial){
            ++pageInitial;
            saveListViewIndexPosition();
            if(mGroupCount>gMemberList.size()){
                int skipvalue = (pageInitial-1)*50;
                ArrayList<ContactBean> gbLimit = dbContact.getGroupMemberLimit(skipvalue, 50, "MG" + Vars.gUID);
                gMemberList.addAll(gbLimit);
                adapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
                onLoad();
                loadmore = false;
            }else{
                if (comman.isNetworkAvailable(context)) {
                    createJson("");
                }else{
                    listview.removeLoad();
                    onLoad();
                }
            }
        }else{
            if (flag == true) {
                return;
            } else {
                if (!shared.getSharedValue("mGroupCount" + Vars.gUID).equalsIgnoreCase("N/A")) {
                    if (Integer.parseInt(shared.getSharedValue("mGroupCount" + Vars.gUID)) > gMemberList.size()) {
                        ++pageInitial;
                        if (comman.isNetworkAvailable(context)) {
                            createJson("");
                        } else {
                            listview.removeLoad();
                            onLoad();
                        }
                    }else {
                        //Toast.makeText();
                        isServiceRunning = false;
                        onLoad();
                        listview.removeLoad();
                    }
                }
            }
        }
        onLoad();

    }

    private void scrollMyListViewToBottom() {
        listview.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listview.setSelection(adapter.getCount() - 1);
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

    private int deletedIndex = 0; // used to store lastdeleted location
    private void delUploadTemp(int index, final String mName){
        deletedIndex = index;
        showProgress();
        new AsyncTask<Integer, String, String>(){
            @Override
            protected String doInBackground(Integer... params) {
                // **Code**
                return dbContact.deleteMember(mName, "MG" + Vars.gUID);
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                //Log.e("SSSS", "SSSS"+result);
                if(deletedIndex > gMemberList.size()){
                    return;
                }
                gMemberList.remove(deletedIndex);
                adapter.notifyDataSetChanged();
                listview.invalidateViews();
                hideProgress();

            }
        }.execute(1);


    }

}
