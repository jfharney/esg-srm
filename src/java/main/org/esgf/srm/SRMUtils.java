package org.esgf.srm;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class SRMUtils {

	private static String THREDDS_DATAROOT = "/thredds/fileServer/esg_dataroot/";
	
	
	
	
	public static String gridftp2http(String gsiftp) {
		String http = "";
		
		http = transformServerName(gsiftp);
		
		http = http.replace("gsiftp", "http");
		
		http = http.replace("//lustre/esgfs/", THREDDS_DATAROOT);
		
		
		return http;
	}
	
	public static String extractServerName(String url) {
		
		String serverName = null;
		
		serverName = url.substring(0, url.indexOf("?"));
		
		return serverName;
	}
	
	public static String transformServerName(String url) {
		return url.replace("esg2-sdnl1.ccs.ornl.gov", "esg.ccs.ornl.gov");
	}
	
	public static String stripIndex(String url) {
		
		int endIndex = url.indexOf("|");
		if(endIndex == -1) {
			return url;
		} 
		else {
			return url.substring(0,endIndex);
		}
	}
	
	public static String extractFile() {
		String extractedFile = "";
		
		return extractedFile;
	}
	
	//input
	//srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc
	//output
	//gsiftp://esg2-sdnl1.ccs.ornl.gov//lustre/esgfs/SRM/shared/V.0.0-505553807/t341f02.FAMIPr.cam2.h0.1978-09.nc
	public static String [] simulateSRM(String [] inputFiles) {
		String [] outputFiles = new String [inputFiles.length];
		
		for(int i=0;i<inputFiles.length;i++) {
			String tempFile = inputFiles[i].replace("srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://", "file:///");
			//tempFile = transformServerName(tempFile);
			
			File f = new File(tempFile);
			String fileName = f.getName();
			
			String outputFile = "gsiftp://esg.ccs.ornl.gov:2811//lustre/esgfs/SRM/" + fileName;
			
			outputFiles[i] = outputFile;
		}
		
		return outputFiles;
	}
	
	public static void main(String [] args) {
		//String url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
        //        "SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL/CESM1" +
        //        "/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-01.nc";
		
		
		String url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
					 "SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/" +
				     "t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc";
		
		String [] srm_urls = new String [1];
		
		srm_urls[0] = url;
		
		String [] gsiftp = simulateSRM(srm_urls);
		
		for(int i=0;i<gsiftp.length;i++) {
			System.out.println("gsiftp: " + gsiftp[i]);
		}
		
		
		//String gsiftp = "gsiftp://esg2-sdnl1.ccs.ornl.gov//lustre/esgfs/SRM/shared/V.0.0-505553807/t341f02.FAMIPr.cam2.h0.1978-09.nc";
		
		//gsiftp = transformServerName(gsiftp);
		
		//System.out.println("gsiftp: " + gsiftp);
		//String http = gridftp2http(gsiftp);
		

		//System.out.println(http);
	}
}
