import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class KeywordHandler {
    private Interpreter interpreter;
    private File activeFile; 
    private String activeLine;
    public int tabCounter;
    /*
     * TODO: figure out a way to increment tabCounter based on start curly brackets { , 2 different cases depending 
     *     on whether code is written with { on its own line or at the end of a line 
     */
    public KeywordHandler(Interpreter interpreter) {
        this.interpreter = interpreter;    
    }
    
    public boolean handle(String keyword) {
        switch(Keyword.fromString(keyword)) {
            case CLASS:
                System.out.println("Found keyword: class");
                createClass();
                break;
            case DOUBLE:
            case INTEGER:
                System.out.println("Found keyword: int");
                createVariable();
                break;      
            case STRING:
                //
                break;
            case RETURN:
                //
                break;                
            case SYSTEM:
                System.out.println("Found keyword: System");
                createSystemCall();
                break;
            case ENDCURLY:
                //System.out.println("Found end curly bracket");
                tabCounter--; //generally go back a tab when a block of code ends.   
                break;
            case NOTFOUND:
                if(checkForConstructor()) {
                    createConstructor();
                    return true;
                } else {
                    System.out.println("No keyword found.");
                }
                return false;
        }
        return true;
    }
    
    private boolean checkForConstructor() {
        activeLine = interpreter.activeLine;
        String[] tokens = activeLine.trim().split("\\(");
        
        if(tokens.length < 2)
            return false;
        
        // This does not work for constructors that have params
        if(tokens[1].charAt(0) == ')')
            return true;
            
        return false;
    }
    
    private void createClass() {
        activeLine = interpreter.activeLine;
        String[] tokens = activeLine.split(" ");
        String[] nameTokens = tokens[1].split("\\(");
        String filename = nameTokens[0].trim();
        activeFile = new File("python/" + filename + ".py");
        
        if(activeFile.exists()) 
            activeFile.delete();
            
        try {
            activeFile.createNewFile();
            FileWriter fw = new FileWriter(activeFile);
            fw.write("class " + filename + "():\n");
            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void createConstructor() {
        // We need to add a "tab" counter so we know how far to indent the next line
        System.out.println("Constructor found.");
        try {
            FileWriter fw = new FileWriter(activeFile, true);
            fw.write(tabHandler() + "def __init__(self):\n"); //changed to use new tab system
            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void createSystemCall() {
        /* System methods
         * http://docs.oracle.com/javase/7/docs/api/java/lang/System.html
         * for now we just look at the PrintStream out
         * 
         * completed:
         * 
         */
        
        activeLine = interpreter.activeLine;
        String[] tokens = activeLine.split("\\.");
        
        if(tokens.length < 2)
            return;
        
        // Only test the out case for now. out is a PrintStream
        if(tokens[1].equals("out")) {
            /* PrintStream methods
             * http://docs.oracle.com/javase/7/docs/api/java/io/PrintStream.html
             * for now we just look at println 
             * completed:
             */
            
            if(tokens[2].substring(0,7).equals("println")) { 
                //tokens[2] is println("Hello World!");, use substring method to isolate println
                //2nd argument of substring is actually 1 more than the string will return
                System.out.println("Print new line found");
                String printedString = tokens[2].substring(8, ( tokens[2].length() - 2 ) ); //this should isolate string being printed.  9 is the index of the first character AFTER the first quotation the 2nd argument should be index of the 
                                                                                            //second quotation, but the 2nd quotation will not be included in the returned string  
                try {
                    FileWriter fw = new FileWriter(activeFile, true);
                    fw.write(tabHandler() + "print(" + printedString + ")\n");
                    fw.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void createVariable() {
        activeLine = interpreter.activeLine;
        activeLine = activeLine.substring(activeLine.indexOf(" ")).replaceAll(" ", "");
        String[] tokens = activeLine.split(" |=|;");
        
        try {
            FileWriter fw = new FileWriter(activeFile, true);
            String output = tokens[0];
            
            if(tokens.length >= 2)
                output = output.concat(" = " + tokens[1]);
            else
                output = output.concat(" = 0");
            fw.write(tabHandler() + output + "\n");
            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    //contain tab escapes in a single string, call this every time we write to the file
    //this should work for decrementing tabs as well as long as we correctly keep track of tabCount in the functions
    private String tabHandler() {
        String tabString = "";
        for(int i = 0; i < tabCounter; i++) {
            tabString += "\t";
        }
        return tabString;
    }
}
