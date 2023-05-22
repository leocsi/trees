import java.util.HashMap;
import java.util.List;
import java.lang.Math;
import java.util.function.ToDoubleFunction;
import java.util.ArrayList;

public class ParamHandler {
    static String[] argumentList = {
            "mb",
            "mxb",
            "ml",
            "mxl",
//                "ma",
//                "mxa",
            "mxd",
            "dim"
    };

    double minBranches;
    double maxBranches;
    double minBranchLength;
    double maxBranchLength;
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
        this.maxDepth = Integer.parseInt(paramsRaw.get("mxd"));
        this.dim = Integer.parseInt(paramsRaw.get("dim"));
        this.branchWeights = parsedoubleArray(paramsRaw.get("bw"));
        this.branchGrowth = parsedoubleArray(paramsRaw.get("bg"));

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
            if (i == 0 && this.maxDepth > 0) {
                growths.add(i, growthWithLimit(weight, growth, this.maxDepth));
            } else {
                growths.add(i, growth(weight, growth));
            }
            this.growths = growths;
        }
    }

    public ToDoubleFunction<Integer> growthWithLimit(double weight, double growth, int limit) {
        return (Integer depth) -> weight * Math.pow(growth, depth) + Math.floor((float) (depth / limit)) * Math.pow(depth, 1.4f);
    }

    public ToDoubleFunction<Integer> growth(double weight, double growth) {
        return (Integer depth) -> weight * Math.pow(growth, depth);
    }
}