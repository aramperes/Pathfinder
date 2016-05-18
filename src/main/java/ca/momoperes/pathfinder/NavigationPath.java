package ca.momoperes.pathfinder;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class NavigationPath {

    private Collection<PathNode> openNodes;
    private Collection<PathNode> closedNodes;
    private Point pointA, pointB;
    private Collection<Point> obstacles;

    public NavigationPath(Point pointA, Point pointB, Collection<Point> obstacles) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.obstacles = Collections.unmodifiableCollection(obstacles);
        this.openNodes = new ArrayList<PathNode>();
        this.closedNodes = new ArrayList<PathNode>();
    }

    private Collection<PathNode> getPath() {

        Collection<PathNode> path = new ArrayList<PathNode>();
        PathNode current = nodeAt(closedNodes, pointB);
        PathNode startNode = nodeAt(closedNodes, pointA);

        while (current != startNode) {
            path.add(current);
            current = current.getParent();
        }

        System.out.println("Done!");
        for (PathNode openNode : path) {
            System.out.println("(" + openNode.getPoint().x + ";" + openNode.getPoint().y + "): F=" + openNode.getF());
        }
        return null;
    }

    public boolean same(Point a, Point b) {
        return a.x == b.x && a.y == b.y;
    }

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

    public boolean same(PathNode a, PathNode b) {
        return a.getPoint() == b.getPoint();
    }

    public boolean contains(Collection<PathNode> collection, PathNode node) {
        return contains(collection, node.getPoint());
    }

    public boolean contains(Collection<PathNode> collection, Point p) {
        return nodeAt(collection, p) != null;
    }

    public PathNode nodeAt(Collection<PathNode> collection, Point p) {
        for (PathNode pathNode : collection) {
            if (same(pathNode.getPoint(), p))
                return pathNode;
        }
        return null;
    }

    public PathNode calculateNode(Point point) {
        double g = Math.ceil(distance(point, pointA));
        double h = Math.ceil(distance(point, pointB));
        double f = g + h;
        return new PathNode(point, g, h, f);
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
