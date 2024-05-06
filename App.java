import javax.swing.JOptionPane;

public class App {
	public static int sized;
	public static void main(String[] args) {
		runGame();
	}
	
	static void runGame() {
		int mysize = popUp();
		Minesweeper game2 = new Minesweeper(mysize);
	}
	
	static int popUp() {
		String size;
		int mysize = 0;
		size = JOptionPane.showInputDialog(null, "enter board size");
		try {
			mysize = Integer.parseInt(size);
		} catch (NumberFormatException e) {
			mysize = 20;
		}
		mysize = Integer.parseInt(size);
		sized = mysize;
		return mysize;
	}
}