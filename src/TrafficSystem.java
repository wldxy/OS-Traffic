import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.jar.Pack200;

/**
 * Created by ocean on 16-4-29.
 */
public class TrafficSystem implements Runnable {
    Road[] road = new Road[8];
    SpecialRoad[] specialRoad = new SpecialRoad[4];
    Semaphore[] sp = new Semaphore[8];
    Light[] lights = new Light[8];

    Integer[][] roadRe = {
            {1, 3, 4},
            {0, 5, 6},
            {3, 5, 6},
            {0, 2, 7},
            {0, 5, 7},
            {1, 2, 4},
            {1, 2, 7},
            {3, 4, 6}
    };
    Integer[][] start = {
            {7, -1},
            {8, -1},
            {18, 7},
            {18, 8},
            {10, 18},
            {9, 18},
            {-1, 10},
            {-1, 9}
    };
    Integer[][] sstart = {
            {6, -1},
            {18, 6},
            {11, 18},
            {-1, 11}
    };
    Integer[][] roadDirectx;
    Integer[][] roadDirecty;

    Boolean[][] isRoad = new Boolean[8][];

    TrafficSystem() {
        init();
        for (int i =0;i < 8;i++) {
            sp[i] = new Semaphore(0);
            road[i] = new Road(sp[i], start[i][0], start[i][1], roadDirectx[i], roadDirecty[i]);
            new Thread(road[i]).start();
        }
    }

    private void init() {
        roadDirectx = new Integer[8][];
        roadDirecty = new Integer[8][];

        for (int i = 0;i < 4;i++) {
            roadDirectx[2*i] = new Integer[19];
            roadDirecty[2*i] = new Integer[19];
            roadDirectx[2*i+1] = new Integer[17];
            roadDirecty[2*i+1] = new Integer[17];
        }

        for (int i = 0;i < 19;i++) {
            roadDirectx[0][i] = 0;
            roadDirecty[0][i] = 1;
            roadDirectx[2][i] = -1;
            roadDirecty[2][i] = 0;
            roadDirectx[4][i] = 0;
            roadDirecty[4][i] = -1;
            roadDirectx[6][i] = 1;
            roadDirecty[6][i] = 0;
        }

        for (int i = 0;i < 8;i++) {
            roadDirectx[1][i] = 0;
            roadDirecty[1][i] = 1;
            roadDirectx[3][i] = -1;
            roadDirecty[3][i] = 0;
            roadDirectx[5][i] = 0;
            roadDirecty[5][i] = -1;
            roadDirectx[7][i] = 1;
            roadDirecty[7][i] = 0;
        }

        for (int i = 10;i < 17;i++) {
            roadDirectx[1][i] = 1;
            roadDirecty[1][i] = 0;
            roadDirectx[3][i] = 0;
            roadDirecty[3][i] = 1;
            roadDirectx[5][i] = -1;
            roadDirecty[5][i] = 0;
            roadDirectx[7][i] = 0;
            roadDirecty[7][i] = -1;
        }

        for (int i = 1;i < 8;i+=2) {
            for (int j = 8;j < 10;j++) {
                if (roadDirectx[i][0] != 0)
                    roadDirectx[i][j] = roadDirectx[i][0];
                else
                    roadDirectx[i][j] = roadDirectx[i][15];
                if (roadDirecty[i][0] != 0)
                    roadDirecty[i][j] = roadDirecty[i][0];
                else
                    roadDirecty[i][j] = roadDirecty[i][15];
            }
        }

        lights[0] = new Light(350, 570, 40, 25, true);
        lights[1] = new Light(400, 570, 40, 25, true);
        lights[2] = new Light(320, 350, 25, 40, true);
        lights[3] = new Light(320, 400, 25, 40, true);
        lights[4] = new Light(500, 320, 40, 25, true);
        lights[5] = new Light(450, 320, 40, 25, true);
        lights[6] = new Light(570, 500, 25, 40, true);
        lights[7] = new Light(570, 450, 25, 40, true);
    }

    public synchronized void run() {
        while (true) {
            int fmax = 0, maxn = road[0].getPriortiy();
            for (int i = 1;i < 8;i++) {
                if (road[i].getPriortiy() > maxn) {
                    maxn = road[i].getPriortiy();
                    fmax = i;
                }
            }
            int smax = roadRe[fmax][0];
            maxn = road[roadRe[fmax][0]].getPriortiy();
            for (int i = 1;i < 3;i++) {
                if (road[roadRe[fmax][i]].getPriortiy() > maxn) {
                    maxn = road[i].getPriortiy();
                    smax = roadRe[fmax][i];
                }
            }
//
//            System.out.printf("%d  %d\n", fmax, smax);
//            for (int i = 0;i < 8;i++) {
//                System.out.printf("%d  ", sp[i].availablePermits());
//            }
//            System.out.println();

            lights[fmax].isRed = false;
            lights[smax].isRed = false;
            sp[fmax].release();
            sp[smax].release();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                sp[smax].acquire();
                lights[smax].isYellow = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                sp[fmax].acquire();
                lights[fmax].isYellow = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lights[smax].isYellow = false;
            lights[fmax].isYellow = false;
            lights[smax].isRed = true;
            lights[fmax].isRed = true;
        }
    }

    public Road getRoad(Integer i) {
        return road[i];
    }
}