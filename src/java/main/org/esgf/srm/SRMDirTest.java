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
//SRMDirTest
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

public class SRMDirTest {

   public static void main(String[] args) {
     String serverUrl = "";
     String uid="";
     String logPath="";
     String log4jlocation="";
     String fileType="volatile";
     String retentionPolicy="replica";
     String accessLatency="online";
     String storageInfo="";
     boolean debug = false;
     boolean silent = false;
     boolean rmFile=false;
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
       else if(args[i].equals("-uid") && i+1 < args.length) {
          uid = args[i+1];
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
       else if(args[i].equals("-storageinfo") && i+1 < args.length) {
          storageInfo = args[i+1];
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
       else if(args[i].equals("-rmfile")) {
          rmFile=true;
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
    System.out.println("debug="+debug);
    System.out.println("silent="+silent);
    System.out.println("filestoragetype="+fileType);
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

    if(rmFile) {
      System.out.println("RemoveFile="+rmFile);
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
      SRMServer cc = new SRMServer
			(log4jlocation, logPath,debug,delegationNeeded);
      cc.connect(serverUrl);
      cc.ping(uid);
      SRMRequest req = new SRMRequest();
      req.setSRMServer(cc);
      req.setAuthID(uid);
      req.addFiles(surl, null,null);
      req.setFileStorageType(fileType);
      req.setRetentionPolicy(retentionPolicy);
      req.setAccessLatency(accessLatency);
      req.setStorageSystemInfo(storageInfo);
      if(!rmFile) {
        req.srmLs();
        req.checkStatus();
        
      }
      else {
        req.srmRm();
      }

      int sleepTime  = 10;
	  
      SRMRequestStatus response = req.getStatus();
      while(response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_QUEUED ||
              response.getReturnStatus().getStatusCode() == TStatusCode.SRM_REQUEST_INPROGRESS){
    	  System.out.println("\nRequest.status="+response.getReturnStatus().getStatusCode());
	        System.out.println("request.explanation="+response.getReturnStatus().getExplanation());
	        
	    	System.out.println("SRM-CLIENT: Next status call in "+ sleepTime + " secs");
      	Thread.currentThread().sleep(sleepTime * 1000);
      	sleepTime*=2;
      	
      	if(sleepTime>=600){
      		sleepTime=600;
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
    		cc.disconnect();
    		System.exit(-1);
    	}
      }
      
      
      //req.printResults();
      if(response != null) {
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
      }
      cc.disconnect();
    }catch(Exception e) {
      e.printStackTrace();
    }
   }

   @SuppressWarnings("deprecation")
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


   public static void showUsage() {
       System.out.println("............................................");
       System.out.println("java gov.lbl.srm.client.api.SRMDirTest ");
       System.out.println("-serviceurl      <endpoint>");
       System.out.println("-uid      <authid>");
       System.out.println("-filetype      <volative|permanent|durable>(default:volatile)");
       System.out.println("-retentionpolicy <replica|output|custodial>(default:replica)");
       System.out.println("-accesslatency <online|nearline>(default:online)");
       System.out.println("-logpath      <path to logfile>");
       System.out.println("-surls      <surls are given delimiter is \";\">");
       System.out.println("\t srm://dmx.lbl.gov:6250/srm/v2/server?SFN=/srmcache/~/put.test;"+
				              "srm://dmx.lbl.gov:6250/srm/v2/server?SFN=/srmcache/~/put.test.1");
       System.out.println("-rmfile              (to do rmfile default:false)");
       System.out.println("-debug  (true|fase) (default:false)");
       System.out.println("-silent (true|false) (default:false)");
       System.out.println("-help  (to show this message)");
       System.exit(1);
     }
}

