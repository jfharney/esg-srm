/** 
 *
 * BeStMan Copyright (c) 2007-2008, The Regents of the University of California,
 * through Lawrence Berkeley National Laboratory (subject to receipt of any
 * required approvals from the U.S. Dept. of Energy).  All rights reserved.
 *
 * If you have questions about your rights to use or distribute this software,
 * please contact Berkeley Lab's Technology Transfer Department at TTD@lbl.gov.
 *
 * NOTICE.  This software was developed under partial funding from the
 * U.S. Department of Energy.  As such, the U.S. Government has been
 * granted for itself and others acting on its behalf a paid-up,
 * nonexclusive, irrevocable, worldwide license in the Software to
 * reproduce, prepare derivative works, and perform publicly and
 * display publicly.  Beginning five (5) years after the date permission
 * to assert copyright is obtained from the U.S. Department of Energy,
 * and subject to any subsequent five (5) year renewals, the
 * U.S. Government is granted for itself and others acting on its
 * behalf a paid-up, nonexclusive, irrevocable, worldwide license in
 * the Software to reproduce, prepare derivative works, distribute
 * copies to the public, perform publicly and display publicly, and
 * to permit others to do so.
 *
 * Email questions to SRM@LBL.GOV
 * Scientific Data Management Research Group
 * Lawrence Berkeley National Laboratory
 *
*/

/**
 * @author      Ekhlas Sonu <esonu@uga.edu>
 * @version     1.0                                    
 * @since       2012-08-07          
 */

package org.esgf.srm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import gov.lbl.srm.StorageResourceManager.*;
import gov.lbl.srm.client.wsdl.*;

import org.apache.log4j.PropertyConfigurator;
import org.esgf.legacy.EmailNotifier;
import org.esgf.legacy.Emailer;
import org.esgf.legacy.Emailer1;
import org.esgf.srm.email.Email;
import org.esgf.srm.email.EmailUtils;

/**
 * @author      Ekhlas Sonu <esonu@uga.edu>
 * @version     1.0                                    
 * @since       2012-08-07          
 */

public class SRMRequestObject1 {
	
	
	private Email initialEmail;
	private Email responseEmail;
	
	private String [] urls;
	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	
	private String [] t_urls;
	
	private static int minSleep = 10;
	
	private static int maxSleep = 600;
	
	private String file_request_type;
	/*
	private String openId;
	private String proxyId;
	private String proxyPwd;
	private String url;
	private String mailTo;
	
	private String [] urls;
	
	private static String mailPropFile = "./src/java/main/mail.properties"; //"./mail.properties";
	private static int minSleep = 10;
	private static int maxSleep = 600;
	*/
	
	public SRMRequestObject1() {
		this.urls = null;
		//this.file_ids = null;
		this.t_urls = null;
		
		//initialize things here
		setInitialEmail(new Email());
		setResponseEmail(new Email());
		
	}
	
	public SRMRequestObject1(String [] urls,String file_request_type) {
		this.urls = urls;
		//this.file_ids = file_ids;
		this.t_urls = null;
		
		this.file_request_type = file_request_type;
		
				
	}
	
	
	
	/**
	 * Run BeStMan GET request to extract the file at url from SRM server
	 *<p>
	 *This method has been adopted from SRMGetTest.java file in the BeStMan api 
	 *@see https://codeforge.lbl.gov/frs/download.php/178/bestman2.java.api-2.0.0.tar.gz
	 *Multiple source url (surl) can be passed with the get request in the array. Again see 
	 *original file. 
	 *
	 *@return retStr url of the retrieved file in the cache or failure message
	 */
	
