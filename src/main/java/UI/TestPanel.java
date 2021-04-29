package UI;

import javax.swing.*;

public class TestPanel extends JPanelBase {

    public TestPanel() {
        this.add(new JTextField("TextField", 20));
    }

    @Override
    public String getTitle() {
        return "Item 2";
    }

}
