import Exceptions.InvalidConfigFileException;
import Exceptions.InvalidNumberOfArgumentsException;
import java.util.concurrent.ThreadLocalRandom;


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

                try {
                    switch (currentLine[0]){
                        case "branch-number-bounds":
                            updateArgumentsMap(arguments, new String[]{"mb", "mxb"}, elements);
                            continue;
                        case "max-depth":
                            updateArgumentsMap(arguments, new String[]{"mxd"}, elements);
                            continue;
                        case "branch-length-bounds":
                            updateArgumentsMap(arguments, new String[]{"ml", "mxl"}, elements);
                            continue;
                        case "dim":
                            updateArgumentsMap(arguments, new String[]{"dim"}, elements);
                            continue;
                        case "branch-weights":
                            updateArgumentsMap(arguments, new String[]{"bw"}, elements);
                            continue;
                        case "branch-growth":
                            updateArgumentsMap(arguments, new String[]{"bg"}, elements);
                            continue;
                        case "angle-bounds":
                            updateArgumentsMap(arguments, new String[]{"ma", "mxa"}, elements);
                            continue;
                        case "trunk-size":
                             updateArgumentsMap(arguments, new String[]{"ts"}, elements);
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

    static <T> T[] concatArrays(T[] a1, T[] a2){
        T[] result = Arrays.copyOf(a1, a1.length + a2.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    static void updateArgumentsMap(HashMap<String, String> map, String[] expectedArguments, String[] givenArguments) throws InvalidNumberOfArgumentsException{
        if (expectedArguments.length != givenArguments.length){
            throw new InvalidNumberOfArgumentsException(givenArguments.length, expectedArguments.length);
        }
        else{
            for (int i = 0; i<expectedArguments.length; i++){
                map.put(expectedArguments[i], givenArguments[i]);
            }
        }
    }

    static int weightedProbabilityExperiment(double[] weights){
        double weightsSum = Arrays.stream(weights).sum();
        double weight = ThreadLocalRandom.current().nextDouble(0, weightsSum);
        for (int i = 0;i<weights.length; i++){
            weight = weight - weights[i];
            if (weight< 0){
                return i;
            }
        }
        return -1;
    }
}
