package com.example.contactplusgroup.adapter;

import com.example.contactplusgroup.fragment.ContactList;
import com.example.contactplusgroup.fragment.GroupList;
import com.example.contactplusgroup.fragment.PublicDirectory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter{

	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	
	public GroupList gplIst;
	
	@SuppressWarnings("static-access")
	public Fragment getItem(int num) {
    	switch (num) {
		case 0:
			// ContactList fragment activity
			return new ContactList().newInstance();
		case 1:
			// private directories activity
			gplIst = new GroupList().newInstance();
			return gplIst;
		case 2:
			// public directories activity
			return new PublicDirectory().newInstance();

		}

		return null;
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return "tab 1";
            case 1: return "tab 2";
			case 2: return "tab 3";
            default: return null;
        }
    }

}
