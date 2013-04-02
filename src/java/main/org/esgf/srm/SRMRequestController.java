package org.esgf.srm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import lbnl.legacy.SrmClientSoapBindingImpl;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.esgf.srm.email.Attachment;
import org.esgf.srm.email.Email;
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

	
	private static boolean isProduction = false;

	private static boolean serviceTest = false;
	
	private static boolean oldImpl = false;
	
	private SRMReq srm_request;
	private SRMResp srm_response;
	private Bestman bestman;
	
	private String file_request_type;
	private String openid;
	
	@RequestMapping(method=RequestMethod.POST, value="/srmrequest")
	//public ModelAndView addEmployee(@RequestBody String body) {
	public @ResponseBody String addSRMRequest(HttpServletRequest request,final HttpServletResponse response) {
		
		for(Object key : request.getParameterMap().keySet() ) {
			System.out.println("Key: " + (String) key);
		}
		
		//grab params
		System.out.println("In HTTP POST: addSRMRequest");
		
		String openid = request.getParameter("openid");
		if(openid == null) {
			this.openid = "jfharney";
		} else {
			this.openid = openid;
		}

		String length = request.getParameter("length");
		if(length == null) {
			length = "1";
		}
		
		String file_request_type = request.getParameter("file_request_type");
		if(file_request_type == null) {
			this.file_request_type = "http";
			//file_request_type = "http";
		} else if(file_request_type.equals("http")){
			this.file_request_type = "http";
		} else if(file_request_type.equals("gridftp")) {
			this.file_request_type = "gridftp";
		} else if(file_request_type.equals("globusonline")) {
			this.file_request_type = "globusonline";
		} else {
			this.file_request_type = "http";
		}
		
		
		
		String [] file_urls = null; 
		
		System.out.println("Length: " + length);
		
		if(length.equals("1")) {
			file_urls = new String[1];
			String file_url = request.getParameter("url");
			System.out.println("file_url: " + file_url);
			file_urls[0] = file_url;
		} 
		else {
			file_urls = new String[Integer.parseInt(length)];
			String [] urls = request.getParameterValues("url");
			for(int i=0;i<urls.length;i++) {
				file_urls[i] = urls[i];
			}
		}

			
		System.out.println("running in production?..." + isProduction);
		
		for(int i=0;i<file_urls.length;i++) {
			System.out.println("file_url: " + i + " " + file_urls[i]);
		}
		
		this.bestman = new Bestman(file_urls);
		if(isProduction) {
			System.out.println(this.bestman.toString());
			this.bestman.get();
			
			//send the response from bestman
			this.srm_response = this.bestman.getSrm_response();
			
			System.out.println("\nSRM RESPONSE\n\n" + this.srm_response.toXML() + "\n\n\n");
		} else {
			srm_response = SRMUtils.simulateSRM(file_urls);
		}
		
		
			
		if(srm_response == null) {
			return "<srm_response>" + Utils.responseMessage + "</srm_response>";
		} else {
			
			//System.out.println(new XmlFormatter().format(srm_response.toXML()) + "\n");
			return new XmlFormatter().format(srm_response.toXML()) + "\n";
		}
		
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

		
		if(serviceTest) {
			String queryString = //"url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc" +
					 
					"&url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc" +
					/*			 "&url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-11.nc" + 
								 "&url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-12.nc" +
								 "&url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-01.nc" + 
								 "&url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-02.nc" + 
								 "&url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-03.nc" + 
								 "&url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-04.nc" +
								 "&url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-05.nc" +
					*/			 "&url=srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-06.nc" +
					
								 "&length=2";
					//"&length=10";

			
			String url = "http://localhost:8080/esg-srm/service/srmrequest?";
			
			String full_url = url + queryString;
			
			full_url += "&file_request_type=gridftp";
			
			// create an http client
	        HttpClient client = new HttpClient();

	        //attact the dataset id to the query string
	        PostMethod method = new PostMethod(full_url);
	        
	        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
	                new DefaultHttpMethodRetryHandler(3, false));
	        
	        String responseBody = null;
	        
	        try {
	            // execute the method
	            int statusCode = client.executeMethod(method);

	            System.out.println("statusCode: " + statusCode);
	            
	            if (statusCode != HttpStatus.SC_OK) {
	                    System.out.println("Method failed: " + method.getStatusLine());

	            }

	            // read the response
	            responseBody = method.getResponseBodyAsString();
	        } catch (HTTPException e) {
	            System.out.println("Fatal protocol violation");
	            e.printStackTrace();
	        } catch (IOException e) {
	        	System.out.println("Fatal transport error");
	            e.printStackTrace();
	        } finally {
	            method.releaseConnection();
	        }

		} else {
			
			
			String [] url = {
								"srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc",
								"srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-06.nc"
							};
			
			String length = "2";

			
			mockRequest.addParameter("url", url);
			mockRequest.addParameter("length", length);
			
			SRMRequestController sc = new SRMRequestController();
			String srm_responseXML = sc.addSRMRequest(mockRequest, mockResponse);
			
			SRMResp srm_response = new SRMResp();
			srm_response.fromXML(srm_responseXML);
			
			System.out.println("Response message: " + srm_response.getMessage());
			
		}
	    
	}
	
	private static void printResultFiles(String [] results) {
		for(int i=0;i<results.length;i++) {
			System.out.println("Result: " + i + " " + results[i]);
		}
	}




	/**
	 * @return the srm_request
	 */
	public SRMReq getSrm_request() {
		return srm_request;
	}




	/**
	 * @param srm_request the srm_request to set
	 */
	public void setSrm_request(SRMReq srm_request) {
		this.srm_request = srm_request;
	}




	/**
	 * @return the srm_response
	 */
	public SRMResp getSrm_response() {
		return srm_response;
	}




	/**
	 * @param srm_response the srm_response to set
	 */
	public void setSrm_response(SRMResp srm_response) {
		this.srm_response = srm_response;
	}




	/**
	 * @return the bestman
	 */
	public Bestman getBestman() {
		return bestman;
	}




	/**
	 * @param bestman the bestman to set
	 */
	public void setBestman(Bestman bestman) {
		this.bestman = bestman;
	}
	
	
	public void writeInitialEmail() {
		//first email
		
		Email email1 = new Email();
		Attachment attachment1 = new Attachment();
		attachment1.setAttachmentName("wget.sh");
		attachment1.setAttachmentContent("New wget content");
		email1.setAttachment(attachment1);
		email1.setHeaderText("Your request for data has been submitted.");
		
		String bodyStr = "";
		bodyStr += "Your data request for the following files:\n";
		for(int i=0;i<this.srm_request.getFile_urls().length;i++) {
			bodyStr += "\t" + this.srm_request.getFile_urls()[i] + "\n";
		}
		bodyStr += "\nHas been submitted.  Please note that it may take some time to extract the data off tertiary storage and onto the local filesystem.  A confirmation email will be sent to this address soon with instructions on how to access this data.";
		email1.setBodyText(bodyStr);
		
		if(isProduction) {
			email1.sendEmail();
		} else {
			System.out.println(email1);
		}
		
	}
	
	
	public void writeConfirmEmail() {
		String openid = this.openid;
		String file_request_type = this.file_request_type;
		String [] responseUrls = this.srm_response.getResponse_urls();

		
		if(file_request_type.equals("gridftp")) {
			//write the confirm email with gridftp attachement
		} else if(file_request_type.equals("globusonline")) {
			//write the confirm email with the globus online attachment
			
		} else {
			//write the confirm email with the wget attachement
			
			//second email
			Email email2 = new Email();
			
			//assemble the header text here
			email2.setHeaderText("Your request for data has been successfully staged!");
			
			//assemble the body text here
			String bodyStr = "";
			bodyStr += "Your request for data has been successfully staged!\n";
			bodyStr += "The data you requested was the following:\n";
			for(int i=0;i<this.srm_request.getFile_urls().length;i++) {
				bodyStr += "\t" + this.srm_request.getFile_urls()[i] + "\n";
			}
			bodyStr += "\nAttached is a wget get script that may be run on any shell.\n";
			
			
			
		}
		
		
	}
	
	
	public void writeConfirmationEmail() {
		//second email
		Email email2 = new Email();
		
		//assemble the header text here
		email2.setHeaderText("Your request for data has been successfully staged!");
		
		//assemble the body text here
		String bodyStr = "";
		bodyStr += "Your request for data has been successfully staged!\n";
		bodyStr += "The data you requested was the following:\n";
		for(int i=0;i<this.srm_request.getFile_urls().length;i++) {
			bodyStr += "\t" + this.srm_request.getFile_urls()[i] + "\n";
		}
		if(this.srm_request.equals("http")) {
			bodyStr += "\nAttached is a wget get script that may be run on any shell.\n";
		} else {
			bodyStr += "\nPlease follow the URL below to initiate accessing the data using the Globus Online workflow ";
		}
		bodyStr += "If you have any additional questions, contact your institution's node's administrator or the ESGF technical team at esgf-user@lists.llnl.gov.\n\n";
		//bodyStr += "ESGF team\n";
		//bodyStr += srm_response.getMessage();
		email2.setBodyText(bodyStr);
		
		//if there are no urls, dont send the attachment, just the message
		if(srm_response.getResponse_urls() != null) {
			Attachment attachment2 = new Attachment();
			attachment2.setAttachmentName("wget.sh");
			String wgetContent = "";
			for(int i=0;i<srm_response.getResponse_urls().length;i++) {
				wgetContent += "wget " + srm_response.getResponse_urls()[i] + "\n";
			}
			attachment2.setAttachmentContent(wgetContent);
			email2.setAttachment(attachment2);
		}
		
		if(isProduction) {
			email2.sendEmail();
		} else {
			System.out.println(email2);
		}
		
	}
	
	
}



