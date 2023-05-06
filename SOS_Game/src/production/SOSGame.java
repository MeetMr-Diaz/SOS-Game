package production;

import javax.swing.*;
import java.util.Random;
public class SOSGame extends SOSGameGetters {
    public SOSGame(int n){
        super( n);
        TOTAL_ROWS = TOTAL_COLUMNS = n;
        deleteOldFile();
        initGame();
    }
    public  void makeMove(int row, int column, int type) {

        if( grid[row][column] != Cell.EMPTY){

            JOptionPane.showMessageDialog(null, "This cell is already occupied!", "Invalid Move", JOptionPane.ERROR_MESSAGE);
        }

        if (row >= 0 && row < TOTAL_ROWS && column >= 0 && column < TOTAL_COLUMNS && grid[row][column] == Cell.EMPTY) {
            x = row;
            y = column;
            grid[row][column] = (type == 0) ? Cell.S : Cell.O;
            sosInfo.add(checkSOS());
            updateGameState();
        }
    }
    public void makeRandomMove(int type) {
        int numberOfEmptyCells = getNumberOfEmptyCells();
        if (numberOfEmptyCells == 0) {
            return;
        }
        Random random = new Random();
        int targetMove = random.nextInt(numberOfEmptyCells);
        int index = 0;
        for (int row = 0; row < TOTAL_ROWS; ++row) {
            for (int col = 0; col < TOTAL_COLUMNS; ++col) {
                if (grid[row][col] == SOSGameGetters.Cell.EMPTY) {
                    if (targetMove == index) {
                        boolean r = random.nextBoolean();
                        makeMove(row, col, type);
                        return;
                    } else
                        index++;
                }
            }
        }
    }
    public int getNumberOfEmptyCells() {
        int numberOfEmptyCells = 0;
        for (int row = 0; row < TOTAL_ROWS; ++row) {
            for (int col = 0; col < TOTAL_COLUMNS; ++col) {
                if (grid[row][col] == SOSGameGetters.Cell.EMPTY) {
                    numberOfEmptyCells++;
                }
            }
        }
        return numberOfEmptyCells;
    }
    public void updateGameState() {
        if (currentGameModeType == GameModeType.Simple||currentGameModeType == GameModeType.General){
            int x = checkWinner();
            if (x > 0) {
                if (x == 1)
                    currentGameState = GameState.BLUE_WON;
                else if (x == 2)
                    currentGameState = GameState.RED_WON;
                else if (x==3) {
                    currentGameState = GameState.DRAW;
                }
            }else if (isFull())
                currentGameState = GameState.DRAW;
        }
    }
    public  boolean isFull() {
       for (int row = 0; row < size; row++) {
           for (int col = 0; col < size; col++) {
               if (grid[row][col] == Cell.EMPTY) {
                   return false;
               }
           }
       }
       return true;
   }
    public  int checkWinner() {
        if (currentGameModeType == GameModeType.Simple) {
            if (blueScore > 0){
                return 1; //blue wins
            }else if (redScore > blueScore){
                return 2; // red wins
            }else if(sosInfo.isEmpty()){
                return 0; //no winner
            }else {
                return -1;
            }
        }  else {
            if (!isFull())
                return 0;
            if (blueScore > redScore)
                return 1;
            else if (blueScore < redScore)
                return 2;
            else {
                return 3;
            }
        }
    }
}