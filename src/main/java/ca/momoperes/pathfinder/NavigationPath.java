package ca.momoperes.pathfinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NavigationPath {

    private Collection<PathNode> openNodes;
    private Collection<PathNode> closedNodes;
    private IntVector pointA, pointB;
    private Collection<IntVector> map;

    /**
     * Represents a path with two target points and a collection of obstacles.
     * To calculate the actual path, use NavigationPath#calculatePath.
     *
     * @param pointA the first target point
     * @param pointB the second target point
     * @param map    a collection of obstacles to the path
     */
    public NavigationPath(IntVector pointA, IntVector pointB, Collection<IntVector> map) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.map = Collections.unmodifiableCollection(map);
        this.openNodes = new ArrayList<PathNode>();
        this.closedNodes = new ArrayList<PathNode>();
    }

    /**
     * Calculates the path between the two points
     *
     * @return a collection of all the final path nodes
     */
    public Collection<PathNode> calculatePath() {
        openNodes.add(calculateNode(pointA));
        while (true) {
            PathNode current = lowest(openNodes);
            openNodes.remove(current);
            closedNodes.add(current);

            if (same(current.getPoint(), pointB))
                return getPath();
            IntVector[] neighbours = getNeighbours(current.getPoint());
            Collection<IntVector> unreachable = getUnreachableSurroundings(current.getPoint(), neighbours);
            for (IntVector neighbour : neighbours) {
                if (neighbour == null) {
                    continue;
                }
                if (unreachable.contains(neighbour) || contains(closedNodes, neighbour)) {
                    continue;
                }
                PathNode node = calculateNode(neighbour);
                double newCost = current.getG() + distance(current.getPoint(), neighbour);
                if (newCost < node.getG() || !contains(openNodes, neighbour) || neighbours.length - unreachable.size() <= 1) {
                    node.setG(newCost);
                    node.setH(distance(neighbour, pointB));
                    node.setF(node.getG() + node.getH());
                    node.setParent(current);
                    if (!contains(openNodes, neighbour)) {
                        openNodes.add(node);
                    }
                }
            }
        }
    }

    private Collection<PathNode> getPath() {
        ArrayList<PathNode> path = new ArrayList<PathNode>();
        PathNode current = nodeAt(closedNodes, pointB);
        PathNode startNode = nodeAt(closedNodes, pointA);

        while (current != startNode) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Gets the node with the lowest F value in the collection
     *
     * @param collection the collection of nodes
     * @return the node with the lowest F value
     */
    public PathNode lowest(Collection<PathNode> collection) {
        PathNode lowest = null;
        for (PathNode pathNode : collection) {
            if (lowest == null) {
                lowest = pathNode;
                continue;
            }
            if (pathNode.getF() < lowest.getF() || (pathNode.getF() == lowest.getF() && pathNode.getH() < lowest.getH())) {
                lowest = pathNode;
            }
        }
        return lowest;
    }

    /**
     * Gets all the neighbours from a point
     *
     * @param point the central point
     * @return the point's neighbours
     */
    public IntVector[] getNeighbours(IntVector point) {
        List<IntVector> points = new ArrayList<IntVector>();
        points.add(getVectorAt(point.x + 1, point.y));
        points.add(getVectorAt(point.x + 1, point.y + 1));
        points.add(getVectorAt(point.x + 1, point.y - 1));
        points.add(getVectorAt(point.x - 1, point.y));
        points.add(getVectorAt(point.x - 1, point.y + 1));
        points.add(getVectorAt(point.x - 1, point.y - 1));
        points.add(getVectorAt(point.x, point.y + 1));
        points.add(getVectorAt(point.x, point.y - 1));
        return points.toArray(new IntVector[points.size()]);
    }

    public Collection<IntVector> getUnreachableSurroundings(IntVector point, IntVector[] vectors) {
        int z = point.z;
        Collection<IntVector> collection = new ArrayList<IntVector>();
        for (IntVector vector : vectors) {
            if (vector == null)
                continue;
            if (Math.abs(vector.z - z) > 1) {
                continue;
            }
            collection.add(vector);
        }
        return collection;
    }

    public IntVector getVectorAt(int x, int y) {
        for (IntVector intVector : map) {
            if (intVector.x == x && intVector.y == y)
                return intVector;
        }
        return null;
    }

    /**
     * Gets a node in a collection from a point
     *
     * @param collection the collection containing the node
     * @param p          the point
     * @return the node, null if it could not be found
     */
    public PathNode nodeAt(Collection<PathNode> collection, IntVector p) {
        for (PathNode pathNode : collection) {
            if (same(pathNode.getPoint(), p))
                return pathNode;
        }
        return null;
    }

    /**
     * Calculates a node from a given point
     *
     * @param point the position of the node
     * @return the node
     */
    public PathNode calculateNode(IntVector point) {
        double g = Math.ceil(distance(point, pointA));
        double h = Math.ceil(distance(point, pointB));
        double f = g + h;
        return new PathNode(point, g, h, f);
    }

    private boolean same(IntVector a, IntVector b) {
        return a.x == b.x && a.y == b.y;
    }

    private boolean same(PathNode a, PathNode b) {
        return same(a.getPoint(), b.getPoint());
    }

    private boolean contains(Collection<PathNode> collection, PathNode node) {
        return contains(collection, node.getPoint());
    }

    private boolean contains(Collection<PathNode> collection, IntVector p) {
        return nodeAt(collection, p) != null;
    }

    private double distance(IntVector a, IntVector b) {
        return Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
    }

    public IntVector getPointA() {
        return pointA;
    }

    public IntVector getPointB() {
        return pointB;
    }

    public Collection<IntVector> getMap() {
        return map;
    }
}
