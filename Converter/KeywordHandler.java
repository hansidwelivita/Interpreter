import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Arrays;

public class KeywordHandler {
    private Interpreter interpreter;
    private File activeFile; 
    private String activeLine, className;
    
    private ArrayList<OutputHandler> output;
    private ArrayList<String> variables;
    
    private int lineCounter, classCounter;
    public int tabCounter;
    
    private boolean hasConstructor = false;
    private ArrayList<String> constructorVariables;
    
    
    /*
     * TODO: figure out a way to increment tabCounter based on start curly brackets { , 2 different cases depending 
     *     on whether code is written with { on its own line or at the end of a line 
     */
    public KeywordHandler(Interpreter interpreter) {
        this.interpreter = interpreter;    
        output = new ArrayList<OutputHandler>();
        variables = new ArrayList<String>();   
        constructorVariables = new ArrayList<String>();
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
                //
                break;                
            case SYSTEM:
                System.out.println("Found keyword: System");
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
        output.add(new OutputHandler(tabCounter, "class " + className + "():\n", Keyword.CLASS));
    }
    
    private void writeFunction() {
        //need to be able to recognize function params
        String funcName;
        String allParams = "";
        activeLine = interpreter.activeLine;
        String firstParams = activeLine.substring(activeLine.indexOf("(")+1 , activeLine.lastIndexOf(")")); //get inside of parenthesis
        String[] secondParams = firstParams.split("[ (,]+");
        String[] variableNames = new String[secondParams.length/2];
        int variableCounter = 0;
        if(firstParams.equals("")) {
            System.out.println("Empty");
            String[] tokens = activeLine.split("[ {]");
            output.add(new OutputHandler(tabCounter, tabHandler() + "def " + tokens[2] + ":\n", Keyword.FUNCTION));
        }
        else {
            for(int i = 1; i < secondParams.length; i+=2) {
                variableNames[variableCounter] = secondParams[i]; 
                variableCounter++;
            
            }
            for(int i = 0; i < variableNames.length; i++) {
                if(i == variableNames.length)
                    allParams = allParams + variableNames[i];
                else
                    allParams = allParams + variableNames[i] + ",";
            }
            String[] tokens = activeLine.split("[ (]");
           
            output.add(new OutputHandler(tabCounter, tabHandler() + "def " + tokens[2] + "(" + allParams + " ):\n" , Keyword.FUNCTION));
        }
    }
    
    private void createConstructor() {
        System.out.println("Constructor found.");
        activeLine = interpreter.activeLine;
        activeLine = activeLine.replace(className, "");
        activeLine = activeLine.substring(activeLine.indexOf("(") + 1, activeLine.lastIndexOf(")"));
        activeLine = activeLine.replaceAll(",", " ");
        String[] tokens = activeLine.split("\\s+");
        
        ConstructorHandler constructorHandler = new ConstructorHandler();
        for(int i = 0; i < tokens.length; i++)
            if(i%2 == 1)
                constructorHandler.activeVariables.add(tokens[i]);
        
        /*if(hasConstructor) {
            ArrayList<String> currentConstructorVariables = new ArrayList<String>();
            for(int i = output.size() - 1; i > 0; i--) {
                if(output.get(i).type == Keyword.CONSTRUCTOR) {
                    String holder = output.get(i).output;
                    String beginning = holder.substring(0, holder.lastIndexOf(")"));
                 
                    for(int c = 0; c < tokens.length; c++) {
                        if(c%2 == 1 && !constructorVariables.contains(tokens[c])) {
                            constructorVariables.add(tokens[c]);
                            beginning += ", " + tokens[c] + "=None";
                        }
                        if(c%2 == 1)
                            currentConstructorVariables.add(tokens[c]);
                    }
                    beginning += "):\n";
            
                    output.get(i).output = beginning;
                }
            }
            
            String out = "if(";
            for(int i = 0; i < constructorVariables.size(); i++) {
                if(i < constructorVariables.size() - 1) {
                    if(currentConstructorVariables.contains(constructorVariables.get(i)))
                        out += constructorVariables.get(i) + " is not None and ";
                    else
                        out += constructorVariables.get(i) + " is None and ";
                } else
                    if(currentConstructorVariables.contains(constructorVariables.get(i)))
                        out += constructorVariables.get(i) + " is not None):\n";
                    else
                        out += constructorVariables.get(i) + " is None):\n";
            }
            
            output.add(new OutputHandler(tabCounter, out, Keyword.IF));
        } else {
            hasConstructor = true;
        
            String line = "def __init__(self";            
            for(int i = 0; i < tokens.length; i++) {
                if(i%2 == 1 && !constructorVariables.contains(tokens[i])) {
                    constructorVariables.add(tokens[i]);
                    line += ", " + tokens[i] + "=None";
                }
            }
            line += "):\n";
            
            output.add(new OutputHandler(tabCounter, tabHandler() + line, Keyword.CONSTRUCTOR));
            for(String var : variables) {
                tabCounter += 1;
                output.add(new OutputHandler(tabCounter, tabHandler() + var, Keyword.VARIABLE));
                tabCounter -= 1;
            }
        }*/
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
                System.out.println("Print new line found");
                String printedString = "\'" + activeLine.substring(activeLine.indexOf("\"")+1, activeLine.lastIndexOf("\"")) + "\'";
                //String printedString = tokens[2].substring(8, ( tokens[2].length() - 2 ) ); //this should isolate string being printed.  8 is the index of the first character AFTER the first quotation the 2nd argument should be index of the 
                                                                                            //second quotation, but the 2nd quotation will not be included in the returned string  
                output.add(new OutputHandler(tabCounter, tabHandler() + "print " + printedString + " \n", Keyword.STRING));
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
                
            output.add(new OutputHandler(tabCounter, tabHandler() + variableText, Keyword.VARIABLE));
        }
    }
    
    private void writeComment() {
        activeLine = interpreter.activeLine.replaceAll("//","");
        output.add(new OutputHandler(tabCounter, tabHandler() + "#" + activeLine + "\n", Keyword.LINECOMMENT));
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
    
    
    public void writeFile() {
        activeFile = new File("python/" + className + ".py");
        
        if(activeFile.exists()) 
            activeFile.delete();
            
        try {
            activeFile.createNewFile();
            FileWriter fw = new FileWriter(activeFile);
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
        public ArrayList<String> activeVariables;
        
        public ConstructorHandler() {
            activeVariables = new ArrayList<String>();
        }
    }
}
