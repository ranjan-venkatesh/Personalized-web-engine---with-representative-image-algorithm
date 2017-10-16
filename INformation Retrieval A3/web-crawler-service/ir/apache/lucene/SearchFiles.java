package ir.apache.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/** Implements Search using Apache Lucene */

/**
 * @author Ranjan Venkatesh
 *
 */
public class SearchFiles {

	/**
	 * Private Constructor
	 */
	private SearchFiles() {
	}

	/**
	 * Executes Search using a specified Index by calling doPagingSearch(....)
	 * internally
	 * 
	 * @param indexPath
	 *            Path to document folder
	 * @param queryString
	 *            query string to search in index files
	 * @return searchResultDataList List of search results
	 * @throws Exception
	 *             If there is any Exception
	 */
	public static List<SearchResultData> searching(Path indexPath, String queryString) throws Exception {

		String field = "contents";

		int hitsPerPage = 50;// number of results to be displayed at once

		IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath));

		IndexSearcher searcher = new IndexSearcher(reader);

		Analyzer analyzer = new EnglishAnalyzer();

		BufferedReader in = null;

		List searchResultDataList = new ArrayList();

		/**
		 * Initializing BufferedReader
		 */

		in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		System.out.println(indexPath.toAbsolutePath());
		QueryParser parser = new QueryParser(field, analyzer);
		while (true) {
			if (queryString == null) { // prompt the
										// user
				System.out.println("Enter query!!");
			}

			String line = queryString != null ? queryString : in.readLine();

			if (line == null || line.length() == -1) {
				break;
			}

			line = line.trim();
			if (line.length() == 0) {
				break;
			}

			Query query = parser.parse(line);
			System.out.println("Searching for: " + query.toString(field) + "(Stem of-" + line + ")");

			searchResultDataList = doPagingSearch(in, searcher, query, hitsPerPage, queryString == null);

			if (queryString != null) {
				break;
			}
		}
		reader.close();

		return searchResultDataList;
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 *
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 * @param in
	 *            BufferedReader to read input given
	 * @param searcher
	 *            To search through indexed documents
	 * @param query
	 *            Search query
	 * @param hitsPerPage
	 *            Number of results to be displayed at once
	 * @param interactive
	 *            Check if there is any input
	 * @return searchResultDataList List of search results
	 * @throws IOException
	 *             If there is a low-level I/O error
	 */
	public static List<SearchResultData> doPagingSearch(BufferedReader in, IndexSearcher searcher,
			Query query, int hitsPerPage, boolean interactive) throws IOException/* , ParseException */ {

		List<SearchResultData> searchResultDataList = new ArrayList<SearchResultData>();
		// Collect enough documents to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits
						+ " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);
			System.out.println("Rank\tURL\t\t\t\t\t  Score\tTitle(for HTML files)\t Summary(for HTML files)");
			System.out.println(
					"-----------------------------------------------------------------------------------------------------------------------------------------------------------------");

			for (int i = start; i < end; i++) {
				Document doc = new Document();
				SearchResultData searchResultData = new SearchResultData();
				doc = searcher.doc(hits[i].doc);
				searchResultData.setUrl(doc.get("path"));
				searchResultData.setTitle(doc.get("title"));
				searchResultData.setSummary(doc.get("summary"));
				searchResultData.setImageUrl(doc.get("imageUrl"));
				searchResultDataList.add(searchResultData);
				float relevantScore = hits[i].score;
				System.out.print((i + 1) + ".\t" + doc.get("path") + "\t" + relevantScore + "\t\t  " + doc.get("title")
						+ "\t" + doc.get("summary") + "\t" + doc.get("imageUrl") + "\t");
			}

			if (!interactive || end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;

				while (true) {
					System.out.print("Press (q)uit to search another query");
					if (start - hitsPerPage >= 0) {
						System.out.print(",(p)revious page,or enter number to jump to a page. ");
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print(",(n)ext page,or enter number to jump to a page. ");
					}
					System.out.println(" ");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0) == 'q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start += hitsPerPage;
						}
						break;
					} else {
						try {
							int page = Integer.parseInt(line);
							if ((page - 1) * hitsPerPage < numTotalHits) {
								start = (page - 1) * hitsPerPage;
								break;
							} else {
								System.out.println("No such page");
							}
						} catch (java.lang.NumberFormatException exception) {
							System.out.println("Undesired input !");
							System.exit(-1);
						}

					}
				}
				if (quit)
					break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
		return searchResultDataList;
	}

}
