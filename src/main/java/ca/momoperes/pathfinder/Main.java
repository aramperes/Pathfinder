package ca.momoperes.pathfinder;

import java.awt.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Point a = new Point(0, 0);
        Point b = new Point(10, 10);
        long time = System.currentTimeMillis();
        new NavigationPath(a, b, Arrays.asList(new Point(6, 6))).calculatePath();
        System.out.println("Done in " + (System.currentTimeMillis() - time));
    }

}
