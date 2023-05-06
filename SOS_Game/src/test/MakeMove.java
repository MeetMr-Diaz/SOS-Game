package test;

import org.junit.Test;
import production.GUI;
import production.SOSGame;
import static org.junit.Assert.assertEquals;
public class MakeMove {
    @Test
    public void MoveTest() {

        SOSGame game = new SOSGame(3);
        GUI gui = new GUI(game);

        game.makeMove(0, 0,1);
        game.makeMove(1, 1,1);
        new SOSGame(3);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(SOSGame.Cell.O, game.grid[0][0]); // Check if the grid is still unchanged
        assertEquals('B', game.getTurn()); // Check if it's still player 1's turn
    }
}