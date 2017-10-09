package com.example.contactplusgroup.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.contactplusgroup.fragment.AllUser;
import com.example.contactplusgroup.fragment.Blood;
import com.example.contactplusgroup.fragment.City;
import com.example.contactplusgroup.fragment.Profession;

/**
 * Created by mipanther on 11/3/16.
 */
public class FilterTabsAdapter extends FragmentStatePagerAdapter {

    public FilterTabsAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }


    @SuppressWarnings("static-access")
    public Fragment getItem(int num) {
        switch (num) {
            case 0:
                // Top Rated fragment activity
                return new AllUser().newInstance(num);
            case 1:
                // Games fragment activity
                return new Blood().newInstance(num);
            case 2:
                return new City().newInstance(num);
            case 3:
                return new Profession().newInstance(num);
        }

        return null;
    }
    @Override
    public int getCount() {
        return 4;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return "tab 1";
            case 1: return "tab 2";
            case 2: return "tab 3";
            case 3: return "tab 4";

            default: return null;
        }
    }

}
