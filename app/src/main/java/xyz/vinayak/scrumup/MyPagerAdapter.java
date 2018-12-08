package xyz.vinayak.scrumup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {

            case 0: return ChatFragment.newInstance("Chat Fragment");
            case 1: return TasksFragment.newInstance("Tasks Fragment");
            case 2: return StatsFragment.newInstance("Stats Fragment");
            default: return TasksFragment.newInstance("Tasks Fragment");
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {

            case 0: return "Chat Room";
            case 1: return "Tasks";
            case 2: return "Stats";
            default: return null;
        }
    }
}