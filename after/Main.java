import java.util.*;

public class Main{
    public static void main(String[] args){
        Worker workerObj = new Worker("David", 20);
        String x = workerObj.getName() + " " + workerObj.getAge();
        iterates(x);
    }
    public static void iterates(String worker){
        for(int i = 0; i < 5; i++){
            System.out.println(worker);
        }
    }
}    