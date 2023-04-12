public class RandomFood {
    private int PIXEL;
    private int MAX_POS;
    public RandomFood(int PIXEL){
        this.PIXEL = PIXEL;
    }

    public int random(){
        int r = (int) (Math.random() * MAX_POS)+1;
        return r;
    }
    public int getFood() {
        return (random() * PIXEL);
    }

}
