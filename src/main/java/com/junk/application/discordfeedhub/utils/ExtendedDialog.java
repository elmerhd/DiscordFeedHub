package com.junk.application.discordfeedhub.utils;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *
 * @author elmerhd
 */
public class ExtendedDialog extends JDialog{
    
    /**
     * Creates new form ExtendedDialog
     */
    public ExtendedDialog(JFrame parent, String title, JPanel contentPanel) {
        super(parent, true);
        this.setTitle(title);
        this.getContentPane().add(contentPanel);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.addEscapeDisposeSupport();
    }
    
    public void addEscapeDisposeSupport() {
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
              .put(escapeKey, "ESCAPE");
        this.getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExtendedDialog.this.dispose();
            }
        });
    }
}
