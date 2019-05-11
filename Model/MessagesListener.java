package Model;

import View.Message;

public interface MessagesListener {
	public void messageEvent(MessagesSender sender, Message msg);
}
