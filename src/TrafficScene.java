import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by ocean on 16-4-29.
 */
public class TrafficScene extends JPanel implements Runnable {
    private TrafficSystem TS = new TrafficSystem();

    public TrafficScene() {
        new Thread(TS).start();
    }

    @Override
    public synchronized void run() {
        while (true) {
            this.repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, 900, 900);
//        g.setFont(new Font("宋体", Font.BOLD, 30));
//        g.drawString("Java模拟交通", 500, 10);

        g.setColor(Color.BLACK);
        g.fillRect(0, 294, 900, 8);
        g.fillRect(0, 598, 900, 8);
        g.fillRect(294, 0, 8, 900);
        g.fillRect(598, 0, 8, 900);

        g.setColor(Color.darkGray);
        for (int i = 7;i < 12;i++) {
            g.fillRect(0, i*50-1, 300, 2);
            g.fillRect(600, i*50-1, 300, 2);
            g.fillRect(i*50-1, 0, 2, 300);
            g.fillRect(i*50-1, 600, 2, 300);
        }

        for (int i = 0;i< 8;i++) {
            switch (i / 2) {
                case 0:
                    g.setColor(Color.blue);
                    break;
                case 1:
                    g.setColor(Color.magenta);
                    break;
                case 2:
                    g.setColor(Color.cyan);
                    break;
                case 3:
                    g.setColor(Color.yellow);
            }

            Road t = TS.getRoad(i);
            //System.out.printf("%d:  ", i);
            LinkedList<Car> car = t.vecCar;
            for (int j = 0;j < car.size();j++) {
                if (!car.get(j).getIsFinish()) {
                    Integer x = car.get(j).getX();
                    Integer y = car.get(j).getY();
                    //System.out.printf("%d * %d    ", x, y);
                    g.fill3DRect(x*50+10, y*50+10, 30, 30, true);
                }
            }
            //System.out.printf("--------\n");
        }

        Light[] lights = TS.lights;
        g.setColor(Color.darkGray);
        for (int i = 0;i < 8;i++) {
            g.fillRect(lights[i].x, lights[i].y, lights[i].width, lights[i].height);
        }
        for (int i = 0;i < 8;i++) {
            if (lights[i].isRed) {
                g.setColor(Color.red);
                if (lights[i].width > lights[i].height)
                    g.fillOval(lights[i].x, lights[i].y+5, 13, 13);
                else
                    g.fillOval(lights[i].x+5, lights[i].y, 13, 13);
            }
            else if (lights[i].isYellow) {
                g.setColor(Color.yellow);
                if (lights[i].width > lights[i].height)
                    g.fillOval(lights[i].x+15, lights[i].y+5, 13, 13);
                else
                    g.fillOval(lights[i].x+5, lights[i].y+15, 13, 13);

            }
            else {
                g.setColor(Color.green);
                if (lights[i].width > lights[i].height)
                    g.fillOval(lights[i].x+30, lights[i].y+5, 13, 13);
                else
                    g.fillOval(lights[i].x+5, lights[i].y+30, 13, 13);
            }

        }
    }
}