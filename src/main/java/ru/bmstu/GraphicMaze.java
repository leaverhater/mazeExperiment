package ru.bmstu;

import javax.swing.*;
import java.awt.*;

/**
 * User: voznyuk
 * Date: 24.11.13
 * Time: 17:52
 */
class Surface extends JPanel {
    private GraphicMaze gm;

    public Surface(GraphicMaze gm) {
        this.gm = gm;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gm.drawMaze(g);
    }
}

public class GraphicMaze extends JFrame {


    private Maze maze;
    Path leePath;
    Path randomPath;

    private int[] dy = {-1, 0, 1, 0};
    private int[] dx = {0, 1, 0, -1};


    private Color BLACK = new Color(0, 0, 0);
    private Color WHITE = new Color(255, 255, 255);
    private Color RED = new Color(255, 0, 0);
    private Color GREEN = new Color(0, 255, 0);
    private int cellSize = 32;

    private Graphics2D g2d;
    private Surface sf;

    private boolean showLeePath = false;
    private boolean showRandomPath = false;


    public GraphicMaze(Maze maze) {
        this.maze = maze;
        this.leePath = maze.getLeePath();
        this.randomPath = maze.getRandomPath();
        init();
        initUI();
    }

    private void init() {

    }

    private void initUI() {
        setTitle("Graphic Maze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        sf = new Surface(this);
//        add(sf);
        setSize(maze.w * cellSize, maze.h * cellSize);
        setLocationRelativeTo(null);
    }

    public void drawMaze(Graphics g) {
//        System.out.println("Initializing graphics");
        g2d = initGraphics(g);
        for (int i = 0; i < maze.h; i++) {
            for (int j = 0; j < maze.w; j++) {
                //Если проход
                if (maze.mazeValue(i, j)) {
                    g2d.setColor(WHITE);
                } else {
                    g2d.setColor(BLACK);
                }
                g2d.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
        if (showLeePath) {
            g2d.setColor(RED);
//            int x = maze.getStartX();
//            int y = maze.getStartY();
            int x = maze.getExitX();
            int y = maze.getExitY();

            for (int way : leePath.getPathArray()) {
                g2d.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                x += dx[way];
                y += dy[way];
            }
            g2d.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        }
        if (showRandomPath) {
            g2d.setColor(GREEN);
            int x = maze.getStartX();
            int y = maze.getStartY();
            for (int way : randomPath.getPathArray()) {
                g2d.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                x += dx[way];
                y += dy[way];
            }
            g2d.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        }
    }

    private Graphics2D initGraphics(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        return g2d;
    }

    public Maze getMaze() {
        return maze;
    }

    public void setShowLeePath(boolean showLeePath) {
        this.showLeePath = showLeePath;
    }

    public void setShowRandomPath(boolean showRandomPath) {
        this.showRandomPath = showRandomPath;
    }
}