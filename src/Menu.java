import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Menu implements Runnable, ActionListener
{
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;



    public void run()
    {
        frame = new JFrame("Java Menubar Example");
        menuBar = new JMenuBar();

        // file menu
        fileMenu = new JMenu("File");

        // edit menu
        editMenu = new JMenu("Edit");

        // menus toevoegen aan menu
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // menubar in de frame zetten
        frame.setJMenuBar(menuBar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public void actionPerformed(ActionEvent ev)
    {
        SampleDialog dialog = new SampleDialog();
        dialog.setModal(true);
        dialog.setVisible(true);
    }


    private class SampleDialog extends JDialog implements ActionListener
    {
        private JButton okButton = new JButton("OK");

        private SampleDialog()
        {
            super(frame, "Testing", true);
            JPanel panel = new JPanel(new FlowLayout());
            panel.add(okButton);
            getContentPane().add(panel);
            okButton.addActionListener(this);
            setPreferredSize(new Dimension(400, 400));
            pack();
            setLocationRelativeTo(frame);
        }

        public void actionPerformed(ActionEvent ev)
        {
            setVisible(false);
        }
    }
}