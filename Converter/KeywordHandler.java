import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Arrays;

public class KeywordHandler {
    private final boolean DEBUG = true;


    private Interpreter interpreter;
    private File activeFile; 
    private String activeLine, className;
    
    private ArrayList<OutputHandler> output;
    
    
    /*
     * Constructor stuff 
     */
    private ArrayList<ConstructorHandler> constructors; // Holds activeConstructors as they are being processed
    private ConstructorHandler activeConstructor; // The current constructor that the interpreter is on.
    private boolean inConstructor = false;  // This lets the file writer know to add to the activeConstructor
    // ----- END
    
    
    private ArrayList<String> variables;
    
    private int lineCounter, classCounter;
    public int tabCounter;
    
    private boolean hasConstructor = false;
    private ArrayList<String> constructorVariables;
	
	/*
	 * java.lang.Object calls?
	 */
	public SystemHandler system;
	public PrintStreamHandler printStream;
    
    
    /*
     * TODO: figure out a way to increment tabCounter based on start curly brackets { , 2 different cases depending 
     *     on whether code is written with { on its own line or at the end of a line 
     */
    public KeywordHandler(Interpreter interpreter) {
        this.interpreter = interpreter;   
        
        output = new ArrayList<OutputHandler>();
        constructors = new ArrayList<ConstructorHandler>();
        
        variables = new ArrayList<String>();   
        constructorVariables = new ArrayList<String>();
		
		system = new SystemHandler(this);
		printStream = new PrintStreamHandler(this);
    }
    
    public boolean handle(String keyword) {
        switch(Keyword.fromString(keyword)) {
            case CLASS:
                System.out.println("Found keyword: class");
                createClass();
                break;
            case DOUBLE:
            case INTEGER:
                System.out.println("Found keyword: variable");
                createVariable();
                break;      
            case STRING:
                //
                break;
            case RETURN:
                writeReturn();
                break;                
            case SYSTEM:
                System.out.println("Found keyword: System");
				system.handleCall(interpreter.activeLine);
                createSystemCall();
                break;
            case LINECOMMENT:
                System.out.println("Found line comment");
                writeComment();
                break;
            //case PRIVATEFUNCTION:
                //System.out.println("Private Function");
            case PUBLICFUNCTION:
                System.out.println("Public Function");
                writeFunction();
                break;
                
            case NOTFOUND:
                if(checkForConstructor()) {
                    createConstructor();
                    return true;
                } else if(arithmeticCheck()){ // check for arithmetic signs or assignment statements 
                    writeArithmetic();
                    return true;
                } else {
                    System.out.println("No keyword found.");
                }
                return false;
        }
        return true;
    }
    
    private void add(OutputHandler oneHandle) {
        if(inConstructor) {
            tabCounter++;
            activeConstructor.outputs.add(new OutputHandler(tabCounter, "\t" + oneHandle.output, oneHandle.type));
            tabCounter--;
        }else
            output.add(oneHandle);
    }
    
    private boolean checkForConstructor() {
        activeLine = interpreter.activeLine;
        String[] tokens = activeLine.trim().split("\\(");
        
        if(tokens.length < 2)
            return false;
        
        // This needs to be reworked
        if(tokens[0].equals(className))
            return true;
            
        return false;
    }
    
    private void createClass() {
        activeLine = interpreter.activeLine;
        String[] tokens = activeLine.split(" ");
        String[] nameTokens = tokens[1].split("\\(");
        className = nameTokens[0].trim();
        classCounter = tabCounter + 1;
        //output.add(new OutputHandler(tabCounter, "class " + className + "():\n", Keyword.CLASS));
    }
    
