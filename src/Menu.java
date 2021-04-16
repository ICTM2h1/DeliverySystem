import javax.swing.*;

public class Menu {
    private final JMenu menu, submenu;
    private final JMenuItem i1, i2, i3;

    public Menu() {
        JFrame f = new JFrame("Pathfinder");
        JMenuBar mb = new JMenuBar();
        menu = new JMenu("Menu");
        submenu = new JMenu("Sub Menu");
        i1 = new JMenuItem("Item 1");
        i2 = new JMenuItem("Item 2");
        i3 = new JMenuItem("Item 3");
        menu.add(i1);
        menu.add(i2);
        menu.add(i3);
        mb.add(menu);
        f.setJMenuBar(mb);
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
    }

}