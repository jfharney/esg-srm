package org.esgf.srm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;
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
		
		SRMRequestObject obj = new SRMRequestObject(request);
		
//		obj.setOpenId(request.getParameter("openid"));
//		obj.setProxyId(request.getParameter("proxyid"));
//		obj.setProxyPwd(request.getParameter("pass"));
//		obj.setUrl(request.getParameter("url"));
		
		System.out.println("OpenId=" + obj.getOpenId() +"\nProxyId="+ obj.getProxyId() + "\nPassword=" + obj.getProxyPwd() +"\nURL="+ obj.getUrl());
		
		
		//TODO: Invoke BeStMan Here
		obj.formBeStManCopyRequest();
		
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
	
	public static void main(String args[]){
		final MockHttpServletRequest mockRequest7 = new MockHttpServletRequest();

		String openid7 = "openid1";
		String name7 = "name1";
//		String body7 = BookmarkVars.datacartPayloadTest1;
		mockRequest7.addParameter("openid", openid7);
		mockRequest7.addParameter("name", name7);
//	    mockRequest7.addParameter("body", body7);
//	    mockRequest7.addParameter("db", DB_TYPE);
	    
	    
	}
	
	
}