    private void writeFunction() {
        inConstructor = false;
        
        //need to be able to recognize function params
        String funcName;
        String allNames = "";
        activeLine = interpreter.activeLine;
        String firstParams = activeLine.substring(activeLine.indexOf("(")+1 , activeLine.lastIndexOf(")")); //get inside of parenthesis
        String[] secondParams = firstParams.split("[ (,]+");
        String[] variableNames = new String[secondParams.length/2];
        int variableCounter = 0;
        if(firstParams.equals("")) { //check for function that doesn't accept arguments
            System.out.println("No Parameters");
            String[] tokens = activeLine.split("[ {]");
            output.add(new OutputHandler(tabCounter, tabHandler() + "def " + tokens[2] + ":\n", Keyword.FUNCTION));
        }
        else {
            for(int i = 1; i < secondParams.length; i+=2) { //isolate variable names
                variableNames[variableCounter] = secondParams[i]; 
                variableCounter++;
            }
            for(int i = 0; i < variableNames.length; i++) {
                if(i == (variableNames.length)-1 )
                    allNames = allNames + variableNames[i];
                else
                    allNames = allNames + variableNames[i] + ",";
            }
            String[] tokens = activeLine.split("[ (]");
           
            output.add(new OutputHandler(tabCounter, tabHandler() + "def " + tokens[2] + "(" + allNames + "):\n" , Keyword.FUNCTION));
        }
    }
    
    private void writeReturn() {
        activeLine = interpreter.activeLine.substring(0, interpreter.activeLine.indexOf(";"));
        output.add(new OutputHandler(tabCounter, tabHandler() + activeLine, Keyword.RETURN));
    }
    
    private void createConstructor() {
        if(DEBUG)
            System.out.println("Constructor found.");
        
        inConstructor = true;
        activeConstructor = new ConstructorHandler();
        
        activeLine = interpreter.activeLine.replace(className, "");
        activeLine = activeLine.substring(activeLine.indexOf("(") + 1, activeLine.lastIndexOf(")")).replaceAll(",", " ");
        String[] tokens = activeLine.split("\\s+");

        for(int i = 0; i < tokens.length; i++) {
            if(i%2 == 1)
                activeConstructor.activeVariables.add(new Variable(tokens[i], tokens[i-1]));
            if(i%2 == 1 && !constructorVariables.contains(tokens[i]))
                constructorVariables.add(tokens[i]);
        }
        
        constructors.add(activeConstructor);
    }
    
