package org.esgf.srm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lbnl.SrmClientSoapBindingImpl;

import org.esgf.srm.utils.Utils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class SRMRequestController {

	private static String SRM_Server_host = "esg.css.ornl.gov";
	
	@RequestMapping(method=RequestMethod.GET, value="/srmrequest")
    public @ResponseBody String getSRMRequest(HttpServletRequest request) throws Exception {
		
		System.out.println("In ESG-SRM SRMRequestController HTTP GET: getSRMRequest");
		
		SRMObj srm = new SRMObj();
		
		srm.get();
		
		
/* Invoking the new implementation
	       try {
	
	    	   SrmClientSoapBindingImpl s = new SrmClientSoapBindingImpl();
	
	    	   s.srmGet("httpg://esg.ccs.ornl.gov:46790/srm/v2/server","gsiftp://esg.ccs.ornl.gov/lustre/esgfs/SRMTemp/test.data");

	       } catch(Exception e) {
	          e.printStackTrace();
	       }
*/		
		return "<response>" + Utils.responseMessage + "</response>\n";
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST, value="/srmrequest")
	//public ModelAndView addEmployee(@RequestBody String body) {
	public @ResponseBody String addSRMRequest(HttpServletRequest request,final HttpServletResponse response) {
		
		//return "<a>a</a>";
		System.out.println("In HTTP POST: addSRMRequest");
		

		SRMObj srm = new SRMObj();
		
		srm.get();
		
		return "<response>" + Utils.responseMessage + "</response>";
		
		/*
		System.out.println("In addSRMRequest");
		
		System.out.println("TIME: "+System.currentTimeMillis());
		String response1 = "<srm_url>srm</srm_url>";
		
		
		String [] urls = getUrls(request);
		
		
		//SRMRequestObject1 obj = new SRMRequestObject1(urls);
		SRMRequestObject obj = new SRMRequestObject(request);
		
		
	
		//Invoke BeStMan here
		//response = obj.runBeStManGetRequest();
		//obj.get();	
		//return response1;
		//return response;
		try {
			String resp = obj.runBeStManGetRequest();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		*/
		
		
	}
	
	
	private static String [] getUrls(HttpServletRequest request) {
		
		String url = request.getParameter("url");

		String [] urls = null;
		
		if(url == null) {
			urls = new String[2];
			
			urls[0] = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
					"SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL/CESM1" +
					"/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-01.nc";
			urls[1] = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
					"SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL" +
					"/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-12.nc";
		} else {
			urls = url.split(";");
			
			for(int i=0;i<urls.length;i++) {
				System.out.println("url: " + urls[i]);
			}
		}
		return urls;
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
		final MockHttpServletResponse mockResponse = new MockHttpServletResponse();

		//Set dummy parameter for testing purpose.
		/*
		
		String openId = "openid1";
		//
		//String url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
		//		"SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL/CESM1" +
		//		"/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-01.nc;" +
		//		"srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
		//		"SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL" +
		//		"/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-12.nc";
		//
		String url = "srm://esg.ccs.ornl.gov:46790/srm/v2/server?" +
				"SFN=mss://esg.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL/CESM1" +
				"/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-01.nc;";
		//
		//		"srm://esg.ccs.ornl.gov:46790/srm/v2/server?" +
		//		"SFN=mss://esg.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL" +
		//		"/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-12.nc";
		///
		String proxyId = "proxyid1";
		String password = "password";
		String toEmail = "";

		mockRequest.addParameter("openid", openId);
		mockRequest.addParameter("url", url);
	    mockRequest.addParameter("proxyid", proxyId);
	    mockRequest.addParameter("pass", password);
	    mockRequest.addParameter("email", toEmail);
	    
		SRMRequestController srm = new SRMRequestController();
		try {
			//String response = srm.addSRMRequest(mockRequest,mockResponse);
			srm.addSRMRequest(mockRequest,mockResponse);
			//System.out.println(response);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		*/
		
		
 	   
		
	    
	}
	
	
}




/*
System.out.println("In getSRMRequest controller");
System.out.println("TIME: "+System.currentTimeMillis());


for(Object key : request.getParameterMap().keySet()) {
	System.out.println("Key: " + (String) key);
}
*/	



/*
//System.out.println("OpenId=" + obj.getOpenId() +"\nProxyId="+ obj.getProxyId() + "\nPassword=" + obj.getProxyPwd() +"\nURL="+ obj.getUrl() + "\nEmail To:" + obj.getToemail());

//if(obj.getOpenId()==null||obj.getOpenId()==""){
//	response = "<srm_error>NullOpenId</srm_error>";
//	return response;
//}
//
//if(obj.getProxyId()==null||obj.getProxyId()==""){
//	response = "<srm_error>NullProxyId</srm_error>";
//	return response;
//}
//
//
//if(obj.getProxyPwd()==null||obj.getProxyPwd()==""){
//	response = "<srm_error>NullProxyPwd</srm_error>";
//	return response;
//}
//
//if(obj.getToemail()==null||obj.getToemail()==""){
//	response = "<srm_error>NullEmailAdress</srm_error>";
//	return response;
//}
*/	





/*
String response = "<srm_url>srm</srm_url>";

String [] urls = getUrls(request);

SRMRequestObject1 obj = new SRMRequestObject1(urls);

//System.out.println("OpenId=" + obj.getOpenId() +"\nProxyId="+ obj.getProxyId() + "\nPassword=" + obj.getProxyPwd() +"\nURL="+ obj.getUrl() + "\nEmail To:" + obj.getToemail());

//if(obj.getOpenId()==null||obj.getOpenId()==""){
//	response = "<srm_error>NullOpenId</srm_error>";
//	return response;
//}
//
//if(obj.getProxyId()==null||obj.getProxyId()==""){
//	response = "<srm_error>NullProxyId</srm_error>";
//	return response;
//}
//
//
//if(obj.getProxyPwd()==null||obj.getProxyPwd()==""){
//	response = "<srm_error>NullProxyPwd</srm_error>";
//	return response;
//}
//
//if(obj.getToemail()==null||obj.getToemail()==""){
//	response = "<srm_error>NullEmailAdress</srm_error>";
//	return response;
//}

//Invoke BeStMan here
//response = obj.runBeStManGetRequest();
obj.get();	

return response;
*/
