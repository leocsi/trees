import processing.core.PApplet;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String[] processingArgs = {"MySketch"};

        String floatArguments = String.join("|", ParamHandler.floatArgumentList);
        String arrayArguments = String.join("|", ParamHandler.arrayArgumentList);
        Pattern pattern = Pattern.compile("(-(("+floatArguments+")\\s-?([0-9]+\\.)?-?[0-9]+|("+arrayArguments+")\\s((([0-9]+\\.)?[0-9]+),?)+|(c\\s[^\\s]*))\\s?)*");

        Matcher matcher = pattern.matcher(String.join(" ", args));
        if (matcher.matches()){
            Trees trees;
            HashMap<String, String> commandLineArgs = new HashMap<>();
            for (int i = 0; i<args.length; i = i+2){
                commandLineArgs.put(args[i].substring(1), args[i+1]); //put cl input values in hashmap
            }
            if (commandLineArgs.keySet().size() == 0){ //sample run with default params
                trees = new Trees(new HashMap<String, String>());
            }
            else if (commandLineArgs.containsKey("c")){ //config file specified
                String path = commandLineArgs.get("c");
                HashMap<String, String> configArguments = Util.readFromConfigFile(path);

                if (commandLineArgs.keySet().size() != 1){
                    commandLineArgs.remove("c");
                    configArguments.putAll(commandLineArgs); //update config file values with cl input values
                }
                trees = new Trees(configArguments);
            }
            else{
                trees = new Trees(commandLineArgs);
            }

            PApplet.runSketch(processingArgs, trees);
        }
        else{
            System.out.println("Could not parse arguments. See repo for running instructions.");
            System.exit(-1);
        }
    }
}
