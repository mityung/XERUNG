package com.example.contactplusgroup.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.contactplusgroup.adapter.ContactAdapter;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.ContactBean;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.example.contactplusgroup.sqlite.GroupDb;
import com.example.contactplusgroup.utils.HanziToPinyin;
import com.example.contactplusgroup.views.SideBar;
import com.example.contactplusgroup.widgets.ProgressDialog;
import com.mityung.contactdirectory.ContactSync;
import com.mityung.contactdirectory.R;

public class ContactList extends Fragment implements SideBar.OnTouchingLetterChangedListener{
	
	
	// ArrayList
    ArrayList<ContactBean> selectUsers;
    LinkedHashMap<String, String> nameNum = new LinkedHashMap<String, String>();
    List<ContactBean> temp;
    TextView txtNoData;
    private RelativeLayout relProgress;
    // Contact List
    ListView listView;
    // Cursor to load contacts list
    Cursor phones, email;
    LinearLayout layMain;

    // Pop up
    ContentResolver resolver;
    ContactAdapter adapter;
    
    private SharedPreferanceData shared = null;
	private Context context;
	private Comman comman = null;
	private ProgressDialog pbBar = null;
	
	@SuppressWarnings("unused")
	private int position;
	GroupDb dbContact = null;

	/**
	 * Static instance of the Profile
	 * @return
	 */
	public static ContactList newInstance() {
		ContactList f = new ContactList();
		Bundle b      = new Bundle();
		f.setArguments(b);
		return f;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_contact_list, container,false);
	    findViewids(rootView);
	    initialise();
		return rootView;
	}
	
	private void initialise(){
		getRefer();
		setOnClick();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(Build.VERSION.SDK_INT < 23){
				startContactLoader();
		}else {
			if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED) {
				startContactLoader();
			}else{
				getPermissionToReadUserContacts();
			}


		}
	}

	private void startSyncService(){
		Intent i = new Intent(context, ContactSync.class);
		context.startService(i);
		//Log.e("Start", "service Started");
	}

	private void findViewids(View view) {
		listView  = (ListView) view.findViewById(R.id.lvContactList);
		layMain   = (LinearLayout)view.findViewById(R.id.laymain);
		txtNoData = (TextView)view.findViewById(R.id.txtNoContact);
		relProgress = (RelativeLayout)view.findViewById(R.id.layProgressresult);
		SideBar mSideBar = (SideBar) view.findViewById(R.id.school_friend_sidrbar);
        TextView mDialog = (TextView) view.findViewById(R.id.school_friend_dialog);

		mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
	}


	private void startContactLoader(){
		selectUsers = new ArrayList<ContactBean>();
			resolver    = getActivity().getContentResolver();
			phones      = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

			LoadContact loadContact = new LoadContact();
			loadContact.execute();
	}
	
	private void showProgress() {

		relProgress.setVisibility(View.VISIBLE);
		layMain.setVisibility(View.GONE);

	}

	private void hideProgress() {

		relProgress.setVisibility(View.GONE);
		layMain.setVisibility(View.VISIBLE);

	}
	
	private void getRefer(){
		comman    = new Comman();
		context   = getActivity();
		pbBar     = new ProgressDialog();
		shared    = new SharedPreferanceData(getActivity());
		dbContact = new GroupDb(getActivity());
	}
	
	private void setOnClick(){		
		
	}

// Load data on background
	class LoadContact extends AsyncTask<Void, Void, Void> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
        	showProgress();
		} catch (Exception e) {
			// TODO: handle exception
		}
        
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Get Contact list from Phone

        if (phones != null) {
			if(dbContact != null)
            	dbContact.truncateContactTable();
            HashMap<String, String> data = new HashMap<String, String>();
            while (phones.moveToNext()) {
                Bitmap bit_thumb = null;
                String id          = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name        = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String EmailAddr   = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                String newPhinr    = phoneNumber.replaceAll("\\s+","").replaceAll("\\-", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\#", "").replaceAll("\\_", "");
                String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                try {
                    if (image_thumb != null) {
                        bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                    } else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ContactBean selectUser = new ContactBean();
                if (!data.containsValue(newPhinr)) {
                	selectUser.setThumb(bit_thumb);
                    selectUser.setName(name);
                    selectUser.setNumber(newPhinr);
                    selectUser.setPhotoURI(image_thumb);
                    selectUser.setPinyin(HanziToPinyin.getPinYin(selectUser.getName()));
                    data.put(name, newPhinr);
					if(dbContact != null)
                    dbContact.addContact(selectUser);
                    if (newPhinr != null) {
                    	 selectUsers.add(selectUser);
                    	 nameNum.put(selectUser.getName(), selectUser.getNumber());
                    }
                }

            }
        } else {

        }
        phones.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        hideProgress();
		if (selectUsers.size() == 0) {
			txtNoData.setVisibility(View.VISIBLE);
			layMain.setVisibility(View.GONE);
			return;
		}
		if(null!=getActivity()){
			adapter = new ContactAdapter(getActivity(), R.layout.fragment_contact_list, selectUsers);
			listView.setAdapter(adapter);
			// Select item on listclick
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

					ContactBean data = selectUsers.get(i);
					String number = data.getNumber();
					comman.callPhone(number, getActivity());
				}
			});
			listView.setFastScrollEnabled(true);
			if(!shared.getSharedValue("SyncState").equalsIgnoreCase("Yes")){
				startSyncService();
				shared.saveSharedData("SyncState", "Yes");
			}
		}
    }
}

	
	@Override
	public void onTouchingLetterChanged(String s) {
		// TODO Auto-generated method stub
		if (adapter != null) {
            position = adapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            listView.setSelection(position);
        } else if (s.contains("#")) {
        	listView.setSelection(0);
        }
	}

	// Identifier for the permission request
	private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

	// Called when the user is performing an action which requires the app to read the
	// user's contacts
	public void getPermissionToReadUserContacts() {
		// 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
		// checking the build version since Context.checkSelfPermission(...) is only available
		// in Marshmallow
		// 2) Always check for permission (even if permission has already been granted)
		// since the user can revoke permissions at any time through Settings
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
				!= PackageManager.PERMISSION_GRANTED) {

			// The permission is NOT already granted.
			// Check if the user has been asked about this permission already and denied
			// it. If so, we want to give more explanation about why the permission is needed.
			if (shouldShowRequestPermissionRationale(
					Manifest.permission.READ_CONTACTS)) {
				// Show our own UI to explain to the user why we need to read the contacts
				// before actually requesting the permission and showing the default UI
			}

			// Fire off an async request to actually get the permission
			// This will show the standard permission request dialog UI
			requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
					READ_CONTACTS_PERMISSIONS_REQUEST);
		}
	}

	// Callback with the request from calling requestPermissions(...)
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String permissions[],
										   @NonNull int[] grantResults) {
		// Make sure it's our original READ_CONTACTS request
		if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
			if (grantResults.length == 1 &&
					grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(context, "Read Contacts permission granted.", Toast.LENGTH_SHORT).show();
				startContactLoader();
			} else {
				Toast.makeText(context, "Read Contacts permission denied.", Toast.LENGTH_SHORT).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

}