/*
private void runOldImpl(String [] file_urls) {

	//dbug
	this.srm_request = new SRMReq(file_urls);
	
	
	this.bestman = new Bestman(this.srm_request);
	
	
	
	//run srm workflow
	if(file_urls != null) {

		//first email
		//System.out.println("Sending initial email");
		
		Email email1 = new Email();
		Attachment attachment1 = new Attachment();
		attachment1.setAttachmentName("wget.sh");
		attachment1.setAttachmentContent("New wget content");
		email1.setAttachment(attachment1);
		email1.setHeaderText("EMAIL1");
		email1.setBodyText("EMAILBODY1 - sent from srm request controller");
		
		if(isProduction) {
			email1.sendEmail();
		} else {
			System.out.println(email1);
		}
		
		
		
		SRMRequestObject1 srm = new SRMRequestObject1(file_urls,file_request_type);
		
		
		SRMResp srm_response = null;
		if(isProduction) {
			System.out.println("About to send request to bestman...");
			
			try {
				srm_response = srm.runBeStManGetRequest();
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {

			System.out.println("Simulating SRM processing...");
			
			if(file_urls != null) {
				
				srm_response = SRMUtils.simulateSRM(file_urls);
				String [] response_urls = SRMUtils.gridftp2httpArr(srm_response.getResponse_urls());
				
				
				response_urls = SRMUtils.replaceCacheNames(response_urls);
				
				if(file_request_type.equals("http")) {
					srm_response.setResponse_urls(response_urls);
				}
				
			} 
			System.out.println(srm_response);
			
		}
		
		
		
		
		
		//second email
		Email email2 = new Email();
		
		//assemble the header text here
		email2.setHeaderText("EMAIL2");
		
		//assemble the body text here
		String bodyText = "EMAILBODY2 - from srm controller\n";
		bodyText += srm_response.getMessage();
		email2.setBodyText(bodyText);
		
		//if there are no urls, dont send the attachment, just the message
		if(srm_response.getResponse_urls() != null) {
			Attachment attachment2 = new Attachment();
			attachment2.setAttachmentName("wget.sh");
			String wgetContent = "";
			for(int i=0;i<srm_response.getResponse_urls().length;i++) {
				wgetContent += "wget " + srm_response.getResponse_urls()[i] + "\n";
			}
			attachment2.setAttachmentContent(wgetContent);
			email2.setAttachment(attachment2);
		}
		
		if(isProduction) {
			email2.sendEmail();
		} else {
			System.out.println(email2);
		}
		
	}
}
*/



