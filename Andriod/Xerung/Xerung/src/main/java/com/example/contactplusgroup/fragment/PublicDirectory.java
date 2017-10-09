package com.example.contactplusgroup.fragment;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.contactplusgroup.adapter.GroupAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.GroupBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.webservice.Webservice.WebServiceInterface;
import com.mityung.contactdirectory.R;
import com.mityung.contactdirectory.SearchListView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * Created by mipanther on 29/3/16.
 */
public class PublicDirectory extends Fragment implements WebServiceInterface{

    @SuppressWarnings("unused")
    private int position;
    // Contact List
    ListView listView;
    // Cursor to load contacts list
    private LinearLayout layMain;
    private RelativeLayout relProgress, relInternet, relNetwork;
    private TextView txtInternet, txtInternetNote, txtnetwork;
    private SharedPreferanceData shared = null;
    private Context context;
    private Comman comman = null;
    /**
     * Static instance of the Profile
     * @return
     */
    public static PublicDirectory newInstance() {
        PublicDirectory f = new PublicDirectory();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_public_directory, container,false);
        findViewids(rootView);
        initialise();
        return rootView;
    }

    private void findViewids(View view) {
        listView = (ListView) view.findViewById(R.id.lvPublicGroupList);
        relProgress = (RelativeLayout)view.findViewById(R.id.layProgressresult);
        relNetwork = (RelativeLayout)view.findViewById(R.id.layNoresultserver);
        txtnetwork = (TextView)view.findViewById(R.id.txtNoresultnetwork);
        txtnetwork.setTypeface(ManagerTypeface.getTypeface(getActivity(), R.string.typeface_roboto_regular));
        layMain = (LinearLayout)view.findViewById(R.id.laymain);
        txtInternet = (TextView)view.findViewById(R.id.txtcantConnect);
        txtInternet.setTypeface(ManagerTypeface.getTypeface(getActivity(), R.string.typeface_roboto_regular));
        txtInternetNote = (TextView)view.findViewById(R.id.txtcantConnectNote);
        txtInternetNote.setTypeface(ManagerTypeface.getTypeface(getActivity(), R.string.typeface_roboto_regular));
        relInternet = (RelativeLayout)view.findViewById(R.id.layinternetresult);
    }

    private void initialise(){
        getRefer();
        setOnClick();
        createSearchJson();
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

    private void getRefer(){
        comman  = new Comman();
        context = getActivity();
    }

    private void setOnClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GroupBean gb = tbList.get(i);
                String gUId = gb.getmGroupId();
                String gAccess = gb.getmGroupAccessType();
                String gName = gb.getmGroupName();
                Intent iintent = new Intent(getActivity(), SearchListView.class);
                iintent.putExtra("gUID", gUId);
                iintent.putExtra("gAccess", gAccess);
                iintent.putExtra("gName", gName);
                startActivity(iintent);
            }
        });
    }

    private boolean createSearchJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("PGROUPID", 0);
            json.put("PSERACHTEXT", "");
            json.put("PGROUPACCESSTYPE", 2);

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
        showProgress();
        String jsonData = jbObject.toString();
        new Webservice(this, Vars.webMethodName.get(11)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(11));
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
        }
        if(processName.equals(Vars.webMethodName.get(11))){
            parseJsonSearch(result);
        }
    }

    //[{"MADEBYPHONENO":"8475068367","MADEBYNAME":"rakeshsharma"
    //,"GROUPID":"1","TAGNAME":"mygroup","DESCRITION":"hdjdjdjdjjj"
    //	,"GROUPPHOTO":"","MEMBERCOUNT":"2","ADMINFLAG":"0"}]
    ArrayList<GroupBean> tbList = new ArrayList<GroupBean>();
    private void parseJsonSearch(String result){
        try {
            JSONArray jArray = new JSONArray(result);
            GroupBean gb = null;
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                if(json.length()==0){
                    return;
                }
                gb = new GroupBean();
                gb.setmGroupName(json.getString("GROUPNAME"));
                gb.setmGroupId(json.getString("GROUPID"));
                gb.setmGroupTag(json.getString("TAGNAME"));
                gb.setmGroupDesc(json.getString("DESCRITION"));
                gb.setmPhoto(json.getString("GROUPPHOTO"));
                gb.setmGroupAdmin("2");
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

    private void setAdapter(){
        if(tbList.size()==0){
            return;
        }
        try {
            GroupAdapter adapter = new GroupAdapter(getActivity(), R.layout.fragment_public_directory, tbList);
            listView.setAdapter(adapter);
        }catch (Exception e){

        }
    }

}
