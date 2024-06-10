package ru.itmo.game.util;

import java.util.*;

public class HadlerEnemies {
    public static List<Point> findFreeCells(boolean[][] grid) {
        List<Point> freeCells = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!grid[i][j]) {
                    freeCells.add(new Point(i, j));
                }
            }
        }
        return freeCells;
    }

    public static List<Point> randomWalk(boolean[][] grid, int steps) {
        List<Point> freeCells = findFreeCells(grid);
        Random random = new Random();

        if (freeCells.isEmpty()) {
            return Collections.emptyList();
        }

        Point currentCell = freeCells.get(random.nextInt(freeCells.size()));
        List<Point> path = new ArrayList<>();
        path.add(currentCell);

        while (steps > 0) {
            List<Point> neighbors = getNeighbors(grid, currentCell);
            if (neighbors.isEmpty()) {
                break;
            }
            currentCell = neighbors.get(random.nextInt(neighbors.size()));
            path.add(currentCell);
            steps--;
        }

        while (!path.get(0).equals(currentCell)) {
            List<Point> neighbors = getNeighbors(grid, currentCell);
            if (neighbors.isEmpty()) {
                break;
            }
            currentCell = neighbors.get(random.nextInt(neighbors.size()));
            path.add(currentCell);
        }

        return path;
    }

    private static List<Point> getNeighbors(boolean[][] grid, Point cell) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dir : directions) {
            int newX = cell.x + dir[0];
            int newY = cell.y + dir[1];
            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length && !grid[newX][newY]) {
                neighbors.add(new Point(newX, newY));
            }
        }

        return neighbors;
    }

    public static int[][] convertBooleanToInt(boolean[][] booleanMatrix) {
        int n = booleanMatrix.length;
        int m = booleanMatrix[0].length;
        int[][] intMatrix = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                intMatrix[i][j] = booleanMatrix[i][j] ? 1 : 0;
            }
        }

        return intMatrix;
    }

    public static void addPathToMatrix(int[][] matrix, List<Point> path) {
        for (Point point : path) {
            matrix[point.x][point.y] = 2;
        }
    }

    public static List<Point> findPath(boolean[][] grid, Point mob, Point player) {
        int n = grid.length;
        int m = grid[0].length;
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        boolean[][] visited = new boolean[n][m];
        Point[][] parent = new Point[n][m];
        Queue<Point> queue = new LinkedList<>();
        queue.add(mob);
        visited[mob.x][mob.y] = true;
        parent[mob.x][mob.y] = null;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.x == player.x && current.y == player.y) {
                return reconstructPath(parent, player);
            }

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (newX >= 0 && newX < n && newY >= 0 && newY < m && !grid[newX][newY] && !visited[newX][newY]) {
                    queue.add(new Point(newX, newY));
                    visited[newX][newY] = true;
                    parent[newX][newY] = current;
                }
            }
        }

        return Collections.emptyList(); // ???? ????? ?? ?????
    }

    private static List<Point> reconstructPath(Point[][] parent, Point end) {
        List<Point> path = new ArrayList<>();
        for (Point at = end; at != null; at = parent[at.x][at.y]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }
}
