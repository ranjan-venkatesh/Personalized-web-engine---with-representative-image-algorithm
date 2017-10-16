package ir.apache.lucene;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/** Implements Indexing using Apache Lucene */
/**
 * @author Ranjan Venkatesh
 *
 */
public class IndexFiles {

	/**
	 * Private Constructor
	 */

	private IndexFiles() {
	}

	/**
	 * Executes Indexing of all HTML files under a URL path , by calling indexurls(...) internally
	 * 
	 * @param url
	 *            URL Path to HTML document
	 * @param user
	 * 			  user name for directory identity
	 * @param indexPath
	 * 			  directory where index file are saved
	 *       
	 *           
	 * @return indexPath
	 */
	
	public static Path indexingURL(String url,String user,Path indexPath) {
		boolean create = false;
		
           
                Date start = new Date();
                try {
                System.out.println("\nIndexing to directory '" + indexPath + "'...\n");
                Directory dir = FSDirectory.open(indexPath);

    			/**
    			 * EnglishAnalyzer implements PorterStemmer Algorithm using
    			 * PorterStemFilter
    			 */
    			Analyzer analyzer = new EnglishAnalyzer();

    			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

    			if (create) {
    				// Create a new index in the directory, removing any
    				// previously indexed documents:
    				iwc.setOpenMode(OpenMode.CREATE);
    			} else {
    				/**
    				 * This block is unused. Kept for future modifications
    				 */
    				// Add new documents to an existing index:
    				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
    			}

    			// Optional: for better indexing performance, if you
    			// are indexing many documents, increase the RAM
    			// buffer. But if you do this, increase the max heap
    			// size to the JVM (eg add -Xmx512m or -Xmx1g):
    			//
    			// iwc.setRAMBufferSizeMB(256.0);

    			IndexWriter writer = new IndexWriter(dir, iwc);
    			indexurls(writer, url);

    			// NOTE: if you want to maximize search performance,
    			// you can optionally call forceMerge here. This can be
    			// a terribly costly operation, so generally it's only
    			// worth it when your index is relatively static (ie
    			// you're done adding documents to it):
    			//
    			// writer.forceMerge(1);

    			writer.close();

    			Date end = new Date();
    			System.out.println("\nFinished indexing in " + (end.getTime() - start.getTime()) + " total milliseconds");

            } catch (IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
                
                return indexPath;
		}
	
	/**
	 * Indexes the given HTML file using the given writer.
	 *
	 * @param writer
	 *            Writer to the index where the given file/dir info will be
	 *            stored
	 * @param url
	 *            The HTML document to index
	 * @throws IOException
	 *             If there is a low-level I/O error
	 */
	
	public static void indexurls(final IndexWriter writer, String url) throws IOException {
		 Document doc = new Document();
			org.jsoup.nodes.Document htmlDoc = null;
			
			try{
				htmlDoc = Jsoup.connect(url).get();
				Field urlField = new StringField("path", url, Field.Store.YES);
				doc.add(urlField);
				Field titleField = new StringField("title", htmlDoc.title(), Field.Store.YES);
				doc.add(titleField);
				Field bodyField = new TextField("contents", new BufferedReader(new InputStreamReader(new ByteArrayInputStream(htmlDoc.body().text().getBytes()), StandardCharsets.UTF_8)));
				doc.add(bodyField);
				Field summaryField = new StringField("summary", htmlDoc.body().getElementsByTag("summary").text(), Field.Store.YES);
				doc.add(summaryField);
				
				//RepresentativeImageFetcher fetches the most representative image for that URL applying the algorithm
				Field imageUrlField = new StringField("imageUrl",representativeImageFetcher(url),Field.Store.YES);
				doc.add(imageUrlField);
				
				if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
					// New index, so we just add the document (no old document can
					// be there):
					System.out.println("adding " + url);
					writer.addDocument(doc);
				} else {
					/**
					 * This block is unused. Kept for future modifications
					 */
					// Existing index (an old copy of this document may have been
					// indexed) so
					// we use updateDocument instead to replace the old one matching
					// the exact
					// path, if present:
					System.out.println("updating " + url);
					writer.updateDocument(new Term("path", url), doc);
				}

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	
	/**
	 * Ranks the top most representative images for the HTML Doc and returns the most representative one 
	 *
	 * @param url
	 *           The HTML document resource
	 *  
	 *  @return srcImg
	 */
	
	public static String representativeImageFetcher(String url)
	{
		org.jsoup.nodes.Document htmlDoc = null;
		String srcLink = null;
		StringUtils stringUtils = new StringUtils();
		int count = 0;
		HashMap<String,String> imageLinkMap = new HashMap<String,String>();
		try{
			htmlDoc = Jsoup.connect(url).get();
			Element content = htmlDoc.body();
			String title = htmlDoc.title().trim();
			
			for (Element e : content.select("img"))
			{
				if(count==6)
					break;
				else
				{
					srcLink = e.attr("src");
				    if(srcLink.contains("://") && !(srcLink.contains("?") || srcLink.contains("ads.") || srcLink.contains("ad.")))
				    {
				    imageLinkMap.put(srcLink, "0");
				    String trimmedTitle = stringUtils.deleteWhitespace(title.toLowerCase().trim());
				    String trimmedAlt = stringUtils.deleteWhitespace(e.attr("alt").toLowerCase().trim());
				    String trimmedTitleAttr = stringUtils.deleteWhitespace(e.attr("title").toLowerCase().trim());
				    String trimmedId = stringUtils.deleteWhitespace(e.attr("id").toLowerCase().trim());
					   
					    if(stringUtils.contains(trimmedTitle, trimmedAlt) || stringUtils.contains(trimmedTitle, trimmedTitleAttr))
					    {
					    	imageLinkMap.replace(srcLink, Integer.toString(Integer.parseInt(imageLinkMap.get(srcLink))+1)); 
					    }
					    if(stringUtils.contains(trimmedId, "logo"))
					    {
					    	imageLinkMap.replace(srcLink, Integer.toString(Integer.parseInt(imageLinkMap.get(srcLink))+1)); 
					    }
					    if(!(e.attr("width").equals("")||e.attr("height").equals(" ")))
					    {
					    	if(Integer.parseInt(e.attr("width"))>=100 && Integer.parseInt(e.attr("height"))>=100)
					    	imageLinkMap.replace(srcLink, Integer.toString(Integer.parseInt(imageLinkMap.get(srcLink))+1)); 
					    }
				    }
				    count++;
				}		
			}
			
			//Voting to pick the most representative image
			List<Integer> voteList = new ArrayList<Integer>();
			for(Map.Entry<String, String> entry : imageLinkMap.entrySet())
			{
				voteList.add(Integer.parseInt(entry.getValue()));
				System.out.println("-------"+entry.getKey()+" ---------------"+entry.getValue()+"--------");
			}
			Collections.sort(voteList);
			Collections.reverse(voteList);
			for(Map.Entry<String, String> entry : imageLinkMap.entrySet())
			{
				if(entry.getValue() == Integer.toString(voteList.get(0)))
					{
						srcLink = entry.getKey();
						break;
					}
			}
			
			if(!(srcLink.startsWith("http://")||srcLink.startsWith("https://")))
			{
				srcLink = "https://www.wsdot.wa.gov/NR/rdonlyres/F3D31D0F-C137-41F7-9C67-9EBDFF8E3627/0/NotAvailable.jpg";
			}
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return srcLink;
	}

	/**
	 * Printing Parsed Documents
	 * 
	 * @param indexPath
	 *            Path to index folder
	 */
	public static void getParsedDocs(Path indexPath) {
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath));
			Document tempDoc = null;
			for (int i = 0; i < reader.numDocs(); i++) {
				tempDoc = reader.document(i);
				System.out.println((i + 1) + ". " + tempDoc.get("path"));
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
