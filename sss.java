import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StepResponseSystem extends JPanel {

    ArrayList<Double> time = new ArrayList<>();
    ArrayList<Double> output = new ArrayList<>();

    public void simulate() {

        double dt = 0.01;
        double tEnd = 20;

        // состояния
        double w2_1 = 0;
        double w2_2 = 0;
        double w3 = 0;
        double w4 = 0;
        double y = 0;

        for (double t = 0; t < tEnd; t += dt) {

            double r = 1; // вход — единичный скачок

            // обратная связь
            double feedback = 0.2 * y;

            // ошибка
            double e = r - feedback;

            // W2 = 0.2/(2p+1)^2
            w2_1 += dt * (-w2_1 + e);
            w2_2 += dt * (-w2_2 + w2_1);
            double outW2 = 0.2 * w2_2;

            // W3 = 1/p
            w3 += dt * outW2;

            // W4 = 3/(4p+1)
            w4 += dt * (-w4 + 3 * w3) / 4.0;

            y = w4;

            time.add(t);
            output.add(y);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        g.drawLine(50, h - 50, w - 50, h - 50);
        g.drawLine(50, 50, 50, h - 50);

        for (int i = 1; i < time.size(); i++) {
            int x1 = 50 + (int)(time.get(i-1) * 30);
            int y1 = h - 50 - (int)(output.get(i-1) * 40);

            int x2 = 50 + (int)(time.get(i) * 30);
            int y2 = h - 50 - (int)(output.get(i) * 40);

            g.drawLine(x1, y1, x2, y2);
        }
    }

    public static void main(String[] args) {
        StepResponseSystem panel = new StepResponseSystem();
        panel.simulate();

        JFrame frame = new JFrame("Переходный процесс системы");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
    }
}
