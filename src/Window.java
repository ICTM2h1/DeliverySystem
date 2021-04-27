import java.awt.*;
import javax.swing.*;

public class Window {
    private final static String BUTTONPANEL = "Menu 1";
    private final static String TEXTPANEL = "Menu 2";
    private final static int extraWindowWidth = 400;
    private final static int extraWindowHeight = 400;

    public Window() {
        JFrame frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addComponentToPane(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();


        JPanel card1 = new JPanel() {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                size.height += extraWindowHeight;
                return size;
            }
        };

        card1.add(new JButton("Button 1"));
        card1.add(new JButton("Button 2"));
        card1.add(new JButton("Button 3"));

        JPanel card2 = new JPanel();
        card2.add(new JTextField("TextField", 20));

        tabbedPane.addTab(BUTTONPANEL, card1);
        tabbedPane.addTab(TEXTPANEL, card2);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }
}
