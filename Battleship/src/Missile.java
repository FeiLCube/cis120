
import java.awt.*;

public class Missile {
	
	private static final int SIZE = BattleshipGameCourt.SQUARE_LENGTH;
	private int px, py;
	private boolean hit;
	
	public Missile(int px, int py, boolean hit) {
		this.px = px;
		this.py = py;
		this.hit = hit;
	}
	
	public int px() {
		return px;
	}
	
	public int py() {
		return py;
	}
	
	public void setHit(boolean b) {
		hit = b;
	}
	
	public void draw(Graphics g) {
		if (hit) {
			g.setColor(Color.RED);
			g.fillOval(px, py, SIZE, SIZE);
		} else {
			g.setColor(Color.BLACK);
			g.drawOval(px, py, SIZE, SIZE);
		}
	}
}
