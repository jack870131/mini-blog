package pers.model;

import java.util.List;

public interface MessageDao {
	List<Message> messageBy(String username);
	void createMessage(Message message);
	void deleteMessageBy(String username, String millis);
	List<Message> newestMessage(int n);
}
