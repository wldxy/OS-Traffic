import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ocean on 16-4-29.
 */
public class Scene extends JFrame {
    private TrafficScene TScene = new TrafficScene();

    public Scene() {
        this.add(TScene);
        this.setSize(900, 900);
        TScene.repaint();
        this.setVisible(true);
        new Thread(TScene).start();
    }

    public static void main(String[] args) {
        new Scene();
    }

}


