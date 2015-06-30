import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Interpreter {
    private ArrayList<String> fileInput;
    private KeywordHandler keywordHandler;
    
    public String activeLine = "";
    
    public static void main(String[] args) {
        new Interpreter();
    }
    
    public Interpreter() {
        fileInput = new ArrayList<String>();
        keywordHandler = new KeywordHandler(this);
        
        readFile("HelloWorld.java"); //eventually use JFileChooser or equivalent 
    }
    
    public void readFile(String filename) {
        File file = new File(filename);
        
        try {
            Scanner fileReader = new Scanner(file);
            
            while(fileReader.hasNext()) {
                fileInput.add(fileReader.nextLine());
            }
            
            interpret();
        } catch(FileNotFoundException e) {
            e.getStackTrace();
            // If GUI is developed output error message to the user.
        }
    }
    
    public void interpret() {
        for(int i = 0; i < fileInput.size(); i++) {
            activeLine = fileInput.get(i).trim();
            System.out.println("Reading in line: " + activeLine + " at tab " + keywordHandler.tabCounter);
            String[] tokens = activeLine.split(" ");
            System.out.print("\t");
            
            if( (tokens[0].length() >= 2) && tokens[0].substring(0,2).equals("//"))
                keywordHandler.handle("//");
            // This may eventually be a while ! true
            else if(!keywordHandler.handle(tokens[0])) {
                tokens = activeLine.split("\\.");
                System.out.print("\t");
                keywordHandler.handle(tokens[0]);
            }
            
            if(activeLine.contains("{"))
                keywordHandler.tabCounter += 1;
            if(activeLine.contains("}"))
                keywordHandler.tabCounter -= 1;
        }
        
        keywordHandler.writeFile();
    }
}
