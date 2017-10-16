package ir.apache.lucene;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import crawler.Crawl;

/**
 * Implements Web Crawler System
 * 
 * @author Ranjan
 *
 */
public class TestLucene {

	/**
	 * Execution of Information Retrieval System begins here.
	 * 
	 * @param args
	 *            Path to document folder
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Path indexPath = null;// To store path_to_index_folder
		Set<String> urlSet = new HashSet<String>();
		//String url = "http://www.magdeburgindians.com";
		//System.out.println("hello1");
		Crawl crawl = new Crawl();
		//System.out.println("hello2");
			try {
					
					boolean exit = false;
					Scanner userInput = new Scanner(System.in);
					int choice = 0;
					//System.out.println("hello3");
					// Now prompt Menu to user for Indexing and Searching
					urlSet = crawl.startCrawl("http://www.magdeburgindians.com",1);
					//System.out.println("hello4");
					while (!exit) {
						System.out.print("\n\nSelect the operation to continue:");
						System.out.print("\n1.Indexing");
						System.out.print("\n2.List Parsed Files");
						System.out.print("\n3.Searching");
						System.out.println("\n4.Exit");

						if (userInput.hasNextInt())
							choice = userInput.nextInt();

						switch (choice) {
						case 1:
							/**
							 * Indexing
							 */
							for(String url : urlSet)
							{
								//indexPath = IndexFiles.IndexingURL(url,"nobody1");
							}
							break;
						case 2:
							/**
							 * Printing Parsed Files
							 */
							if (indexPath == null) {
								System.out.println(
										"There is no index available at the moment. Please create an Index first.");
								System.out.println();
								break;
							} else {
								System.out.println("\n\nThe Parsed Files are listed below:");
								IndexFiles.getParsedDocs(indexPath);
								break;
							}

						case 3:
							/**
							 * Search for a query
							 */

							try {
								if (indexPath == null) {
									System.out.println(
											"There is no index available at the moment. Please create an Index first.");
									break;
								} else {
									SearchFiles.searching(indexPath,"magdeburg");
								}

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						case 4:
							exit = true;
							break;
						
						default:
							System.out.print("\nWrong Choice!! Please provide a correct input.");
							exit = true;
							break;

						}

					}
					userInput.close();// closing the Scanner

				}
			/*}*/ catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}


