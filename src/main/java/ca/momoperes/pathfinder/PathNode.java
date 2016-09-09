package ca.momoperes.pathfinder;

import java.awt.*;

public class PathNode {

    private IntVector point;
    private double g, h, f;
    private PathNode parent;

    public PathNode(IntVector point, double g, double h, double f) {
        this.point = point;
        this.g = g;
        this.h = h;
        this.f = f;
    }

    public IntVector getPoint() {
        return point;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

    public double getF() {
        return f;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setF(double f) {
        this.f = f;
    }

    public PathNode getParent() {
        return parent;
    }

    public void setParent(PathNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(point.x) + ";" + String.valueOf(point.y) + ";" + String.valueOf(point.z) + "): F=" + String.valueOf(f);
    }
}