	@SuppressWarnings("static-access")
	public SRMResp runBeStManGetRequest() throws Exception{

		SRMResp srm_response = new SRMResp();
		
		//message
		String message = "<srm_error>SRMInitializationError</srm_error>";
	    
		//return string
		String retStr = null;
		
		
		/**
		 * The following variables and subsequent code is adopted from the BeStMan api
		 */
	    String serverUrl = "";
	    String uid="";
	    String logPath="/tmp/esg-srm.log";
	    String log4jlocation="";
	    String storageInfo="";
	    String fileType="volatile";
	    String retentionPolicy="replica";
	    String accessLatency="online";
	    boolean debug = false;
	    boolean delegationNeeded=false;
		
	    String ttemp = System.getProperty("log4j.configuration");
	    System.out.println("ttemp = "+ ttemp);
	    if(ttemp != null && !ttemp.equals("")) {
	       log4jlocation = ttemp;
	    }
	    
	    
	    
	    String[] surl = new String[this.urls.length];
	    
	    for(int i=0;i<this.urls.length;i++) {
	    	//surl[i] = SRMUtils.stripIndex(this.urls[i]);
	    	surl[i] = SRMUtils.transformServerName(this.urls[i]);
	    	System.out.println("surl[" + i + "]: " + surl[i]);
	    }
	    
	    
	    if(surl==null || surl.length==0 || surl[0] == null || surl[0] == "") {
	       System.out.println("Please provide the correct surl");
	       message = "<srm_error>Invalid or null SURLS</srm_error>";
	       
		}
	    
	    /**
	     * The server of the url is the part of the first SURL 
	     * that appears before the ? sign.
	     * 
	     */
		//for(int i=0;i<)
	    serverUrl = SRMUtils.extractServerName(surl[0]);
	    
	    System.out.println("\n\n\tTRYING BESTMAN REQUEST to server" + serverUrl + "\n\n");
		
	    
		
	    try{
	    	if(!storageInfo.equals("")) {
		       delegationNeeded=true;
		    }
	    	System.out.println("\n\n\ttrying...");
		    SRMServer cc = new SRMServer(log4jlocation, logPath, debug, delegationNeeded);
		    System.out.println("Credential name: " + cc.getCredential().getName());
		    System.out.println("CC Initialized");
//		    outLogFile.write("CC Initialized"+"\n");
		    cc.connect(serverUrl);
		    System.out.println("Connection Established");
//		    outLogFile.write("Connection Established"+"\n");
		    
		    SRMRequest req = new SRMRequest();
		    req.setSRMServer(cc);
		    req.setAuthID(uid);
		    req.setRequestType("get");
		    
		    System.out.println("surl: " + surl[0]);
		    
		    req.addFiles(surl, null,null);
		    req.setStorageSystemInfo(storageInfo);
		    req.setFileStorageType(fileType);
		    req.setRetentionPolicy(retentionPolicy);
		    req.setAccessLatency(accessLatency);
		    System.out.println("Submitting...\n\n");
		    
		    System.out.println("\n\nOLD IMPLEMENTATION");
			
		    String str = "";
			
			str += "uid: " + uid + "\n";
			str += "logPath: " + logPath + "\n";
			str += "log4jlocation: " + log4jlocation + "\n";
			str += "storageInfo: " + storageInfo + "\n";
			str += "fileType: " + fileType + "\n";
			str += "retentionPolicy: " + retentionPolicy + "\n";
			str += "accessLatency: " + accessLatency + "\n";
			str += "debug: " + debug + "\n";
			str += "delegationNeeded: " + delegationNeeded + "\n";
			for(int i=0;i<surl.length;i++) {
				str += "file_url: " + surl[i] + "\n";
			}
			str += "server_url: " + serverUrl;

			System.out.println(str);
			
		    System.out.println("\n\nEND OLD IMPLEMENTATION\n\n\n\n");
		    
		    req.submit();
		    
		    req.checkStatus();
		    int sleepTime  = minSleep;
		    SRMRequestStatus response = req.getStatus();
		    
		    String [] response_file_urls = new String[this.urls.length];
		    
		    if(response != null){
		    	while(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_QUEUED ||
		                response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_INPROGRESS){
		    		System.out.println("\nRequest.status="+response.getReturnStatus().getStatusCode());
//		    		outLogFile.write("\nRequest.status="+response.getReturnStatus().getStatusCode()+"\n");
			        System.out.println("request.explanation="+response.getReturnStatus().getExplanation());
//			        outLogFile.write("request.explanation="+response.getReturnStatus().getExplanation()+"\n");
			        
			    	System.out.println("SRM-CLIENT: Next status call in "+ sleepTime + " secs");
//			    	outLogFile.write("SRM-CLIENT: Next status call in "+ sleepTime + " secs"+"\n");
		        	Thread.currentThread().sleep(sleepTime * 1000);
		        	sleepTime*=2;
		        	
		        	if(sleepTime>=maxSleep){
		        		sleepTime=maxSleep;
		        	}
		        	
		        	//CHECK STATUS AGAIN
		        	req.checkStatus();
				    response = req.getStatus();
		        	
				    
				    //If failed to extract then exit
		        	if(!(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_SUCCESS || 
		        			response.getReturnStatus().getStatusCode() == TStatusCode.SRM_FILE_PINNED ||
		        			response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_QUEUED ||
		        			response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_INPROGRESS)){
		        		System.out.println("SRM failed to extract file. Exiting.");
		        		message = "<srm_error>" + response.getReturnStatus().getStatusCode().toString() + "</srm_error>";
		        		System.out.println("status code: " + response.getReturnStatus().getStatusCode().toString());
		        		System.out.println("explanation: " + response.getReturnStatus().getExplanation());
		        		//sendRequestFailed(response.getReturnStatus().getStatusCode().toString());
		        		cc.disconnect();
		        		
		        	}
		        	
		        	
			    }
		    	System.out.println("\nStatus.code="+response.getReturnStatus().getStatusCode());
		        System.out.println("\nStatus.exp="+response.getReturnStatus().getExplanation());
		        
		    	if(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_SUCCESS ||
    	          response.getReturnStatus().getStatusCode() == TStatusCode.SRM_FILE_PINNED) {
		    		retStr="";
    	          HashMap map = response.getFileStatuses();
    	          Set set = map.entrySet();
    	          Iterator i = set.iterator();
    	          int j=0;
    	          while(i.hasNext()) {
    	             Map.Entry me = (Map.Entry) i.next();
    	             String key =  (String) me.getKey();
    	             Object value = me.getValue();
    	             if(value != null) {
    	                FileStatus fileStatus = (FileStatus) value;
    	                org.apache.axis.types.URI uri = fileStatus.getTransferSURL();
    	                System.out.println("\nTransferSURL="+uri);
    	                org.apache.axis.types.URI uri2 = fileStatus.getTURL();
    	                System.out.println("\nTURL="+uri2);
    	                System.out.println("Pin Time:"+fileStatus.getPinLifeTime());	
     	                
    	                
    	               
    	                retStr += (uri.toString()+";");	//Return value
    	                //response_file_urls[j] = uri.toString();
    	             }
    	          }//end while
    	          cc.disconnect();
    	          //Notify by e-mail that request has been completed successfully.//
	              //sendRequestCompletion(retStr);
    	          
    	          //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	        		
    	       }//end if
		    	
		    }
		    
		    
	    }catch(Exception e) {
	    	System.out.println("\n\n\tping failed...");
	    	e.printStackTrace();
	    }
			
			
		if(retStr != null) {
			String [] response_urls = retStr.split(";");
			
			
			//determine the url name
			if(this.file_request_type.equals("http")) {
				
				response_urls = SRMUtils.gridftp2httpArr(response_urls);
				
			}
			
			srm_response.setResponse_urls(response_urls);
			message = "Doing fine";
			
		}
		srm_response.setMessage(message);
		
		//return retStr;
		return srm_response;
	}

