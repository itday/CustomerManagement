package application;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Creates the details tab.
 * 
 * @author Sebastian Müller
 * @since 14.03.2018
 */

public class DetailsTab {

    private Controller controller;

    private JPanel jPanel;

    private JPanel       formPanel;
    private JTextField[] fields;
    private JPanel       buttonJPanel;

    private Box horzontalBox;

    public DetailsTab(Controller controller) {
        this.controller = controller;
    }

    public Component draw() {
        jPanel = new JPanel(new BorderLayout());

        generateForm();
        generateButtons();

        jPanel.add(formPanel, BorderLayout.CENTER);
        jPanel.add(buttonJPanel, BorderLayout.SOUTH);

        return jPanel;
    }

    public JTextField[] getFields() {
        return fields;
    }

    private void generateForm() {
        FormLayout formLayout = new FormLayout("25dlu, right:pref, 3dlu, 160px, 7dlu, right:pref, 3dlu, 160px",
                                               "10dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu");
        // formLayout.setColumnGroups(new int[][] { { 1, 5 }, { 3, 7 } });

        PanelBuilder builder = new PanelBuilder(formLayout);
        builder.border(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10),
                                                          BorderFactory.createTitledBorder("Customer details")));

        CellConstraints cc = new CellConstraints();

        fields = new JTextField[6];

        int r = 2;
        builder.addLabel("Family name", cc.xy(2, r));
        builder.add(fields[0] = new JTextField(), cc.xy(4, r));

        builder.addLabel("First name", cc.xy(6, r));
        builder.add(fields[3] = new JTextField(), cc.xy(8, r));

        r = 4;
        builder.addLabel("Street", cc.xy(2, r));
        builder.add(fields[1] = new JTextField(), cc.xy(4, r));

        builder.addLabel("House number", cc.xy(6, r));
        builder.add(fields[4] = new JTextField(), cc.xy(8, r));

        r = 6;
        builder.addLabel("City", cc.xy(2, r));
        builder.add(fields[2] = new JTextField(), cc.xy(4, r));

        builder.addLabel("ZIP code", cc.xy(6, r));
        builder.add(fields[5] = new JTextField(), cc.xy(8, r));

        for (JTextField field : fields)
            field.addKeyListener(controller.getHandleEnterSave());

        formPanel = builder.getPanel();
    }

    private void generateButtons() {
        buttonJPanel = new JPanel(new BorderLayout());

        Button b1 = new Button("Cancel");
        Button b2 = new Button("Reset Details");
        Button b3 = new Button("Save Details");

        b1.addActionListener(controller.getHandleCancle());
        b2.addActionListener(controller.getHandleReset());
        b3.addActionListener(controller.getHandleSave());

        // Enables saveDetails button
        if (Main.saveButtonEnabled) {
            b3.setEnabled(true);
        } else {
            b3.setEnabled(false);
            KeyListener listener = new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {}

                @Override
                public void keyReleased(KeyEvent e) {
                    b3.setEnabled(controller.checkFields());
                }

                @Override
                public void keyPressed(KeyEvent e) {}
            };

            for (JTextField text : fields)
                text.addKeyListener(listener);
        }

        horzontalBox = Box.createHorizontalBox();
        horzontalBox.add(b1);
        horzontalBox.add(b2);
        horzontalBox.add(b3);

        buttonJPanel.add(horzontalBox, BorderLayout.EAST);
    }
}