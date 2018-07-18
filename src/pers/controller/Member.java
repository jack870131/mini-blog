package pers.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.model.Message;
import pers.model.UserService;

@WebServlet(
		urlPatterns={"/member"},
		initParams={
				@WebInitParam(name = "MEMBER_PATH", value = "member.view")
		}
)
public class Member extends HttpServlet {
	private final String USERS = "c:/workspace/users";
	private final String MEMBER_PATH = "member.view";
	private final String LOGIN_PATH = "index.html";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		UserService userService = (UserService) getServletContext().getAttribute("userService");
		List<Message> messages = userService.messages(getUsername(request));
		
		request.setAttribute("messages", messages); //將取得的訊息設為請求屬性
		request.getRequestDispatcher(getInitParameter("MEMBER_PATH")).forward(request, response); //轉發處理畫面的 Servlet
	}

	/**
	 * 訊息存至 .txt, 並以時間為主黨名
	 * @param username
	 * @return
	 * @throws IOException 
	 */
	private Map<Long, String> messages(String username) throws IOException {
		Path userhome = Paths.get(USERS, username);
		
		Map<Long, String> messages = new TreeMap<>(Comparator.reverseOrder());
		try(DirectoryStream<Path> txts = Files.newDirectoryStream(userhome, "*.text")) {
			for(Path txt : txts) {
				String millis = txt.getFileName().toString().replace(".txt", "");
				String blabla = Files.readAllLines(txt).stream()
						.collect(Collectors.joining(System.lineSeparator()));
				messages.put(Long.parseLong(millis), blabla);
			}
		}
		return messages;
	}

	private String getUsername(HttpServletRequest request) {
		return (String) request.getSession().getAttribute("login"); //目前從"login"屬性取得使用者名稱
	}
}
