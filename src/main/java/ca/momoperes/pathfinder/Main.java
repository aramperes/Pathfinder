package ca.momoperes.pathfinder;

import java.util.ArrayList;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();

        Collection<IntVector> map = map(
              // 0  1  2  3  4  5  6  7  8  9
                "1  1  1  1  1  1  1  1  1  1", // 0
                "1  1  1  1  1  1  1  1  1  1", // 1
                "1  1  1  1  1  1  1  1  1  1", // 2
                "1  1  1  1  1  1  1  1  1  1", // 3
                "1  1  1  2  3  4  1  1  1  1", // 4
                "1  1  1  1  1  1  1  1  1  1", // 5
                "1  1  1  1  1  1  1  1  1  1", // 6
                "1  1  1  1  1  1  1  1  1  1", // 7
                "1  1  1  1  1  1  1  1  1  1", // 8
                "1  1  1  1  1  1  1  1  1  1"  // 9
        );

        Collection<PathNode> nodes = new NavigationPath(new IntVector(5, 4, 4), new IntVector(2, 2, 1), map).calculatePath();
        for (PathNode node : nodes) {
            System.out.println(node);
        }
        System.out.println("Done in " + String.valueOf((double) (System.currentTimeMillis() - time) / 20) + " ticks.");
    }

    private static Collection<IntVector> map(String... vectors) {
        Collection<IntVector> map = new ArrayList<IntVector>();
        for (int y = 0; y < vectors.length; y++) {
            String l = vectors[y].replace(" ", "");
            for (int x = 0; x < l.length(); x++) {
                int z = Integer.valueOf(String.valueOf(l.charAt(x)));
                map.add(new IntVector(x, y, z));
            }
        }
        return map;
    }

}
