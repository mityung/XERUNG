package com.mityung.contactdirectory;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.contactplusgroup.adapter.FilterTabsAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.github.florent37.diagonallayout.DiagonalLayout;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class GroupView extends AppCompatActivity {

    private RelativeLayout mLayback;
    private TextView mTxtGroupName, mTxtGroupDesc, mTxtGrouptag, mTxtPending;
    private LinearLayout layedit;
    private ImageView imgAdd, imgSearch;
    private TabLayout tabLayout;
    private Comman comman = null;
    private SharedPreferanceData shared = null;
    Context context;
    private ImageView ivPhoto;
    private ViewPager pager;
    private FilterTabsAdapter pagerAdapter;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_directory_details);
        initialise();
    }

    private void initialise() {
        getRefer();
        findViewIds();
        getBundleData();
        setOnClick();
    }

    String mGroupName = "";
    String mGroupId = "";
    String mGroupDesc = "";
    String mGroupTag = "";
    String mGroupAdmin = "";
    String mGPhoto = "";

    private void getBundleData() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mGroupName = extras.getString("gName");
            mGroupId = Vars.gUID;
            mGroupDesc = extras.getString("gDesc");
            mGroupTag = extras.getString("gTag");
            mGroupAdmin = Vars.gAdmin;
            showAddMember(mGroupAdmin);
            mGPhoto = Vars.gPhoto;
            mTxtGroupName.setText(mGroupName);
            mTxtGroupDesc.setText(mGroupDesc);
            mTxtGrouptag.setText(mGroupTag);
            // and get whatever type user account id is
            setImage(mGPhoto);
        }
    }

    private void setImage(String img) {
        // Set image if exists
        try {
            if (img != null) {
                if (img.length() != 0) {
                    if (!img.equalsIgnoreCase("null")) {
                        ivPhoto.setImageResource(0);
                        ivPhoto.setImageBitmap(comman.decodeBase64(img));
                    } else {
                        ivPhoto.setImageBitmap(null);
                        ivPhoto.setImageResource(R.drawable.ic_group_square_white_64dp);
                    }
                } else {
                    ivPhoto.setImageBitmap(null);
                    ivPhoto.setImageResource(R.drawable.ic_group_square_white_64dp);
                }
            } else {
                ivPhoto.setImageResource(R.drawable.ic_group_square_white_64dp);
            }
            // Seting round image
        } catch (OutOfMemoryError e) {
            // Add default picture
            ivPhoto.setImageResource(R.drawable.ic_group_square_white_64dp);
            e.printStackTrace();
        }
    }

    private void showAddMember(String state) {
        if (state.equalsIgnoreCase("1")) {
            imgAdd.setVisibility(View.VISIBLE);
            mTxtPending.setVisibility(View.VISIBLE);
        } else {
            imgAdd.setVisibility(View.GONE);
            mTxtPending.setVisibility(View.GONE);
        }
    }

    private void getRefer() {
        res = this.getResources();
        comman = new Comman();
        shared = new SharedPreferanceData(this);
        context = this;

    }

    private void findViewIds() {
        mLayback = (RelativeLayout) findViewById(R.id.layBack);
        mTxtGroupName = (TextView) findViewById(R.id.txtGroupName);
        mTxtGroupName.setTypeface(ManagerTypeface.getTypeface(GroupView.this, R.string.typeface_android));
        mTxtGroupDesc = (TextView) findViewById(R.id.txtGroupdesc);
        mTxtGroupDesc.setTypeface(ManagerTypeface.getTypeface(GroupView.this, R.string.typeface_roboto_regular));
        mTxtGrouptag = (TextView) findViewById(R.id.txtGroupTag);
        mTxtGrouptag.setTypeface(ManagerTypeface.getTypeface(GroupView.this, R.string.typeface_roboto_regular));
        layedit = (LinearLayout) findViewById(R.id.diagonalLayout);
        imgAdd = (ImageView) findViewById(R.id.txtAddMemberGroup);
        imgSearch = (ImageView) findViewById(R.id.imgSearchMember);
        ivPhoto = (ImageView) findViewById(R.id.imgProfilePicSet);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pagerfilter);
        mTxtPending = (TextView) findViewById(R.id.txtPendingRequest);
        // init view pager
        pagerAdapter = new FilterTabsAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
        setTabIcons();
        try {
            tabLayout.getTabAt(0).getCustomView().setSelected(true);
        } catch (Exception e) {
            // Add default picture
            e.printStackTrace();
        }
    }

    private void setTabIcons() {
        View AllView = getLayoutInflater().inflate(R.layout.custom_view, null);
        ImageView iconAll = (ImageView) AllView.findViewById(R.id.imageView);
        TextView textViewAll = (TextView) AllView.findViewById(R.id.textView2);
        iconAll.setImageResource(R.drawable.imageall);
        textViewAll.setText("All Users");
        tabLayout.getTabAt(0).setCustomView(AllView);

        View BloodView = getLayoutInflater().inflate(R.layout.custom_view, null);
        ImageView iconBlood = (ImageView) BloodView.findViewById(R.id.imageView);
        TextView textViewBlood = (TextView) BloodView.findViewById(R.id.textView2);
        iconBlood.setImageResource(R.drawable.imageblood);
        textViewBlood.setText("Blood");
        tabLayout.getTabAt(1).setCustomView(BloodView);

        View CityView = getLayoutInflater().inflate(R.layout.custom_view, null);
        ImageView iconCity = (ImageView) CityView.findViewById(R.id.imageView);
        TextView textViewCity = (TextView) CityView.findViewById(R.id.textView2);
        iconCity.setImageResource(R.drawable.imagecity);
        textViewCity.setText("City");
        tabLayout.getTabAt(2).setCustomView(CityView);

        View ProfessionView = getLayoutInflater().inflate(R.layout.custom_view, null);
        ImageView iconProfession = (ImageView) ProfessionView.findViewById(R.id.imageView);
        TextView textViewProfession = (TextView) ProfessionView.findViewById(R.id.textView2);
        iconProfession.setImageResource(R.drawable.imageprofession);
        textViewProfession.setText("Profession");
        tabLayout.getTabAt(3).setCustomView(ProfessionView);
    }

    private void setOnClick() {

        mLayback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                GroupView.this.finish();
            }
        });

        layedit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mGroupAdmin.equalsIgnoreCase("1")) {
                    Intent i = new Intent(GroupView.this, GroupSettings.class);
                    i.putExtra("gDesc", mTxtGroupDesc.getText().toString());
                    i.putExtra("gTag", mTxtGrouptag.getText().toString());
                    i.putExtra("gPhoto", "");
                    i.putExtra("gUID", mGroupId);
                    i.putExtra("gName", mTxtGroupName.getText().toString());

                    startActivityForResult(i, 451);
                }

            }
        });

        imgAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Log.e("ad", "adfafadf");
                Intent i = new Intent(GroupView.this, AddNewMembers.class);
                i.putExtra("gName", mGroupName);
                i.putExtra("gID", mGroupId);
                startActivityForResult(i, 1002);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(GroupView.this, SearchGroupMember.class);
                i.putExtra("gName", mGroupName);
                i.putExtra("gID", mGroupId);
                i.putExtra("gAdmin", mGroupAdmin);
                startActivityForResult(i, 452);
            }
        });

        mTxtPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(GroupView.this, PendingMemberList.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 451) {
            if (resultCode == 1) { // test complete
                String tagname = data.getExtras().getString("TagName");
                String desc = data.getExtras().getString("Descrption");
                setImage(Vars.gPhoto);
                mTxtGroupDesc.setText(desc);
                mTxtGrouptag.setText(tagname);
                Vars.onAdd = true;
            } else { // test uncomplete

            }
        } else if (requestCode == 1002) {
            if (resultCode == 1001) {
                //Log.e("TAg", "User ppppp s");
                Vars.onUpadte = true;
            }
        }
    }


}
