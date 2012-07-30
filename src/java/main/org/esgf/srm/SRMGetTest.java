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

package org.esgf.srm;


import gov.lbl.srm.StorageResourceManager.*;
import gov.lbl.srm.client.wsdl.*;

import java.io.*;
import java.util.*;


import org.apache.log4j.PropertyConfigurator;

//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//SRMGetTest
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

public class SRMGetTest {

  public static void main(String[] args) {
    String serverUrl = "";
    String uid="";
    String logPath="";
    String log4jlocation="";
    String storageInfo="";
    String fileType="volatile";
    String retentionPolicy="replica";
    String accessLatency="online";
    boolean debug = false;
    boolean silent = false;
    boolean releaseFile =false;
    boolean delegationNeeded=false;
    Vector vec = new Vector ();

    String ttemp = System.getProperty("log4j.configuration");
    if(ttemp != null && !ttemp.equals("")) {
       log4jlocation = ttemp;
    }

    for(int  i = 0; i < args.length; i++) {
      System.out.println("ARGS[i]="+args[i]);
      if(args[i].equals("-serviceurl") && i+1 < args.length) {
         serverUrl = args[i+1];
         i++;
      }
      else if(args[i].equals("-logpath") && i+1 < args.length) {
         logPath = args[i+1];
         i++;
      }
      else if(args[i].equals("-storageinfo") && i+1 < args.length) {
         storageInfo = args[i+1];
         i++;
      }
      else if(args[i].equals("-filetype") && i+1 < args.length) {
         fileType = args[i+1];
         i++;
      }
      else if(args[i].equals("-retentionpolicy") && i+1 < args.length) {
         retentionPolicy = args[i+1];
         i++;
      }
      else if(args[i].equals("-accesslatency") && i+1 < args.length) {
         accessLatency = args[i+1];
         i++;
      }
      else if(args[i].equals("-uid") && i+1 < args.length) {
         uid = args[i+1];
         i++;
      }
      else if(args[i].equals("-surls") && i+1 < args.length) {
         String temp = args[i+1];
         StringTokenizer ss = new StringTokenizer(temp,";");
         while (ss.hasMoreTokens()) {
           vec.addElement(ss.nextToken());
         }
         i++;
      }
      else if(args[i].equals("-debug")) {
         debug=true;
      }
      else if(args[i].equals("-delegation")) {
         delegationNeeded=true;
      }
      else if(args[i].equals("-releasefile")) {
         releaseFile=true;
      }
      else if(args[i].equals("-silent")) {
         silent=true;
      }
      else if(args[i].equals("-help")) {
         showUsage();
      }
    }

   System.out.println("ServerUrl="+serverUrl);
   System.out.println("uid="+uid);
   System.out.println("RequestType=get");
   System.out.println("debug="+debug);
   System.out.println("silent="+silent);
   System.out.println("FileType="+fileType);
   System.out.println("RetentionPolicy="+retentionPolicy);
   System.out.println("AccessLatency="+accessLatency);
   System.out.println("Delegation="+delegationNeeded);
   if(!storageInfo.equals("")) {
     System.out.println("StorageInfo="+storageInfo);
   }

   String[] surl = new String[vec.size()];
   for(int i = 0; i < vec.size(); i++) {
       System.out.println("SURL["+i+"]="+vec.elementAt(i));
       surl[i] = (String)vec.elementAt(i);
   }

   if(releaseFile) {
     System.out.println("ReleaseFile="+releaseFile);
   }
   if(serverUrl.equals("")) {
     System.out.println("Cannot connect to service with null serviceurl");
     System.exit(1);
   }

   if(surl == null || surl.length == 0) {
     System.out.println("Please provide the surls");
     System.exit(1);
   }
     
   try {
     if(!storageInfo.equals("")) {
       delegationNeeded=true;
     }
     SRMServer cc = new SRMServer(log4jlocation, logPath, debug, delegationNeeded);
     cc.connect(serverUrl);
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
     req.checkStatus();

     SRMRequestStatus response = req.getStatus();
     if(response != null) {
       System.out.println("\nStatus.code="+response.getReturnStatus().getStatusCode());
       System.out.println("\nStatus.exp="+response.getReturnStatus().getExplanation());
       if(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_SUCCESS ||
          response.getReturnStatus().getStatusCode() == TStatusCode.SRM_FILE_PINNED) {
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
              }
           }//end while
       }//end if
     }//end if


     if(releaseFile) {
       //before release file is called, 
		//user will do the file transfer either with globus-url-copy  or with other tool
       //to the SRM server. ie, copy the SRM server's transfer url to the local machine. 
       //and when the transfer file is successful, user will call release file.
       req.releaseFiles(surl);
     }

     response = req.getStatus();
     req.printResults();
     System.out.println("Getting Request Summary");
     req.getRequestSummary();
     req.printSummary();
     cc.disconnect();
   }catch(Exception e) {
     e.printStackTrace();
   }
  }


  public static void showUsage() {
      System.out.println("............................................");
      System.out.println("java gov.lbl.srm.client.api.SRMGetTest ");
      System.out.println("-serviceurl      <endpoint>");
      System.out.println("-uid      <authid>");
      System.out.println("-logpath      <path to logfile>");
      System.out.println("-filetype      <volative|permanent|durable>(default:volatile)");
      System.out.println("-retentionpolicy <replica|output|custodial>(default:replica)");
      System.out.println("-accesslatency <online|nearline>(default:online)");
      System.out.println("-surls      <surls are given delimiter is \";\">");
      System.out.println("\t srm://dmx.lbl.gov:6250/srm/v2/server?SFN=/srmcache/~/put.test;"+
				              "srm://dmx.lbl.gov:6250/srm/v2/server?SFN=/srmcache/~/put.test.1");
      System.out.println("-releasefile              (to do releasefile default:false)");
      System.out.println("-debug  (true|fase) (default:false)");
      System.out.println("-silent (true|false) (default:false)");
      System.out.println("-help  (to show this message)");
      System.exit(1);
    }
}

