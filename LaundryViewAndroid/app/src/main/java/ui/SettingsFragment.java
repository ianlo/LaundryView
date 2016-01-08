package ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rey.material.widget.Spinner;

import ianlo.net.laundryviewandroid.R;
import ianlo.net.laundryviewandroid.SharedPreferencesConstants;

/**
 * Created by ianlo on 2015-12-16.
 */
public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
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
            }
        });
        return v;
    }
}
