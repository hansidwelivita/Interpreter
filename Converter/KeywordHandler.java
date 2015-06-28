public class KeywordHandler {
    public void handle(String keyword) {
        switch(Keyword.fromString(keyword)) {
            case CLASS:
                System.out.println("Found keyword: class");
                // Call the function for class
                break;
            case NOTFOUND:
                System.out.println("No keyword found.");
                break;
        }
    }
}
