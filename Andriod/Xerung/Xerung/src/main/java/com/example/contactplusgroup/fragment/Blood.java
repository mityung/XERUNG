package com.example.contactplusgroup.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.contactplusgroup.adapter.BloodGroupAdapter;
import com.example.contactplusgroup.adapter.CustomListAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.mityung.contactdirectory.R;

import java.util.ArrayList;

/**
 * Created by mipanther on 11/3/16.
 */
public class Blood extends Fragment {

    private Comman comman = null;
    private SharedPreferanceData shared = null;
    private Context context;
    private ListView listview, listview1;
    private LinearLayout layMain;
    private TextView txtnorecord;
    @SuppressWarnings("unused")
    private int position;
    private static final String ARG_POSITION = "position";
    private static int SPLASH_TIME_OUT = 1000;
    private GroupDb dbContact = null;
    private RelativeLayout relProgress;
    /**
     * Static instance of the Profile
     * @param position
     * @return
     */
    public static Blood newInstance(int position) {
        Blood f = new Blood();
        Bundle b      = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position      = getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.fragment_blood, container, false);
        findViewids(rootView);
        initialise();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(tb.size() >0)
            return;
        tb  = dbContact.getBloodGroup("MG" + Vars.gUID);
        if(tb.size()==0){
            showProgress();
            new Handler().postDelayed(new Runnable() { /*Showing splash screen with a timer. This will be useful when you want to show case your app logo / company*/
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    hideProgress();
                    tb  = dbContact.getBloodGroup("MG" + Vars.gUID);
                    if(tb.size()==0){
                        noRecord();
                        return;
                    }else{
                        bloodAdapter = new BloodGroupAdapter(getActivity(), R.layout.fragment_blood, tb);
                        listview.setAdapter(bloodAdapter);
                        showBloodList();
                        return;
                    }

                }
            }, SPLASH_TIME_OUT);

        }
        bloodAdapter = new BloodGroupAdapter(getActivity(), R.layout.fragment_blood, tb);
        listview.setAdapter(bloodAdapter);
        showBloodList();
    }

    BloodGroupAdapter bloodAdapter = null;
    ArrayList<ContactBean> tb = new ArrayList<ContactBean>();
    private void initialise(){
        getRefer();
        setOnClick();
    }

    private void showBloodList(){
        listview.setVisibility(View.VISIBLE);
        listview1.setVisibility(View.GONE);

    }

    private void hideBloodList(){
        listview.setVisibility(View.GONE);
        listview1.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }


    View view = null;

    private void findViewids(View view) {
        this.view = view;
        listview = (ListView)view.findViewById(R.id.lvList);
        listview1 = (ListView)view.findViewById(R.id.lvList1);
        layMain = (LinearLayout)view.findViewById(R.id.layMain);
        txtnorecord = (TextView)view.findViewById(R.id.txtnorecord);
        relProgress = (RelativeLayout)view.findViewById(R.id.layProgressresult);
    }

    private void noRecord(){
        txtnorecord.setVisibility(View.VISIBLE);
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

    CustomListAdapter adapter = null;
    ArrayList<ContactBean> gMemberList = new ArrayList<ContactBean>();
    boolean hasheader = false;
    private void setAdapter(String blood){
        if(gMemberList.size()==0){
            //hideBloodList();
            return;
        }
        listview1 = (ListView)view.findViewById(R.id.lvList1);
        hideBloodList();
        LayoutInflater l = getActivity().getLayoutInflater();
        ViewGroup v = (ViewGroup)l.inflate(R.layout.listheader, listview1, false);
        TextView txthead = (TextView) v.findViewById(R.id.txtAppName);
        txthead.setText("Back");
        RelativeLayout layback = (RelativeLayout) v.findViewById(R.id.layBack);
        layback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showBloodList();
            }
        });
        if(hasheader == false)
        listview1.addHeaderView(v);
        hasheader = true;
        adapter = new CustomListAdapter(getActivity(), R.layout.fragment_blood, gMemberList);

        listview1.setAdapter(adapter);
    }


    private void getRefer(){

        dbContact = new GroupDb(getActivity());
        comman    = new Comman();
        context   = getActivity();

    }

    private void setOnClick(){

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ContactBean cb  = tb.get(i);
                        String mBlood = cb.getName();
                        Log.e("FAFAF", "DADADA"+mBlood);
                        gMemberList = dbContact.getBloodGroupMember("MG" + Vars.gUID ,mBlood);
                        setAdapter(mBlood);

            }
        });
    }


}

