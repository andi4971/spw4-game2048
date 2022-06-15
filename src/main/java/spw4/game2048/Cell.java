package spw4.game2048;

public class Cell {
    public int x;
    public int y;
    public int randomValue;

    public Cell(int x, int y, int randomValue) {
        this.x = x;
        this.y = y;
        this.randomValue = randomValue;
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }
}
