import java.awt.*;
import java.awt.image.BufferedImage;


public class setupImages {
	private int PIXEL;
	BufferedImage image; 
	Graphics2D g2;
	public setupImages(int PIXEL){
		this.PIXEL = PIXEL;
		this.image = new BufferedImage(this.PIXEL, this.PIXEL, BufferedImage.TYPE_INT_RGB);
		this.g2 = image.createGraphics();
	}

    public Image setupImagesBody(int type) { 
		g2.setPaint(Color.blue);
		g2.fillOval(0, 0, PIXEL, PIXEL);
		g2.dispose();
		return image;
    }

	public Image setupImagesHead(int type) { 
		g2.setPaint(Color.red);
		g2.fillOval(0, 0, PIXEL, PIXEL);
		g2.dispose();
		return image;
    }

	public Image setupImagesFood(int type) { 
		g2.fillOval(2, 2, PIXEL-2, PIXEL-2);
		g2.setPaint(new Color(102,51,0));
		int [] x = {1, PIXEL/2, PIXEL/2};
		int [] y = {1, PIXEL/2, 1};
		g2.fillPolygon(x,y,3);
		g2.dispose();
		return image;
    }
}
