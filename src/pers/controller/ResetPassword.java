package pers.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.model.Account;
import pers.model.UserService;

@WebServlet("/reset_password")
public class ResetPassword extends HttpServlet {
	private final Pattern passwdRegex = Pattern.compile("^\\w{8,16}$");
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String token = request.getParameter("token");
		
		UserService userService = (UserService) getServletContext().getAttribute("userService");
		Optional<Account> optionalAcct = userService.accountByNameEmail(name, email);
		
		if(optionalAcct.isPresent()) { //查詢帳號是否存在
			Account acct = optionalAcct.get();
			if(acct.getPassword().equals(token)) {
				request.setAttribute("acct", acct);
				request.getSession().setAttribute("token", token); //在會話中儲存驗證碼
				request.getRequestDispatcher("/WEB-INF/jsp/reset_password.jsp").forward(request, response);
				return;
			}
		}
		response.sendRedirect("/mini blog");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getParameter("token");
		String storedToken = (String) request.getSession().getAttribute("token");
		if(storedToken == null || !storedToken.equals(token)) {
			response.sendRedirect("/mini blog"); //沒有請求憑據或憑證不符合，重新導向至首頁
			return;
		}
		
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		
		UserService userService = (UserService) getServletContext().getAttribute("userService");
		
		if(!validatePassword(password, password2)) {
			Optional<Account> optionalAcct = userService.accountByNameEmail(name, email);
			request.setAttribute("errors", Arrays.asList("請確認密碼符合格式並再度確認密碼"));
			request.setAttribute("acct", optionalAcct.get());
			request.getRequestDispatcher("/WEB-INF/jsp/reset_password.jsp").forward(request, response);
		} else {
			userService.resetPassword(name, password); //重設密碼
			request.getRequestDispatcher("/WEB-INFjsp/reset_success.jsp").forward(request, response);
		}
	}

	private boolean validatePassword(String password, String password2) {
		return password != null && passwdRegex.matcher(password).find() && password.equals(password2);
	}
}
