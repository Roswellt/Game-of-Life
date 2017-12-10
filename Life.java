import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Life {

    public int[][] world;
    public static int size;

    public Life(int size) {
        world = new int[size][size];
        this.size = size;
        populateWorld(world);
//        printWorld(world);
    }

    public void start(int turnAmount) {
        for(int turn = 0; turn < turnAmount; turn++) {
            world = oneTurn(world);
//            printWorld(world);
        }
    }

    public int[][] oneTurn(int[][] world) {
        int[][] newWorld = new int[size][size];
        copyWorld(world, newWorld);

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                newWorld[i][j] = evaluateCell(world[i][j], i, j);
            }
        }
        return newWorld;
    }

    public int evaluateCell(int cell, int row, int column) {
        int liveNeighbors = 0;

        for(int columnIndex = column - 1; columnIndex <= column + 1; columnIndex++) {
//          Check top
            if((row - 1 >= 0 && row - 1 < size) && (columnIndex >= 0 && columnIndex < size)) {
                if(world[row - 1][columnIndex] == 1) {
                    liveNeighbors++;
                }
            }
//          Check bottom
            if((row + 1 >= 0 && row + 1 < size) && (columnIndex >= 0 && columnIndex < size)) {
                if(world[row + 1][columnIndex] == 1) {
                    liveNeighbors++;
                }
            }
//          Check neighbors
            if((row >= 0 && row < size) && (columnIndex >= 0 && columnIndex < size && columnIndex != column)) {
                if(world[row][columnIndex] == 1) {
                    liveNeighbors++;
                }
            }
        }

//      Rules for an alive cell
        if(cell == 1) {
            if(liveNeighbors < 2 || liveNeighbors > 3) {
                return 0;
            } else if (liveNeighbors == 2 || liveNeighbors == 3) {
                return 1;
            }
        } else {
            if(liveNeighbors == 3) {
                return 1;
            }
        }
        return 0;
    }

    public void copyWorld(int[][] world, int[][] newWorld) {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                newWorld[i][j] = world[i][j];
            }
        }
    }

    public void populateWorld(int[][] world) {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                world[i][j] = ThreadLocalRandom.current().nextInt(0, 1 + 1);
            }
        }
    }

    public static void printWorld(int[][] world) {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                System.out.print(world[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        Life life = new Life(40);

        //Creating X by X grid layout to represent the board and setting values
        Painter painter = new Painter(new GridLayout(size, size));
        for(int i = 0; i < size*size; i++) {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            if(life.world[i % size][(int) Math.floor(i / size)] == 1) panel.setBackground(Color.BLACK);
            painter.add(panel);
            painter.addCell(panel);
        }

        //Containing JFrame
        JFrame frame = new JFrame("DRAW");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(800, 800);
        frame.getContentPane().add(painter);

        ArrayList<JPanel> board = painter.getBoard();

        for(int i = 0; i < 30; i++) {
            life.world = life.oneTurn(life.world);
            for(int boardIndex = 0; boardIndex < board.size(); boardIndex++) {
                if(life.world[boardIndex % size][(int) Math.floor(boardIndex / size)] == 1) board.get(boardIndex).setBackground(Color.BLACK);
                else board.get(boardIndex).setBackground(Color.WHITE);
//                board.get(boardIndex).repaint();
            }
            painter.repaint();
            TimeUnit.MILLISECONDS.sleep(500);
        }

    }

    @SuppressWarnings("serial")
    public static class Painter extends JPanel{

        //Holding all the JPanels in a list to be able to recolor
        private ArrayList<JPanel> board = new ArrayList<>();

        public Painter(LayoutManager layout) {
            super(layout);
        }

        public void addCell(JPanel panel) {
            board.add(panel);
        }

        public ArrayList<JPanel> getBoard() {
            return board;
        }
    }
}
