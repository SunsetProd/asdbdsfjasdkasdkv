import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StepResponseSystem extends JPanel {

    ArrayList<Double> time = new ArrayList<>();
    ArrayList<Double> output = new ArrayList<>();

    public void simulate() {

        double dt = 0.01;
        double tEnd = 20;

        double w2_1 = 0;
        double w2_2 = 0;
        double w3 = 0;
        double w4 = 0;
        double y = 0;

        for (double t = 0; t < tEnd; t += dt) {

            double r = 1;
            double feedback = 0.2 * y;
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

        // ===== ЛИСТИНГ =====
        System.out.println("t\t y(t)");
        for (int i = 0; i < time.size(); i += 20) {
            System.out.printf("%.2f\t %.4f\n", time.get(i), output.get(i));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        int x0 = 60;
        int y0 = h - 60;

        // оси
        g.drawLine(x0, y0, w - 40, y0);
        g.drawLine(x0, 40, x0, y0);

        // метки по времени
        for (int i = 0; i <= 20; i += 2) {
            int x = x0 + i * 30;
            g.drawLine(x, y0 - 5, x, y0 + 5);
            g.drawString(Integer.toString(i), x - 5, y0 + 20);
        }

        // метки по Y
        for (int i = 0; i <= 5; i++) {
            int y = y0 - i * 40;
            g.drawLine(x0 - 5, y, x0 + 5, y);
            g.drawString(String.format("%.1f", (double)i), x0 - 40, y + 5);
        }

        // график
        for (int i = 1; i < time.size(); i++) {
            int x1 = x0 + (int)(time.get(i-1) * 30);
            int y1 = y0 - (int)(output.get(i-1) * 40);

            int x2 = x0 + (int)(time.get(i) * 30);
            int y2 = y0 - (int)(output.get(i) * 40);

            g.drawLine(x1, y1, x2, y2);
        }

        g.drawString("t", w - 30, y0 + 20);
        g.drawString("y(t)", x0 - 40, 30);
        g.drawString("Переходный процесс", w/2 - 60, 20);
    }

    public static void main(String[] args) {
        StepResponseSystem panel = new StepResponseSystem();
        panel.simulate();

        JFrame frame = new JFrame("Переходный процесс системы");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
    }
}
