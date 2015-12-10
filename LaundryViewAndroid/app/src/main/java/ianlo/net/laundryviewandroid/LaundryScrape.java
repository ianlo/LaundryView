package ianlo.net.laundryviewandroid;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import java.io.IOException;
import java.util.logging.Level;

public class LaundryScrape {
	public class Node {
		
	}
	public static void main(String[] args) {
		final WebClient webClient = new WebClient();

	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	    
		HtmlPage page;
		try {
			page = webClient
					.getPage("http://classic.laundryview.com/laundry_room.php?view=c&lr=4997123");
		    
			HtmlDivision monitor = (HtmlDivision) page.getElementById("classic_monitor");
			HtmlTableRow table = ((HtmlTableRow) ((HtmlTable) monitor.getFirstChild()).getRow(0));
			HtmlTable left = (HtmlTable) table.getCell(0).getFirstChild();
			HtmlTable right = (HtmlTable) table.getCell(1).getFirstChild();
			int numWashers = left.getRowCount()/2;
			int numDryers = left.getRowCount()/2;
			
			Machine[] washers = new Machine[numWashers];
			Machine[] dryers = new Machine[numDryers];
			
			for(int i = 0; i < numWashers; i++) {
				//The first row contains the machine number.
				Machine m = new Machine(Machine.WASHER, Integer.parseInt(left.getRow(2 * i).getCell(1).asText()));
				//The text for the machine is on the next row.
				setStatus(m, left.getRow(2 * i + 1).asText());
				washers[i] = m;
			}
			
			for(int i = 0; i < numDryers; i++) {
				//The first row contains the machine number.
				Machine m = new Machine(Machine.DRYER, Integer.parseInt(left.getRow(2 * i).getCell(1).asText()));
				//The text for the machine is on the next row.
				setStatus(m, left.getRow(2 * i + 1).asText());
				dryers[i] = m;
			}

		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}

	}
	
	public static Machine setStatus(Machine m, String machineText) {
		//Set the status of the machines based on what is in the scraped text.
		if (machineText.contains("time remaining")) {
			m.setStatus(Machine.RUNNING);
			//Scrape the time remaining from the text and set it to the machine's time remaining.
			int startIndex = machineText.indexOf("remaining") + 10;
			int endIndex = machineText.indexOf("min") - 1;
			m.setTimeRemaining(Integer.parseInt(machineText.substring(startIndex, endIndex)));
		} 
		else if (machineText.contains("out of service")) {
			m.setStatus(Machine.OUTOFSERVICE);
		}
		else if (machineText.contains("ended")) {
			m.setStatus(Machine.ENDED);
		}
		else {
			m.setStatus(Machine.AVAILABLE);
		}
		return m;
	}

}

