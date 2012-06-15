package org.esgf.srm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dw.spring3.rest.bean.Employee;

@Controller
public class SRMRequestController {

	
	@RequestMapping(method=RequestMethod.GET, value="/srmrequest")
    public @ResponseBody String getSRMRequest(HttpServletRequest request) {
		System.out.println("In getSRMRequest");
		String response = "<srm_url>srm</srm_url>";
		
		String openId = request.getParameter("openid");
		String proxy = request.getParameter("proxy");
		
		System.out.println("OpenId=" + openId +"\nProxy="+proxy);
	
		return response;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/srmrequest")
	//public ModelAndView addEmployee(@RequestBody String body) {
	public @ResponseBody void addSRMRequest(@RequestBody String body, HttpServletRequest request) {
		System.out.println("In addSRMRequest");
		
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/srmrequest")
	public @ResponseBody void removeEmployee(HttpServletRequest request) {
		System.out.println("In removeSRMRequest");
		
	}
	
	
}
