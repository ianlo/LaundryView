package ianlo.net.laundryviewandroid;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by ianlo on 2015-12-16.
 */
public class MachineFragmentWrapper extends Fragment {
    MachineFragment washerFragment;
    MachineFragment dryerFragment;
    ArrayList<MachineFragment> fragments;

    public static MachineFragmentWrapper newInstance(MainActivity activity) {
        MachineFragmentWrapper f = new MachineFragmentWrapper();
        // Create new fragments for the washer and dryer pages.
        f.washerFragment = MachineFragment.newInstance(activity);
        f.washerFragment.setTitle("Washers");
        f.dryerFragment = MachineFragment.newInstance(activity);
        f.dryerFragment.setTitle("Dryers");
        // Set up the fragment arraylist.
        f.fragments = new ArrayList<MachineFragment>();
        f.fragments.add(f.washerFragment);
        f.fragments.add(f.dryerFragment);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.machine_fragment_wrapper, container, false);

        // Create a new fragment adapter if it wasn't done before.
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), fragments);

        //Pass it to the view pager so that we can swipe between fragments.
        ViewPager pager =
                (ViewPager) v.findViewById(R.id.viewpager);
        pager.setAdapter(fragmentAdapter);


        // Give the TabLayout the ViewPager so the tabs work too.
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

        return v;
    }

    public void updateFragments(Machine[] washers, Machine[] dryers) {
        washerFragment.setMachines(washers);
        dryerFragment.setMachines(dryers);
        if (washerFragment.getView() != null && dryerFragment.getView() != null) {
            washerFragment.updateLaundryViews();
            dryerFragment.updateLaundryViews();
        }
    }
}
