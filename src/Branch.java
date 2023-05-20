import processing.core.PApplet;
import processing.core.PGraphics;

public class Branch {
    public double[] start;
    public double[] end;
    public double angle;

    public Branch(PGraphics graphicsHandler, double x1, double y1, double x2, double y2, double theta){
        this.start = new double[]{x1,y1};
        this.end = new double[]{x2, y2};
        this.angle = theta;

        graphicsHandler.line((float)x1, (float)y1, (float)x2, (float)y2);
    }
}
