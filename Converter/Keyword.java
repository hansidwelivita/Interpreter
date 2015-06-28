public enum Keyword {
    CLASS("class"),
    INTEGER("int"),
    DOUBLE("double"),
    STRING("String"),
    RETURN("return"),
    //try, catch, public, private, for, while
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