	/**
	 * @return the responseEmail
	 */
	public Email getResponseEmail() {
		return responseEmail;
	}

	/**
	 * @param responseEmail the responseEmail to set
	 */
	public void setResponseEmail(Email responseEmail) {
		this.responseEmail = responseEmail;
	}
	
	
	public Email getInitialEmail() {
		return initialEmail;
	}

	public void setInitialEmail(Email initialEmail) {
		this.initialEmail = initialEmail;
	}
	
	
	public String getInitialEmailText() {
		String text = "";
		
		text += "Your request for file(s):\n";
		
		for(int i=0;i<this.urls.length;i++) {
			text += this.urls[i] + "\n";
		}
		
		text += "\n has been submitted to SRM. It may take some time to retreive the" +
 		" data. You will receive another email when the data is ready for download along with the download link. " +
 		"\nThe link will be active for about 4 days after which it will be deactivated and you will be asked to resubmit " +
 		"your request. \n\n";
		
		return text;
	}
	
	public String getWgetEmailText() {
		String text = "";
		
		text += "Your request for file(s):\n";
		
		for(int i=0;i<this.urls.length;i++) {
			text += this.urls[i] + "\n";
		}
		
		text += "\n has been successfully retrieved from the deep storage archive.  Please use the attached wget script. \n\n";
		
		return text;
	}

	
	
}
