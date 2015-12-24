package ianlo.net.laundryviewandroid;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
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

public class MainActivity extends AppCompatActivity {
    Machine[] washers;
    Machine[] dryers;

    // Used to process html in javascript.
    public class JSInterface {
        @JavascriptInterface
        public void processHTML(String data) {
            Log.d("Laundry", "Loading HTML");
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
            washers = new Machine[numWashers];
            dryers = new Machine[numDryers];
            // Populate the arrays.
            for (int i = 0; i < numWashers; i++) {
                washers[i] = new Machine(Machine.WASHER, Integer.parseInt(left.get(2 * i).getTextExtractor().toString()));
                washers[i].setStatusWithString(left.get(2 * i + 1).getTextExtractor().toString());
            }
            for (int i = 0; i < numDryers; i++) {
                dryers[i] = new Machine(Machine.DRYER, Integer.parseInt(right.get(2 * i).getTextExtractor().toString()));
                dryers[i].setStatusWithString(right.get(2 * i + 1).getTextExtractor().toString());
            }
            // Pass the machines to the fragments and update the fragments.
            washerFragment.setMachines(washers);
            washerFragment.updateLaundryViews();

            dryerFragment.setMachines(dryers);
            dryerFragment.updateLaundryViews();
        }
    }

    WebView wv;
    MachineFragment washerFragment;
    MachineFragment dryerFragment;

    // The drawer layout
    DrawerLayout mDrawerLayout;
    // The listview inside the navigation drawer.
    ListView mDrawerList;

    private boolean drawerOpen = false;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(
                getResources().getDrawable(R.drawable.drawer_shadow),
                GravityCompat.START);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Close the drawers and unlock them
        //mDrawerLayout.closeDrawers();
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, getResources().getStringArray(R.array.drawer_items)));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {

                // Initializes intent the the chosen activity
                switch (position) {
                    case 0:
                        newFragment(new MachineFragmentWrapper());
                        break;
                    case 1:
                        newFragment(new MachineFragmentWrapper());
                        break;
                    default:
                        break;
                }
                // Highlight the selected item, update the title, and close the
                // drawer
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
            }

        });

        toolbar.setNavigationIcon(R.drawable.ic_drawer);

        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Remove the shadow on the action bar.
        getSupportActionBar().setElevation(0);

        newFragment(new MachineFragmentWrapper());

        // Create a new webview for the webrequest.
        // We can't do a regular GET request because LaundryView loads its page using Javascript.
        // The Webview simulates a browser and loads the Javascript properly.
        wv = new WebView(this);

        wv.getSettings().setJavaScriptEnabled(true);

        // Use our JS interface to process the HTML.
        wv.addJavascriptInterface(new JSInterface(), "HTML");

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Call the JS interface to process the HTMl.
                wv.loadUrl("javascript:window.HTML.processHTML(document.documentElement.innerHTML);");
            }
        });

        // Load the laundry page so that we can scrape the data.
        loadUrl(RoomConstants.STEVER);
    }


    public void loadUrl(String url) {
        // Moved to a separate function so it can be called from the fragment.
        wv.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

        // Close the keyboard if it is open
        if (getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Create new fragment and transaction
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        // Replace whatever is in the fragment_container view with this
        // fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.main_fragment, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

}
