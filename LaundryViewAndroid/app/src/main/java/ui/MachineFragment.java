package ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ianlo.net.laundryviewandroid.LaundryRoom;
import ianlo.net.laundryviewandroid.Machine;
import ianlo.net.laundryviewandroid.MachineAdapter;
import ianlo.net.laundryviewandroid.R;

/**
 * Created by ianlo on 2015-12-16.
 */
public class MachineFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Machine[] machines;
    private String title = "No title";
    private MainActivity mainActivity;
    private LaundryRoom laundryRoom;

    public static MachineFragment newInstance(MainActivity activity, LaundryRoom laundryRoom) {
        MachineFragment f = new MachineFragment();
        f.mainActivity = activity;
        f.laundryRoom = laundryRoom;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.machine_fragment, container, false);
        // Set up the RecyclerView for the cards.
        mRecyclerView = (RecyclerView) v.findViewById(R.id.machine_fragment_rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Refresh the info when the user swipes down.
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.machine_fragment_srl);
        // Change the colour of the refresh to our primary colour.
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.primary,
                R.color.primary,
                R.color.primary);
        // To refresh, load the URL again in the WebView.
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do not show the ProgressDialog when we load the data because we have the circle spinny thing from the SwipeRefreshLayout
                mainActivity.loadUrl(laundryRoom.getUrl(), false);
            }
        });
        // If the machines were loaded previously, update the Views.
        if (machines != null) updateLaundryViews();
        return v;
    }

    public void setMachines(Machine[] machines) {
        this.machines = machines;
    }

    public void updateLaundryViews() {
        // We have to edit the UI so add a new thread for the UI thread.
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Remove all preexisting cards.
                mRecyclerView.removeAllViews();
                // Create a MachineAdapter so the cards show the Machine info.
                mAdapter = new MachineAdapter(MachineFragment.this.getContext(), machines, mainActivity);
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
