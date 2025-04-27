package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccusationDialog extends JDialog {
    private JComboBox<String> personBox, weaponBox, roomBox;
    private Solution accusation = null;

    public AccusationDialog(Frame owner,
                            List<String> people,
                            List<String> weapons,
                            List<String> rooms)
    {
        super(owner, "Make an Accusation", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Person dropdown
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Person:"), gbc);
        personBox = new JComboBox<>(people.toArray(new String[0]));
        gbc.gridx = 1;
        add(personBox, gbc);

        // Weapon dropdown
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Weapon:"), gbc);
        weaponBox = new JComboBox<>(weapons.toArray(new String[0]));
        gbc.gridx = 1;
        add(weaponBox, gbc);

        // Room dropdown
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Room:"), gbc);
        roomBox = new JComboBox<>(rooms.toArray(new String[0]));
        gbc.gridx = 1;
        add(roomBox, gbc);

        // Buttons row
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submit = new JButton("Submit");
        JButton cancel = new JButton("Cancel");
        buttonRow.add(submit);
        buttonRow.add(cancel);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonRow, gbc);

        // Button logic
        submit.addActionListener(e -> {
            accusation = new Solution(
                (String)personBox.getSelectedItem(),
                (String)weaponBox.getSelectedItem(),
                (String)roomBox.getSelectedItem()
            );
            dispose();
        });
        cancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(owner);
    }

    /** @return the chosen accusation, or null if Cancel was hit. */
    public Solution getSolution() {
        return accusation;
    }
}
