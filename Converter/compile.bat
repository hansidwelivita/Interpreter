javac Interpreter.java Keyword.java KeywordHandler.java HelloWorld.java 
javac SystemHandler.java PrintStreamHandler.java ObjectHandler.java
java Interpreter

del KeywordHandler$1.class KeywordHandler$ConstructorHandler.class KeywordHandler$OutputHandler.class KeywordHandler$Variable.class
del Interpreter.class Keyword.class KeywordHandler.class HelloWorld.class SystemHandler.class

cd python
more HelloWorld.py
cd ..