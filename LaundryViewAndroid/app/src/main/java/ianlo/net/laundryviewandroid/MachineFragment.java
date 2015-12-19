package ianlo.net.laundryviewandroid;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private TextView loadingTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.machine_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.machine_fragment_rv);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Add swipe down to refresh feature.
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.machine_fragment_srl);
        // Change the colour of the refresh to our primary colour.
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.primary,
                R.color.primary,
                R.color.primary);
        // To refresh, load the Url again in the Webview.
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).loadUrl();
            }
        });

        // Show the user that the machine info is loading.
        loadingTV = (TextView) v.findViewById(R.id.machine_fragment_loading_tv);

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
                // Make the loading textview invisible.
                loadingTV.setVisibility(View.INVISIBLE);

                mRecyclerView.removeAllViews();
                // specify an adapter (see also next example)
                mAdapter = new MachineAdapter(MachineFragment.this.getContext(), machines);
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
