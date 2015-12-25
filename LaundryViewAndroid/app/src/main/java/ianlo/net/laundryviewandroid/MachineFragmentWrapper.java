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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.machine_fragment_wrapper, container, false);


        // Create new fragments for the washer and dryer pages.
        MachineFragment washerFragment = new MachineFragment();
        washerFragment.setTitle("Washers");
        MachineFragment dryerFragment = new MachineFragment();
        dryerFragment.setTitle("Dryers");

        // Create a list of fragments to pass to the fragment adapter.
        ArrayList<MachineFragment> fragments = new ArrayList<MachineFragment>();
        fragments.add(washerFragment);
        fragments.add(dryerFragment);

        // Create a new fragment adapter
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments);

        //Pass it to the view pager so that we can swipe between fragments.
        ViewPager pager =
                (ViewPager) v.findViewById(R.id.viewpager);
        pager.setAdapter(fragmentAdapter);

        // Give the TabLayout the ViewPager so the tabs work too.
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

        return v;
    }

}