package ru.bmstu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Hello world!
 *
 */
public class App 
{
    private static GraphicMaze gm;
    private static Surface sf;
    private static boolean showLeePath = false;
    private static boolean showRandomPath = false;

    private static int[][] intMaze = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0},
            {0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0},
            {0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1},
            {0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0},
            {0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0},
            {0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0},
            {0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    private static Maze maze = new Maze(intMaze);

    public static void addComponentToPane(Container pane) {
        JPanel panel = new JPanel();
        sf = new Surface(gm);
        sf.setPreferredSize(new Dimension(500,500));
        panel.add(sf);
        JButton leeButton = new JButton("Показать путь агента");
        leeButton.addActionListener(new ShowLeePathListener());
        panel.add(leeButton);
        JButton randomButton = new JButton("Показать случайный путь");
        randomButton.addActionListener(new ShowRandomPathListener());
        panel.add(randomButton);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        pane.add(panel);
    }

    public static void createGUI() {
        JFrame frame = new JFrame("Лабиринт");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    private static class ShowLeePathListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
//            showLeePath = true;
//            showRandomPath = false;
            gm.setShowLeePath(true);
            gm.setShowRandomPath(false);
            sf.repaint();
        }
    }

    private static class ShowRandomPathListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
//            showRandomPath = true;
//            showLeePath = false;
            gm.setShowLeePath(false);
            gm.setShowRandomPath(true);
            sf.repaint();
        }
    }



    public static void main( String[] args )
    {
        maze.setStartXY(0, 1);
        maze.printPath(maze.getRandomPath());
        maze.printPath(maze.getLeePath());
        maze.printStat();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gm = new GraphicMaze(maze);
                addComponentToPane(gm.getContentPane());
                gm.pack();
                gm.setVisible(true);
            }
        });
    }
}
