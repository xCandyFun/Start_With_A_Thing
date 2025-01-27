package org.example.HunterAITest;

import org.example.Entities.Wall;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Hunter {
    private int x,y;
    private double speed;
    private List<Point> path;
    private final int gridSize = 20;
    private final List<Wall> walls;

    public Hunter(int x, int y, double speed, List<Wall> walls){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.walls = walls;
        this.path = new ArrayList<>();
    }

    public void updatePath(int targetX, int targetY){
        path = calculatePath(x, y, targetX, targetY);
    }

    public void move(){
        if (!path.isEmpty()) {
            Point next = path.get(0);
            int dx = next.x - x;
            int dy = next.y - y;

            // Move toward the next point on the path
            if (Math.abs(dx) > Math.abs(dy)){
                x += (dx > 0) ? speed : -speed;
            }else {
                y += (dy > 0) ? speed : -speed;
            }

            // Remove the point if we've reached it
            if (Math.abs(x - next.x) < speed && Math.abs(y - next.y) < speed) {
                path.remove(0);
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x,y,30,30);
    }
    private List<Point> calculatePath(int startX, int startY, int targetX, int targetY) {
        // Grid-based A* implementation
        int cols = 900 / gridSize;
        int rows = 600 / gridSize;

        boolean[][] obstacles = new boolean[cols][rows];
        for (Wall wall : walls) {
            int startCol = wall.getX() / gridSize;
            int startRow = wall.getY() / gridSize;
            int endCol = (wall.getX() + wall.getWidth()) / gridSize;
            int endRow = (wall.getY() + wall.getHeight()) / gridSize;

            for (int col = startCol; col <= endCol; col++){
                for (int row = startRow; row <= endRow; row++){
                    obstacles[col][row] = true;
                }
            }
        }

        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(a -> a.fCost));
        Set<Point> closedList = new HashSet<>();

        Point start = new Point(startX / gridSize, startY / gridSize);
        Point target = new Point(targetX / gridSize, targetY / gridSize);

        openList.add(new Node(start, null, 0, manhattanDistance(start, target)));

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.position.equals(target)) {
                return reconstructPath(current);
            }

            closedList.add(current.position);

            for (Point neighbor : getNeighnors(current.position, cols, rows)) {
                if (closedList.contains(neighbor) || obstacles[neighbor.x][neighbor.y]) continue;

                int gCost = current.gCost + 1;
                int hCost = manhattanDistance(neighbor, target);
                Node neighborNode = new Node(neighbor, current, gCost, gCost + hCost);

                boolean isInOpenList = openList.stream().anyMatch(n -> n.position.equals(neighbor) && n.fCost <= neighborNode.fCost);
                if (!isInOpenList) openList.add(neighborNode);

            }
        }

        return new ArrayList<>();
    }

    private List<Point> getNeighnors(Point position, int cols, int rows) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        for (int[] dir : directions) {
            int nx = position.x + dir[0];
            int ny = position.y + dir[1];
            if (nx >= 0 && ny >= 0 && nx < cols && ny < rows) {
                neighbors.add(new Point(nx, ny));
            }
        }
        return neighbors;
    }

    private int manhattanDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private List<Point> reconstructPath(Node node) {
        List<Point> path = new ArrayList<>();
        while (node != null) {
            path.add(0, new Point(node.position.x * gridSize, node.position.y * gridSize));
            node = node.parent;
        }
        return path;
    }

    private static class Node {
        Point position;
        Node parent;
        int gCost;
        int fCost;

        Node(Point position, Node parent, int gCost, int fCost) {
            this.position = position;
            this.parent = parent;
            this.gCost = gCost;
            this.fCost = fCost;
        }
    }
}
