package org.esgf.srm;

public class SRMReq {
	
    private String uid="";
    private String logPath="/tmp/esg-srm.log";
    private String log4jlocation="";
    private String storageInfo="";
    private String fileType="volatile";
    private String retentionPolicy="replica";
    private String accessLatency="online";
    private boolean debug = false;
    private boolean delegationNeeded=false;
    

	private String [] file_urls;
    private String server_url;
    
    public SRMReq() {
    	this.uid = "";
    	this.logPath = "/tmp/esg-srm.log";
    	this.log4jlocation = "";
    	String ttemp = System.getProperty("log4j.configuration");
	    //System.out.println("ttemp = "+ ttemp);
	    if(ttemp != null && !ttemp.equals("")) {
	       this.log4jlocation = ttemp;
	    }
	    
    	this.storageInfo = "";
    	this.fileType = "volatile";
    	this.retentionPolicy = "replica";
    	this.accessLatency = "online";
    	this.debug = false;
    	this.delegationNeeded = false;
    	
    	this.file_urls = null;
    	this.setServer_url(null);
    	
    	
    }
    
    public SRMReq(String [] file_urls) {
    	
    	this.file_urls = new String [file_urls.length];
    	for(int i=0;i<file_urls.length;i++) {
    		System.out.println("i: " + i);
    		System.out.println("\t" + file_urls[i]);
    		this.file_urls[i] = SRMUtils.transformServerName(file_urls[i]);
    	}
    	this.setServer_url(SRMUtils.extractServerName(this.file_urls[0]));
    }
    
    
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public String getLog4jlocation() {
		return log4jlocation;
	}

	public void setLog4jlocation(String log4jlocation) {
		this.log4jlocation = log4jlocation;
	}

	public String getStorageInfo() {
		return storageInfo;
	}

	public void setStorageInfo(String storageInfo) {
		this.storageInfo = storageInfo;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getRetentionPolicy() {
		return retentionPolicy;
	}

	public void setRetentionPolicy(String retentionPolicy) {
		this.retentionPolicy = retentionPolicy;
	}

	public String getAccessLatency() {
		return accessLatency;
	}

	public void setAccessLatency(String accessLatency) {
		this.accessLatency = accessLatency;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isDelegationNeeded() {
		return delegationNeeded;
	}

	public void setDelegationNeeded(boolean delegationNeeded) {
		this.delegationNeeded = delegationNeeded;
	}

	/**
	 * @return the server_url
	 */
	public String getServer_url() {
		return server_url;
	}

	/**
	 * @param server_url the server_url to set
	 */
	public void setServer_url(String server_url) {
		this.server_url = server_url;
	}


    
    public String[] getFile_urls() {
		return file_urls;
	}

	public void setFile_urls(String[] file_urls) {
		this.file_urls = file_urls;
	}
	
	public String toString() {
		
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
		for(int i=0;i<file_urls.length;i++) {
			str += "file_url: " + file_urls[i] + "\n";
		}
		str += "server_url: " + server_url;
		
		
		
		return str;
	}
    
}
