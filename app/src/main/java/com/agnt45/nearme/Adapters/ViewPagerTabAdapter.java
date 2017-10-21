package com.agnt45.nearme.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.agnt45.nearme.AddNewEventFragment;
import com.agnt45.nearme.EventsAddedHistoryFragment;
import com.agnt45.nearme.EventsMapViewFragment;


/**
 * Created by Agnt45 on 16-10-2017.
 */

public class ViewPagerTabAdapter extends FragmentPagerAdapter {
    public ViewPagerTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
            {

                EventsMapViewFragment eventsMapViewFragment =  new EventsMapViewFragment();
                return eventsMapViewFragment;
            }
            case 1:
            {
                AddNewEventFragment addNewEventFragment  = new AddNewEventFragment();
                return addNewEventFragment;
            }
            case 2:
            {
                EventsAddedHistoryFragment eventsAddedHistoryFragment = new EventsAddedHistoryFragment();
                return eventsAddedHistoryFragment;
            }
            default:{
                return null ;
            }
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int pos){
        switch (pos){
            case 0:
                return "EVENTS";
            case 1:
                return "ADD EVENTS";
            case 2:
                return "ADDED EVENTS";

            default:
                return null;
        }
    }
}
