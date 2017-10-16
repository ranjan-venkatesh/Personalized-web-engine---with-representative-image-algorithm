package ir.apache.lucene;

public class CrawlServiceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CrawlService service = new CrawlService();
		String res=null;
		res=service.executeService("http://www.magdeburgindians.com", "1", "about","ranjan5");
		
		System.out.println(res);
	}

}
