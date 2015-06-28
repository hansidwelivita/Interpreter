import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class KeywordHandler {
    private Interpreter interpreter;
    private File activeFile; 
    
    private String activeLine;
    
    public KeywordHandler(Interpreter interpreter) {
        this.interpreter = interpreter;    
    }
    
    public boolean handle(String keyword) {
        switch(Keyword.fromString(keyword)) {
            case CLASS:
                System.out.println("Found keyword: class");
                createClass();
                break;
            case INTEGER:
                //
                break;
            case DOUBLE:
                //
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
            fw.write("\tdef __init__(self):\n");
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
             * 
             * completed:
             * 
             */
        }
    }
}
