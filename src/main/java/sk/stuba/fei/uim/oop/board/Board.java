package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import sk.stuba.fei.uim.oop.pipe.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;

public class Board extends JPanel {
    @Getter
    private Tile[][] board;
    private final int[][] boardVisited;
    @Getter
    private final ArrayList<ArrayList<Integer>> path;
    @Getter
    private final ArrayList<Integer> startPos;
    @Getter
    private final ArrayList<Integer> endPos;
    private final int boardSize;
    private boolean doSearch;
    public Board(int size) {
        boardSize = size;
        boardVisited = new int[size][size];
        path = new ArrayList<>();

        startPos = new ArrayList<>();
        endPos = new ArrayList<>();

        initBoard(boardSize);

        int randomStart = (int) (Math.random() * boardSize);
        startPos.add(randomStart);
        startPos.add(0);

        int randomEnd = (int) (Math.random() * boardSize);
        endPos.add(randomEnd);
        endPos.add(boardSize-1);

        initStart(randomStart, 0);
        initEnd(randomEnd, boardSize-1);

        doSearch = true;
        initPath();
        updateBoard();
    }
    private void updateBoard() {
        for (int i = 0; i< path.size(); i++) {
            int xValue = path.get(i).get(0);
            int yValue = path.get(i).get(1);

            int[] angleArray = {0, 90, 180, 270};
            int randomRotation = angleArray[(int) (Math.random() * angleArray.length)];

            int startPosX = startPos.get(0);
            int startPosY = startPos.get(1);

            int endPosX = endPos.get(0);
            int endPosY = endPos.get(1);

            if ((xValue == startPosX && yValue == startPosY) || (xValue == endPosX && yValue == endPosY)) {
                board[xValue][yValue].setRotation((board[xValue][yValue].getRotation() + randomRotation)%360);
                continue;
            }
            else if ((i > 0 && i < path.size() - 1) && (!Objects.equals(path.get(i - 1).get(0), path.get(i + 1).get(0)) && !Objects.equals(path.get(i - 1).get(1), path.get(i + 1).get(1)))) {
                remove(xValue * boardSize + yValue);
                board[xValue][yValue] = new CurvedPipe();
                add(board[xValue][yValue], xValue * boardSize + yValue );
            } else {
                remove(xValue * boardSize + yValue);
                board[xValue][yValue] = new StraightPipe();
                add(board[xValue][yValue], xValue * boardSize + yValue );
            }

            board[xValue][yValue].setRotation((board[xValue][yValue].getRotation() + randomRotation)%360);
        }
    }
    private void initPath() {
        for (int i = 0; i < boardSize; i++) {
            Arrays.fill(boardVisited[i], 1);
        }

        int startPosX = startPos.get(0);
        int startPosY = startPos.get(1);

        int endPosX = endPos.get(0);
        int endPosY = endPos.get(1);

        depthFirstSearch(startPosX, startPosY);

        boardVisited[startPosX][startPosY] = 0;
        boardVisited[endPosX][endPosY] = 0;

        for (ArrayList<Integer> p : path) {
            int x = p.get(0);
            int y = p.get(1);
            boardVisited[x][y] = 0;
        }
    }
    private void depthFirstSearch(int row, int column) {
        boardVisited[row][column] = 0;

        if (!doSearch) {
            return;
        }

        ArrayList<Integer> currentPos = new ArrayList<>();
        currentPos.add(row);
        currentPos.add(column);
        path.add(currentPos);

        int endPosX = endPos.get(0);
        int endPosY = endPos.get(1);
        if (row == endPosX && column == endPosY) {
            doSearch = false;
            return;
        }

        int[] rows = {-1, 0, 1, 0};
        int[] cols = {0, 1, 0, -1};

        ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(directions);

        for (int direction : directions) {
            int nextRow = row + rows[direction];
            int nextColumn = column + cols[direction];

            if (nextRow < 0 || nextColumn < 0 || nextRow >= boardSize || nextColumn >= boardSize || boardVisited[nextRow][nextColumn] == 0) {
                continue;
            }

            if (direction == 0) {
                boardVisited[row-1][column] = 0;
            } else if (direction == 1) {
                boardVisited[row][column+1] = 0;
            } else if (direction == 2) {
                boardVisited[row+1][column] = 0;
            } else {
                boardVisited[row][column-1] = 0;
            }

            depthFirstSearch(nextRow, nextColumn);
        }

        if (path.get(path.size() - 1).get(0) == row && path.get(path.size() - 1).get(1) == column) {
            path.remove(path.size() - 1);
        }
    }
    private void initBoard(int size) {
        board = new Tile[size][size];

        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.WHITE, 2));

        setLayout(new GridLayout(size, size));
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                board[row][column] = new Empty();
                add(board[row][column]);
            }
        }
    }
    private void initStart(int x, int y) {
        remove(x * boardSize + y);
        board[x][y] = new StartPipe();
        add(board[x][y], x * boardSize + y );
    }
    private void initEnd(int x, int y) {
        remove(x * boardSize + y);
        board[x][y] = new EndPipe();
        add(board[x][y], x * boardSize + y);
    }
    public void clearHighlight() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                board[row][column].setHighlight(false);
            }
        }
    }
}

