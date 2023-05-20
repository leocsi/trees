import java.util.List;
import java.lang.Math;
import java.util.function.ToDoubleFunction;
import java.util.ArrayList;

public class ParamHandler {
    double minBranches;
    double maxBranches;
    double minBranchLength;
    double maxBranchLength;
    int maxDepth;
    int dim;
    double[] branchWeights;
    double[] branchGrowth;
    List<ToDoubleFunction<Integer>> growths;


    public ParamHandler(String[] paramsRaw){
        this.minBranches = Double.parseDouble(paramsRaw[0]);
        this.maxBranches = Double.parseDouble(paramsRaw[1]);
        this.minBranchLength = Double.parseDouble(paramsRaw[2]);
        this.maxBranchLength = Double.parseDouble(paramsRaw[3]);
        this.maxDepth = Integer.parseInt(paramsRaw[4]);
        this.dim = Integer.parseInt(paramsRaw[5]);
        this.branchWeights = parsedoubleArray(paramsRaw[6]);
        this.branchGrowth = parsedoubleArray(paramsRaw[7]);

        this.constructNewGrowthFunctions();
    }

    public double[] parsedoubleArray(String rawArray){
        String[] stringArray = rawArray.split(",");
        int size = stringArray.length;
        double[] doubleArray = new double[size];
        for (int i = 0; i<size; i++){
            doubleArray[i] = Double.parseDouble(stringArray[i]);
        }
        return doubleArray;
    }

    private void constructNewGrowthFunctions() {
        List<ToDoubleFunction<Integer>> growths = new ArrayList<>(branchWeights.length);
        for (int i = 0; i<this.branchWeights.length; i++){
            double weight = this.branchWeights[i];
            double growth = this.branchGrowth[i];
            if (i == 0 && this.maxDepth > 0){
                    growths.add(i, growthWithLimit(weight, growth, this.maxDepth));
            }
            else{
                growths.add(i, growth(weight, growth));
            }
        this.growths = growths;
        }
    }

    public ToDoubleFunction<Integer> growthWithLimit(double weight, double growth, int limit){
        return (Integer depth) -> weight * Math.pow(growth, depth) + Math.floor((float)(depth / limit)) * Math.pow(depth, 1.4f);
    }

    public ToDoubleFunction<Integer> growth(double weight, double growth){
        return (Integer depth) -> weight * Math.pow(growth, depth);
    }
}
