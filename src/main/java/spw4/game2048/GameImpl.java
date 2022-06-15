package spw4.game2048;

import java.util.Arrays;
import java.util.Random;

public class GameImpl implements Game {
    private static final int EMPTY = 0;
    private static final int FOUR_TILE_UPPER_LIMIT = 10;
    private static final int FOUR_TILE_LOWER_LIMIT = 9;
    private static final int WIN = 2048;

    public static Random random = new Random();
    private int[][] board;
    private int moves = 0;
    private int score = 0;

    public GameImpl() {
    }

    public GameImpl(int[][] state) {
        board = state;
    }

    public int getMoves() {
        return moves;
    }

    public int getScore() {
        return score;
    }

    public int getValueAt(int x, int y) {
        if (!isInBounds(x, y))
            throw new IllegalArgumentException("Position at x=" + x + " y=" + y + " not a valid position");
        return board[y][x];
    }

    public boolean isOver() {
        return !hasEmptyField() && !canCombine();
    }

    private boolean canCombine() {
        for (int i = 0; i < BOARD_SIZE - 1; i++) {
            for (int j = 0; j < BOARD_SIZE - 1; j++) {
                if (board[i][j] == board[i + 1][j]) return true;
                if (board[i][j] == board[i][j + 1]) return true;
            }
        }
        return false;
    }

    private boolean hasEmptyField() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == EMPTY) return true;
            }
        }
        return false;
    }

    public boolean isWon() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == WIN) return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Moves: ").append(getMoves());
        sb.append("\t");
        sb.append("Score: ").append(getScore());
        sb.append("\n");
        for (int[] row : board) {
            for (int cell : row) {
                String value = cell == EMPTY ? "." : Integer.toString(cell);
                sb.append(value);
                sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void initialize() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        spawnNewTile();
        spawnNewTile();
    }

    public void move(Direction direction) {
        // 1. transpose to face left
        int turns = 0;
        switch (direction) {
            case up -> turns = 3;
            case down -> turns = 1;
            case left -> {
            } // nothing to do; board is aligned
            case right -> turns = 2;
        }
        rotate(turns);

        // 1.5 push all left
        pushLeft();

        // 2. combine from left to right with predecessor
        for (int[] row : board) {
            for (int i = 0; i < BOARD_SIZE - 1; i++) {
                if (row[i] == row[i + 1]) {
                    row[i] *= 2;
                    row[i + 1] = EMPTY;
                    score += row[i];
                }
            }
        }

        // 3. move to furthest left position
        pushLeft();

        // 3.5 transpose back to original position
        turns = 0;
        switch (direction) {
            case up -> turns = 1;
            case down -> turns = 3;
            case left -> {
            } // nothing to do; board is aligned
            case right -> turns = 2;
        }
        rotate(turns);

        // 4. spawn new tile
        spawnNewTile();

        // 5. increment move count
        moves++;
    }

    private void pushLeft() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            int[] tempRow = Arrays.stream(board[i]).filter(n -> n != EMPTY).toArray();
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = j < tempRow.length ? tempRow[j] : 0;
            }
        }
    }

    private void rotate(int turns) {
        for (int times = 0; times < turns; times++) {
            int[][] ret = new int[board.length][board[0].length];
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    ret[c][board.length - 1 - r] = board[r][c];
                }
            }
            board = ret;
        }
    }

    private void spawnNewTile() {
        int xPos, yPos;

        do {
            xPos = getRandomBoardSizePosition();
            yPos = getRandomBoardSizePosition();
        } while (!isEmptyField(xPos, yPos));

        board[xPos][yPos] = getNewTileValue();
    }

    private int getRandomBoardSizePosition() {
        return random.nextInt(BOARD_SIZE);
    }

    private int getNewTileValue() {
        int rand = random.nextInt(FOUR_TILE_UPPER_LIMIT);
        if (rand >= FOUR_TILE_LOWER_LIMIT) {
            return 4;
        }
        return 2;
    }

    private boolean isEmptyField(int x, int y) {
        return board[x][y] == EMPTY;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < BOARD_SIZE && y < BOARD_SIZE;
    }
}
