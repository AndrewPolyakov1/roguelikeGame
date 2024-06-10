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

    public static List<Point> randomWalk(boolean[][] grid, Point src, int steps) {
        List<Point> freeCells = findFreeCells(grid);
        Random random = new Random();

        if (freeCells.isEmpty()) {
            return Collections.emptyList();
        }

        Point currentCell = src;
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

    public static List<Point> pathFinding(boolean[][] grid, Point mob, Point player) {
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
                return reconstructPath1(parent, player);
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

    private static List<Point> reconstructPath1(Point[][] parent, Point end) {
        List<Point> path = new ArrayList<>();
        for (Point at = end; at != null; at = parent[at.x][at.y]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    private List<Point> Pathfinding(Point start, Point end, boolean[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Point[][] parent = new Point[rows][cols];

        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited[start.x][start.y] = true;

        int[] dirX = {-1, 1, 0, 0};
        int[] dirY = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(end)) {
                return reconstructPath(parent, end);
            }

            for (int i = 0; i < 4; i++) {
                int newX = current.x + dirX[i];
                int newY = current.y + dirY[i];

                if (isValidMove(newX, newY, rows, cols, grid, visited)) {
                    queue.add(new Point(newX, newY));
                    visited[newX][newY] = true;
                    parent[newX][newY] = current;
                }
            }
        }

        return new ArrayList<>();
    }

    private boolean isValidMove(int x, int y, int rows, int cols, boolean[][] grid, boolean[][] visited) {
        return x >= 0 && x < rows && y >= 0 && y < cols && grid[x][y] && !visited[x][y];
    }

    public static List<Point> reconstructPath(Point[][] parent, Point end) {
        List<Point> path = new ArrayList<>();
        for (Point at = end; at != null; at = parent[at.x][at.y]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public static double heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    public static List<Point> pathEscapeFinding(boolean[][] ceilGrid, Point possitionEnemy, Point possPlayer) {
        List<Point> path = new ArrayList<>();
        path.add(possitionEnemy);

        Point bestMove = null;
        double maxDistance = -1;

        int[][] directions = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int[] direction : directions) {
            int newX = possitionEnemy.x + direction[0];
            int newY = possitionEnemy.y + direction[1];

            if (newX >= 0 && newY >= 0 && newX < ceilGrid.length && newY < ceilGrid[0].length && !(ceilGrid[newX][newY])) {
                Point newPoint = new Point(newX, newY);
                double distance = calculateDistance(newPoint, possPlayer);

                if (distance > maxDistance) {
                    maxDistance = distance;
                    bestMove = newPoint;
                }
            }
        }

        if (bestMove != null) {
            path.add(bestMove);
        }

        return path;
    }

    public static List<Point> pathAttackFinding(boolean[][] ceilGrid, Point possitionEnemy, Point possPlayer) {
        List<Point> path = new ArrayList<>();
        path.add(possitionEnemy);

        Point bestMove = null;
        double minDistance = Double.MAX_VALUE;

        int[][] directions = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int[] direction : directions) {
            int newX = possitionEnemy.x + direction[0];
            int newY = possitionEnemy.y + direction[1];

            if (newX >= 0 && newY >= 0 && newX < ceilGrid.length && newY < ceilGrid[0].length && !(ceilGrid[newX][newY])) {
                Point newPoint = new Point(newX, newY);
                double distance = calculateDistance(newPoint, possPlayer);

                if (distance < minDistance) {
                    minDistance = distance;
                    bestMove = newPoint;
                }
            }
        }

        if (bestMove != null) {
            path.add(bestMove);
        }

        return path;
    }

    public static List<Point> findPathToClosestIdlePoint(Point position, List<Point> pathIdle, boolean[][] ceilGrid) {
        Point closestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Point idlePoint : pathIdle) {
            double distance = calculateDistance(position, idlePoint);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = idlePoint;
            }
        }

        if (closestPoint == null) {
            return new ArrayList<>();
        }

        List<Point> path = new ArrayList<>();
        path.add(position);

        Point nextStep = getNextStepTowards(position, closestPoint, ceilGrid);
        System.out.println(nextStep);
        if (nextStep != null) {
            path.add(nextStep);
        }

        return path;
    }

    private static Point getNextStepTowards(Point position, Point target, boolean[][] ceilGrid) {
        Point bestMove = null;
        double minDistance = Double.MAX_VALUE;

        int[][] directions = {
                {-1, 0}, // ?????
                {1, 0},  // ????
                {0, -1}, // ?????
                {0, 1}   // ??????
        };

        for (int[] direction : directions) {
            int newX = position.x + direction[0];
            int newY = position.y + direction[1];

            if (newX >= 0 && newY >= 0 && newX < ceilGrid.length && newY < ceilGrid[0].length && !(ceilGrid[newX][newY])) {
                Point newPoint = new Point(newX, newY);
                double distance = calculateDistance(newPoint, target);

                // ???? ????? ? ??????????? ??????????? ?? ????
                if (distance < minDistance) {
                    minDistance = distance;
                    bestMove = newPoint;
                }
            }
        }

        return bestMove;
    }

    public static double calculateDistance(Point a, Point b) {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
