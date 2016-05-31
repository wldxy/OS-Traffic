import java.util.concurrent.Semaphore;

/**
 * Created by ocean on 16-4-25.
 */
public class SpecialCar implements Runnable {
    private SpecialRoad road;
    private Boolean isFinish;
    private Integer x, y;
    private Integer numPace = 0;
    private Semaphore cross;

    public SpecialCar(SpecialRoad road, Integer x, Integer y, Semaphore sp) {
        this.road = road;
        isFinish = false;
        this.x = x;
        this.y = y;
        cross = sp;
    }

    public Boolean getIsFinish() {
        return isFinish;
    }

    public void run() {
        while (!meetLine()) {
            runNext();
        }
        cross.release();
        while (!meetEnd()) {
            runNext();
        }
        isFinish = true;
    }

    public void runNext() {
        Integer dx = road.getDirectX(numPace);
        Integer dy = road.getDirectY(numPace);
        x += dx;
        y += dy;
        numPace++;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Boolean meetLine() {
        if (x == 6) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean meetEnd() {
        if (x >= 19) {
            isFinish = true;
            return true;
        } else {
            return false;
        }
    }
}
