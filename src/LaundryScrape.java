import java.io.IOException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class LaundryScrape {

	public static void main(String[] args) {
		final WebClient webClient = new WebClient();
		HtmlPage page;
		try {
			page = webClient
					.getPage("http://classic.laundryview.com/laundry_room.php?view=c&lr=4997123");
			HtmlDivision monitor = (HtmlDivision) page.getElementById("classic_monitor");
			HtmlTableRow table = ((HtmlTableRow) ((HtmlTable) monitor.getFirstChild()).getRow(0));
			HtmlTable left = (HtmlTable) table.getCell(0).getFirstChild();
			HtmlTable right = (HtmlTable) table.getCell(1).getFirstChild();
			System.out.println(left.asXml());
			//TODO iterate through the table and generate Machine objects for each machine.
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}

	}

}

