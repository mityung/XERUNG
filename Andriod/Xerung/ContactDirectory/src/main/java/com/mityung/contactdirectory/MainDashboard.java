package com.mityung.contactdirectory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.common.Vars;
import com.example.contactplusgroup.fragment.ContactList;
import com.example.contactplusgroup.fragment.GroupList;
import com.example.contactplusgroup.fragment.PublicDirectory;
import com.example.contactplusgroup.utils.ManagerTypeface;
import com.example.contactplusgroup.utils.UtilsDevice;
import com.example.contactplusgroup.utils.UtilsMiscellaneous;
import com.example.contactplusgroup.views.CircleImageView;
import com.example.contactplusgroup.views.ScrimInsetsFrameLayout;
import com.example.contactplusgroup.views.ShakingBell;
import com.example.contactplusgroup.webservice.Webservice;
import com.example.contactplusgroup.widgets.Dialog;
import com.example.contactplusgroup.widgets.Logout;
import com.example.contactplusgroup.widgets.ProgressDialog;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 * Created by panther on 17/3/17.
 */
public class MainDashboard extends AppCompatActivity implements View.OnClickListener, Webservice.WebServiceInterface {

    private final static double sNAVIGATION_DRAWER_ACCOUNT_SECTION_ASPECT_RATIO = 9d / 16d;
    private static final int REQUEST_CODE_ENABLE = 11;
    private MainDashboard mainActivity = null;
    private int value = 0;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mFrameLayout_AccountView;
    private LinearLayout mNavDrawerEntriesRootView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
    private FrameLayout mFrameLayout_Dashborad, mFrameLayout_search, mFrameLayout_create,
            mFrameLayout_sync, mFrameLayout_logout;
    private TextView mTextView_Dashborad, mTextView_search, mTextView_create,
            mTextView_sync, mTextView_logout;
    private TextView mTextView_AccountDisplayName, mTextView_AccountDisplayNumber;
    private CircleImageView navigation_drawer_user_account_picture_profile;
    private Typeface snTextBold;
    private Resources res;
    private Comman comman = null;
    private Context context;
    private SharedPreferanceData shared = null;
    private ProgressDialog pbBar = null;
    final String TAG = this.getClass().toString();
    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;
    private Toolbar mToolbar;
    ShakingBell shakingBell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*get element id*/
        // Toolbar
        snTextBold = Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        View v = (View) LayoutInflater.from(this).inflate(R.layout.custom_action_bar, mToolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mToolbar.addView(v, lp);
        ((TextView) v.findViewById(R.id.titlefragment1)).setTypeface(ManagerTypeface.getTypeface(MainDashboard.this, R.string.typeface_android), Typeface.BOLD);
        ((TextView) v.findViewById(R.id.titlefragment2)).setTypeface(ManagerTypeface.getTypeface(MainDashboard.this, R.string.typeface_android), Typeface.BOLD);
        setSupportActionBar(mToolbar);
        shakingBell = (ShakingBell) findViewById(R.id.shakeBell);


        res = this.getResources();
        comman = new Comman();
        // init toolbar (old action bar)
        mainActivity = this;
        context = this;
        shared = new SharedPreferanceData(this);
        pbBar = new ProgressDialog();

        // Layout resources
        mFrameLayout_AccountView = (FrameLayout) findViewById(R.id.navigation_drawer_account_view);
        mNavDrawerEntriesRootView = (LinearLayout) findViewById(R.id.navigation_drawer_linearLayout_entries_root_view);
        mFrameLayout_Dashborad = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_dashboard);
        mFrameLayout_search = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_search);
        mFrameLayout_create = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_create);
        mFrameLayout_sync = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_sync);
        mFrameLayout_logout = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_logout);

        mTextView_Dashborad = (TextView) findViewById(R.id.navigation_drawer_items_textView_dashboard);
        mTextView_search = (TextView) findViewById(R.id.navigation_drawer_items_textView_search);
        mTextView_create = (TextView) findViewById(R.id.navigation_drawer_items_textView_create);
        mTextView_sync = (TextView) findViewById(R.id.navigation_drawer_items_textView_sync);
        mTextView_logout = (TextView) findViewById(R.id.navigation_drawer_items_textView_logout);

        mTextView_AccountDisplayName = (TextView) findViewById(R.id.navigation_drawer_account_information_display_name);
        mTextView_AccountDisplayNumber = (TextView) findViewById(R.id.navigation_drawer_account_information_display_number);

        mTextView_Dashborad.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_regular), Typeface.BOLD);
        mTextView_search.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_regular), Typeface.BOLD);
        mTextView_create.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_regular), Typeface.BOLD);
        mTextView_sync.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_regular), Typeface.BOLD);
        mTextView_logout.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_regular), Typeface.BOLD);
        mTextView_AccountDisplayName.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_regular));
        mTextView_AccountDisplayNumber.setTypeface(ManagerTypeface.getTypeface(this, R.string.typeface_roboto_regular));


        navigation_drawer_user_account_picture_profile = (CircleImageView) findViewById(R.id.navigation_drawer_user_account_picture_profile);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_DrawerLayout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark));
        mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.main_activity_navigation_drawer_rootLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_opened, R.string.navigation_drawer_closed) {
            @SuppressLint("NewApi")
            public void onDrawerClosed(View view) {
                mActionBarDrawerToggle.syncState();
                Log.e("in main Activity", "onDrawerClosed: ");
                invalidateOptionsMenu();
            }

            @SuppressLint("NewApi")
            public void onDrawerOpened(View drawerView) {
                mActionBarDrawerToggle.syncState();
                Log.e("in main Activity", "onDrawerOpen: ");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mActionBarDrawerToggle.syncState();

        // Navigation Drawer layout width
        int possibleMinDrawerWidth = UtilsDevice.getScreenWidth(this)
                - UtilsMiscellaneous.getThemeAttributeDimensionSize(this,
                android.R.attr.actionBarSize);
        int maxDrawerWidth = getResources().getDimensionPixelSize(
                R.dimen.navigation_drawer_max_width);

        mScrimInsetsFrameLayout.getLayoutParams().width = Math.min(
                possibleMinDrawerWidth, maxDrawerWidth);
        // Nav Drawer item click listener
        mFrameLayout_AccountView.setOnClickListener(this);
        mFrameLayout_Dashborad.setOnClickListener(this);
        mFrameLayout_search.setOnClickListener(this);
        mFrameLayout_create.setOnClickListener(this);
        mFrameLayout_sync.setOnClickListener(this);
        mFrameLayout_logout.setOnClickListener(this);
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNav.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.bottomBarItemOne:
                                selectedFragment = ContactList.newInstance();
                                break;
                            case R.id.bottomBarItemSecond:
                                selectedFragment = GroupList.newInstance();
                                break;
                            case R.id.bottomBarItemThird:
                                selectedFragment = PublicDirectory.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, ContactList.newInstance());
        transaction.commit();
        mFrameLayout_Dashborad.setSelected(true);
        setProfile();
        if(shared.getSharedValue("ProfileUpdate").equalsIgnoreCase("N/A")){
            shared.saveSharedData("ProfileUpdate", "Yes");
            //profileUpdateDialog(MainDashboard.this, getString(R.string.profile_update), getString(R.string.profile_update_msg));
        }
        createFetchInviteJson();
        shakingBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainDashboard.this, Notification.class));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.navigation_drawer_account_view) {
            try {

                mDrawerLayout.closeDrawer(Gravity.START);
                startActivity(new Intent(MainDashboard.this, UserProfile.class));
            } catch (Exception e) {
            }

            // If the user is signed in, go to the profile, otherwise show sign up / sign in
        } else {
            switch (view.getId()) {
                case R.id.navigation_drawer_items_list_linearLayout_dashboard: {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle("Dashboard");
                    }

                    break;
                }

                case R.id.navigation_drawer_items_list_linearLayout_search: {
                    startActivity(new Intent(MainDashboard.this, SearchList.class));
                    break;
                }

                case R.id.navigation_drawer_items_list_linearLayout_create: {
                    startActivity(new Intent(MainDashboard.this, AddNewGroup.class));
                    //   view.setSelected(true);
                    break;
                }


                case R.id.navigation_drawer_items_list_linearLayout_sync:
                    // Show about activity

                    break;

                case R.id.navigation_drawer_items_list_linearLayout_logout:
                    // Show about activity
                    if(comman.isNetworkAvailable(this)){
                        new Logout(MainDashboard.this, getString(R.string.action_logout), mainActivity).show();
                    }else{
                        Toast.makeText(this, getString(R.string.you_dont_have_internet), Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;
            }

            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    private void setProfile() {
        mTextView_AccountDisplayName.setText(shared.getSharedValue("PNAME"));
        mTextView_AccountDisplayNumber.setText(shared.getSharedValue("PMOBILE"));
        String imagePreferance = shared.getSharedValue("PPHOTO");
        if (!shared.getSharedValue("PPHOTO").equalsIgnoreCase("N/A")) {
            try {
                if (imagePreferance != null) {
                    if (imagePreferance.length() != 0) {
                        if (!imagePreferance.equalsIgnoreCase("null")) {
                            Bitmap p = comman.decodeBase64(imagePreferance);
                            navigation_drawer_user_account_picture_profile.setImageBitmap(p);
                        } else {
                            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_white_64dp);
                            navigation_drawer_user_account_picture_profile.setImageBitmap(icon1);
                        }
                    } else {
                        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_white_64dp);
                        navigation_drawer_user_account_picture_profile.setImageBitmap(icon1);
                    }
                } else {
                    Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_white_64dp);
                    navigation_drawer_user_account_picture_profile.setImageBitmap(icon1);
                }
            } catch (OutOfMemoryError e) {
                // Add default picture
                Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_white_64dp);
                navigation_drawer_user_account_picture_profile.setImageBitmap(icon1);
            }
        }
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
        } else {

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        createFetchInviteJson();
        setProfile();
    }

    private void callFetchInviteWebServiceProcess(JSONObject jbObject) {
        String jsonData = jbObject.toString();
        new Webservice(this, Vars.webMethodName.get(10)).execute(jsonData, Vars.gateKeperHit, Vars.webMethodName.get(10));
        //showProgress();
    }

    //[{"GROUPNAME":"mityung","INVITATIONSENDBY":"8881670074","NAME":"shivam","GROUPID","2"}]

    @Override
    public void onWebCompleteResult(String result, String processName) {
        // TODO Auto-generated method stub
        //hideProgress();
        if (result == null) {
            return;
        }
        if (result.trim().equalsIgnoreCase("null")|| result.trim().equalsIgnoreCase("0")) {
            return;
        }
        if(processName.equals(Vars.webMethodName.get(10))){
            parseJsonfetch(result);
        }
    }

    int mCount =0;
    private void parseJsonfetch(String result){
        try {
            JSONArray jArrayPending = new JSONArray(result);
            for(int i=jArrayPending.length()-1;i<jArrayPending.length();i++){
                JSONObject jbPending = jArrayPending.getJSONObject(i);
                shared.saveSharedData("NCOUNT", jbPending.getString("COUNT").trim());
                mCount = Integer.parseInt(shared.getSharedValue("NCOUNT"));
                if(mCount!=0)
                    shakingBell.shake(mCount);
                else
                    shakingBell.shake(mCount);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void profileUpdateDialog(Context context, String title, String msg) {
        Dialog dialog = new Dialog(context, title, msg);
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainDashboard.this, ProfileSetting.class));
            }
        });
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Register.this.finish();

            }
        });
        dialog.show();
    }




}

