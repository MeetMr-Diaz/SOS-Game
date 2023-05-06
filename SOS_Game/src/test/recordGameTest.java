package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import production.SOSGameGetters;
import java.io.*;
public class recordGameTest {
    @Test
    public void testRecordMove() throws IOException {
        // Test saving a move to a new file
        String file = "SavedGames.txt";
        SOSGameGetters.recordMove(1, 1, 'B', SOSGameGetters.Cell.S,
                SOSGameGetters.GameState.PLAYING, SOSGameGetters.GameModeType.Simple);

        File savedGames = new File(file);
        assertTrue(savedGames.exists());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        assertEquals("1 1 B S PLAYING Simple", line);
        reader.close();
        savedGames.delete();

        // Test saving a move to an existing file
        SOSGameGetters.recordMove(1, 1, 'B', SOSGameGetters.Cell.S,
                SOSGameGetters.GameState.PLAYING, SOSGameGetters.GameModeType.Simple);

        SOSGameGetters.recordMove(2, 2, 'R', SOSGameGetters.Cell.O,
                SOSGameGetters.GameState.PLAYING, SOSGameGetters.GameModeType.Simple);

        savedGames = new File(SOSGameGetters.FileGame);
        assertTrue(savedGames.exists());
        reader = new BufferedReader(new FileReader(SOSGameGetters.FileGame));
        line = reader.readLine();
        assertEquals("1 1 B S PLAYING Simple", line);
        line = reader.readLine();
        assertEquals("2 2 R O PLAYING Simple", line);
        reader.close();
        savedGames.delete();
    }
}