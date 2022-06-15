package spw4.game2048;

public interface Game {
    static final int BOARD_SIZE = 4;
    void initialize();
    void move(Direction direction);
    int getMoves();
    int getScore();
    int getValueAt(int x, int y);
    boolean isOver();
    boolean isWon();
}
