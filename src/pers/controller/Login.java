package pers.controller;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.model.Message;
import pers.model.UserService;

@WebServlet(
		urlPatterns={"/login"},
		initParams={
				@WebInitParam(name = "SUCCESS_PATH", value = "member"),
				@WebInitParam(name = "ERROR_PATH", value = "/WEB-INF/jsp/index.jsp")
		}
)
public class Login extends HttpServlet {
    private final String USERS = "c:/workspace/users";
    private final String SUCCESS_PATH = "member";
    private final String ERROR_PATH = "/WEB-INF/jsp/index.jsp";
    
	protected void doPost(
	        HttpServletRequest request, HttpServletResponse response) 
	                        throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
	    UserService userService = (UserService) getServletContext().getAttribute("userService");
	    Optional<String> optionalPasswd = userService.encryptedPassword(username, password);
	    
	    String page;
	    try {
            request.login(username, optionalPasswd.get());
            request.getSession().setAttribute("login", username);
            response.sendRedirect(getInitParameter("SUCCESS_PATH"));
        } catch(NoSuchElementException | ServletException e) {
            request.setAttribute("errors", Arrays.asList("登入失敗"));
            List<Message> newest = userService.newestMessages(10);
            request.setAttribute("newest", newest);
            request.getRequestDispatcher(getInitParameter("ERROR_PATH"))
                   .forward(request, response);
        }
	}
}
