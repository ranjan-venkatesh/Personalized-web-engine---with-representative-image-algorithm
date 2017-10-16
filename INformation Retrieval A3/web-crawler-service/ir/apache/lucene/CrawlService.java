package ir.apache.lucene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import crawler.Crawl;

/** Implements Crawl Service using Apache Lucene */

/**
 * @author Ranjan Venkatesh
 *
 */
public class CrawlService {
	
	/**
	 * Executes crawling service using a specified url by calling IndexFiles.indexingURL() in case if the url is not yet indexed.In either case, the following process handles the searching part for the given query in indexed files.  The procedure is followed for each user
	 * 
	 * 
	 * @param url
	 *            url to index or search or crawl through
	 * @param depth
	 *            crawling depth for the given url
	 * @param query
	 *            query for the search in indexed files
	 * @param user
	 *            user name for the separation of indexing directories
	 * 
	 * @return result List of search results in Json form
	 *
	 */
	public String executeService(String url,String depth,String query,String user)
	{
		Crawl crawl = new Crawl();
		Set<String> urlSet = new HashSet<String>();
		String userDirectory = "default";
		Boolean urlIndexed = false;
		if(!user.trim().equals(""))
			userDirectory= user.trim();
		Path indexPath = Paths.get("/home/"+userDirectory+"/Documents/WebCrawlerUsingLucene/Indexes/");
		if (!Files.exists(indexPath)) {
			try{
				Files.createDirectories(indexPath);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		try {
			urlIndexed = checkIfUrlIndexed(url,indexPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List searchResultDataList = new ArrayList();
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		Set set = null;
		
		if(!urlIndexed)
		{
			System.out.println("URL is not indexed!!");
			urlSet = crawl.startCrawl(url,Integer.parseInt(depth));
			for(String urlUnit : urlSet)
			{
				indexPath = IndexFiles.indexingURL(urlUnit,user.trim(),indexPath);
			}
		}
		try {
			searchResultDataList=SearchFiles.searching(indexPath,query);
			set = new HashSet(searchResultDataList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			result = mapper.writeValueAsString(set);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result; 
	}

	/**
	 * checks if given url is already indexed
	 * 
	 * 
	 * @param url
	 *            url to index or search or crawl through
	 * @param indexPath
	 *            path to the index files directory
	 * 
	 * @return urlIndexed boolean value depicting if the given url is indexed
	 * @throws Exception
	 *             If there is any Exception
	 */
	private Boolean checkIfUrlIndexed(String url, Path indexPath) throws IOException {
		// TODO Auto-generated method stub
		Boolean urlIndexed=false;
		IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath));// create IndexReader
		for (int i=0; i<reader.maxDoc(); i++) {
		    Document doc = reader.document(i);
		    
		    String indexUrl = doc.get("path");
		    if(indexUrl.contains(url) || indexUrl.equals(url))
		    {
		    	
		    	System.out.println("URL is indexed!!");
				
		    	urlIndexed = true;
		    	break;
		    }
		    
}
		
		return urlIndexed;
	}
}
