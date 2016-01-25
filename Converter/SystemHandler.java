import java.util.Arrays;

class SystemHandler {
	private KeywordHandler keywords;
	
	public SystemHandler(KeywordHandler keywords) {
		this.keywords = keywords;
	}
	
	public void handleCall(String input) {
		input = input.replaceFirst("System.", "");
		String[] tokens = input.split("\\.");
        
        if(tokens.length == 0)
            return;
        
        // Only test the out case for now. out is a PrintStream
		
		switch(tokens[0]) {
			// Field Summary
			case "err":
				break;
			case "in":
				break;
			
				
			// Method Summary
			case "arraycopy":
				break;
			case "clearProperty":
				break;
			case "console":
				break;
			case "currentTimeMillis":
				break;
			case "exit":
				break;
			case "gc":
				break;
			case "getenv":
				break;
			case "getProperties":
				break;
			case "getProperty":
				break;
			case "getSecurityManager":
				break;
			case "identityHashCode":
				break;
			case "inheritedChannel":
				break;
			case "lineSeparator":
				break;
			case "load":
				break;
			case "loadLibrary":
				break;
			case "mapLibraryname":
				break;
			case "nanoTime":
				break;
			/*
			 * Modifier and Type:
			 * 	static PrintStream
			 *
			 * Description:
			 * 	The "standard" output stream.
			 */
			case "out":
				input = input.replaceFirst("out.", "");
				out(input);
				break;
			case "runFinalization":
				break;
			case "setErr":
				break;
			case "setIn":
				break;
			case "setOut":
				break;
			case "setProperty":
				break;
			case "setSecurityManager":
				break;
				
			default:
				break;
		}
	}
	
	public void out(String input) {
		keywords.printStream.handleCall(input);
	}
}