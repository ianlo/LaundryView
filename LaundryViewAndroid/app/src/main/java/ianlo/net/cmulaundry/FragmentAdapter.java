package ianlo.net.cmulaundry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import ui.MachineFragment;

/**
 * Created by ianlo on 2015-12-17.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<MachineFragment> fragments;
    public FragmentAdapter(FragmentManager fm, ArrayList<MachineFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
