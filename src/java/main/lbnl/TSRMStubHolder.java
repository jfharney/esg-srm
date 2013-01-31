package lbnl;

//import javax.xml.rpc.Stub;

import gov.lbl.srm.StorageResourceManager.*;

//httpg
import org.globus.axis.gsi.GSIConstants;
import org.ietf.jgss.GSSCredential;
//import javax.xml.rpc.Stub;
import org.apache.axis.types.URI;
import java.net.URL;

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

public class TSRMStubHolder {
    private ISRM _srmStub = null;

    public TSRMStubHolder (String endPoint) {
	System.out.println("endpoint="+endPoint);
	initStub(endPoint);
    }

    public void initStub(String endPoint) {
      try {
        _srmStub = TSRMStubHolder.getISRMHandle(endPoint);
      } catch (Exception e) {
	e.printStackTrace();
	throw new RuntimeException("Failed to init stub");
      }
    }    

    public ISRM getStub() {
	return _srmStub;
    }

    public void useCredential(GSSCredential cred) {
	TSRMStubHolder.useCredential((org.apache.axis.client.Stub)_srmStub, cred);
    }

    public static void useCredential(org.apache.axis.client.Stub stub, GSSCredential cred) {
	if (cred == null) {
	    throw new RuntimeException ("There is no credential! Did you do delegation?");
	} 
	stub._setProperty(GSIConstants.GSI_CREDENTIALS, cred);	
	stub._setProperty(org.globus.axis.transport.GSIHTTPTransport.GSI_CREDENTIALS,cred);
    }

    public static URL getURL(String url) {
	org.apache.axis.types.URI uri = null;
	try {
	    uri = new org.apache.axis.types.URI(url);
	}  catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
	try {
	    if (uri.getScheme().equalsIgnoreCase("httpg")) {
		org.globus.net.protocol.httpg.Handler m = new org.globus.net.protocol.httpg.Handler();
		return new URL(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath(), m);
	    } else if (uri.getScheme().equalsIgnoreCase("https")) {
		org.globus.net.protocol.https.Handler m = new org.globus.net.protocol.https.Handler();
		return new URL(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath(), m);
	    } else {
		return new URL(url);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static ISRM getISRMHandle(String url) {
	System.out.println("url="+url);
	SimpleProvider provider = new SimpleProvider();
	SimpleTargetedChain c = null;

	if (url.startsWith("httpg")) {
	    //String result =org.theshoemakers.which4j.Which4J.which(org.globus.axis.transport.GSIHTTPSender.class);            
	    System.out.println("....httpg handle");
	    c = new SimpleTargetedChain(new GSIHTTPSender());
	    provider.deployTransport("httpg",c);
	} else if (url.startsWith("https")) {
	    System.out.println("....https handle");
	    c = new SimpleTargetedChain(new HTTPSSender());
	    provider.deployTransport("https",c);
	} else {
	    System.out.println(".........always supports: http");
	    c = new SimpleTargetedChain(new HTTPSender());
	    provider.deployTransport("http",c);
	}

	Util.registerTransport();

	SRMServiceLocator service = new SRMServiceLocator(provider);
	URL uu = getURL(url);
	if (uu == null) {
	    return null;
	}

	try {
	    // Now use the service to get a stub to the service
	    ISRM srm = service.getsrm(uu);
	    ((org.apache.axis.client.Stub)srm)._setProperty(org.globus.axis.transport.GSIHTTPTransport.GSI_AUTHORIZATION,
							    org.globus.gsi.gssapi.auth.NoAuthorization.getInstance());

	    ((org.apache.axis.client.Stub)srm)._setProperty(org.globus.axis.transport.GSIHTTPTransport.GSI_MODE,
							    org.globus.axis.transport.GSIHTTPTransport.GSI_MODE_FULL_DELEG);

	    ((org.apache.axis.client.Stub)srm)._setProperty(org.globus.gsi.GSIConstants.AUTHZ_REQUIRED_WITH_DELEGATION, Boolean.FALSE);
	    return srm;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;	    
	}

    }
}
