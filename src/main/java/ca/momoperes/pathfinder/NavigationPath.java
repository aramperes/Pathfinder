package ca.momoperes.pathfinder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NavigationPath {

    private Collection<PathNode> openNodes;
    private Collection<PathNode> closedNodes;
    private Point pointA, pointB;
    private Collection<Point> obstacles;

    /**
     * Represents a path with two target points and a collection of obstacles.
     * To calculate the actual path, use NavigationPath#calculatePath.
     *
     * @param pointA    the first target point
     * @param pointB    the second target point
     * @param obstacles a collection of obstacles to the path
     */
    public NavigationPath(Point pointA, Point pointB, Collection<Point> obstacles) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.obstacles = Collections.unmodifiableCollection(obstacles);
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
            Point[] neighbours = getNeighbours(current.getPoint());
            for (Point neighbour : neighbours) {
                if (obstacles.contains(neighbour) || contains(closedNodes, neighbour))
                    continue;

                PathNode node = calculateNode(neighbour);
                double newCost = current.getG() + distance(current.getPoint(), neighbour);
                if (newCost < node.getG() || !contains(openNodes, neighbour)) {
                    node.setG(newCost);
                    node.setH(distance(neighbour, pointB));
                    node.setF(node.getG() + node.getH());
                    node.setParent(current);
                    if (!contains(openNodes, neighbour))
                        openNodes.add(node);
                }
            }
        }
    }

    private Collection<PathNode> getPath() {
        Collection<PathNode> path = new ArrayList<PathNode>();
        PathNode current = nodeAt(closedNodes, pointB);
        PathNode startNode = nodeAt(closedNodes, pointA);

        while (current != startNode) {
            path.add(current);
            current = current.getParent();
        }

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
    public Point[] getNeighbours(Point point) {
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(point.x + 1, point.y));
        points.add(new Point(point.x + 1, point.y + 1));
        points.add(new Point(point.x + 1, point.y - 1));
        points.add(new Point(point.x - 1, point.y));
        points.add(new Point(point.x - 1, point.y + 1));
        points.add(new Point(point.x - 1, point.y - 1));
        points.add(new Point(point.x, point.y + 1));
        points.add(new Point(point.x, point.y - 1));
        return points.toArray(new Point[points.size()]);
    }

    /**
     * Gets a node in a collection from a point
     *
     * @param collection the collection containing the node
     * @param p          the point
     * @return the node, null if it could not be found
     */
    public PathNode nodeAt(Collection<PathNode> collection, Point p) {
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
    public PathNode calculateNode(Point point) {
        double g = Math.ceil(distance(point, pointA));
        double h = Math.ceil(distance(point, pointB));
        double f = g + h;
        return new PathNode(point, g, h, f);
    }

    private boolean same(Point a, Point b) {
        return a.x == b.x && a.y == b.y;
    }

    private boolean same(PathNode a, PathNode b) {
        return same(a.getPoint(), b.getPoint());
    }

    private boolean contains(Collection<PathNode> collection, PathNode node) {
        return contains(collection, node.getPoint());
    }

    private boolean contains(Collection<PathNode> collection, Point p) {
        return nodeAt(collection, p) != null;
    }


    private double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
    }

    public Point getPointA() {
        return pointA;
    }

    public Point getPointB() {
        return pointB;
    }

    public Collection<Point> getObstacles() {
        return obstacles;
    }
}
