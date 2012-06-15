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
		
		SRMRequestObject obj = new SRMRequestObject();
		
		obj.setOpenId(request.getParameter("openid"));
		obj.setProxyId(request.getParameter("proxyid"));
		obj.setProxyPwd(request.getParameter("pass"));
		obj.setUrl(request.getParameter("url"));
		
		System.out.println("OpenId=" + obj.getOpenId() +"\nProxyId="+ obj.getProxyId() + "\nPassword=" + obj.getProxyPwd() +"\nURL="+ obj.getUrl());
		
		if(obj.getOpenId()==null || obj.getOpenId().contentEquals("")){
			System.out.println("Null openid");
		}
	
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
