/**
 * SrmClientSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package lbnl;

import gov.lbl.srm.StorageResourceManager.*;


public class SrmClientSoapBindingImpl implements lbnl.SrmClient{

    public static void main(String [] args) {
       System.out.println("Hello");

       
       try {

    	   SrmClientSoapBindingImpl s = new SrmClientSoapBindingImpl();

    	   s.srmGet("httpg://esg.ccs.ornl.gov:46790/srm/v2/server","gsiftp://tmp/test.data");

       } catch(Exception e) {
          e.printStackTrace();
       }
       
    }

    public String srmGet(java.lang.String srmServiceUrl, java.lang.String src) throws java.rmi.RemoteException
    {
	TSRMStubHolder srm = getStub(srmServiceUrl);
	if (srm == null) {
	    return "Error:No stub!";
	}

	try {
	    SrmPrepareToGetRequest r = new SrmPrepareToGetRequest();
	    int size = 1; // one pair of src/tgt here
	    TGetFileRequest[] fileReqList = new TGetFileRequest[size];

	    fileReqList[0] = new TGetFileRequest();
	    fileReqList[0].setSourceSURL(helper.createTSURL(src));
	    //fileReqList[0].setTargetSURL(helper.createTSURL(tgt));

	    ArrayOfTGetFileRequest col = new ArrayOfTGetFileRequest();
	    col.setRequestArray(fileReqList);
	    r.setArrayOfFileRequests(col);

	    SrmPrepareToGetResponse result = srm.getStub().srmPrepareToGet(r);

	    if (result == null) {
		return "Error: null response from server";
	    }
	    TStatusCode code = result.getReturnStatus().getStatusCode();
	    String exp = result.getReturnStatus().getExplanation();

	    System.out.println("return code="+code+", exp="+exp);

	    /*if (code == TStatusCode.SRM_REQUEST_QUEUED) {
		// wait forever
	    }*/
	    return result.getRequestToken();
	} catch (Exception e) {
	    e.printStackTrace();
	    return "Exception from server";
	}
	    
    }

    public String srmCopy(java.lang.String srmServiceUrl, java.lang.String src, java.lang.String tgt) throws java.rmi.RemoteException
    {
	TSRMStubHolder srm = getStub(srmServiceUrl);
	if (srm == null) {
	    return "Error: No stubs!";
	}

	try {
	    SrmCopyRequest r = new SrmCopyRequest();
	    int size = 1; // one pair of src/tgt here
	    TCopyFileRequest[] fileReqList = new TCopyFileRequest[size];

	    fileReqList[0] = new TCopyFileRequest();
	    fileReqList[0].setSourceSURL(helper.createTSURL(src));
	    fileReqList[0].setTargetSURL(helper.createTSURL(tgt));

	    r.setOverwriteOption(TOverwriteMode.NEVER);

	    ArrayOfTCopyFileRequest col = new ArrayOfTCopyFileRequest();
	    col.setRequestArray(fileReqList);
	    r.setArrayOfFileRequests(col);

	    SrmCopyResponse result = srm.getStub().srmCopy(r);

	    if (result == null) {
		return "Error: null response from server";
	    }

	    TStatusCode code = result.getReturnStatus().getStatusCode();
	    String exp = result.getReturnStatus().getExplanation();

	    System.out.println("return code="+code+", exp="+exp);

	    
	    return result.getRequestToken();
	} catch (Exception e) {
	    e.printStackTrace();
	    return "Server threw exception";
	}
	    
    }
	

    private TSRMStubHolder getStub(String srmServiceUrl) {
	String proxyPath = "/tmp/x509up_u";
	try {
	    TSRMStubHolder srm = helper.getValidStub(srmServiceUrl);
	    if (srm == null) {
		System.out.println(" Unable to talk to remote srm: "+srmServiceUrl);
	    }
	    return srm;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public java.lang.String srmPing(java.lang.String srmServiceUrl) throws java.rmi.RemoteException 
    {
	String result= "srmPing() to:"+srmServiceUrl;
	String proxyPath = "/tmp/x509up_u";
	try {
	    //String uid = helper.getUID();
	    //proxyPath += uid;
	    //result += "And cred="+helper.getCredential(proxyPath);
	    TSRMStubHolder srm = helper.getValidStub(srmServiceUrl);
	    if (srm == null) {
		return result+" Unable to talk to remote srm: "+srmServiceUrl;
	    }
	   
	    SrmPingRequest req = new SrmPingRequest();
	    SrmPingResponse response = srm.getStub().srmPing(req);
	    if (response == null) {
		return result+" Unable to get response from srm";
	    } else {
		return result+" successful. version="+response.getVersionInfo();
	    }
	    
	} catch (Exception e) {
	    e.printStackTrace();
	    return "Error: unable to call srmPing";
	}
    }
}
