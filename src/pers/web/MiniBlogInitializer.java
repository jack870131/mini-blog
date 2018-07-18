package pers.web;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import pers.model.AccountDao;
import pers.model.AccountDaoJdbcImpl;
import pers.model.GmailService;
import pers.model.MessageDao;
import pers.model.MessageDaoJdbcImpl;
import pers.model.UserService;

@WebListener
public class MiniBlogInitializer implements ServletContextListener {
	private DataSource dataSource() {
		try {
			Context initContext = new InitialContext(); //通過 JNDI 取得 DataSource
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			return (DataSource) envContext.lookup("jdbc/mini blog");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		DataSource dataSource = dataSource();
		ServletContext context = sce.getServletContext();
		AccountDao acctDao = new AccountDaoJdbcImpl(dataSource);
		MessageDao messageDao = new MessageDaoJdbcImpl(dataSource);
		context.setAttribute("emailService", 
				new GmailService(context.getInitParameter("MAIL_USER"), 
						context.getInitParameter("MAIL_PASSWORD")));
	}
}
