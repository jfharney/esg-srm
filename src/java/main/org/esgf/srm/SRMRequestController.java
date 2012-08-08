package org.esgf.srm;

import java.io.IOException;

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
    public @ResponseBody String getSRMRequest(HttpServletRequest request) throws Exception {
		System.out.println("In getSRMRequest");
		System.out.println("TIME: "+System.currentTimeMillis());
		String response = "<srm_url>srm</srm_url>";
		
		SRMRequestObject obj = new SRMRequestObject(request);
				
		System.out.println("OpenId=" + obj.getOpenId() +"\nProxyId="+ obj.getProxyId() + "\nPassword=" + obj.getProxyPwd() +"\nURL="+ obj.getUrl() + "\nEmail To:" + obj.getToemail());
		
		
		//Invoke BeStMan here
		response = obj.runBeStManGetRequest();
				
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
	
	/**
	 * For testing purpose only      
	 *
	 * A dummy main program to be used for testing purpose
	 *     
	 */
	
	public static void main(String args[]){
		final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

		//Set dummy parameter for testing purpose.
		
		String openId = "openid1";
		String url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
				"SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL/CESM1" +
				"/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-01.nc;" +
				"srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
				"SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL" +
				"/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-12.nc";
		String proxyId = "proxyid1";
		String password = "password";
		String toEmail = "ekhlas.sonu@gmail.com";

		mockRequest.addParameter("openid", openId);
		mockRequest.addParameter("url", url);
	    mockRequest.addParameter("proxyid", proxyId);
	    mockRequest.addParameter("pass", password);
	    mockRequest.addParameter("email", toEmail);
	    
		SRMRequestController srm = new SRMRequestController();
		try {
			String response = srm.getSRMRequest(mockRequest);
			System.out.println(response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	    
	}
	
	
}
