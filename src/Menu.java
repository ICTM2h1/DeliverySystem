import java.awt.*;
import javax.swing.*;

public class Menu {
    final static String BUTTONPANEL = "Menu 1";
    final static String TEXTPANEL = "Menu 2";
    final static int extraWindowWidth = 400;
    final static int extraWindowHeight = 400;

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

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Menu demo = new Menu();
        demo.addComponentToPane(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        UIManager.put("swing.boldMetal", Boolean.FALSE);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
