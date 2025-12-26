package com.junk.application.discordfeedhub.utils;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author elmerhd
 */
public class ExtendedDialog extends JDialog{
    
    /**
     * Creates new form ConfirmationDialog
     */
    public ExtendedDialog(JFrame parent, String title, JPanel contentPanel) {
        super(parent, true);
        this.setTitle(title);
        this.getContentPane().add(contentPanel);
        this.pack();
        this.setLocationRelativeTo(parent);
    }
}
