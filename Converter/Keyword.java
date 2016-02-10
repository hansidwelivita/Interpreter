public enum Keyword {
    CLASS("class"),
    CONSTRUCTOR("CONSTRUCTOR"), // DO NOT CHECK THIS
	EXTENDS("extends"),
    VARIABLE("VARIABLE"), // DO NOT CHECK THIS
    FUNCTION("FUNCTION"), // DO NOT CHECK THIS
    INTEGER("int"),
    DOUBLE("double"),
    STRING("String"),
    RETURN("return"),
    SYSTEM("System"),
    LINECOMMENT("//"),
    PUBLIC("public"),
    PRIVATE("private"),
	PROTECTED("protected"),
	ABSTRACT("abstract"),
    ARITHMETIC("arithmetic"),
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
                if(str.equals(k.str)) {
                   return k; 
                }
            }
        }
        return NOTFOUND;
    }
}
