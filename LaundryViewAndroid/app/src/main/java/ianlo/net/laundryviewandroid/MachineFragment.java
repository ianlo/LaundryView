package ianlo.net.laundryviewandroid;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ianlo on 2015-12-16.
 */
public class MachineFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Machine[] machines;
    private String title = "No title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.machine_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return v;
    }

    public void setMachines(Machine[] machines) {
        this.machines = machines;
    }

    public void updateLaundryViews() {
        // We have to edit the UI so add a new thread for the UI thread.
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.removeAllViews();
                // specify an adapter (see also next example)
                mAdapter = new MachineAdapter(machines);
                mRecyclerView.setAdapter(mAdapter);
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
