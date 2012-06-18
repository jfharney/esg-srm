package org.esgf.srm;

import javax.servlet.http.HttpServletRequest;

public class SRMRequestObject {
	private String openId;
	private String proxyId;
	private String proxyPwd;
	private String url;
	
	public SRMRequestObject(HttpServletRequest request){
		this.openId = request.getParameter("openid");
		this.proxyId = request.getParameter("proxyid");
		this.proxyPwd = request.getParameter("pass");
		this.url = request.getParameter("url");
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
	
}
