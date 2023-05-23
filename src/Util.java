import Exceptions.InvalidConfigFileException;
import Exceptions.InvalidNumberOfArgumentsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Util {
    static public HashMap<String, String> readFromConfigFile(String fileName){
        HashMap<String, String> arguments = new HashMap<>();
        try{
            String relPath = "./conf/" + fileName;
            Path absPath = Paths.get(relPath).toAbsolutePath().normalize();
            File configFile = new File(absPath.toUri());
            Scanner fileReader = new Scanner(configFile);
            int linenr = 0;
            while (fileReader.hasNextLine()){
                linenr ++;
                String[] currentLine = fileReader.nextLine().split("\\s");
                String[] elements = Arrays.copyOfRange(currentLine,1, currentLine.length);

                int expectedLength;
                try {
                    switch (currentLine[0]){
                        case "branch-number-bounds":
                            expectedLength = 2;
                            if (elements.length != expectedLength){
                                throw new InvalidNumberOfArgumentsException(elements.length, expectedLength);
                            }
                            else{
                                arguments.put("mb", elements[0]);
                                arguments.put("mxb", elements[1]);
                            }
                            continue;

                        case "max-depth":
                            expectedLength = 1;
                            if (elements.length != expectedLength){
                                throw new InvalidNumberOfArgumentsException(elements.length, expectedLength);
                            }
                            else{
                                arguments.put("mxd", elements[0]);
                            }
                            continue;

                        case "branch-length-bounds":
                            expectedLength = 2;
                            if (elements.length != expectedLength){
                                throw new InvalidNumberOfArgumentsException(elements.length, expectedLength);
                            }
                            else{
                                arguments.put("ml", elements[0]);
                                arguments.put("mxl", elements[1]);
                            }
                            continue;

                        case "dim":
                            expectedLength = 1;
                            if (elements.length != expectedLength){
                                throw new InvalidNumberOfArgumentsException(elements.length, expectedLength);
                            }
                            else{
                                arguments.put("dim", elements[0]);
                            }
                            continue;

                        case "branch-weights":
                            expectedLength = 1;
                            if (elements.length != expectedLength){
                                throw new InvalidNumberOfArgumentsException(elements.length, expectedLength);
                            }
                            else{
                                arguments.put("bw", elements[0]);
                            }
                            continue;

                        case "branch-growth":
                            expectedLength = 1;
                            if (elements.length != expectedLength){
                                throw new InvalidNumberOfArgumentsException(elements.length, expectedLength);
                            }
                            else{
                                arguments.put("bg", elements[0]);
                            }
                            continue;
                        case "angle-bounds":
                            expectedLength = 2;
                            if (elements.length != expectedLength){
                                throw new InvalidNumberOfArgumentsException(elements.length, expectedLength);
                            }
                            else{
                                arguments.put("ma", elements[0]);
                                arguments.put("mxa", elements[1]);
                            }
                            continue;
                        default:
                            throw new InvalidConfigFileException(Integer.toString(linenr), "\""+currentLine[0]+"\". Parameter not recognised.");
                    }
                } catch (InvalidNumberOfArgumentsException e) {
                    throw new InvalidConfigFileException(Integer.toString(linenr), currentLine[0]+". "+e.getMessage());
                } catch (NumberFormatException e) {
                    throw new InvalidConfigFileException(Integer.toString(linenr), currentLine[0] + ". Expected numbers as arguments.");
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("Config file could not be found.");
            System.exit(-1);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return arguments;
    }

}
