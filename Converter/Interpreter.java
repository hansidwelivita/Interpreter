import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Interpreter {
    private ArrayList<String> fileInput;
    
    
    public static void main(String[] args) {
        new Interpreter();
    }
    
    public Interpreter() {
        fileInput = new ArrayList<String>();
        readFile("Interpreter.java");
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
            String[] tokens = fileInput.get(i).split(" ");
            for(int t = 0; t < tokens.length; t++) {
                System.out.println(tokens[t]);
            }
        }
    }
}
