package ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.rey.material.widget.Spinner;

import ianlo.net.laundryviewandroid.NotificationReceiver;
import ianlo.net.laundryviewandroid.R;
import ianlo.net.laundryviewandroid.SharedPreferencesConstants;

/**
 * Created by ianlo on 2015-12-16.
 */
public class SettingsFragment extends Fragment {
    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
        // Get the parent activity.
        mainActivity = (MainActivity) getActivity();
        // Initialize UI elements.
        Spinner roomSpinner = (Spinner) v.findViewById(R.id.settings_room_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.laundry_rooms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(adapter);
        // If a room was saved in the past, load that room to the Spinner.
        final SharedPreferences preferences = getActivity().getSharedPreferences(SharedPreferencesConstants.NAME, Context.MODE_PRIVATE);
        if (preferences.getInt(SharedPreferencesConstants.SELECTED_ROOM_POSITION, -1) != -1
                && preferences.getString(SharedPreferencesConstants.SELECTED_ROOM, null) != null) {
            roomSpinner.setSelection(preferences.getInt(SharedPreferencesConstants.SELECTED_ROOM_POSITION, 0));
        }
        roomSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                // Save the selected room to SharedPreferences.
                String roomName = parent.getSelectedItem().toString();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SharedPreferencesConstants.SELECTED_ROOM, roomName);
                editor.putInt(SharedPreferencesConstants.SELECTED_ROOM_POSITION, position);
                editor.apply();
                ((MainActivity) getActivity()).reloadData(roomName);
            }
        });
        Button clearButton = (Button) v.findViewById(R.id.settings_clear_notifications);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the PendingIntent and then cancel the scheduled notification.
                AlarmManager alarmMgr = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(mainActivity, NotificationReceiver.class);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(mainActivity, 0, intent, PendingIntent.FLAG_NO_CREATE);
                alarmMgr.cancel(alarmIntent);
                alarmIntent.cancel();
                Log.d("LaundryView", "Notification Cancelled");
                Toast.makeText(mainActivity, "Notification Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
