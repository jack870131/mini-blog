package pers.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {
		"/member", "/member.view", "/new_message", 
		"/del_message", "/logout"
		}, 
		initParams = { 
		@WebInitParam(
		name = "Login_PATH", value = "index.html")
})

public class AccessController extends HttpFilter {
	private String LOGIN_PATH;
	
	public void init() throws ServletException {
		this.LOGIN_PATH = getInitParameter("LOGIN_PATH"); //設定登入頁面 
	}
	
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("login") == null) {
			response.sendRedirect(LOGIN_PATH); //重新導向至登入頁面
		} else {
			chain.doFilter(request, response); //只有在具備login屬性時，才呼叫 doFiler()
		}
	}
}
