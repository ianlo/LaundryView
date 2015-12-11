package ianlo.net.laundryviewandroid;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by ianlo on 2015-12-10.
 */
public class LaundryRequest extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://classic.laundryview.com/laundry_room.php?view=c&lr=4997123").get();
            String html = doc.html();
            Log.d("Laundry", html);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        final WebClient webClient = new WebClient();
//
//        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
//        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
//
//        HtmlPage page;
//        try {
//            page = webClient
//                    .getPage("http://classic.laundryview.com/laundry_room.php?view=c&lr=4997123");
//
//            HtmlDivision monitor = (HtmlDivision) page.getElementById("classic_monitor");
//            HtmlTableRow table = ((HtmlTableRow) ((HtmlTable) monitor.getFirstChild()).getRow(0));
//            HtmlTable left = (HtmlTable) table.getCell(0).getFirstChild();
//            HtmlTable right = (HtmlTable) table.getCell(1).getFirstChild();
//            int numWashers = left.getRowCount()/2;
//            int numDryers = left.getRowCount()/2;
//
//            Machine[] washers = new Machine[numWashers];
//            Machine[] dryers = new Machine[numDryers];
//
//            for(int i = 0; i < numWashers; i++) {
//                //The first row contains the machine number.
//                Machine m = new Machine(Machine.WASHER, Integer.parseInt(left.getRow(2 * i).getCell(1).asText()));
//                //The text for the machine is on the next row.
//                m.setStatusWithString(left.getRow(2 * i + 1).asText());
//                washers[i] = m;
//            }
//
//            for(int i = 0; i < numDryers; i++) {
//                //The first row contains the machine number.
//                Machine m = new Machine(Machine.DRYER, Integer.parseInt(left.getRow(2 * i).getCell(1).asText()));
//                //The text for the machine is on the next row.
//                m.setStatusWithString(left.getRow(2 * i + 1).asText());
//                dryers[i] = m;
//            }
//
//        } catch (FailingHttpStatusCodeException | IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }


}
