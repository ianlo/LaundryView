package layouts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ianlo.net.cmulaundry.FragmentAdapter;
import ianlo.net.cmulaundry.LaundryRoom;
import ianlo.net.cmulaundry.Machine;
import ianlo.net.cmulaundry.R;

/**
 * Created by ianlo on 2015-12-16.
 */
public class MachineFragmentWrapper extends Fragment {
    private MachineFragment washerFragment;
    private MachineFragment dryerFragment;
    private ArrayList<MachineFragment> fragments;

    public static MachineFragmentWrapper newInstance(MainActivity activity, LaundryRoom laundryRoom) {
        MachineFragmentWrapper f = new MachineFragmentWrapper();
        // Create new Fragments for the washer and dryer pages.
        f.washerFragment = MachineFragment.newInstance(activity, laundryRoom);
        f.washerFragment.setTitle("Washers");
        f.dryerFragment = MachineFragment.newInstance(activity, laundryRoom);
        f.dryerFragment.setTitle("Dryers");
        // Set up the ArrayList containing the washer and dryer Fragments.
        f.fragments = new ArrayList<MachineFragment>();
        f.fragments.add(f.washerFragment);
        f.fragments.add(f.dryerFragment);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.machine_fragment_wrapper, container, false);
        // Create a new Fragment adapter if it wasn't done before.
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), fragments);
        // Pass it to the View Pager so that we can swipe between Fragments.
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
        // We can only update the Views if onCreateView() was called on the Fragment.
        if (washerFragment.getView() != null && dryerFragment.getView() != null) {
            washerFragment.updateLaundryViews();
            dryerFragment.updateLaundryViews();
        }
    }
}
