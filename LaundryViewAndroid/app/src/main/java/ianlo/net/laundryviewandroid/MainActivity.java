package ianlo.net.laundryviewandroid;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.util.ArrayList;
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
    FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Remove the shadow on the action bar.
        getSupportActionBar().setElevation(0);

        // Create new fragments for the washer and dryer pages.
        washerFragment = new MachineFragment();
        washerFragment.setTitle("Washers");
        dryerFragment = new MachineFragment();
        dryerFragment.setTitle("Dryers");

        // Create a list of fragments to pass to the fragment adapter.
        ArrayList<MachineFragment> fragments = new ArrayList<MachineFragment>();
        fragments.add(washerFragment);
        fragments.add(dryerFragment);

        // Create a new fragment adapter
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        //Pass it to the view pager so that we can swipe between fragments.
        ViewPager pager =
                (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(fragmentAdapter);

        // Give the TabLayout the ViewPager so the tabs work too.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

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
        loadUrl();
    }

    public void loadUrl() {
        // Moved to a separate function so it can be called from the fragment.
        wv.loadUrl("http://classic.laundryview.com/laundry_room.php?view=c&lr=4997123");
        // Boss: http://classic.laundryview.com/laundry_room.php?lr=4997152
        // Doherty: http://classic.laundryview.com/laundry_room.php?lr=4997151
        // Donner: http://classic.laundryview.com/laundry_room.php?lr=4997150
        // Hamerschlag (Broken): http://classic.laundryview.com/laundry_room.php?lr=4997146
        // Henderson: http://classic.laundryview.com/laundry_room.php?lr=4997147
        // Margaret Morrison 101: http://classic.laundryview.com/laundry_room.php?lr=4997144
        // Margaret Morrison 102 (Broken): http://classic.laundryview.com/laundry_room.php?lr=4997143
        // Margaret Morrison 103: http://classic.laundryview.com/laundry_room.php?lr=4997141
        // Margaret Morrison 104: http://classic.laundryview.com/laundry_room.php?lr=4997142
        // Margaret Morrison 105: http://classic.laundryview.com/laundry_room.php?lr=4997140
        // Margaret Morrison Storefront: http://classic.laundryview.com/laundry_room.php?lr=4997139
        // Mcgill: http://classic.laundryview.com/laundry_room.php?lr=4997136
        // Morewood A: http://classic.laundryview.com/laundry_room.php?lr=4997145
        // Morewood D: http://classic.laundryview.com/laundry_room.php?lr=4997132
        // Morewood E 3rd Floor: http://classic.laundryview.com/laundry_room.php?lr=4997137
        // Morewood E 4th Floor: http://classic.laundryview.com/laundry_room.php?lr=4997128
        // Morewood E 5th Floor: http://classic.laundryview.com/laundry_room.php?lr=4997127
        // Morewood E 6th Floor: http://classic.laundryview.com/laundry_room.php?lr=4997125
        // Morewood E 7th Floor: http://classic.laundryview.com/laundry_room.php?lr=4997122
        // Mudge B (Broken): http://classic.laundryview.com/laundry_room.php?lr=4997120
        // Mudge C: http://classic.laundryview.com/laundry_room.php?lr=4997119
        // Neville: http://classic.laundryview.com/laundry_room.php?lr=4997121
        // Res on 5th 4th Floor (Broken): http://classic.laundryview.com/laundry_room.php?lr=4997116
        // Res on 5th 5th Floor (Broken): http://classic.laundryview.com/laundry_room.php?lr=4997118
        // Resnick: http://classic.laundryview.com/laundry_room.php?lr=4997117
        // Scobell (Broken): http://classic.laundryview.com/laundry_room.php?lr=4997115
        // Shirley: http://classic.laundryview.com/laundry_room.php?lr=4997114
        // Spirit House (Broken): http://classic.laundryview.com/laundry_room.php?lr=4997112
        // Welch: http://classic.laundryview.com/laundry_room.php?lr=4997113
        // Woodlawn (Broken): http://classic.laundryview.com/laundry_room.php?lr=4997111
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

}
