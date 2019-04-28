package View;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = -4668039234172198234L;

	public enum MsgType {
		Info,
		Warning,
		Problem
	}
	
	private String message;
	private MsgType type;
	
	public Message(String msg, MsgType type) {
		this.message = msg;
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public MsgType getType() {
		return type;
	}
}