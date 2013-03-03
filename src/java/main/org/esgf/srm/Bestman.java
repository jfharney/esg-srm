package org.esgf.srm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import gov.lbl.srm.StorageResourceManager.TStatusCode;
import gov.lbl.srm.client.wsdl.FileStatus;
import gov.lbl.srm.client.wsdl.SRMRequest;
import gov.lbl.srm.client.wsdl.SRMRequestStatus;
import gov.lbl.srm.client.wsdl.SRMServer;

public class Bestman {

	private static final int MIN_SLEEP = 10;
	private static final int MAX_SLEEP = 600;
	
	private SRMReq srm_request;
	private SRMResp srm_response;
	
	public Bestman() {
		this.srm_request = new SRMReq();
		this.srm_response = new SRMResp();
	}
	
	public Bestman(SRMReq srm_request) {
		this.srm_request = srm_request;
		this.srm_response = new SRMResp();
		
	}
	
	public Bestman(String [] file_urls) {
		this.srm_request = new SRMReq(file_urls);
		this.srm_response = new SRMResp();
	}

	public void get() {

	    System.out.println("END GET()");
	    
	    String message = "";

    	String retStr = "";
    	
    	String [] response_file_urls = new String[this.srm_request.getFile_urls().length];
	    
	    try{
	    	if(!this.srm_request.getStorageInfo().equals("")) {
		       this.srm_request.setDelegationNeeded(true);
		    }
	    	System.out.println("\n\n\ttrying...");
		    SRMServer cc = new SRMServer(this.srm_request.getLog4jlocation(), 
		    							 this.srm_request.getLogPath(), 
		    							 this.srm_request.isDebug(),
		    							 this.srm_request.isDelegationNeeded());
		    
		    
		    System.out.println("Credential name: " + cc.getCredential().getName());
		    System.out.println("Server url: " + this.srm_request.getServer_url());
		    
		    System.out.println("CC Initialized");
//		    outLogFile.write("CC Initialized"+"\n");
		    cc.connect(this.srm_request.getServer_url());
		    System.out.println("Connection Established");
		    
		    
		    
		    SRMRequest req = new SRMRequest();
		    req.setSRMServer(cc);
		    req.setAuthID(this.srm_request.getUid());
		    req.setRequestType("get");
		    
		    
		    req.addFiles(this.srm_request.getFile_urls(), null,null);
		    req.setStorageSystemInfo(this.srm_request.getStorageInfo());
		    req.setFileStorageType(this.srm_request.getFileType());
		    req.setRetentionPolicy(this.srm_request.getRetentionPolicy());
		    req.setAccessLatency(this.srm_request.getAccessLatency());
		    System.out.println("Submitting...\n\n");
		    
		    System.out.println(this.toString());
		    
		    req.submit();
		    
		    req.checkStatus();
		    
		    int sleepTime  = MIN_SLEEP;
		    
		    
		    SRMRequestStatus response = req.getStatus();
		    
		    
		    
	    	
	    	
		    if(response != null) {
		    	while(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_QUEUED ||
		                response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_INPROGRESS){
		    
		    		System.out.println("\nRequest.status="+response.getReturnStatus().getStatusCode());
			        System.out.println("request.explanation="+response.getReturnStatus().getExplanation());
//			        outLogFile.write("request.explanation="+response.getReturnStatus().getExplanation()+"\n");
			        System.out.println("Request urls (first 3)");
			        for(int i=0;i<this.srm_request.getFile_urls().length;i++) {
			        	if(i < 3) {
			        		System.out.println("url: " + this.srm_request.getFile_urls()[i]);
			        	}
			        }
			    	System.out.println("SRM-CLIENT: Next status call in "+ sleepTime + " secs");
		        	Thread.currentThread().sleep(sleepTime * 1000);
		        	sleepTime*=2;
		    	
		        	if(sleepTime>=MAX_SLEEP){
		        		sleepTime=MAX_SLEEP;
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
		    				
		    				//org.apache.axis.types.URI uri2 = fileStatus.getTURL();
		    				
		    				//System.out.println("\nTURL="+uri2);
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
		    
		    System.out.println("ReturnStr: " + retStr);
			
			if(retStr != null) {
				String [] response_urls = retStr.split(";");
				
				this.srm_response.setResponse_urls(response_urls);
				message = "Doing fine";
				
			}
			
		    
		    
	    } catch(Exception e) {
	    	System.out.println("\n\n\tping failed...");
	    	e.printStackTrace();
	    }

	    
	    System.out.println("END GET()");
	    
		this.srm_response.setMessage(message);
		
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
	
	public String toString() {
		String str = "";
		
		str += "Request:";
		str += this.srm_request.toString();
		
		return str;
	}
	
}
