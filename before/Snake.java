import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

class Position {
	public int x;
	public int y;
}
class Panel extends JPanel implements ActionListener { //Bloaters(Large Class) dan Change_preventers (divergent_change)  -> extract class
    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;
    private final int PIXEL;
    private final int MAX_LENGTH;
    private final int MAX_POS;
    private final int DELAY = 100;
	private final int HEAD = 1;
	private final int BODY = 2;
	private final int FOOD = 3;
    private final Position pos[];

    private int length;
    private int food_x;
    private int food_y;
	
    private int LEFT = 1;
    private int RIGHT = 2;
    private int UP = 4;
    private int DOWN = 8;
	private int move = RIGHT;
    private boolean alive = true;
	private boolean moved = false;

    private Timer timer;
    private Image body;
    private Image food;
    private Image head;

	private String msg = "Game Over";

    public Panel(int width, int height, int pixel) {
		this.BOARD_WIDTH = width;
		this.BOARD_HEIGHT = height;
		this.PIXEL = pixel;
		this.MAX_LENGTH = BOARD_WIDTH*BOARD_HEIGHT;
		this.MAX_POS = (BOARD_WIDTH/PIXEL)-2;
		this.pos = new Position[MAX_LENGTH];
		for(int i=0;i<pos.length;i++)
			pos[i] = new Position();
		
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        head = setupImages(HEAD);
        body = setupImages(BODY);
        food = setupImages(FOOD);
        initGame();
    }

    private Image setupImages(int type) { 
		BufferedImage image = new BufferedImage(PIXEL, PIXEL, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		switch(type) { //oo_abuser(switch statements) dan dispensables(duplicate code) -> bikin subclasses
			case BODY:
					g2.setPaint(Color.blue);
					g2.fillOval(0, 0, PIXEL, PIXEL);
					g2.dispose();
				break;
			case HEAD:
					g2.setPaint(Color.red);
					g2.fillOval(0, 0, PIXEL, PIXEL);
					g2.dispose();
				break;
			case FOOD:
					g2.setPaint(Color.green);
					g2.fillOval(2, 2, PIXEL-2, PIXEL-2);
					g2.setPaint(new Color(102,51,0));
					int [] x = {1, PIXEL/2, PIXEL/2};
					int [] y = {1, PIXEL/2, 1};
					g2.fillPolygon(x,y,3);
					g2.dispose();
				break;
		}
		return image;
    }

    private void initGame() {
        length = 3;

        for (int i=0; i<length; i++) {
            pos[i].x = PIXEL*5 - i * 10;
            pos[i].y = PIXEL*5;
        }
        
        randomFood();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (alive) {
            g.drawImage(food, food_x, food_y, this);
			g.drawImage(head, pos[0].x, pos[0].y, this);
            for (int i = 1; i < length; i++)
				g.drawImage(body, pos[i].x, pos[i].y, this);

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (BOARD_WIDTH - metr.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
    }

    private void randomFood() {
        int r = (int) (Math.random() * MAX_POS)+1;
        food_x = ((r * PIXEL));

        r = (int) (Math.random() * MAX_POS)+1;
        food_y = ((r * PIXEL));
    }

    @Override //long method and comments -> extract method
    public void actionPerformed(ActionEvent e) {
        if (alive) {
            // check food
			if ((pos[0].x == food_x) && (pos[0].y == food_y)) {
				length++;
				randomFood();
				int d = DELAY-length;
				if(d < 40) d = 40;
				timer.setDelay(d);
			}
            // checkCollision
			for (int i=length; i>0; i--) {
				if (pos[0].x == pos[i].x && pos[0].y == pos[i].y) { 
					alive = false;
				}
			}
			if (pos[0].y >= BOARD_HEIGHT)
				alive = false;
			if (pos[0].y < 0)
				alive = false;
			if (pos[0].x >= BOARD_WIDTH)
				alive = false;
			if (pos[0].x < 0)
				alive = false;
			
			if (!alive)
				timer.stop();
            
			//move
			for (int i=length; i>0; i--) {
				pos[i].x = pos[i-1].x;
				pos[i].y = pos[i-1].y;
			}

			if (move == LEFT)
				pos[0].x -= PIXEL;
			if (move == RIGHT)
				pos[0].x += PIXEL;
			if (move == UP)
				pos[0].y -= PIXEL;
			if (move == DOWN)
				pos[0].y += PIXEL;
			moved=true;
			repaint();
		}
	}

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
			if(!moved) return;
            int key = e.getKeyCode();
			switch(key) {
				case KeyEvent.VK_LEFT:  if(move!=RIGHT) move = LEFT; break;
				case KeyEvent.VK_RIGHT: if(move!=LEFT) 	move = RIGHT; break;
				case KeyEvent.VK_UP:    if(move!=DOWN) 	move = UP; break;
				case KeyEvent.VK_DOWN:  if(move!=UP)   	move = DOWN; break;
			}
			moved = false;
        }
    }
}

public class Snake extends JFrame {
    public Snake() {
        add(new Panel(600, 600, 20));
        setResizable(false);
        pack();
        setTitle("Snake Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
