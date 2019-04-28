package Model;

import java.io.Serializable;

import View.Message;

public class MessageEventListener implements Serializable {
	protected static final long serialVersionUID = 6172404051430531910L;
	
	public MessageEventListener() {}
	public void messageEvent(Message message) {}
}
