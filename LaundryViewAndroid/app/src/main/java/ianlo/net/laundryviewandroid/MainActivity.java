package ianlo.net.laundryviewandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Machine[] washers;
    Machine[] dryers;

    //Used to process html in javascript.
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
            int numWashers = left.size()/2;
            int numDryers = right.size()/2;
            // Create arrays to keep track of the machines.
            washers = new Machine[numWashers];
            dryers = new Machine[numDryers];
            // Populate the arrays.
            for(int i = 0; i < numWashers; i++) {
                washers[i] = new Machine(Machine.WASHER, Integer.parseInt(left.get(2 * i).getTextExtractor().toString()));
                washers[i].setStatusWithString(left.get(2 * i + 1).getTextExtractor().toString());
            }
            for(int i = 0; i < numDryers; i++) {
                dryers[i] = new Machine(Machine.DRYER, Integer.parseInt(right.get(2 * i).getTextExtractor().toString()));
                dryers[i].setStatusWithString(right.get(2 * i + 1).getTextExtractor().toString());
            }
            updateLaundryViews();
        }
    }

    WebView wv;
    LinearLayout washerList;
    LinearLayout dryerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        washerList = (LinearLayout) findViewById(R.id.washerList);
        dryerList = (LinearLayout) findViewById(R.id.dryerList);

        wv = new WebView(this);
        wv.getSettings().setJavaScriptEnabled(true);

        wv.addJavascriptInterface(new JSInterface(), "HTML");

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wv.loadUrl("javascript:window.HTML.processHTML(document.documentElement.innerHTML);");
            }
        });
        wv.loadUrl("http://classic.laundryview.com/laundry_room.php?view=c&lr=4997123");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            wv.loadUrl("http://classic.laundryview.com/laundry_room.php?view=c&lr=4997123");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateLaundryViews() {
        //We have to edit the UI so add a new thread for the UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Clear the previous values.
                washerList.removeAllViews();
                //Add the title
                TextView washerTitle = new TextView(MainActivity.this);
                washerTitle.setText("Washers:");
                washerList.addView(washerTitle);
                //Add the info for all the washers.
                for(Machine m : washers) {
                    TextView tv = new TextView(MainActivity.this);
                    tv.setText(m.getNumber() + ": " + m.getStringStatus());
                    washerList.addView(tv);
                }
                //Clear the previous values.
                dryerList.removeAllViews();
                //Add the title.
                TextView dryerTitle = new TextView(MainActivity.this);
                dryerTitle.setText("Dryers:");
                dryerList.addView(dryerTitle);
                //Add the info for all the washers.
                for(Machine m : dryers) {
                    TextView tv = new TextView(MainActivity.this);
                    tv.setText(m.getNumber() + ": " + m.getStringStatus());
                    dryerList.addView(tv);
                }
            }
        });

    }
}
