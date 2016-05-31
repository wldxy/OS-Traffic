import java.awt.*;
import java.util.concurrent.Semaphore;

/**
 * Created by ocean on 16-4-25.
 */
public class Car implements Runnable {
    private Semaphore last;
    private Semaphore next = new Semaphore(0);
    private Integer priortiy;
    private Road road;
    private Boolean isFinish;
    private Boolean isLeave;
    private Integer x, y;
    private Integer numPace, maxlen;

    public Car(Integer priortiy, Road road, Semaphore last, Integer x, Integer y, Integer maxlen) {
        this.priortiy = priortiy;
        this.road = road;
        this.last = last;
        isFinish = false;
        isLeave = false;
        this.x = x;
        this.y = y;
        this.maxlen = maxlen;
        numPace = 0;
    }

    public Integer getPriortiy() {
        return priortiy;
    }

    public void setPriortiy(Integer priortiy) {
        this.priortiy = priortiy;
    }

    public Semaphore getNextSp() {
        return next;
    }

    public void setLastSp(Semaphore sp) {
        last = sp;
    }

    public Boolean getIsFinish() {
        return isFinish;
    }

    public Boolean getIsLeave() { return isLeave; }

    public Integer getX() { return x; }

    public Integer getY() { return y; }


    public void run() {
        try {
            last.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!meetLine() && !meetEnd()) {
            try {
                last.acquire();
                runNext();
                next.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!meetEnd()) {
            while (!road.getCanRun()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isLeave = true;

            while (!meetEnd()) {
                try {
                    last.acquire();
                    runNext();
                    next.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            next.release();
        }

    }

    public void runNext() {
        Integer dx = road.getDirectX(numPace);
        Integer dy = road.getDirectY(numPace);
        x += dx;
        y += dy;
        numPace++;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Boolean meetLine() {
        if (numPace == 6) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean meetEnd() {
        if (numPace >= maxlen) {
            isFinish = true;
            return true;
        } else {
            return false;
        }
    }
}
