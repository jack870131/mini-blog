package pers.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.model.UserService;

@WebServlet(
		urlPatterns={"/del_message"},
		initParams={
				@WebInitParam(name = "MEMBER_PATH", value = "member")
		}
)
public class DelMessage extends HttpServlet {
	private final String USERS = "c:/workspace/users";
	private final String LOGIN_PATH = "index.html";
	private final String MEMBER_PATH = "member";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("login") == null) {
			response.sendRedirect(LOGIN_PATH);
			return;
		}
		
		String millis = request.getParameter("millis");
		if(millis != null) {
			UserService userService = (UserService) getServletContext().getAttribute("userService");
			userService.deleteMessage(getUsername(request), millis);
		}
		response.sendRedirect(getInitParameter("MEMBER_PATH"));
	}

	private void deleteMessage(String username, String millis) throws IOException {
		Path txt = Paths.get(USERS, username, String.format("%s.txt", millis));
		Files.delete(txt);
	}

	private String getUsername(HttpServletRequest request) {
		return (String) request.getSession().getAttribute("login");
	}
}
