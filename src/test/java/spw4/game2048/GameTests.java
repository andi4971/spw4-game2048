package spw4.game2048;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static spw4.game2048.Direction.*;

public class GameTests {

    Game game;

    @BeforeEach
    void setup() {
        game = new GameImpl();
        GameImpl.random = new Random();
    }

    @Test
    @DisplayName("Game.imitialize() creates a new game with two tiles")
    void initializeCreatesGameWithTwoTiles() {
        var cell1 = new Cell(0, 0, 9);
        var cell2 = new Cell(3, 3, 4);
        GameImpl.random = new IntRandomStub(Arrays.asList(cell1, cell2));

        game.initialize();

        int emptyCount = 0;
        int resultValue1 = -1;
        int resultValue2 = -1;
        for (int x = 0; x < Game.BOARD_SIZE; x++) {
            for (int y = 0; y < Game.BOARD_SIZE; y++) {
                if (cell1.equals(x, y)) {
                    resultValue1 = game.getValueAt(x, y);
                } else if (cell2.equals(x, y)) {
                    resultValue2 = game.getValueAt(x, y);
                } else {
                    emptyCount++;
                }

            }
        }

        int finalEmptyCount = emptyCount;
        int finalResultValue1 = resultValue1;
        int finalResultValue2 = resultValue2;
        assertAll(() -> {
            assertEquals(4, finalResultValue1);
            assertEquals(2, finalResultValue2);
            assertEquals(14, finalEmptyCount);
        });
    }

    @Test
    @DisplayName("moving the tiles to the right combines the correct tiles")
    void moveTilesToRightCombinesCorrectTiles() {
        game = new GameImpl(new int[][]{
                {0,0,2,2},
                {4,0,0,4},
                {0,0,0,0},
                {0,0,256,256},
        });
        game.move(right);

        assertAll(() -> {
            assertEquals(4, game.getValueAt(3, 0));
            assertEquals(8, game.getValueAt(3, 1));
            assertEquals(512, game.getValueAt(3, 3));
        });
    }

    @Test
    @DisplayName("moving the tiles to the left combines the correct tiles")
    void moveTilesToLeftCombinesCorrectTiles() {
        game = new GameImpl(new int[][]{
                {0,0,2,2},
                {0,0,4,4},
                {0,0,0,0},
                {0,0,256,256},
        });
        game.move(left);

        assertAll(() -> {
            assertEquals(4, game.getValueAt(0, 0));
            assertEquals(8, game.getValueAt(0, 1));
            assertEquals(512, game.getValueAt(0, 3));
        });
    }

    @Test
    @DisplayName("moving the tiles down combines the correct tiles")
    void moveTilesDownCombinesCorrectTiles() {
        game = new GameImpl(new int[][]{
                {4,0,0,4},
                {4,2,0,4},
                {0,2,0,256},
                {0,0,0,256},
        });
        game.move(down);

        assertAll(() -> {
            assertEquals(8, game.getValueAt(0, 3));
            assertEquals(4, game.getValueAt(1, 3));
            assertEquals(512, game.getValueAt(3, 3));
            assertEquals(8, game.getValueAt(3, 2));
        });
    }

    @Test
    @DisplayName("moving the tiles up combines the correct tiles")
    void moveTilesUpCombinesCorrectTiles() {
        game = new GameImpl(new int[][]{
                {4,0,0,4},
                {4,2,0,4},
                {0,2,0,256},
                {0,0,0,256},
        });
        game.move(up);

        assertAll(() -> {
            assertEquals(8, game.getValueAt(0, 0));
            assertEquals(4, game.getValueAt(1, 0));
            assertEquals(8, game.getValueAt(3, 0));
            assertEquals(512, game.getValueAt(3, 1));
        });
    }


    //TODO get moves

    @DisplayName("getMoves shows correct number of moves")
    @ParameterizedTest(name = "moves = {0}")
    @ValueSource(ints = {0, 3, 15})

    void getMovesShowsCorrectNumberOfMoves(int moves){
        game.initialize();

        for (int i = 0; i < moves; i++) {
            game.move(down);
        }

        assertEquals(moves, game.getMoves());
    }

    @Test
    @DisplayName("toString accurately displays game information")
    void toStringAccuratlyDisplaysGameInformation(){
        GameImpl.random = new IntRandomStub(Arrays.asList(
                new Cell(0, 0, 2),
                new Cell(0, 1, 2),
                new Cell(0, 1, 4),
                new Cell(0, 0, 4),
                new Cell(2, 0, 4)
        )
        );
        game.initialize();

        game.move(down);
        game.move(down);
        game.move(down);

        String expected = """
                Moves: 3\tScore: 8
                .\t.\t2\t.\t
                .\t.\t.\t.\t
                4\t.\t.\t.\t
                4\t.\t.\t.\t
                """;

        String result = game.toString();

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("getScore correctly calculates score")
    void getScoreCorrectlyCalculatesScore() {

        GameImpl.random = new IntRandomStub(Arrays.asList(
                new Cell(0, 0, 2),
                new Cell(0, 1, 2),
                new Cell(0, 1, 4),
                new Cell(0, 0, 4),
                new Cell(2, 0, 4)
        )
        );
        game.initialize();

        game.move(down);
        game.move(down);
        game.move(down);

        assertEquals(8, game.getScore());

    }

    @Test
    @DisplayName("getScore is Zero on game start")
    void getScoreIsZeroOnGameStart() {
        game.initialize();
        assertEquals(0, game.getScore());
    }

    @Test
    @DisplayName("gameWin shows that the game has been on when two 1024 blocks are merged")
    void isWonIsTrueWhenGameHasBeenWon() {
        game = new GameImpl(new int[][]{
                {2,4,1024,1024},
                {4,2,4,32},
                {2,4,2,4},
                {4,2,4,0},
        });

        game.move(right);
        assertTrue(game.isWon());
    }

    @Test
    @DisplayName("gameWin is false when game has not been won yet")
    void isWonIsFalseWhenGameHasNotBeenWonYet() {
        game.initialize();

        assertFalse(game.isWon());
    }

    @Test
    @DisplayName("gameOver shows that the game has been lost when no new blocks can be added")
    void isOverIsTrueWhenGameHasBeenLost() {
        game = new GameImpl(new int[][]{
                {2,4,16,512},
                {4,2,4,32},
                {2,4,2,4},
                {4,2,4,8},
        });

        assertTrue(game.isOver());
    }

    @Test
    @DisplayName("gameOver is false when game has not been lost yet")
    void isOverIsFalseWhenGameHasNotBeenLostYet() {
        game.initialize();

        assertFalse(game.isOver());
    }

    @ParameterizedTest(name = "x = {0}, y = {1}")
    @CsvSource({"3,-3", "-1,0", "-1,1"})
    @DisplayName("getValueAt throws IllegalArgumentException on invalid index")
    void getValueAtThrowsExceptionOnInvalidIndex(int x, int y) {
        game.initialize();
        assertThrows(IllegalArgumentException.class, () -> {
           game.getValueAt(x,y);
        });
    }


}
