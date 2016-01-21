package ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ianlo.net.laundryviewandroid.Machine;
import ianlo.net.laundryviewandroid.NotificationReceiver;
import ianlo.net.laundryviewandroid.R;

/**
 * Created by ianlo on 2015-12-16.
 */
public class HomeFragment extends Fragment {
    private LinearLayout machineInfoLayout;
    private TextView machineInfoTV;

    private Button getInfoBtn;
    private EditText machineET;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        // Get the parent activity to get the machine info.
        mainActivity = (MainActivity) getActivity();
        // Views for the machineInfoLayout.
        machineInfoLayout = (LinearLayout) v.findViewById(R.id.home_machine_info_layout);
        machineInfoTV = (TextView) v.findViewById(R.id.home_machine_info_TV);
        // Views for machine selection.
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
                        showMachineInfo(selected);
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
        // TEMPORARY until we save the machine number.
        showDataEntryLayout();
        return v;
    }

    public Machine findMachine(int num) {
        // Make sure the data has loaded.
        if (mainActivity.getWashers() != null && mainActivity.getDryers() != null) {
            // Iterate through the washers and dryers in mainActivity.
            for (Machine m : mainActivity.getWashers()) {
                // If we find the machine with the right number, return it.
                if (m.getNumber() == num) {
                    return m;
                }
            }
            for (Machine m : mainActivity.getDryers()) {
                // If we find the machine with the right number, return it.
                if (m.getNumber() == num) {
                    return m;
                }
            }
            return null;
        }
        // If the data has not been loaded yet, show a toast.
        else {
            Toast.makeText(mainActivity, "Data has not been loaded.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void showDataEntryLayout() {
        // Hide the machineInfo layout.
        machineInfoLayout.setVisibility(View.INVISIBLE);
        // Clear the EditText.
        machineET.setText("");
    }

    public void showMachineInfo(Machine selected) {
        // Close the keyboard if it is open.
        if (mainActivity.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(mainActivity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        // Show the data specific to that machine.
        machineInfoLayout.setVisibility(View.VISIBLE);
        machineInfoTV.setText(selected.getStringStatus());
        AlarmManager alarmMgr = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mainActivity, NotificationReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mainActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Set the notification to the time when the machine is finished.
        alarmMgr.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + selected.getTimeRemaining() * 60 * 1000, alarmIntent);
    }

    // Cancel the previously set notification.
    public void cancelNotification() {
        Intent intent = new Intent(mainActivity, NotificationReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mainActivity, 0, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmMgr = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(alarmIntent);
        alarmIntent.cancel();
    }
}
