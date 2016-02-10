public class ClassCreationHandler {
	AccessType type;
	String name = "";
	
	public ClassCreationHandler(AccessType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public void addExtends(String extendsName) {
		System.out.println(name + " now extends " + extendsName);
	}
}