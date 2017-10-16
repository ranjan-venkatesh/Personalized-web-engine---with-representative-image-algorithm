package com.grp10.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ir.apache.lucene.CrawlService;

/**
 * @author Ranjan Venkatesh
 *
 */
@Controller
public class WebCrawlerController {

	private String urlGlobal=null;
	

	/**
	 * Default URL after succesfull login 
	 * 
	 * @return render search page
	 * 
	 */
	
	@RequestMapping(value = "/search**", method = RequestMethod.GET)
	public ModelAndView adminPage() {

		ModelAndView model = new ModelAndView();
		model.setViewName("search");

		return model;

	}


	/**
	 * Default URL for login 
	 * 
	 * @return render login page
	 * 
	 */
	
	@RequestMapping(value ={"/","/login"}, method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("login");

		return model;

	}
	
	/**
	 * AJAX URL for loading the search result data
	 * 
	 * @return JSon object of the returned search data
	 * 
	 */
	
	@RequestMapping(value = "/queryForm",method = RequestMethod.POST)
	@ResponseBody
	public String getSearchResultViaAjax(@RequestParam("url") String url,@RequestParam("query") String query,@RequestParam("depth") String depth,@RequestParam("user") String user,HttpSession session){
		Boolean oldIndex = true;
		System.out.println(url);
		System.out.println(query);
		System.out.println(depth);
		System.out.println(oldIndex);
		//urlGlobal = url;
		System.out.println(session.getAttribute("urlGlobal"));
		
		CrawlService service = new CrawlService();
		String res=null;
		if(session.getAttribute("urlGlobal") == null)
			{
			oldIndex = false;
			session.setAttribute("urlGlobal",url.trim().toLowerCase());
			}
		else 
		{
			if(!session.getAttribute("urlGlobal").equals(url.trim().toLowerCase()))
			{
			oldIndex = false;
			session.setAttribute("urlGlobal",url.trim().toLowerCase());
			}
		}
		System.out.println(oldIndex);
		res=service.executeService(url.trim().toLowerCase(), depth.trim(), query.trim(),user.trim());
		
		return res;
	}

}