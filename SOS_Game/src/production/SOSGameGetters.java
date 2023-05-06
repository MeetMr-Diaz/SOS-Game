package production;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
public class SOSGameGetters {
    public enum Cell {EMPTY, S, O}
    public enum GameModeType {Simple, General}
    public enum GameState {PLAYING, DRAW, BLUE_WON, RED_WON}
    public int TOTAL_ROWS;
    public int TOTAL_COLUMNS;
    public Cell[][] grid;
    public int size;
    public char turn;
    public int x;
    public int y;
    public int blueScore;
    public int redScore;
    public ArrayList<ArrayList<Integer>> sosInfo;
    public GameModeType currentGameModeType;
    public GameState currentGameState;
    public static final String FileGame = "SavedGames.txt";
    public char userSymbol;
    public SOSGameGetters(int size) {
        grid = new Cell[size][size];
        currentGameModeType = GameModeType.Simple;
        this.size = size;
    }
    public void initGame() {
        grid = new Cell[size][size];
        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {
                grid[row][col] = Cell.EMPTY;
            }
        }
        currentGameState = GameState.PLAYING;
        turn = 'B';
        blueScore = 0;
        redScore = 0;
        sosInfo = new ArrayList<>();
    }
    public  ArrayList<Integer> checkSOS() {
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        if (turn == 'B')
            coordinates.add(0);
        else
            coordinates.add(1);
        boolean isWinningTurn = false;

        //Record game if selected
        if (GUI.isRecordGameCheckBoxSelected()) {
            if (turn == 'B') {
                recordMove(x, y, turn, userSymbol == 'S' ? Cell.S : Cell.O, currentGameState,currentGameModeType);
            } else {
                recordMove(x, y, turn, userSymbol == 'S' ? Cell.O : Cell.S, currentGameState, currentGameModeType);
            }
        }

        if (grid[x][y] == Cell.O && (grid[x][y] != Cell.EMPTY))  {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (x - i < 0 || x - i >= TOTAL_ROWS || x + i < 0 || x + i >= TOTAL_ROWS || y - j < 0
                            || y - j >= TOTAL_COLUMNS || y + j < 0 || y + j >= TOTAL_COLUMNS)
                        continue;
                    if (grid[x - i][y - j] == Cell.S && grid[x + i][y + j] == Cell.S) {
                        coordinates.add(x - i);
                        coordinates.add(y - j);
                        coordinates.add(x + i);
                        coordinates.add(y + j);
                        if (turn == 'B'){
                            blueScore++;
                        }
                        else{
                            if (!isWinningTurn) {
                                redScore++;
                            }
                        }
                        isWinningTurn = true;
                       break;
                    }
                }
            }
        } else {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (x + 2 * i < 0 || x + 2 * i >= TOTAL_ROWS || y + 2 * j < 0 || y + 2 * j >= TOTAL_COLUMNS)
                        continue;
                    if (grid[x + 2 * i][y + 2 * j] == Cell.S && grid[x + i][y + j] == Cell.O) {
                        coordinates.add(x);
                        coordinates.add(y);
                        coordinates.add(x + 2 * i);
                        coordinates.add(y + 2 * j);

                        if (turn == 'B'){
                            blueScore++;
                        }
                        else{
                            if (!isWinningTurn){
                                redScore++;

                            }
                        }
                        isWinningTurn = true;
                        break;
                    }
                }
            }
        }
        System.out.println("red score "+redScore);
        System.out.println("blue score "+blueScore);
        if (!isWinningTurn)
            turn = (turn == 'B') ? 'R' : 'B';

        System.out.println("Current turn " + turn);

        return coordinates;
    }

    public static void recordMove(int row, int column, char turn, Cell symbolType, GameState currentGameState, GameModeType GameModeType) {

        File savedGames = new File(FileGame);
        try {
            String moveToRecord = String.format("%s %s %s %s %s %s\n",  row,column, turn,symbolType,currentGameState,GameModeType);
            if (savedGames.isFile()) {
                FileWriter fileWriter = new FileWriter(savedGames, true);
                fileWriter.write(moveToRecord);
                fileWriter.close();
            }
            else {
                savedGames.createNewFile();
                FileWriter fileWriter = new FileWriter(savedGames);
                fileWriter.write(moveToRecord);
                fileWriter.close();
            }
        }
        catch (IOException exception) {
            System.out.println("An error occurred during filing");
        }
    }
    void deleteOldFile() {
        File fileToDelete = new File(FileGame);
        if (fileToDelete.isFile()) {
            fileToDelete.delete();
        }
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < size && column >= 0 && column < size) {
            return grid[row][column];
        }else {
            return null;
        }
    }
    public void setCurrentGameType(GameModeType currentGameModeType) {
        this.currentGameModeType = currentGameModeType;
    }
    public int sizeBoard() {
        return TOTAL_COLUMNS * TOTAL_ROWS;
    }
    public  char getTurn() {
        return turn;
    }
    public void resetGame() {
        initGame();
    }
    public int getTotalRows() {
        return TOTAL_ROWS;
    }
    public int getTotalColumns() {
        return TOTAL_COLUMNS;
    }
    public GameModeType getCurrentGameType() {
        return currentGameModeType;
    }
    public ArrayList<ArrayList<Integer>> getSosInfo() {
        return sosInfo;
    }
    public GameState getGameState() {
        return currentGameState;
    }
    public Object getGrid() {
        return grid;
    }
}