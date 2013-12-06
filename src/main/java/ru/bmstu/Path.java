package ru.bmstu;

import java.util.ArrayList;
import java.util.List;

/**
 * User: vadya
 * Date: 22.11.13
 * Time: 21:08
 */
public class Path {
    private List<Integer> path = new ArrayList<Integer>();
    public void addWay (int way) {
        path.add(way);
    }
    public List<Integer> getPathArray() {
        return path;
    }

    public void printPath () {
        for (int i : path) {
            System.out.print(i + " ");
        }
    }
    public int getSize () {
        return path.size();
    }
    public int getWay(int index) {
        return path.get(index);
    }

    public void clean () {
        path.clear();
    }
}