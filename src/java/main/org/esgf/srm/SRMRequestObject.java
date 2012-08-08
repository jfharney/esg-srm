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

/**
 * @author      Ekhlas Sonu <esonu@uga.edu>
 * @version     1.0                                    
 * @since       2012-08-07          
 */

public class SRMRequestObject {
	private String openId;
	private String proxyId;
	private String proxyPwd;
	private String url;
	private String mailTo;
	
	private static String mailPropFile = "./mail.properties"; //"./src/java/main/mail.properties";
	private static int minSleep = 10;
	private static int maxSleep = 600;
	
	/**
	 * Constructor                 
	 *
	 * Constructor that  takes the http request 
	 *  and extracts the required parameters from it:
	 *  URL, OpenId, ProxyId, Proxy Password, and 
	 *  users email id
	 *
	 * @param  request The HttpServletRequest object containing parameters 
	 * 
	 */
	
	public SRMRequestObject(HttpServletRequest request){
		this.openId = request.getParameter("openid");
		
		//If null or empty OpenId
		if(this.getOpenId()==null || this.getOpenId().contentEquals("")){
			System.out.println("Null openid");
		}
		
		this.proxyId = request.getParameter("proxyid");
		
		//If null or empty ProxyId
		if(this.getProxyId()==null || this.getProxyId().contentEquals("")){
			System.out.println("Null ProxId");
		}
		
		this.proxyPwd = request.getParameter("pass");
		
		//If null or empty Password
		if(this.getProxyPwd()==null || this.getProxyPwd().contentEquals("")){
			System.out.println("Null Proxy Password");
		}
	
		this.url = request.getParameter("url");
		
		//If null or empty url
		if(this.getUrl()==null || this.getUrl().contentEquals("")){
			System.out.println("Null URL");
			url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
					"SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL" +
					"/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-12.nc";
		}
		
		this.mailTo = request.getParameter("email");
		//If null or empty url
		if(this.getUrl()==null || this.getUrl().contentEquals("")){
			System.out.println("NULL to email id");
		}
	}
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getProxyId() {
		return proxyId;
	}
	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}
	public String getProxyPwd() {
		return proxyPwd;
	}
	public void setProxyPwd(String proxyPwd) {
		this.proxyPwd = proxyPwd;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getToemail() {
		return mailTo;
	}
	public void setToemail(String toemail) {
		this.mailTo = toemail;
	}

	/**
	 * Send confirmation of submission of SRM get request                
	 *
	 */
	private void sendSubmissionConfirmation(){
		String subject = "Your request has been submitted.";
		String body = "Your request for file(s) in link\n"+ url +"\n has been submitted to SRM. It may take some time to retreive the" +
         		" data. You will receive another email when the data is ready for download along with the download link. " +
         		"\nThe link will be active for about 4 days after which it will be deactivated and you will be asked to resubmit " +
         		"your request. \n\nThanks.";
        sendEmail(subject, body);
	}
	
	/**
	 * Send confirmation after the request has been executed               
	 *
	 */
	private void sendRequestCompletion(String turl){
		String subject = "Your request has been completed successfuly.";
		String body = "Your request for file(s) in link\n"+ url +"\n has been retreived successfully from SRM. You may download the " +
				"file(s) from the following link: \n" + turl + "\n "+
         		"\nThe link will be active for about 4 days after which it will be deactivated and you will be asked to resubmit " +
         		"your request. \n\nThanks.";
        sendEmail(subject, body);
	}
	
	
	/**
	 * Send notification if the request failed execute for any reason              
	 *
	 */
	private void sendRequestFailed(String status){
		String subject = "Your request could not be completed.";
		String body = "Your request for file(s) in link\n"+ url +"\n could not be completed by SRM. The reason for the failure was " +
				"cited as the error code \n" + status + ". Please try again later to check if the problem has been resolved." +
						"\n\nThanks.";
        sendEmail(subject, body);
	}
	
	/**
	 * Send email
	 * 
	 * Send email to the address in variable mailTo. 
	 * Reads file mail.properties to extract mailing options
	 * 
	 * @param subject subject of the email
	 * @param body the body of the email
	 */
	
	private void sendEmail(String subject, String body){
		String to = this.mailTo;
		String host="smtp.ornl.gov";
		String port="25";
		String from= "";
		String password = "";
		String user = "e1g";
		Properties prop = new Properties();
		try {
            prop.load(new FileInputStream(mailPropFile));

            
            host = prop.getProperty("mailHost");
            port = prop.getProperty("mailPort");
            from = prop.getProperty("mailFrom");
            user = prop.getProperty("user");
            
            password = prop.getProperty("password");
            
            List<String> argList = new ArrayList<String>();
            
            argList.add("-to");
            argList.add(to);
            System.out.println("email to: "+ to);
            
            argList.add("-from");
            argList.add(from);
            System.out.println("email from: "+ from);
            
            argList.add("-user");
            argList.add(user);
            System.out.println("user: "+ user);
            
            argList.add("-host");
            argList.add(host);
            System.out.println("host: "+ host);
            
            argList.add("-port");
            argList.add(port);
            System.out.println("port: "+ port);
            
            argList.add("-subject");
            argList.add(subject);
            System.out.println("subject: "+ subject);
            
            argList.add("-body");
            argList.add(body);
            System.out.println("body: "+ body);
            
            argList.add("-password");
            argList.add(password);
            
            
            String args[] = argList.toArray(new String[argList.size()]);
//            System.out.println("args[].length = "+args.length);
            
            for(int i=0; i < argList.size(); i++){
            	args[i] = argList.get(i);
            }
            EmailNotifier submitted = new EmailNotifier(args);
            
            submitted.sendMail();
            
            
	    } catch (Exception e) {
	    	System.out.println("SRMRequestObject->sendSubmissionConfirmation: Something went wrong while reading the property file");
	    	e.printStackTrace();
	    }
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
	public String runBeStManGetRequest() throws Exception{

		/**
		 * The return value. Initially assume that connection to server failed
		 */
		String retStr = "<srm_error>SRMInitializationError</srm_error>";
	    
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
	    
	    /**
	     * This part is necessary but currently throws an exception. 
	     * The exception is ignored.
	     */
	    String ttemp = System.getProperty("log4j.configuration");
	    System.out.println("ttemp = "+ ttemp);
	    if(ttemp != null && !ttemp.equals("")) {
	       log4jlocation = ttemp;
	    }
	    
//	    FileWriter logFile = new FileWriter("/tmp/esg-srm-log.out", true);
//	    BufferedWriter outLogFile = new BufferedWriter(logFile);
	    
	    /**
	     * Changed this portion to handle multiple urls
	     */
	    StringTokenizer st = new StringTokenizer(url,";");
	    
	    String[] surl = new String[st.countTokens()];
	    //String[] surl = new String[1];
	    int ctr=0;
	    while(st.hasMoreTokens()){
	    	surl[ctr] = st.nextToken();
	    	ctr++;
	    }
	    //surl[0] = url;
	    
	    if(surl==null || surl.length==0 || surl[0] == null || surl[0] == "" || url.indexOf("?") < 0) {
	       System.out.println("Please provide the correct surl");
	       retStr = "<srm_error>Invalid or null SURLS</srm_error>";
//	       outLogFile.write(retStr+"\n");
	       return retStr;
	    }
	    
	    /**
	     * The server of the url is the part of the first SURL 
	     * that appears before the ? sign.
	     * 
	     */
	    serverUrl = url.substring(0, url.indexOf("?"));
	    
	    System.out.println("Server URL = "+serverUrl);
//	    outLogFile.write("Server URL = "+serverUrl+"\n");
//	    outLogFile.write("SURL = "+"\n");
	    System.out.println("SURL = ");
	    for (int i=0; i<surl.length; i++){
	    	System.out.println(surl[i]);
//		    outLogFile.write(surl[i]+"\n");
	    }
	    
	    try{
	    	if(!storageInfo.equals("")) {
		       delegationNeeded=true;
		    }
		    SRMServer cc = new SRMServer(log4jlocation, logPath, debug, delegationNeeded);
		    
		    System.out.println("CC Initialized");
//		    outLogFile.write("CC Initialized"+"\n");
		    cc.connect(serverUrl);
		    System.out.println("Connection Established");
//		    outLogFile.write("Connection Established"+"\n");
		    cc.ping(uid);
		    SRMRequest req = new SRMRequest();
		    req.setSRMServer(cc);
		    req.setAuthID(uid);
		    req.setRequestType("get");
		    req.addFiles(surl, null,null);
		    req.setStorageSystemInfo(storageInfo);
		    req.setFileStorageType(fileType);
		    req.setRetentionPolicy(retentionPolicy);
		    req.setAccessLatency(accessLatency);
		    req.submit();
		   
		    
		    //Send Email Notifying that the request has been submitted. 
		    sendSubmissionConfirmation();
		    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		    
		    req.checkStatus();
		    int sleepTime  = minSleep;
		    SRMRequestStatus response = req.getStatus();
		    
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
		        		retStr = "<srm_error>" + response.getReturnStatus().getStatusCode().toString() + "</srm_error>";
		        		sendRequestFailed(response.getReturnStatus().getStatusCode().toString());
		        		cc.disconnect();
		        		return retStr;
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
    	                
    	             }
    	          }//end while
    	          cc.disconnect();
    	          //Notify by e-mail that request has been completed successfully.//
	              sendRequestCompletion(retStr);
	              //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	        		
    	       }//end if
		    	
		    }
		      
		    
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	    
	    StringTokenizer st2 = new StringTokenizer(retStr,";");
	    
	    /**
	     * Used to store the local location of the files on the server cache
	     * 
	     */
	    
	    ArrayList<String> serverFileLocation = new ArrayList<String>();
	    
	    while(st2.hasMoreTokens()){
	    	String tmpToken = st2.nextToken();
	    	if(tmpToken != null && tmpToken!="" && tmpToken.indexOf("/lustre")>=0){
	    		serverFileLocation.add(tmpToken.substring(tmpToken.indexOf("/lustre")));
	    		System.out.println("File on cache:" + tmpToken.substring(tmpToken.indexOf("/lustre")));
	    	}
	    }
	    
	    
	    
		return ("<srm_url>"+retStr+"</srm_url>");
	}
	
	
	/**
	 * Run BeStMan Ls request 
	 *<p>
	 *This method has been adopted from SRMDirTest.java file in the BeStMan api 
	 *@see https://codeforge.lbl.gov/frs/download.php/178/bestman2.java.api-2.0.0.tar.gz
	 *The plan is to use this code to generate a list of files and then use the list to 
	 *extract respective files.
	 *<p>
	 *Need to decide what needs to be done with the return value of the SRMLs request.
	 *
	 *@return retStr Currently doen't contain any information except if error occurs
	 */
	
	@SuppressWarnings("static-access")
	public String runBeStManLsRequest() throws Exception{

	    String retStr = "";
	    
	    String serverUrl = "";
	    String uid="";
	    String logPath="";
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
	    
	    String[] surl = new String[1];
	    surl[0] = url;
	    
	    if(surl == null || surl.length == 0) {
	       System.out.println("Please provide the surls");
	       retStr = "<srm_error>Invalid or null SURLS</srm_error>";
	       return retStr;
	    }
	    
	    serverUrl = url.substring(0, url.indexOf("?"));
	    
	    System.out.println("Server URL = "+serverUrl);
	    System.out.println("SURL = "+surl[0]);
	    
	    try{
	    	if(!storageInfo.equals("")) {
		       delegationNeeded=true;
		    }
		    SRMServer cc = new SRMServer(log4jlocation, logPath, debug, delegationNeeded);
		    
		    System.out.println("CC Initialized");
		    cc.connect(serverUrl);
		    System.out.println("Connection Established");
		    cc.ping(uid);
		    SRMRequest req = new SRMRequest();
		    req.setSRMServer(cc);
		    req.setAuthID(uid);
		    req.addFiles(surl, null,null);
		    req.setFileStorageType(fileType);
		    req.setRetentionPolicy(retentionPolicy);
		    req.setAccessLatency(accessLatency);
		    req.setStorageSystemInfo(storageInfo);
		      
		    req.srmLs();
	        req.checkStatus();
	        
	        int sleepTime  = minSleep;
		    SRMRequestStatus response = req.getStatus();
		    
		    if(response != null){
		    	while(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_QUEUED ||
		                response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_INPROGRESS){
		    		System.out.println("\nRequest.status="+response.getReturnStatus().getStatusCode());
			        System.out.println("request.explanation="+response.getReturnStatus().getExplanation());
			        
			    	System.out.println("SRM-CLIENT: Next status call in "+ sleepTime + " secs");
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
		        		retStr = "<srm_error>" + response.getReturnStatus().getStatusCode().toString() + "</srm_error>";
		        		cc.disconnect();
		        		return retStr;
		        	}
			    }
		    	System.out.println("\nStatus.code="+response.getReturnStatus().getStatusCode());
		        System.out.println("\nStatus.exp="+response.getReturnStatus().getExplanation());
		        
		        if(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_SUCCESS) {
		        	System.out.println("SRM-CLIENT: ............................");
		            System.out.println("SRM-CLIENT: ....Printing Results..........");
		            System.out.println("TimeStamp="+response.getTimeStamp());
		            System.out.println
		    			("RequestToken="+response.getRequestToken());
		            TReturnStatus rStatus = response.getReturnStatus();
		            if(rStatus != null) {
		              TStatusCode code = rStatus.getStatusCode();
		              System.out.println("Status="+code);
		              System.out.println("Explanation="+rStatus.getExplanation());
		            }
		            HashMap pathDetails = response.getPathDetails();
		            Collection pathDetailsCollection = pathDetails.values();
		            Object[] obj = pathDetailsCollection.toArray();
		            System.out.println("LS output");
		            for(int i = 0; i < obj.length; i++) {
		              PathDetail pDetails = (PathDetail) obj[i];
		              printMetaDataDetails("\t\t",pDetails);
		            }//end for(pathDetails.size() > 0)
		          
		          cc.disconnect();
		        }
		        
		    }
		    
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	    
		return retStr;
	}

	/**
	 * This was a part of SRMDirTest.java file in BeStMan api. 
	 * @see https://codeforge.lbl.gov/frs/download.php/178/bestman2.java.api-2.0.0.tar.gz
	 */
	
	private static void printMetaDataDetails(String prefix, PathDetail pDetails) 
		throws Exception {
     if(pDetails.getPath() != null) {
       System.out.println(prefix+"SURL="+ pDetails.getPath());
     }
     if(pDetails.getSize() != null) {
       System.out.println(prefix+"Bytes="+ pDetails.getSize());
     }
     if(pDetails.getFileType()  != null) {
       System.out.println(prefix+"FileType="+pDetails.getFileType());
     }
     if(pDetails.getFileStorageType() != null) {
       System.out.println(prefix+"StorageType="+pDetails.getFileStorageType());
     }
     if(pDetails.getStatus() != null) {
       TReturnStatus rStatus = pDetails.getStatus();
       TStatusCode code = rStatus.getStatusCode();
       System.out.println(prefix+"Status="+ code);
       if(rStatus.getExplanation () != null) {
         System.out.println(prefix+"Explanation="+ rStatus.getExplanation());
       }
     }
     if(pDetails.getCreatedAtTime() != null) {
       System.out.println(prefix+"CreatedAtTime");
       Calendar cal = pDetails.getCreatedAtTime();
       Date dd = cal.getTime();
       int year = dd.getYear()+1900;
       int month = dd.getMonth();
       int day = dd.getDate();
       int hour = dd.getHours();
       int minute = dd.getMinutes();
       int second = dd.getSeconds();
       System.out.println(prefix+"\tYear="+ year);
       System.out.println(prefix+"\tMonth="+ month);
       System.out.println(prefix+"\tDay="+ day);
       System.out.println(prefix+"\tHour="+ hour);
       System.out.println(prefix+"\tMinute="+ minute);
       System.out.println(prefix+"\tSecond="+ second);
     }
     if(pDetails.getLastModificationTime() != null) {
       System.out.println(prefix+"LastModificationTime");
       Calendar cal = pDetails.getLastModificationTime();
       Date dd = cal.getTime();
       int year = dd.getYear()+1900;
       int month = dd.getMonth();
       int day = dd.getDate();
       int hour = dd.getHours();
       int minute = dd.getMinutes();
       int second = dd.getSeconds();
       System.out.println(prefix+"\tYear="+ year);
       System.out.println(prefix+"\tMonth="+ month);
       System.out.println(prefix+"\tDay="+ day);
       System.out.println(prefix+"\tHour="+ hour);
       System.out.println(prefix+"\tMinute="+ minute);
       System.out.println(prefix+"\tSecond="+ second);
     }
     if(pDetails.getRetentionPolicyInfo() != null) {
       TRetentionPolicy retentionPolicy =
         pDetails.getRetentionPolicyInfo().getRetentionPolicy();
       TAccessLatency accessLatency =
         pDetails.getRetentionPolicyInfo().getAccessLatency();
       if(retentionPolicy != null && retentionPolicy.getValue() != null) {
          System.out.println(prefix+"RetentionPolicy="+ 
				retentionPolicy.getValue());
        }
        if(accessLatency != null && accessLatency.getValue() != null) {
          System.out.println(prefix+"AccessLatency="+ accessLatency.getValue());
        }
     }
     if(pDetails.getFileLocality() != null) {
       System.out.println(prefix+"FileLocality="+
          pDetails.getFileLocality().getValue());
     }
     if(pDetails.getArrayOfSpaceTokens() != null) {
       ArrayOfString arrayOfString = pDetails.getArrayOfSpaceTokens();
       String[] sss = arrayOfString.getStringArray();
       for(int j = 0; j < sss.length; j++) {
         System.out.println(prefix+"SpaceTokens["+j+"]="+sss[j]);
       }
     }
     if(pDetails.getLifeTimeAssigned()  != null) {
       Integer ii = pDetails.getLifeTimeAssigned();
       System.out.println(prefix+"LifeTimeAssigned="+ii.intValue());
     }
     if(pDetails.getLifeTimeLeft()  != null) {
       Integer ii = pDetails.getLifeTimeLeft();
       System.out.println(prefix+"LifeTimeLeft="+ii.intValue());
     }
     if(pDetails.getCheckSumType()  != null) {
       System.out.println(prefix+"CheckSumType="+pDetails.getCheckSumType());
     }
     if(pDetails.getCheckSumValue()  != null) {
       System.out.println(prefix+"CheckSumValue="+pDetails.getCheckSumValue());
     }
     if(pDetails.getOwnerPermission()  != null) {
       TUserPermission perm = pDetails.getOwnerPermission();
       System.out.println 
        (prefix+"OwnerPermission.getUserID="+perm.getUserID());
       TPermissionMode mode = perm.getMode();
       if(mode != null) {
         System.out.println
          (prefix+"OwnerPermission.getMode="+mode.toString());
       }
     }
     if(pDetails.getGroupPermission()  != null) {
       TGroupPermission perm = pDetails.getGroupPermission();
       System.out.println
        (prefix+"GroupPermission.getUserID="+perm.getGroupID());
       TPermissionMode mode = perm.getMode();
       if(mode != null) {
        System.out.println
        (prefix+"GroupPermission.getMode="+mode.toString());
       }
     }
     if(pDetails.getOtherPermission()  != null) {
       TPermissionMode perm = pDetails.getOtherPermission();
       if(perm != null) {
         System.out.println
          (prefix+"OtherPermission.getMode="+perm.toString());
      }
     }
     if(pDetails.getSubPath() != null) {
        PathDetail[] mp = pDetails.getSubPath();
        for(int i = 0; i < mp.length; i++) {
          printMetaDataDetails(prefix+"\t\t\t",mp[i]);
        }
     }
   }
}
