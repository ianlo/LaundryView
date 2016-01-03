package ianlo.net.laundryviewandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by ianlo on 2015-12-16.
 */
public class HomeFragment extends Fragment {
    private Button getInfoBtn;
    private EditText machineET;
    private MainActivity mainActivity;
    private LinearLayout dataEntrylayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        // Get the parent activity to get the machine info.
        mainActivity = (MainActivity) getActivity();
        dataEntrylayout = (LinearLayout) v.findViewById(R.id.home_data_entry_layout);
        machineET = (EditText) v.findViewById(R.id.home_machine_ET);
        getInfoBtn = (Button) v.findViewById(R.id.home_get_info_BTN);
        getInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Get the Machine number that was entered.
                    int num = Integer.parseInt(machineET.getText().toString());
                    // Find the selected Machine.
                    Machine selected = findMachine(num);
                    // If the machine was found, set up the notification and show the Machine info.
                    if (selected != null) {
                        // TODO: Fill in what to do when a Machine is found.
                        // Hide the data entry layout and show the info specific to that Machine.
                        dataEntrylayout.setVisibility(View.INVISIBLE);
                    }
                    // If the machine did not exist, show a toast saying invalid number.
                    else {
                        Log.d("LaundryView", "Machine number " + num + " not found in list of washers and dryers.");
                        Toast.makeText(mainActivity, "Invalid Machine Number.", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Log.e("LaundryView", "NumberFormatException on Machine Number EditText");
                    Toast.makeText(mainActivity, "Invalid Machine Number.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }

    public Machine findMachine(int num) {
        // Make sure the data has loaded.
        if (mainActivity.washers != null && mainActivity.dryers != null) {
            // Iterate through the washers and dryers in mainActivity.
            for (Machine m : mainActivity.washers) {
                // If we find the machine with the right number, return it.
                if (m.getNumber() == num) {
                    return m;
                }
            }
            for (Machine m : mainActivity.dryers) {
                // If we find the machine with the right number, return it.
                if (m.getNumber() == num) {
                    return m;
                }
            }
        }
        // If the data has not been loaded yet, show a toast.
        else {
            Toast.makeText(mainActivity, "Data has not been loaded.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
