package org.esgf.srm;

import org.esgf.srm.email.Email;

public class SRMObj {

	public SRMObj() {
		
	}
	
	public void get() {
		
		Email email = new Email();
		
		email.sendEmail();
		
		
	}
	
	
}
