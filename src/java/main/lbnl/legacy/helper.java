package lbnl.legacy;

import java.util.Properties;
import javax.security.auth.login.*;
import java.io.*;
import org.gridforum.jgss.*;
import org.ietf.jgss.GSSCredential;
import gov.lbl.srm.StorageResourceManager.*;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.SimpleTargetedChain;
import org.globus.axis.transport.*;
import org.apache.axis.transport.http.HTTPSender;
import java.net.URL;
import org.globus.axis.util.Util;
 
public class helper {

	/*
    public static org.apache.axis.types.URI createTSURL(String surl) {
	try {
	    return new org.apache.axis.types.URI(surl);
	} catch (org.apache.axis.types.URI.MalformedURIException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static GSSCredential getValidCredential() {
	String proxyPath = "/tmp/x509up_u";

	try {
	    String uid = helper.getUID();
	    proxyPath += uid;

	    GSSCredential cred = helper.getCredential(proxyPath);

	    if (cred == null) {
		System.out.println("NO cred found!");
		return null;
	    }
	    if (cred.getRemainingLifetime() == 0) {
		System.out.println("Expired credential");
		return null;
	    }
	    return cred;
	} catch (Exception e) {
	    e.printStackTrace();
	    System.out.println("Unable to get credential");
	    return null;
	}
    }

    public static TSRMStubHolder getValidStub(String srmServiceUrl) {
	TSRMStubHolder srm = new TSRMStubHolder(srmServiceUrl);

	if (srm.getStub() == null) {
	    System.out.println("Unable to get ISRM handler");
	    srm = null;
	    return null;
	}

	GSSCredential cred = helper.getValidCredential();

	if (cred == null) {
	    srm = null;
	    return null;
	}
	srm.useCredential(cred);
	return srm;
    }

    public static GSSCredential getCredential(String proxyPath) 
    {
	try {
	    File proxyFile = new File(proxyPath);

	    byte[] data = new byte[(int)proxyFile.length()];
	    FileInputStream in = new FileInputStream (proxyFile);

	    in.read(data);
	    in.close();

	    ExtendedGSSManager manager = (ExtendedGSSManager) ExtendedGSSManager.getInstance();

	    GSSCredential cred = manager.createCredential(data,
							  ExtendedGSSCredential.IMPEXP_OPAQUE,
							  GSSCredential.DEFAULT_LIFETIME,
							  null,
							  GSSCredential.INITIATE_AND_ACCEPT);
	    return cred;
	} catch (Exception e) {
	    System.out.println("exception!"+e);
	    e.printStackTrace();
	    return null;
	}
    }

    public static String getUID() throws Exception {
	Runtime runTime = Runtime.getRuntime();
	Process process = null;
	BufferedReader buffInReader=null;
	String s = null;
	StringBuffer output = new StringBuffer();
	int exitValue = -1;

	try {
	    //process = runTime.exec("id -a");
	    process = runTime.exec("id");
	    buffInReader = new BufferedReader( new InputStreamReader(process.getInputStream()) ); 
		
	    while ((s = buffInReader.readLine()) != null) {
		int idx = s.indexOf("uid=");
		if(idx != -1) {
		    int idx1 = s.indexOf("(");
		    if(idx1 != -1) {
			output.append(s.substring(idx+4,idx1));
		    } else {
			output.append(s.substring(idx+4));
		    }		
		} else 
		    output.append(s);
	    }
	    buffInReader.close();
	    exitValue = process.waitFor();
	} catch (Exception e) {
	    System.out.println("Exception " + e.getMessage());
	    //throw new IOException("Unable to execute 'id -a'");
	    throw new IOException("Unable to execute 'id'");
	} finally {
	    if (buffInReader != null) {
		try {
		    buffInReader.close();
		} catch (IOException e) {}
	    }
	    if (process != null) {
		try {
		    process.getErrorStream().close();
		} catch (IOException e) {}
		try {
		    process.getOutputStream().close();
		} catch (IOException e) {}
	    }
	}
	
	if (exitValue != 0) {
	    System.out.println("exitValue " + exitValue);
	    //throw new IOException("Unable to perform 'id -a'");
	    if(output.toString().trim().equals("")) {
		throw new IOException("Unable to perform 'id'");
	    }
	}
	return output.toString().trim();
    }


    public static void main(String[] args) throws Exception {
	String srmServiceUrl="httpg://esg.ccs.ornl.gov:46790/srm/v2/server";
System.out.println("Hello");
	String result= "srmPing() to:"+srmServiceUrl;
	String proxyPath = "/tmp/x509up_u";
	try {
	    //String uid = helper.getUID();
	    //proxyPath += uid;
	    //result += "And cred="+helper.getCredential(proxyPath);
	    TSRMStubHolder srm = helper.getValidStub(srmServiceUrl);
	    if (srm == null) {
		System.out.println( result+" Unable to talk to remote srm: "+srmServiceUrl);
		System.exit(1);
	    }

	    SrmPingRequest req = new SrmPingRequest();
	    SrmPingResponse response = srm.getStub().srmPing(req);

	    if (response == null) {
	        System.out.println(result+" Unable to get response from srm");
		System.exit(1);
	    } else {
		System.out.println(result+" successful. version="+response.getVersionInfo());
	    }

	    //
	    //
//
//	    String src = "gsiftp://data3.lbl.gov//tmp/test.data";
//	    System.out.println(" prepare to get "+src);
//	    SrmPrepareToGetRequest r = new SrmPrepareToGetRequest();
//	    int size = 1; // one pair of src/tgt here
//	    TGetFileRequest[] fileReqList = new TGetFileRequest[size];

//	    fileReqList[0] = new TGetFileRequest();
//	    fileReqList[0].setSourceSURL(helper.createTSURL(src));
//	    //fileReqList[0].setTargetSURL(helper.createTSURL(tgt));

//	    ArrayOfTGetFileRequest col = new ArrayOfTGetFileRequest();
//	    col.setRequestArray(fileReqList);
//	    r.setArrayOfFileRequests(col);

//	    SrmPrepareToGetResponse getResponse = srm.getStub().srmPrepareToGet(r);
//	    System.out.println(" response = "+getResponse);
//	    
	} catch (Exception e) {
	    e.printStackTrace();
	    //return "Error: unable to locate proxy path";
	}
    }
*/
}


