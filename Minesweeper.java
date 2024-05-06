import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.lang.Math;

public class Minesweeper extends JFrame{
	private class MineTile extends JButton {
		int r;
		int c;
		
		public MineTile(int r, int c) {
			this.r =r;
			this.c = c;
		}
	}
	
	
	int boardSize = App.sized;
	int numRows = boardSize;
	int numCols = numRows;
	int boardWidth = numCols;
	int boardHeight = numRows;
	int board_total_size = boardWidth * boardHeight;
	MineTile[][] board = new MineTile[numRows][numCols];
	ArrayList<MineTile> mineList;
	
	
	
	Random random = new Random();
	int tilesClicked = 0;	//goal is to click all the tiles except the ones containing mines
	boolean gameOver= false;
	
	JPanel northPanel = new JPanel();
	JLabel textLabel = new JLabel();
	JLabel westTextLabel = new JLabel();
	JPanel textPanel = new JPanel();
	JPanel boardPanel = new JPanel();
	JPanel controlPanel = new JPanel();
	JButton newGame = new JButton("NEW GAME");
	JTextField mines = new JTextField(3);
	

	public double number_mines = board_total_size * 0.125;
	double roundedDouble = Math.round(number_mines);
	int num_mines = (int) roundedDouble;
	int mineLeft = num_mines;
	int minesCounted = num_mines;
	
	Minesweeper(int size) {
		boardSize = size;
		//frame.setVisible(true);
		setTitle("Mine Sweeper");
		setSize(size*35, size*35+50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		boardPanel.setLayout(new GridLayout(boardSize,boardSize));
		setLayout(new BorderLayout());
		
		textLabel.setFont(new Font("Arial", Font.BOLD, 25));
//		textLabel.setHorizontalAlignment(JLabel.CENTER);
		textLabel.setText("Minesweeper"+Integer.toString(minesCounted));
		textLabel.setOpaque(true);
		
		westTextLabel.setFont(new Font("Arial", Font.BOLD, 25));
		westTextLabel.setHorizontalAlignment(JLabel.CENTER);
		westTextLabel.setText("Mines Left: "+minesCounted);
		
		textPanel.setLayout(new BorderLayout());
		add(textPanel, BorderLayout.NORTH);
//		textPanel.add(textLabel);
		textPanel.add(westTextLabel);
//		textPanel.add(mines);
//		
		//boardPanel.setBackground(Color.GREEN);
		
		add(boardPanel);
		controlPanel.add(newGame);
		add(controlPanel, BorderLayout.SOUTH);
		
		
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearmines();
				minesCounted = num_mines;
				westTextLabel.setText("Mines Left: "+minesCounted);
			}
		} );

		
		for (int r = 0; r<numRows; r++) {
			for (int c=0; c<numCols; c++) {
				MineTile tile = new MineTile(r, c);
				board[r][c] = tile;
				
				tile.setFocusable(false);
				tile.setMargin(new Insets(0, 0, 0, 0));
				tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20)); //set bomb text
				tile.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (gameOver) {
							return;
						}
						MineTile tile = (Minesweeper.MineTile) e.getSource();
						
						
						//left click
						if (e.getButton() == MouseEvent.BUTTON1) {
							if (tile.getText()=="") {
								if (mineList.contains(tile)) {
									revealMines();
								}
								else {
									checkMine(tile.r, tile.c);
								}
							}
						}
						//right Click
						else if (e.getButton() == MouseEvent.BUTTON3) {
							if (tile.getText() == "" && tile.isEnabled()) {
								tile.setText("🚩");
								tile.setBackground(Color.YELLOW);
								minesCounted -= 1;
								westTextLabel.setText("Mines Left: "+minesCounted);
							}
							else if (tile.getText() == "🚩") {
								tile.setText("");
								tile.setBackground(null);
								minesCounted += 1;
								westTextLabel.setText("Mines Left: "+minesCounted);
							}
						}
					}
				});
				boardPanel.add(tile);
			}
		}
		
		setVisible(true);
		
		setMines();
		
	}
	
	void setMines() {
		mineList = new ArrayList<MineTile>();
		while (mineLeft > 0) {
			int r = random.nextInt(numRows); //0-7
			int c = random.nextInt(numCols);
			
			MineTile tile = board[r][c];
			if (!mineList.contains(tile)) {
				mineList.add(tile);
				mineLeft -= 1;
			}
		}
	}
	
	void revealMines() {
		for (int i=0; i < mineList.size(); i++) {
			MineTile tile = mineList.get(i);
			tile.setText("💣");
			tile.setBackground(Color.red);
			
			
		}
		
		JOptionPane.showMessageDialog(null,"BOOM!, You lose!");
		gameOver = true;
		textLabel.setText("Game Over");
	}
	
	void checkMine(int r, int c) {
		if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
			return;
		}
		
		MineTile tile = board[r][c];
		if (!tile.isEnabled()) {
			return;
		}
		tile.setEnabled(false);
		tilesClicked += 1;
		
		int minesFound = 0;
		
		//top 3
		minesFound += countMine(r-1, c-1);	//top left
		minesFound += countMine(r-1, c);	//top
		minesFound += countMine(r-1, c+1);	//top right
		
		//left and right
		minesFound += countMine(r, c-1);	//left
		minesFound += countMine(r, c+1);	//right
		
		//bottom 3
		minesFound += countMine(r+1, c-1);	//bottom left
		minesFound += countMine(r+1, c);	//bottom
		minesFound += countMine(r+1, c+1);	//bottom right
		
		if (minesFound > 0) {
			tile.setText(Integer.toString(minesFound));
		} 
		else {
			tile.setText("");
			
			//top 3
			checkMine(r-1, c-1);	//top left
			checkMine(r-1, c);		//top
			checkMine(r-1, c+1);	//top right
			
			//left and right
			checkMine(r, c-1);	//left
			checkMine(r, c+1);	//right
			
			//bottom 3
			checkMine(r+1, c-1);	//bottom left
			checkMine(r+1, c);		//bottom
			checkMine(r+1, c+1);	//bottom right
		}
		
		if (tilesClicked == numRows * numCols - mineList.size()) {
			gameOver = true;
			textLabel.setText("Mines Cleared");
			JOptionPane.showMessageDialog(null,"YOU WIN");
		}
		
	}
	
	void clearmines() {
		dispose(); //Destroy the JFrame object
		App.runGame();
	}
	
	int countMine(int r, int c) {
		if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
			return 0;
		}
		if (mineList.contains(board[r][c])) {
			return 1;
		}
		return 0;
	}
}