/* need to implement these tests
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







/* Invoking the new implementation
try {

	   SrmClientSoapBindingImpl s = new SrmClientSoapBindingImpl();

	   s.srmGet("httpg://esg.ccs.ornl.gov:46790/srm/v2/server","gsiftp://esg.ccs.ornl.gov/lustre/esgfs/SRMTemp/test.data");

} catch(Exception e) {
   e.printStackTrace();
}
*/	



/* old srmrequestobject1 implementation


//run srm workflow
if(file_urls != null) {

	//first email
	//System.out.println("Sending initial email");
	
	Email email1 = new Email();
	Attachment attachment1 = new Attachment();
	attachment1.setAttachmentName("wget.sh");
	attachment1.setAttachmentContent("New wget content");
	email1.setAttachment(attachment1);
	email1.setHeaderText("EMAIL1");
	email1.setBodyText("EMAILBODY1 - sent from srm request controller");
	
	if(isProduction) {
		email1.sendEmail();
	} else {
		//System.out.println(email1);
	}
	
	
	
	SRMRequestObject1 srm = new SRMRequestObject1(file_urls,file_request_type);
	
	System.out.println("Simulating SRM processing...");
	
	SRMResponse srm_response = null;
	if(isProduction) {
		System.out.println("About to send request to bestman...");
		
		try {
			srm_response = srm.runBeStManGetRequest();
		} catch(Exception e) {
			e.printStackTrace();
		}
	} else {
		
		if(file_urls != null) {
			for(int i=0;i<file_urls.length;i++) {
				System.out.println("file_url: " + i + " " + file_urls[i]);
			}
			
			srm_response = SRMUtils.simulateSRM(file_urls);
			String [] response_urls = SRMUtils.gridftp2httpArr(srm_response.getResponse_urls());
			
			
			response_urls = SRMUtils.replaceCacheNames(response_urls);
			
			if(file_request_type.equals("http")) {
				srm_response.setResponse_urls(response_urls);
			}
			
		} 

		
		
		System.out.println(srm_response);
		
	}
	
	
	
	
	
	//second email
	Email email2 = new Email();
	
	//assemble the header text here
	email2.setHeaderText("EMAIL2");
	
	//assemble the body text here
	String bodyText = "EMAILBODY2 - from srm controller\n";
	bodyText += srm_response.getMessage();
	email2.setBodyText(bodyText);
	
	//if there are no urls, dont send the attachment, just the message
	if(srm_response.getResponse_urls() != null) {
		Attachment attachment2 = new Attachment();
		attachment2.setAttachmentName("wget.sh");
		String wgetContent = "";
		for(int i=0;i<srm_response.getResponse_urls().length;i++) {
			wgetContent += "wget " + srm_response.getResponse_urls()[i] + "\n";
		}
		attachment2.setAttachmentContent(wgetContent);
		email2.setAttachment(attachment2);
	}
	
	if(isProduction) {
		email2.sendEmail();
	} else {
		//System.out.println(email2);
	}
	
	
	
	
	
}
*/



