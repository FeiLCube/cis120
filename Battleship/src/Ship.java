
import java.awt.*;

public class Ship {
	
	public static final int SQUARE_SIZE = BattleshipGameCourt.SQUARE_LENGTH;
	private int length;
	private int[] px, py;
	public boolean[] isHit;
	
	public Ship(int length, int[] xCoord, int[] yCoord) {
		this.length = length;
		this.px = xCoord;
		this.py = yCoord;
		isHit = new boolean[length];
		
		for (int i = 0; i < isHit.length; i++) {
			isHit[i] = false;
		}
	}
	
	public int getLength() {
		return this.length;
	}
	
	public int[] getXCoord() {
		return px;
	}
	
	public int[] getYCoord() {
		return py;
	}
	
	public void draw(Graphics g) {
		int minYCoord = 1000;
		int minXCoord = 1000;
		
		for (int coord : px) {
			if (coord < minXCoord) {
				minXCoord = coord;
			}
		}
		
		for (int coord : py) {
			if (coord < minYCoord) {
				minYCoord = coord;
			}
		}
		
		g.setColor(Color.BLACK);
		g.fillRect(minXCoord, minYCoord, SQUARE_SIZE * px.length, SQUARE_SIZE * py.length);
		
		for (int i = 0; i < length; i++) {
			if (isHit[i] == true) {
				g.setColor(Color.RED);
				g.fillOval(px[i], py[i], SQUARE_SIZE, SQUARE_SIZE);
			}
		}
		
	}
}
