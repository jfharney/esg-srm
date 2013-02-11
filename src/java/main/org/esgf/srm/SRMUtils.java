package org.esgf.srm;

public class SRMUtils {

	public static void main(String [] args) {
		//String url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
        //        "SFN=mss://esg2-sdnl1.ccs.ornl.gov/proj/cli049/UHRGCS/ORNL/CESM1" +
        //        "/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-01.nc";
		
		
		String url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
					 "SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/" +
				     "t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.ncfile_id=ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1.t341f02.FAMIPr.cam2.h0.1978-09.nc" +
					 "|esg2-sdnl1.ccs.ornl.gov";
		
		String newUrl = transformServerName(url);
		
		System.out.println(newUrl);
		
		String serverName = extractServerName(newUrl);

		System.out.println(serverName);
		
		String strippedName = stripIndex(newUrl);
		
		System.out.println(strippedName);
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
	
}
