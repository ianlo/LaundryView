package ianlo.net.laundryviewandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class MainActivity extends AppCompatActivity {
    //Used to process html in javascript.
    public class JSInterface {
        @JavascriptInterface
        public void processHTML(String data) {
            Source source = new Source(data);
            Element classicMonitor = source.getElementById("classic_monitor");
            Element table = classicMonitor
                    .getChildElements().get(0)
                    .getChildElements().get(0)
                    .getChildElements().get(0)
                    .getChildElements().get(0)
                    .getChildElements().get(0);
            Element leftBody = table

                    .getChildElements().get(0);
            Element rightBody =table
                    .getChildElements().get(0);
            int numWashers = leftBody.length()/2;
            int numDryers = rightBody.length()/2;

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WebView wv = new WebView(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
