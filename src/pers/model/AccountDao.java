package pers.model;

import java.util.Optional;

public interface AccountDao {
	void createAccount(Account acct);
	Optional<Account> accountByUsername(String name);
	Optional<Account> accountByEmail(String email);
	void activateAccount(Account acct);
	void updatePasswordSalt(String name, String valueOf, String valueOf2);
}
