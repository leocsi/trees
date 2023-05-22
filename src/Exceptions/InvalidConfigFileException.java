package Exceptions;

public class InvalidConfigFileException extends Exception{
    public InvalidConfigFileException(String line, String details){
        super("Incorrect config file format. Error at line: " + line + ", in " + details + "\nSee repository for correct config file templates.");
    }
}

