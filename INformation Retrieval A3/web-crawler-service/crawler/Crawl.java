package crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Abhay Pai 
 *
 */
public class Crawl {

	
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	public Set<String> startCrawl(String url,int depth) {
		try{
		int counter = 0;

		while (counter < depth) {
			String currentURL;

			Crawling c = new Crawling();
			if (this.pagesToVisit.isEmpty()) {
				currentURL = url;
				this.pagesVisited.add(url);
			} else {
				currentURL = this.nextURL();
			}
			if (!(currentURL.equalsIgnoreCase("DepthReached!!"))) {
				c.crawler(currentURL);
			} else {
				counter++;
			}
			this.pagesToVisit.addAll(c.getLinks());
		}
		System.out.println("Total " + this.pagesVisited.size() + " web page(s) visited");
	}
	
	catch(Exception e)
	{
		e.printStackTrace();
	}
		return pagesVisited;
	}

	private String nextURL() {
		String nextURL;
		do {
			nextURL = this.pagesToVisit.remove(0);
		}

		while (this.pagesVisited.contains(nextURL));
		this.pagesVisited.add(nextURL);
		return nextURL;
	}
}
