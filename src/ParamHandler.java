import java.util.HashMap;
import java.util.List;
import java.lang.Math;
import java.util.function.ToDoubleFunction;
import java.util.ArrayList;

public class ParamHandler {
    static String[] floatArgumentList = {
            "mb",
            "mxb",
            "ml",
            "mxl",
            "ma",
            "mxa",
            "mxd",
            "dim"
    };

    static String[] arrayArgumentList = {
            "bw",
            "bg"
    };

    static String[] argumentList = Util.concatArrays(floatArgumentList, arrayArgumentList);

    int trunkSize;
    double minBranches;
    double maxBranches;
    double minBranchLength;
    double maxBranchLength;
    double minAngle;
    double maxAngle;
    int maxDepth;
    int dim;
    double[] branchWeights;
    double[] branchGrowth;
    List<ToDoubleFunction<Integer>> growths;


    public ParamHandler(HashMap<String, String> paramsRaw) {

        if (paramsRaw.keySet().size() != argumentList.length) { //resolve missing arguments
            HashMap<String, String> configParams = Util.readFromConfigFile("default.conf");
            configParams.putAll(paramsRaw);
            paramsRaw = configParams;
        }

        this.minBranches = Double.parseDouble(paramsRaw.get("mb"));
        this.maxBranches = Double.parseDouble(paramsRaw.get("mxb"));
        this.minBranchLength = Double.parseDouble(paramsRaw.get("ml"));
        this.maxBranchLength = Double.parseDouble(paramsRaw.get("mxl"));
        this.minAngle = Double.parseDouble(paramsRaw.get("ma"));
        this.maxAngle = Double.parseDouble(paramsRaw.get("mxa"));
        this.maxDepth = Integer.parseInt(paramsRaw.get("mxd"));
        this.dim = Integer.parseInt(paramsRaw.get("dim"));
        this.branchWeights = parsedoubleArray(paramsRaw.get("bw"));
        this.branchGrowth = parsedoubleArray(paramsRaw.get("bg"));
        this.trunkSize = Integer.parseInt(paramsRaw.get("ts"));


        this.constructNewGrowthFunctions();
    }

    public double[] parsedoubleArray(String rawArray) {
        String[] stringArray = rawArray.split(",");
        int size = stringArray.length;
        double[] doubleArray = new double[size];
        for (int i = 0; i < size; i++) {
            doubleArray[i] = Double.parseDouble(stringArray[i]);
        }
        return doubleArray;
    }

    private void constructNewGrowthFunctions() {
        List<ToDoubleFunction<Integer>> growths = new ArrayList<>(branchWeights.length);
        for (int i = 0; i < this.branchWeights.length; i++) {
            double weight = this.branchWeights[i];
            double growth = this.branchGrowth[i];

            if (i == 0){
                growths.add(i, this.trunkSize>0? lowerBoundedGrowth(weight, growth, this.trunkSize):
                        growth(weight, growth));
            }
            else if(i == 1){
                growths.add(i, this.maxDepth >0? upperBoundedGrowth(weight, growth, this.maxDepth):
                        growth(weight, growth));
            }
            else{
                growths.add(i,
                        this.trunkSize> 0? this.maxDepth> 0? boundedGrowth(weight, growth, this.trunkSize, this.maxDepth):
                                lowerBoundedGrowth(weight, growth, this.trunkSize):
                                this.maxDepth>0? upperBoundedGrowth(weight, growth, this.maxDepth): growth(weight, growth)
                );
            }
        }
        this.growths = growths;
    }

    public ToDoubleFunction<Integer> upperBoundedGrowth(double weight, double growth, int upperBound) {
        return (Integer depth) -> weight * Math.pow(growth, depth) * Math.floor((float) upperBound/depth);
    }

    public ToDoubleFunction<Integer> growth(double weight, double growth) {
        return (Integer depth) -> weight * Math.pow(growth, depth);
    }

    public ToDoubleFunction<Integer> lowerBoundedGrowth(double weight, double growth, int lowerBound){
        return (Integer depth) -> Math.floor((float) (depth / lowerBound)) * weight * Math.pow(growth, depth);
    }

    public ToDoubleFunction<Integer> boundedGrowth(double weight, double growth, int lowerBound, int upperBound){
        return (Integer depth) -> Math.floor((float) (depth / lowerBound)) * (weight * Math.pow(growth, depth)) * Math.floor((float) (upperBound / depth));
    }
}