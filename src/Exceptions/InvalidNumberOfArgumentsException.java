package Exceptions;

public class InvalidNumberOfArgumentsException extends Exception{
    public InvalidNumberOfArgumentsException(int given, int expected){
        super("Expected number of arguments:" +expected + "Given:" + given);
    }
}