package View;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;

import Model.Person.InteractionType;
import Tools.Random;

public class AutomaticAnswers implements Serializable {
	private static final long serialVersionUID = 2748233304375756131L;
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * We use a nested class to store the database content
	 */
	private class AutomaticMessage {
		private InteractionType context;
		private int level;
		private String message;
		
		public AutomaticMessage(InteractionType context, int level, String message) {
			this.context = context;
			this.level = level;
			this.message = message;
		}
		
		public boolean isContext(InteractionType context) {
			return this.context == context;
		}
		
		public int getLevel() {
			return level;
		}
		
		public String getMessage() {
			return message;
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	
	private static final String MESSAGES_FILE_PATH = "src/Data/Messages.csv";
	private static ArrayList<AutomaticMessage> messagesDatabase = null;
	
	private AutomaticAnswers() {
		// Load the file if not yet done since the start of the program
		if (messagesDatabase == null)
			loadMessagesDatabase();
	}
	
	private ArrayList<AutomaticMessage> getAllMessagesDatabase() {
		return messagesDatabase;
	}
	
	private void loadMessagesDatabase() {
		messagesDatabase = new ArrayList<AutomaticMessage>();
		
		BufferedReader buffer;
		
		try {
            // Get the Products csv file 
            FileReader file = new FileReader(MESSAGES_FILE_PATH);
            buffer = new BufferedReader(file);

            String line = null;
            int lineIndex = 0;
            
            while((line = buffer.readLine()) != null) {
            	lineIndex++;
            	
            	// Ignore comments and empty lines
            	if (line.startsWith("//") || line.trim().isEmpty())
            		continue;
            	
            	// The -1 is to prevent to remove trailing empty strings
            	String[] data = line.split("\t", -1);
            	
            	if (data.length != 3) {
                    buffer.close();
                    
            		throw new IllegalArgumentException(
            				String.format("Bad line in the Messages.csv file (at line %d)",
            						lineIndex));
            	}
            	
				AutomaticMessage m = new AutomaticMessage(
						InteractionType.valueOf(data[0]),
						(int) Integer.parseInt(data[1]),
						data[2]);
				
				messagesDatabase.add(m);
            }   

            buffer.close();
        }
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<AutomaticMessage> filterMessagesContext(
			ArrayList<AutomaticMessage> messagesList, InteractionType contextFilter)
	{
		ArrayList<AutomaticMessage> filteredMsgs = new ArrayList<AutomaticMessage>();
		
		for (AutomaticMessage am : messagesList) {
			if (am.isContext(contextFilter)) {
				filteredMsgs.add(am);
			}
		}
		
		return filteredMsgs;
	}
	
	private ArrayList<AutomaticMessage> filterMessagesLevel(
			ArrayList<AutomaticMessage> messagesList, int levelFilter)
	{
		ArrayList<AutomaticMessage> filteredMsgs = new ArrayList<AutomaticMessage>();
		
		/*
		 * First pass: find the target level to get
		 * 
		 * We have to get the lower (or equal) level closest to levelFilter in the list
		 */
		
		int foundLevel = 0;
		
		for (AutomaticMessage am : messagesList) {
			int lvl = am.getLevel();
			
			if (lvl <= levelFilter && lvl > foundLevel) {
				foundLevel = lvl;
			}
		}
		
		/*
		 * Second pass: get all the messages corresponding to the found level
		 */

		for (AutomaticMessage am : messagesList) {
			if (am.getLevel() == foundLevel) {
				filteredMsgs.add(am);
			}
		}
		
		return filteredMsgs;
	}
	
	/**
	 * This function returns an automatic answer adapted for the context and the appreciation.
	 * The messages comes from a CSV database.
	 * 
	 * @param context The message's context
	 * @param appreciationLevel A number between 0 and 1 for the appreciation
	 * @return The selected automatic message
	 */
	public static String getAutomaticAnswer(InteractionType context, double appreciationLevel) {
		String answer = "";
		
		// Convert the -1 to 1 appreciationLevel to a 0 to 100 level
		int level = (int)((appreciationLevel + 1) / 2.0 * 100);
		
		AutomaticAnswers aa = new AutomaticAnswers();
		
		// Filter messages according to level and context
		ArrayList<AutomaticMessage> filteredMsgs = aa.getAllMessagesDatabase();
		filteredMsgs = aa.filterMessagesContext(filteredMsgs, context);
		filteredMsgs = aa.filterMessagesLevel(filteredMsgs, level);
		
		// A message is found for this context and level
		if (filteredMsgs.size() > 0) {
			// Get a random message in the filtered list
			int randIndex = Random.rangeInt(0, filteredMsgs.size() - 1);
			
			answer = filteredMsgs.get(randIndex).getMessage();
		}
		
		return answer;
	}
}
