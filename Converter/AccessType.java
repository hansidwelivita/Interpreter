public enum AccessType {
    PUBLIC("public"),
	PRIVATE("private"),
	PROTECTED("protected"),
	ABSTRACT("abstract"),
    DEFAULT("default");
    
    private String str;
    
    AccessType(String str) {
        this.str = str;
    }
    
    public String getStr() {
        return this.str;
    }
    
    public static AccessType fromString(String str) {
        if(str != null) {
            for(AccessType k: AccessType.values()) {
                if(str.equalsIgnoreCase(k.str)) {
                   return k; 
                }
            }
        }
        return DEFAULT;
    }
}