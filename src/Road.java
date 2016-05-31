import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by ocean on 16-4-25.
 */
public class Road implements Runnable {
    LinkedList<Car> vecCar = new LinkedList<Car>();
    private Semaphore first;
    private Semaphore isRun;
    private Boolean canRun;
    private Integer s_x, s_y;
    private Integer pathx[], pathy[];
    private Integer maxlen;

    public Road(Semaphore isRun, Integer x, Integer y, Integer[] px, Integer[] py) {
        this.isRun = isRun;
        canRun = false;
        s_x = x;
        s_y = y;
        pathx = px;
        pathy = py;
        maxlen = px.length;

        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i < 1000;i++) {
                    try {
                        Thread.sleep((int)(Math.random()*6+1)*1000);
                        addList();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void addList() {
        if (vecCar.isEmpty()) {
            Car car = new Car(1, this, new Semaphore(100), s_x, s_y, maxlen);
            vecCar.add(car);
            new Thread(car).start();
        } else {
            Car car = vecCar.getLast();
            vecCar.add(new Car(1, this, car.getNextSp(), s_x, s_y, maxlen));
            new Thread(car).start();
        }
    }

    public void run() {
        while (true) {
            try {
                isRun.acquire();
                runCar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(8000);
                stopCar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isRun.release();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean getCanRun() {
        return canRun;
    }

    public void stopCar() {
        canRun = false;
    }

    public void runCar() {
        canRun = true;
    }

    public Integer getPriortiy() {
        Integer ans = 0;
        for (int i = 0;i < vecCar.size();i++) {
            if (!vecCar.get(i).getIsLeave()) {
                ans += vecCar.get(i).getPriortiy();
            }
        }
        return ans;
    }

    public Integer getDirectX(Integer i) { return pathx[i]; }

    public Integer getDirectY(Integer i) {
        return pathy[i];
    }

    public Integer getMaxlen() { return maxlen; }
}
