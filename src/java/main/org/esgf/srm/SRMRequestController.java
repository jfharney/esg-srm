package org.esgf.srm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SRMRequestController {

	@SuppressWarnings("Unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/srmrequest")
	public @ResponseBody String postRequest(HttpServletRequest request) {
		
		System.out.println("SRM REQUEST CONTROLLER");
		
		String srm_url = request.getParameter("srm_url");
		
		String response = "null";
		if(srm_url != null) {
			response = "<srm_url>" + srm_url + "</srm_url>";
		}
		
		
		
		return response;
	}
	
	
}
