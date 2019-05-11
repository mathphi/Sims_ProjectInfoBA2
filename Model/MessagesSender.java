package Model;

import View.Message;

public interface MessagesSender {
	public void attachMessagesListener(MessagesListener ml);
	public void addMessage(Message msg);
}
