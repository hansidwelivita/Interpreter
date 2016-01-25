class PrintStreamHandler {
	private KeywordHandler keywords;
	
	public PrintStreamHandler(KeywordHandler keywords) {
		this.keywords = keywords;
	}
	public void handleCall(String input) {
	
		String[] tokens = input.split("\\.*\\(");
		System.out.println("Inside Print Stream Handler with: " + input + " first token " + tokens[0]);
		
		switch(tokens[0]) {
			// Field Summary
			/*
			case "out"
			*/
			
			// Method Summary
			case "append":
				break;
			case "checkError":
				break;
			case "clearError":
				break;
			case "close":
				break;
			case "flush":
				break;
			case "format":
				break;
			case "print":
				break;
			case "printf":
				break;
			/*
			 * Modifier and Type:
			 * 	void
			 *
			 * Description:
			 * 	See below for multiple method descriptions.
			 */
			case "println":
				input = input.replaceFirst("println", "");
				// This doesn't handle having ) inside of a string and such. Think of fix.
				input = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")"));
				System.out.println("Inside of printline is: " + input);
				break;
			case "setError":
				break;
			case "write":
				break;
		}
	}
}