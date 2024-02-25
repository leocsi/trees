import processing.core.PApplet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Trees extends PApplet{
    ParamHandler params;
    double weightsSum;
    int depth;
    boolean init;
    double[] weights;
    Branch root;
    LinkedList<Branch> newEdges;

    public Trees(HashMap<String, String> rawParams){
        this.params = new ParamHandler(rawParams);
        this.init = true;
        this.depth = 1;
        this.weights = this.params.branchWeights;
        this.weightsSum = Arrays.stream(this.weights).sum();
    }

    public void settings(){
        size(params.dim, params.dim);
    }

    public void draw(){
        if (init) {
            this.root = new Branch(null, this.g, params.dim/2f, params.dim-51, params.dim/2f, params.dim-51, 0f);
            newEdges = new LinkedList<>(
                    List.of(this.root));
            init = false;
        }

        if (newEdges.size() > 0) {
            //Recalculate weights for next level of depth
            weightsSum = 0f;
            for (int i = 0; i<params.branchWeights.length; i++) {
                double updatedValue = params.growths.get(i).applyAsDouble(depth);
                this.weights[i] = updatedValue;
                weightsSum += updatedValue;
            }

            sproutBranches();
            killBranches();
            growBranches();
    
            depth++;
        }
        else{
            save("tree.jpg");
            exit();
        }
    }

    private void sproutBranches() {
        LinkedList<Branch> previousEdges = (LinkedList<Branch>) newEdges.clone();
        newEdges.clear();
        for (Branch previousEdge : previousEdges) {
            int newBranches = getSproutNumber(random(0, (float) weightsSum));            
            
            if (newBranches == 0) { //Dead end branch
                continue;
            }
            
            //Calculate new branches with random length and angle
            double currentRootX = previousEdge.end[0];
            double currentRootY = previousEdge.end[1];
            for (int j = 0; j < newBranches; j++) {
                int newBranchLength = Math.round(random(
                        (float)params.minBranchLength,
                        (float)params.maxBranchLength * 2 / depth));
                double newBranchAngle = random(
                        (float) params.minAngle,
                        (float) params.maxAngle);
                double currentEndX = currentRootX + (newBranchLength * cos((float)newBranchAngle));
                double currentEndY = currentRootY + (newBranchLength * sin((float)newBranchAngle));

                Branch newBranch = new Branch(previousEdge, this.g, currentRootX, currentRootY, currentEndX, currentEndY, newBranchAngle);

                //Add new branch to edge branches
                newEdges.add(newBranch);

            }
        }
        //Draw!
        for (Branch branch : newEdges){
            this.g.line((float)branch.start[0], (float)branch.start[1], (float)branch.end[0], (float)branch.end[1]);
        }
    }

    private int getSproutNumber(float weight){
        for (int j = 0; j < params.branchWeights.length; j++) {
            if (weight - params.branchWeights[j] < 0) {
                return j;
            }
        }
        return -1; // Should never happen?
    }

    private void killBranches(){
        //Todo pick pick a few randomly recursive paths to delete
    }

    private void growBranches(){
        //Todo thicken branches with every evolution
    }
}