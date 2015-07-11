class HelloWorld {
    int i = 0;
    double x;
    
    HelloWorld() {
        System.out.println("Hello World!");
        //test comment
    }
    
    HelloWorld(int i) {
        System.out.println("Constructor with input.");
    }
    
    HelloWorld(int l, double d) {
        System.out.println("Second constructor with same input type");
    }
    
    HelloWorld(String test, double d) {
        System.out.println("Constructor with two inputs and variables.");
        int instance_variable = 0;
    }
    
    public void testFunction(int test, String testString) {
        System.out.println("Testing");
        //function test
    }
    
    public void testIndexFunction(){
    }
    
    int z = 3;
}
