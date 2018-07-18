package pers.web;

import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;



@WebFilter("/new_message")
public class HtmlSanitizer {
	private PolicyFactory policy;
	
	public void init() {
		policy = new HtmlPolicyBuilder().allowElements("a", "b", "i", "del", "pre", "code")
				.allowUrlProtocols("http", "https")
				.allowAttributes("href").onElements("a")
				.requireRelNofollowOnLinks().toFactory();
	}
	
	private class SanitizerWrapper extends HttpServletRequestWrapper { //請求包裹氣，會對請求參數進行過濾
		public SanitizerWrapper(HttpServletRequest request) {
			super(request);
		}
		
		public String getParameter(String name) {
			return Optional.ofNullable(getRequest().getParameter(name)).map(policy::sanitize).orElse(null);
		}
	}
	
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		//包裹原請求物件
		chain.doFilter(new SanitizerWrapper(request), response);
	}
}
