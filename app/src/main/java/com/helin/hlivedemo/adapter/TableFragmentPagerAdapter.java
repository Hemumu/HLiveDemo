package com.helin.hlivedemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by helin on 2017/1/3.
 */

public class TableFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> list;
    private String[] titles ={"评论","红包"};

    public TableFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list= list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
