package pers.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

public class AccountDaoJdbcImpl implements AccountDao {
	private DataSource dataSource; //依賴 DataSource
	
	public AccountDaoJdbcImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void createAccount(Account acct) {
		try(Connection conn = dataSource.getConnection(); //透過 DataSource 取得 Connection
			PreparedStatement stmt = conn.prepareStatement(
			"INSERT INTO t_account(name, email, password, salt) VALUES(?, ?, ?, ?)");
				PreparedStatement stmt2 = conn.prepareStatement(
						"INSERT INTO t_account_rolt(name, role) VALUES(?, 'unverified')")) { //先設定未驗證角色
			/*
			 * 取得 Account 中封裝的資訊更新表格欄位
			 */
			stmt.setString(1, acct.getName());
			stmt.setString(2, acct.getEmail());
			stmt.setString(3, acct.getPassword());
			stmt.setString(4, acct.getSalt());
			stmt.executeUpdate();
			/*
			 * 執行變更
			 */
			stmt2.setString(1, acct.getName());
			stmt2.executeUpdate();
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<Account> accountByUsername(String name) {
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM t_account WHERE name = ?")) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return Optional.of(new Account( //查詢到的帳戶資訊封裝為 Account 物件
					rs.getString(1),	
					rs.getString(2),	
					rs.getString(3),	
					rs.getString(4)	
				));
			} else {
				return Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<Account> accountByEmail(String email) {
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM t_account WHERE email = ?")) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return Optional.of(new Account(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4)
				));
			} else {
				return Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void activateAccount(Account acct) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"UPDATE t_account_role SET role = ? WHERE name = ?")) {
			stmt.setString(1, "member");
			stmt.setString(2, acct.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updatePasswordSalt(String name, String password, String salt) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"UPDATE t_account SET password = ?, salt = ? WHERE name = ?")) {
			stmt.setString(1, password);
			stmt.setString(2, salt);
			stmt.setString(3, name);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
