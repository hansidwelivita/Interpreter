public enum Keyword {
    CLASS("class"),
    CONSTRUCTOR("CONSTRUCTOR"), // DO NOT CHECK THIS
    VARIABLE("VARIABLE"), // DO NOT CHECK THIS
    FUNCTION("FUNCTION"), // DO NOT CHECK THIS
    INTEGER("int"),
    DOUBLE("double"),
    STRING("String"),
    RETURN("return"),
    SYSTEM("System"),
    LINECOMMENT("//"),
    PUBLICFUNCTION("public"),
    PRIVATEFUNCTION("private"),
    //try, catch, public, private, for, while, if, else, switch, do
    NOTFOUND("");
    
    private String str;
    
    Keyword(String str) {
        this.str = str;
    }
    
    public String getStr() {
        return this.str;
    }
    
    public static Keyword fromString(String str) {
        if(str != null) {
            for(Keyword k: Keyword.values()) {
                if(str.equalsIgnoreCase(k.str)) {
                   return k; 
                }
            }
        }
        return NOTFOUND;
    }
}