    private void writeConstructorToFile(FileWriter fw) throws IOException{
        tabCounter++;
        String line = tabHandler() + className + "(self";
        for(int i = 0; i < constructorVariables.size(); i++) {
            line += ", " + constructorVariables.get(i) + "=None";
        }
        line += "):\n";
        fw.write(line);
        
        tabCounter++;
        for(int i = 0; i < variables.size(); i++) {
            fw.write(tabHandler() + variables.get(i));
        }
        fw.write("\n");
        
        for(ConstructorHandler c: constructors) {
            line = tabHandler() + "if(";
            for(int i = 0; i < constructorVariables.size(); i++) {
                String var = constructorVariables.get(i);
                if(c.contains(var)) {
                    if(i < constructorVariables.size()-1)
                        line += "(" + var + " is not None and isinstance(" + var + ", " + c.getType(var) + ") and ";
                    else
                        line += var + " is not None):\n";
                } else {
                    if(i < constructorVariables.size()-1)
                        line += var + " is None and ";
                    else
                        line += var + " is None):\n";
                }
            }
            fw.write(line);
            
            tabCounter++;
            for(OutputHandler out : c.outputs) {
                fw.write(out.output);
            }
            tabCounter--;
        }
        tabCounter-=2;
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
             */
            
            if(tokens[2].substring(0,7).equals("println")) { 
                //tokens[2] is println("Hello World!");, use substring method to isolate println
                //2nd argument of substring is actually 1 more than the string will return
                // Should the way it prints depend on what version of python?
                if(DEBUG)
                    System.out.println("Print new line found");
                    
                String printedString = "\'" + activeLine.substring(activeLine.indexOf("\"")+1, activeLine.lastIndexOf("\"")) + "\'";
                //String printedString = tokens[2].substring(8, ( tokens[2].length() - 2 ) ); //this should isolate string being printed.  8 is the index of the first character AFTER the first quotation the 2nd argument should be index of the 
                                                                                            //second quotation, but the 2nd quotation will not be included in the returned string  
                add(new OutputHandler(tabCounter, tabHandler() + "print " + printedString + " \n", Keyword.STRING));
            }
            //eventually add the other print calls: printf, print
        }
    }
    
    private void createVariable() {
        activeLine = interpreter.activeLine;
        activeLine = activeLine.substring(activeLine.indexOf(" ")).replaceAll(" ", "");
        String[] tokens = activeLine.split(" |=|;");
        if(tabCounter == classCounter) {
            String variableText = "self." + tokens[0];
            
            if(tokens.length >= 2)
                variableText = variableText.concat(" = " + tokens[1] + "\n");
            else
                variableText = variableText.concat(" = 0\n");
            
            if(hasConstructor) {
                for(int i = output.size() - 1; i > 0; i--) {
                    if(output.get(i).type == Keyword.CONSTRUCTOR) {
                        int tempTabs = tabCounter;
                        tabCounter = output.get(i).tabCount + 1;
                        output.add(i+1, new OutputHandler(tabCounter, tabHandler() + variableText, Keyword.VARIABLE));
                        tabCounter = tempTabs;
                    }
                }
            } else {
                variables.add(variableText);
            }
        } else {
            String variableText = tokens[0];
            
            if(tokens.length >= 2)
                variableText = variableText.concat(" = " + tokens[1] + "\n");
            else
                variableText = variableText.concat(" = 0\n");
                
            add(new OutputHandler(tabCounter, tabHandler() + variableText, Keyword.VARIABLE));
        }
    }
    
    private boolean arithmeticCheck() {
        activeLine = interpreter.activeLine;
        if(activeLine.contains("+") || activeLine.contains("-") || activeLine.contains("*") || activeLine.contains("/") || activeLine.contains("=") )
            return true;
        else
            return false;
    }
    
    private void writeArithmetic() {
        activeLine = interpreter.activeLine.replace(";","");
        String variableString;
        //check if increment/decrement by 1, if not: write the whole line to the file
        if(activeLine.contains("++") || activeLine.contains("--")) {
            if(activeLine.contains("++"))
                variableString = activeLine.replace("++","") + " += 1";
            else
                variableString = activeLine.replace("--","") + " -= 1";
            output.add(new OutputHandler(tabCounter, tabHandler() + variableString + "\n", Keyword.ARITHMETIC));
        }
        else 
            output.add(new OutputHandler(tabCounter, tabHandler() + activeLine + "\n", Keyword.ARITHMETIC));
            
    }
    
    private void writeComment() {
        activeLine = interpreter.activeLine.replaceAll("//","");
        add(new OutputHandler(tabCounter, tabHandler() + "#" + activeLine + "\n", Keyword.LINECOMMENT));
    }

    //contain tab escapes in a single string, call this every time we write to the file
    //this should work for decrementing tabs as well as long as we correctly keep track of tabCounter in the functions
    private String tabHandler() {
        String tabString = "";
        for(int i = 0; i < tabCounter; i++) {
            tabString += "\t";
        }
        return tabString;
    }
    
    
    public void writeFile() {
        inConstructor = false;
        
        activeFile = new File("python/" + className + ".py");
        
        if(activeFile.exists()) 
            activeFile.delete();
            
        try {
            activeFile.createNewFile();
            FileWriter fw = new FileWriter(activeFile);
            
            fw.write("class " + className + "():\n");
            
            writeConstructorToFile(fw);
            
            for(OutputHandler out : output)
                fw.write(out.output);
            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private class OutputHandler {
        public int tabCount;
        public String output;
        public Keyword type;
        
        public OutputHandler(int tabCount, String output, Keyword type) {
            this.tabCount = tabCount;
            this.output = output;
            this.type = type;
        }
    }
    
    private class ConstructorHandler {
        public ArrayList<Variable> activeVariables;
        public ArrayList<OutputHandler> outputs;
        
        public ConstructorHandler() {
            activeVariables = new ArrayList<Variable>();
            outputs = new ArrayList<OutputHandler>();
        }
        
        public boolean contains(String var) {
            for(Variable v : activeVariables)
                if(v.var.equals(var))
                    return true;
            return false;
        }
        
        public String getType(String var) {
            for(Variable v : activeVariables)
                if(v.var.equals(var))
                    return v.type;
            return "";
        }
    }
    
    private class Variable {
        public String var, type;
        
        public Variable(String var, String type) {
            this.var = var;
            this.type = type;
        }
    }
}
