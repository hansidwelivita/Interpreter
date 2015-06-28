import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Interpreter {
    private ArrayList<String> fileInput;
    private KeywordHandler keywordHandler;
    
    public static void main(String[] args) {
        new Interpreter();
    }
    
    public Interpreter() {
        fileInput = new ArrayList<String>();
        keywordHandler = new KeywordHandler();
        
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
            System.out.println("Reading in line: " + fileInput.get(i).trim());
            String[] tokens = fileInput.get(i).trim().split(" ");
            System.out.print("\t");
            keywordHandler.handle(tokens[0]);
        }
    }
}
