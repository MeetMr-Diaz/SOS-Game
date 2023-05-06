package production;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class GUI extends JFrame{
    private final PlayerBluePanel playerBluePanel;
    private final PlayerRedPanel playerRedPanel;
    public static int CELL_SIZE = 100;
    public static final int GRID_WIDTH = 2;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final int CELL_PADDING = CELL_SIZE / 15;
    public static final int SYMBOL_STROKE_WIDTH = 8;
    private JLabel gameStatusBar;
    private BoardCanvas BoardCanvas;
    private boolean flag;
    private final JFrame frame;
    private final SOSGame game;
    private JRadioButton bluePlayerS;
    private JRadioButton bluePlayerO;
    private JRadioButton SimpleBtn;
    private JRadioButton blueComputer;
    private JRadioButton blueHuman;
    private JRadioButton redPlayerS;
    private JRadioButton GeneralBtn;
    private JRadioButton redPlayerO;
    private JRadioButton redComputer;
    private JRadioButton redHuman;
    public static JCheckBox recordGameCheckBox;
    private JButton replayBtn, newGameBtn;
    private JPanel boardPanel;
    private JPanel gameChoicePanel;
    private  JPanel westPanel;
    private JPanel eastSide;
    private JPanel southSouthPanel;
    private JLabel sosLabel;
    private JLabel boardLabel;
    private JTextArea boardSizeInput;
    private ButtonGroup sosGroup;
    private Container contentPane;

    public GUI(SOSGame game) {
        this.game = game;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SOS game");
        setSize(850,850);

        playerBluePanel = new PlayerBluePanel();
        playerRedPanel = new PlayerRedPanel();

        setContentPane();
        pack();
        setVisible(true);
        frame = this;
        flag = false;
    }
    private void setContentPane() {

        //Component for the game board and game status, both added to boardPanel
        BoardCanvas = new BoardCanvas();
        boardPanel = new JPanel(new BorderLayout());

        BoardCanvas.setPreferredSize(new Dimension(CELL_SIZE * game.size, CELL_SIZE * game.size));
        BoardCanvas.setBounds(300, 200, 350, 350);

        gameStatusBar = new JLabel("  ");
        gameStatusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 13));
        gameStatusBar.setBorder(BorderFactory.createEmptyBorder(2, 0, 4, 0));
        gameStatusBar.setPreferredSize(new Dimension(80,115));

        boardPanel.add(BoardCanvas,BorderLayout.NORTH);
        boardPanel.add(gameStatusBar,BorderLayout.CENTER);

        //components for the top panel for game type and size
        gameChoicePanel = new JPanel();
        gameChoicePanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 5));
        sosLabel = new JLabel("SOS");
        SimpleBtn = new JRadioButton("Simple game",true);
        GeneralBtn = new JRadioButton("General Game");
        boardLabel = new JLabel("Board Size");
        boardSizeInput = new JTextArea(1,3);

        gameChoicePanel.add(sosLabel);
        gameChoicePanel.add(SimpleBtn);
        gameChoicePanel.add(GeneralBtn);
        gameChoicePanel.add(boardLabel);
        gameChoicePanel.add(boardSizeInput);

        //Buttons for simple or general game
        sosGroup = new ButtonGroup();
        sosGroup.add(SimpleBtn);
        sosGroup.add(GeneralBtn);
        SimpleBtn.addActionListener(modeActionListener(SOSGame.GameModeType.Simple, game));
        GeneralBtn.addActionListener(modeActionListener(SOSGame.GameModeType.General, game));

        //Components in the blue player side
        westPanel =new JPanel(new BorderLayout());
        westPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 5));
        // Record game checkbox
        blueComputer= PlayerBluePanel.getBlueComputer();
        bluePlayerS = PlayerBluePanel.getBluePlayerS();
        bluePlayerO = PlayerBluePanel.getBluePlayerO();
        blueHuman = PlayerBluePanel.getBlueHuman();
        recordGameCheckBox = new JCheckBox("Record Game");
        recordGameCheckBox.setSelected(false);
        westPanel.add(playerBluePanel, BorderLayout.CENTER);
        westPanel.setPreferredSize(new Dimension(140,150));
        westPanel.add(recordGameCheckBox,BorderLayout.SOUTH);



        //components in the red player side.
        eastSide = new JPanel(new BorderLayout());
        eastSide.setBorder(BorderFactory.createEmptyBorder(50, 20, 80, 15));
        redPlayerS = PlayerRedPanel.getRedPlayerS();
        redPlayerO = PlayerRedPanel.getRedPlayerO();
        eastSide.setPreferredSize(new Dimension(140,250));
        eastSide.add(playerRedPanel,BorderLayout.NORTH);
        redHuman = PlayerRedPanel.getRedHuman();
        redComputer = PlayerRedPanel.getRedComputer();
        replayBtn = new JButton("Replay");
        replayBtn.setSize(150,75);

        //New game button resets board to giving size
        newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("New Game")){
                    frame.dispose();
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {

                            int size = 3; // default size to 3
                            String input = boardSizeInput.getText();

                            if(input != null && !input.isEmpty()) {
                                size = Integer.parseInt(input);
                            }

                            if(validSize(size)){
                                CELL_SIZE = 300 / size;
                                BoardCanvas.setPreferredSize(new Dimension(CELL_SIZE * size, CELL_SIZE * size));
                                BoardCanvas.setSize(BoardCanvas.getPreferredSize());
                                BoardCanvas.repaint();

                                frame.setPreferredSize(new Dimension(CELL_SIZE * size +200, CELL_SIZE * size + 200));
                                frame.pack();
                                new GUI(new SOSGame(size));
                            }
                            else {

                                JOptionPane.showMessageDialog(boardSizeInput, "Please choose a valid size from 3-9", "Invalid game size", JOptionPane.ERROR_MESSAGE);
                                new GUI(new SOSGame(3));

                            }
                        }
                    });
                }
            }
            public boolean validSize(int size) {
                return size >= 3 && size <= 9;
            }
        });

        southSouthPanel = new JPanel(new GridLayout(2,1));
        southSouthPanel.add(replayBtn);
        southSouthPanel.add(newGameBtn);
        replayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = "SavedGames.txt";

                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                    String line;
                    while ((line = br.readLine()) != null) {

                        System.out.println(line);
                    }
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        });

        eastSide.add(southSouthPanel, BorderLayout.SOUTH);

        //Adding all the panel to a container
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(gameChoicePanel, BorderLayout.NORTH);
        contentPane.add(boardPanel,BorderLayout.CENTER);
        contentPane.add(eastSide,BorderLayout.EAST);
        contentPane.add(westPanel,BorderLayout.WEST);

    }
    private static ActionListener modeActionListener(SOSGame.GameModeType gameModeType, SOSGame game) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setCurrentGameType(gameModeType);
            }
        };
    }
    public static boolean isRecordGameCheckBoxSelected() {
        return recordGameCheckBox.isSelected();
    }
    class BoardCanvas extends JPanel {
        public  BoardCanvas() {
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {

                    String bluePlayer = playerBluePanel.getCurrentBluePlayer();
                    String redPlayer = playerRedPanel.getCurrentRedPlayer();

                    if (bluePlayer==null ) {
                        JOptionPane.showMessageDialog(null, "Please choose a player type before playing");
                        return;
                    }  else if (redPlayer==null) {
                        JOptionPane.showMessageDialog(null, "Red Player please choose a player type first");
                        return;
                    }

                    int rowSelected = e.getY() / CELL_SIZE;
                    int colSelected = e.getX() / CELL_SIZE;
                    int type;
                    boolean gameEnded= false;

                    if (game.getGameState() == SOSGame.GameState.PLAYING) {
                        if (game.getTurn()=='B') {
                            type = bluePlayerS.isSelected() ? 0 : 1;
                            if (bluePlayer.equals("Human")){
                                game.makeMove(rowSelected, colSelected, type);
                                game.updateGameState();
                                if(game.getGameState() == SOSGameGetters.GameState.BLUE_WON&&game.getTurn()=='B'){
                                    gameEnded =true;
                                }
                                if (redPlayer.equals("Computer")&& game.getTurn()=='R'){
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                    game.makeRandomMove(type == 0 ? 1 : 0);
                                }
                            } else if ( bluePlayer.equals("Computer")){
                                game.makeRandomMove(type);
                            }
                        }
                        else if (game.getTurn()=='R') {
                            type = redPlayerS.isSelected() ? 0 : 1;
                            if (redPlayer.equals("Human")) {
                                game.makeMove(rowSelected, colSelected, type);
                                game.updateGameState();
                                if(game.getGameState() == SOSGameGetters.GameState.RED_WON&&game.getTurn()=='R'){
                                    gameEnded =true;
                                }

                            } else if (redPlayer.equals("Computer")&&game.getTurn()=='R') {
                                game.makeRandomMove(type);
                            }
                            game.updateGameState();
                            if(game.getGameState() == SOSGameGetters.GameState.RED_WON&&game.getTurn()=='R'){
                                gameEnded =true;
                            }

                            if (bluePlayer.equals("Computer")&&game.getTurn()=='B'){
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                game.makeRandomMove(type == 0 ? 1 : 0);
                            }
                        }

                    } else if (bluePlayer.isEmpty()||redPlayer.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please choose a player type before playing");
                    }
                    else {
                        game.resetGame();
                    }
                    repaint();
                }
            });
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            drawGridLines(g);
            drawBoardSymbol(g);
            drawWinningLines(g);
            printStatusBar();
        }
        private void drawGridLines(Graphics g) {
            g.setColor(Color.LIGHT_GRAY);
            for (int row = 1; row < game.getTotalRows(); ++row) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF, CELL_SIZE * game.getTotalRows() - 1, GRID_WIDTH,
                        GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < game.getTotalColumns(); ++col) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0, GRID_WIDTH,
                        CELL_SIZE * game.getTotalColumns() - 1, GRID_WIDTH, GRID_WIDTH);
            }
        }
        private void drawBoardSymbol(Graphics g) {
            Graphics2D symbolType = (Graphics2D) g;
            symbolType.setColor(Color.BLACK);
            symbolType.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < game.getTotalRows(); ++row) {
                for (int col = 0; col < game.getTotalColumns(); ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    if (game.getCell(row, col) == SOSGame.Cell.S) {

                        symbolType.setColor(Color.BLUE);
                        symbolType.drawArc(x1 + CELL_SIZE / 5, y1, CELL_SIZE / 2 - CELL_PADDING, CELL_SIZE / 2 - CELL_PADDING,
                                60, 210);
                        symbolType.drawArc(x1 + CELL_SIZE / 5, y1 + CELL_SIZE / 2 - CELL_PADDING, CELL_SIZE / 2 - CELL_PADDING,
                                CELL_SIZE / 2 - CELL_PADDING, 240, 210);
                    } else if (game.getCell(row, col) == SOSGame.Cell.O) {
                        symbolType.setColor(Color.RED);
                        int symbolSize = (int) (CELL_SIZE * 0.5);//cell ratio
                        symbolType.drawOval(x1 + (CELL_SIZE - symbolSize) / 20, y1 + (CELL_SIZE - symbolSize) / 100, symbolSize, symbolSize);
                    }
                }
            }
        }
        private void drawWinningLines(Graphics g) {
            ArrayList<ArrayList<Integer>> info = game.getSosInfo();
            Graphics2D winningLines = (Graphics2D) g;
            if (info == null)
                return;
            for (ArrayList<Integer> it : info) {
                if (it.size() > 1) {
                    if (it.get(0) == 0)
                        winningLines.setColor(Color.BLUE); // for line to mark sos
                    else
                        winningLines.setColor(Color.RED);
                    for (int i = 1; i < it.size(); i += 4) {
                        int x1 = it.get(i + 1) * CELL_SIZE + CELL_SIZE / 2;
                        int y1 = it.get(i) * CELL_SIZE + CELL_SIZE / 2;
                        int x2 = it.get(i + 3) * CELL_SIZE + CELL_SIZE / 2;
                        int y2 = it.get(i + 2) * CELL_SIZE + CELL_SIZE / 2;
                        winningLines.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }
        private void printStatusBar() {
            if (game.getGameState() == SOSGame.GameState.PLAYING) {

                gameStatusBar.setForeground(Color.BLACK);
                if (game.getTurn() == 'B') {
                    gameStatusBar.setText("Blue Player's Turn");
                } else {
                    gameStatusBar.setText("Red Player's Turn");
                }
            } else if (game.getGameState() == SOSGame.GameState.DRAW) {
                gameStatusBar.setForeground(Color.BLACK);
                gameStatusBar.setText("It's a Draw! Click to play again.");
                if(!flag){
                    flag = true;
                }
            } else if (game.getGameState() == SOSGame.GameState.BLUE_WON) {
                gameStatusBar.setForeground(Color.BLUE);//for the status print
                gameStatusBar.setText("Blue player Won! Click to play again.");
                if(!flag){
                    flag=true;
                }
            } else if (game.getGameState() == SOSGame.GameState.RED_WON) {
                gameStatusBar.setForeground(Color.RED);// for the status print
                gameStatusBar.setText("Red player Won! Click to play again.");
                if(!flag){
                    flag = true;
                }
            }
        }
    }
   public static void main(String[] args) {
        //Frame with a default size 3
       GUI gui = new GUI(new SOSGame(3));
   }
}