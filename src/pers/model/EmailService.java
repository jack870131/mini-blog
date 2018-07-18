package pers.model;

import pers.model.Account;

public interface EmailService {
	public void validationLink(Account acct);
	public void failedRegistration(String acctName, String acctEmail);
	void passwordResetLink(Account acct);
}
