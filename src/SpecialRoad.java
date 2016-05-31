import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by ocean on 16-4-25.
 */
public class SpecialRoad implements Runnable {
    LinkedList<SpecialCar> vecCar = new LinkedList<SpecialCar>();
    Integer x, y;
    Integer[] pathx, pathy;
    Semaphore cross = new Semaphore(0);

    public SpecialRoad(Integer x, Integer y, Integer[] px, Integer[] py) {
        this.x = x;
        this.y = y;
        pathx = px;
        pathy = py;

        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i < 1000;i++) {
                    try {
                        Thread.sleep((int)(Math.random()*40+8)*1000);
                        addList();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void addList() {
        SpecialCar car = new SpecialCar(this, x, y, cross);
        vecCar.add(car);
        new Thread(car).start();

    }

    public Integer getDirectX(Integer i) { return pathx[i]; }

    public Integer getDirectY(Integer i) {
        return pathy[i];
    }

    public void run() {
        while (true) {
            try {
                cross.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
