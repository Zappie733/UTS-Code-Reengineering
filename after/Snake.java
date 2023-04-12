import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

class Position {
	public int x;
	public int y;
}
class Panel extends JPanel implements ActionListener {
    private setupPanel setup_panel;
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
		setup_panel = new setupPanel(width, height, pixel);
		this.MAX_LENGTH = setup_panel.getMAX_LENGTH();
		this.MAX_POS = setup_panel.getMAX_POS();
		this.pos = setup_panel.getPos();
		
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(setup_panel.getBOARD_WIDTH(), setup_panel.getBOARD_HEIGHT()));
        setupImages setupimages = new setupImages(setup_panel.getPIXEL());
        head = setupimages.setupImagesHead(HEAD);
        body = setupimages.setupImagesBody(BODY);
        food = setupimages.setupImagesFood(FOOD);
        initGame();

        RandomFood randomFood = new RandomFood(setup_panel.getPIXEL());
        food_x = randomFood.getFood();
        food_y = randomFood.getFood();
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


    @Override
    public void actionPerformed(ActionEvent e) {
        if (alive) {
			checkFood();
			checkCollision();
            move();
			repaint();
		}
	}

    public void checkFood(){
        if ((pos[0].x == food_x) && (pos[0].y == food_y)) {
            length++;
            randomFood();
            int d = DELAY-length;
            if(d < 40) d = 40;
            timer.setDelay(d);
        }
    }
    public void checkCollision(){
        for (int i=length; i>0; i--) {
            if (pos[0].x == pos[i].x && pos[0].y == pos[i].y) { 
                alive = false;
            }
        }
        if (pos[0].y >= setup_panel.getBOARD_HEIGHT())
            alive = false;
        if (pos[0].y < 0)
            alive = false;
        if (pos[0].x >= setup_panel.getBOARD_WIDTH())
            alive = false;
        if (pos[0].x < 0)
            alive = false;
        
        if (!alive)
            timer.stop();
    }
    public void move(){
        for (int i=length; i>0; i--) {
            pos[i].x = pos[i-1].x;
            pos[i].y = pos[i-1].y;
        }

        if (move == LEFT)
            pos[0].x -= setup_panel.getPIXEL();
        if (move == RIGHT)
            pos[0].x += setup_panel.getPIXEL();
        if (move == UP)
            pos[0].y -= setup_panel.getPIXEL();
        if (move == DOWN)
            pos[0].y += setup_panel.getPIXEL();
        moved=true;
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
