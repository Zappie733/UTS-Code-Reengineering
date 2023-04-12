public class setupPanel {
    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;
    private final int PIXEL;
    private Position pos[];

    public setupPanel(int BOARD_WIDTH, int BOARD_HEIGHT, int PIXEL){
        this.BOARD_WIDTH = BOARD_WIDTH;
        this.BOARD_HEIGHT = BOARD_HEIGHT;
        this.PIXEL = PIXEL;
    }

    public int getBOARD_WIDTH() {
        return BOARD_WIDTH;
    }

    public int getBOARD_HEIGHT() {
        return BOARD_HEIGHT;
    }
    
    public int getPIXEL() {
        return PIXEL;
    }

    public int getMAX_LENGTH() {
        return this.BOARD_WIDTH * this.BOARD_HEIGHT;
    }

    public int getMAX_POS() {
        return (this.BOARD_WIDTH/this.PIXEL)-2;
    }

    public Position[] getPos() {
        this.pos = new Position[getMAX_LENGTH()];
		for(int i=0;i<pos.length;i++)
			pos[i] = new Position();
        return pos;
    }
}
