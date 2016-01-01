package ianlo.net.laundryviewandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by ianlo on 2015-12-16.
 */
public class HomeFragment extends Fragment {
    private Button getInfoBtn;
    private MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        // Get the parent activity to get the machine info.
        mainActivity = (MainActivity) getActivity();
        getInfoBtn = (Button) v.findViewById(R.id.home_get_info_BTN);
        getInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure the data has loaded.
                if (mainActivity.washers != null && mainActivity.dryers != null) {
                    // TODO: Fill in code to get data.
                }
                else {
                    Toast.makeText(mainActivity, "Data has not been loaded.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}
