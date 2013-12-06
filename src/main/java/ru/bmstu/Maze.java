package ru.bmstu;

/**
 * User: vadya
 * Date: 01.12.13
 * Time: 18:50
 */
public class Maze {
    //Высота и ширина карты
    public int h;
    public int w;

    //Массив лабиринта. True - проход, false - стена
    private boolean[][] maze;

    //Массив расстояний от выхода
    private int[][] distanceMap;

    //Изменения координат в зависимости от направления: вверх,вправо,вниз,влево
    private int[] dy = {-1, 0, 1, 0};
    private int[] dx = {0, 1, 0, -1};

    private int startX;
    private int startY;

    private int exitX;
    private int exitY;

    private Path leePath;
    private Path randomPath;
    private double leeTime;
    private double randomTime;
    private double distanceMapTime;
    private int attempts;


    public Maze(boolean[][] maze) {
        this.maze = maze;
        this.h = maze.length;
        this.w = maze[0].length;
    }

    public Maze(int[][] maze) {
        this(intMazeToBool(maze));
    }

    public boolean mazeValue(int h, int w) {
        return maze[h][w];
    }

    private static boolean[][] intMazeToBool(int[][] maze) {
        boolean[][] boolMaze = new boolean[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 0) {
                    boolMaze[i][j] = false;
                } else
                    boolMaze[i][j] = true;
            }
        }
        return boolMaze;
    }


    private void createDistanceMap() {
        long startTime = System.nanoTime();
        int d = 1;
        distanceMap = new int[h][w];
        distanceMap[startY][startX] = d;
        int[] dy = {-1, 0, 1, 0};
        int[] dx = {0, 1, 0, -1};
        boolean stop = false;
        while (!stop) {
            stop = true;
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    if (distanceMap[i][j] == d) {
                        for (int z = 0; z < 4; z++) {
                            if (!isOutside(j + dx[z], i + dy[z])) {
                                if (mazeValue(i + dy[z], j + dx[z]) && (distanceMap[i + dy[z]][j + dx[z]] == 0)) {
                                    distanceMap[i + dy[z]][j + dx[z]] = d + 1;
                                    stop = false;
                                }
                            } else if ((i != startY) || (j != startX)) {
                                exitX = j;
                                exitY = i;
//                                System.out.print("Found exit: " + exitX + "x" + exitY);
                            }
                        }
                    }
                }
            }
            d++;
        }
        long endTime = System.nanoTime();
        distanceMapTime = (endTime - startTime) / 1e6;
    }

    public Path getLeePathToExit() {
        long startTime = System.nanoTime();
        int x = exitX;
        int y = exitY;
        Path path = new Path();
        int d = distanceMap[y][x];
//        System.out.println("Distance to mine: " + (d - 1));
        int[] dy = {-1, 0, 1, 0};
        int[] dx = {0, 1, 0, -1};
        while (d != 1) {
            for (int i = 0; i < 4; i++) {
                if (!isOutside(x + dx[i], y + dy[i])) {
                    if (distanceMap[y + dy[i]][x + dx[i]] == d - 1) {
                        path.addWay(i);
                        y = y + dy[i];
                        x = x + dx[i];
//                    System.out.println("Added way " + i);
                        d--;
                        break;
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        leeTime = (endTime - startTime) / 1e6;
        return path;
    }

    public Path getRandomPathToExit() {
        attempts = 0;
        long startTime = System.nanoTime();
        boolean foundExit = false;
        Path path = new Path();
        while (!foundExit) {
            attempts++;
            path.clean();
            int x = startX;
            int y = startY;
            int nextX;
            int nextY;
            int prevX = startX;
            int prevY = startY;
            int nextWay;
            int availableWays = 1;
            while (!foundExit && !(availableWays == 0)) {
                availableWays = 0;
                nextWay = getRandomWay();
                nextX = x + dx[nextWay];
                nextY = y + dy[nextWay];
                //Пока не получим допустимое направление движения
                while (!isValid(nextX, nextY) || ((nextX == prevX) && (nextY == prevY))) {
                    nextWay = getRandomWay();
                    nextX = x + dx[nextWay];
                    nextY = y + dy[nextWay];
                }
                if ((nextX == exitX) && (nextY == exitY)) {
                    path.addWay(nextWay);
                    foundExit = true;
                } else {
                    for (int i = 0; i < 4; i++) {
                        if (isValid(nextX + dx[i], nextY + dy[i]) && ((nextX + dx[i] != x) || (nextY + dy[i] != y))) {
                            availableWays++;
                        }
                    }
                    path.addWay(nextWay);
                    prevX = x;
                    prevY = y;
                    x = nextX;
                    y = nextY;
                }
            }


        }
        long endTime = System.nanoTime();
        randomTime = (endTime - startTime)/1e6;
        return path;
    }

    private boolean isOutside(int x, int y) {
        return ((x > w - 1) || (y > h - 1) || (x < 0) || (y < 0));
    }

    public void setStartXY(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
        createDistanceMap();
        printDistanceMap();
        leePath = getLeePathToExit();
        randomPath = getRandomPathToExit();
    }

    public int getRandomWay() {
        return (int) (Math.random() * 4);
    }

    public void printDistanceMap() {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                System.out.print(distanceMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean isValid (int x, int y) {
        if (isOutside(x, y))
            return false; else
            return mazeValue(y, x);
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public Path getLeePath() {
        return leePath;
    }

    public Path getRandomPath() {
        return randomPath;
    }

    public void printPath (Path path) {
        for (int way : path.getPathArray()) {
            System.out.print(way + " ");
        }
        System.out.println();
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }

    public void printStat () {
        int sampleNum = 10;
        System.out.println("Время в милисекундах");
        System.out.println("Размер лабиринта: " + h + "x" + w);
//        System.out.println("Время построения карты расстояний: " + distanceMapTime);
//        System.out.println("Время нахождения пути агентом: " + leeTime);
        System.out.println("Время нахождения пути агентом: " + (distanceMapTime + leeTime));
        System.out.println("Время нахождения пути случайным образом: " + randomTime);
        System.out.println("Количество попыток нахождения пути случайным образом: " + attempts);
//        System.out.println("Данные за " + sampleNum + " выборок: ");
        double totalRandomTime = 0;
        double totalLeeTime = 0;
        int totalAttemts = 0;
        for (int i = 0; i < sampleNum; i++) {
            getRandomPathToExit();
            createDistanceMap();
            getLeePathToExit();
            totalRandomTime += randomTime;
            totalLeeTime += distanceMapTime + leeTime;
            totalAttemts += attempts;
//            System.out.println("Время нахождения пути случайным образом " + i + ": " + randomTime);
//            System.out.println("Количество попыток нахождения пути случайным образом " + i +": " + attempts);
        }
//        System.out.println("Среднее время нахождения пути агентом: " + (totalLeeTime / sampleNum));
//        System.out.println("Среднее время нахождения пути случайным образом: " + (totalRandomTime / sampleNum));
//        System.out.println("Среднее количество попыток нахождения пути случайным образом: " + (totalAttemts / sampleNum));
//        System.out.println("Отошение среднего времени нахождения случаным образом к время нахождения пути агентом: " + (distanceMapTime + leeTime) * 10 / totalRandomTime);
//        if ((totalRandomTime/sampleNum) > (distanceMapTime + leeTime)) {
//            System.out.println("Алгоритм нахождения пути агентом быстрее на " + (totalRandomTime - totalLeeTime)/totalLeeTime * 100 + "%");
//        } else {
//            System.out.println("Алгоритм нахождения пути случайным образом быстрее на " + (totalLeeTime - totalRandomTime)/totalRandomTime*100 + "%");
//        }
    }
}
