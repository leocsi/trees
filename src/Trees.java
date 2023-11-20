import processing.core.PApplet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Trees extends PApplet{
    ParamHandler params;
    int depth;
    boolean init;
    double[] weights;
    LinkedList<Branch> all;
    LinkedList<Branch> current;
    HashMap<Integer, Integer> newBranches;

    public Trees(HashMap<String, String> rawParams){
        this.params = new ParamHandler(rawParams);
        this.init = true;
        this.depth = 1;
        this.weights = this.params.branchWeights;
        this.all = new LinkedList<>();
        newBranches = new HashMap<>();
    }

    public void settings(){
        size(params.dim, params.dim);
    }

    public void draw(){
        if (init) {
            current = new LinkedList<>(
                    List.of(new Branch(this.g,params.dim/2f, params.dim-51, params.dim/2f, params.dim-51, 0f)));
            init = false;
        }
        if (current.size() > 0) {
            for (int i = 0; i<params.branchWeights.length; i++) {
                double updatedValue = params.growths.get(i).applyAsDouble(depth);
                this.weights[i] = updatedValue;
            }
            branch();
            depth++;
        }
        else{
            save("tree.jpg");
            exit();
        }
    }

    private void branch() {
        LinkedList<Branch> currentBranchesTemp = (LinkedList<Branch>) current.clone();
        current.clear();
        for (Branch currentBranch : currentBranchesTemp) {
            int newBranches = Util.weightedProbabilityExperiment(this.weights);
            this.newBranches.put(newBranches, this.newBranches.getOrDefault(newBranches, 0) + 1);
            if (newBranches == 0) { //Dead end branch
                all.add(currentBranch);
                continue;
            }

            double currentRootX = currentBranch.end[0];
            double currentRootY = currentBranch.end[1];
            for (int j = 0; j < newBranches; j++) {
                int newBranchLength = Math.round(random(
                        (float)params.minBranchLength,
                        (float)params.maxBranchLength * 2 / depth));


                double newBranchAngle = random(
                        (float) params.minAngle,
                        (float) params.maxAngle);
                double currentEndX = currentRootX + (newBranchLength * cos((float)newBranchAngle));
                double currentEndY = currentRootY + (newBranchLength * sin((float)newBranchAngle));
//                if (isOutOfBounds(currentEndX, currentEndY)) {
//                    j--;
//                    continue;
//                }TODO

                Branch newBranch = new Branch(this.g, currentRootX, currentRootY, currentEndX, currentEndY, newBranchAngle);
                current.add(newBranch);

                all.add(currentBranch);

            }
        }
    }

//    private boolean isOutOfBounds(double x, double y) { // INTRODUCE PADDING PARAM INSTEAD OF MAGIC NUMS
//        if (x < 50 || x > 250)
//            return true;
//        return y < 50  || y > 250;
//    }
}