import processing.core.PGraphics;

public class Branch {
    public Branch parent;
    public double[] start;
    public double[] end;
    public double angle;

    public Branch(Branch parent, PGraphics graphicsHandler, double x1, double y1, double x2, double y2, double theta){
        this.parent = parent;
        this.start = new double[]{x1,y1};
        this.end = new double[]{x2, y2};
        this.angle = theta;

    }
}
