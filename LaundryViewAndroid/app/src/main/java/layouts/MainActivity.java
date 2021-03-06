package layouts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.util.List;

import ianlo.net.cmulaundry.LaundryRoom;
import ianlo.net.cmulaundry.Machine;
import ianlo.net.cmulaundry.R;
import ianlo.net.cmulaundry.RoomConstants;
import ianlo.net.cmulaundry.SharedPreferencesConstants;

public class MainActivity extends AppCompatActivity {
    // Arrays to keep track of the information we get back from the LaundryView website.
    private Machine[] washers;
    private Machine[] dryers;
    private ProgressDialog progressDialog;
    // Used to process HTML in Javascript.
    public class JSInterface {
        @JavascriptInterface
        public void processHTML(String data) {
            Log.d("CMU Laundry", "Loading HTML");
            Source source = new Source(data);
            Element classicMonitor = source.getElementById("classic_monitor");
            // Really inefficient grabbing of the table...
            Element table = classicMonitor
                    .getChildElements().get(0)
                    .getChildElements().get(0)
                    .getChildElements().get(0);
            // Get the 2 separate tables and their rows as lists.
            List<Element> left = table
                    .getChildElements().get(0)
                    .getChildElements().get(0)
                    .getChildElements().get(0)
                    .getChildElements();
            List<Element> right = table
                    .getChildElements().get(1)
                    .getChildElements().get(0)
                    .getChildElements().get(0)
                    .getChildElements();
            // Calculate the size. Each machine has 2 TRs.
            int numWashers = left.size() / 2;
            int numDryers = right.size() / 2;
            // Create arrays to keep track of the machines.
            setWashers(new Machine[numWashers]);
            setDryers(new Machine[numDryers]);
            // Populate the arrays.
            for (int i = 0; i < numWashers; i++) {
                getWashers()[i] = new Machine(Machine.WASHER, Integer.parseInt(left.get(2 * i).getTextExtractor().toString()));
                getWashers()[i].setStatusWithString(left.get(2 * i + 1).getTextExtractor().toString());
            }
            for (int i = 0; i < numDryers; i++) {
                getDryers()[i] = new Machine(Machine.DRYER, Integer.parseInt(right.get(2 * i).getTextExtractor().toString()));
                getDryers()[i].setStatusWithString(right.get(2 * i + 1).getTextExtractor().toString());
            }
            // Pass the machines to the Fragments and update the Fragments.
            machineFragmentWrapper.updateFragments(getWashers(), getDryers());
        }
    }

    // UI elements.
    private WebView wv;
    private MachineFragmentWrapper machineFragmentWrapper;
    private SettingsFragment settingsFragment;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the Toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get the saved LaundryRoom.
        SharedPreferences prefs = getSharedPreferences(SharedPreferencesConstants.NAME, MODE_PRIVATE);
        String roomName = prefs.getString(SharedPreferencesConstants.SELECTED_ROOM, "");
        LaundryRoom room = LaundryRoom.getRoom(roomName);
        getSupportActionBar().setTitle(room.getName() + " Laundry");
        // Instantiate Fragments.
        machineFragmentWrapper = MachineFragmentWrapper.newInstance(this, room);
        settingsFragment = new SettingsFragment();
        // Open the HomeFragment by default.
        newFragment(machineFragmentWrapper);
        // Set up the Navigation Drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the ListView.
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, getResources().getStringArray(R.array.drawer_items)));
        // Set the ListView's click listener.
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                // Initializes the chosen Fragment.
                switch (position) {
                    case 0:
                        newFragment(machineFragmentWrapper);
                        break;
                    case 1:
                        newFragment(settingsFragment);
                    default:
                        break;
                }
                // Highlight the selected item and close the drawer.
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
            }

        });
        // Select the first item.
        mDrawerList.setItemChecked(0, true);
        // Set up the toggle.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Enable the nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        // Remove the shadow on the action bar.
        getSupportActionBar().setElevation(0);
        // Create a new WebView for the web request.
        // We can't do a regular GET request because LaundryView loads its page using Javascript.
        // The WebView simulates a browser and loads the Javascript properly.
        wv = new WebView(this);
        wv.getSettings().setJavaScriptEnabled(true);
        // Create a ProgressDialog for the loading process.
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        // Use our JS interface to process the HTML.
        wv.addJavascriptInterface(new JSInterface(), "HTML");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Hide the dialog once the data has been loaded.
                progressDialog.hide();
                // Call the JS interface to process the HTMl.
                wv.loadUrl("javascript:window.HTML.processHTML(document.documentElement.innerHTML);");
            }
        });

        // Load the laundry page so that we can scrape the data. We want the ProgressDialog when the app first starts up.
        loadUrl(room.getUrl(), true);

        // Set the default room to Stever it hasn't been set yet.
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesConstants.NAME, MODE_PRIVATE);
        if (sharedPreferences.getString(SharedPreferencesConstants.SELECTED_ROOM, null) == null
                || sharedPreferences.getInt(SharedPreferencesConstants.SELECTED_ROOM_POSITION, -1) == -1) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SharedPreferencesConstants.SELECTED_ROOM, RoomConstants.STEVER.getName());
            editor.putInt(SharedPreferencesConstants.SELECTED_ROOM_POSITION, 27);
            editor.apply();
        }
    }

    public void loadUrl(String url, boolean showProgressDialog) {
        // Show the ProgressDialog if it was requested.
        if (showProgressDialog) progressDialog.show();
        // Moved to a separate function so it can be called from the Fragment.
        wv.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the Action Bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    public void newFragment(Fragment fragment) {
        // Close the keyboard if it is open.
        if (getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        // Create new Fragment and Transaction.
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        // Replace whatever is in the Fragment view with this Fragment.
        transaction.replace(R.id.main_fragment, fragment);
        transaction.addToBackStack(null);
        // Commit the Transaction.
        transaction.commit();
    }

    public Machine[] getWashers() {
        return washers;
    }

    public void setWashers(Machine[] washers) {
        this.washers = washers;
    }

    public Machine[] getDryers() {
        return dryers;
    }

    public void setDryers(Machine[] dryers) {
        this.dryers = dryers;
    }

    // Called when Settings are changed.
    public void reloadData(String roomName) {
        LaundryRoom room = LaundryRoom.getRoom(roomName);
        // Change the ActionBar title.
        getSupportActionBar().setTitle(room.getName() + " Laundry");
        // Create a new MachineFragmentWrapper with the new room.
        machineFragmentWrapper = MachineFragmentWrapper.newInstance(this, room);
        // Get fresh data.
        loadUrl(room.getUrl(), true);
    }

    // Don't do anything when the back button is pressed. We don't want the fragment to change.
    @Override
    public void onBackPressed() {

    }
}
