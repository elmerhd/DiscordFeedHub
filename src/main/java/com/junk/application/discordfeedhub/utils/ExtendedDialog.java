package com.junk.application.discordfeedhub.utils;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

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
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                DiscordFeedHubLogger.getLogger(ExtendedDialog.class.getName()).log(Level.INFO, "Closing popup dialog");
                ExtendedDialog.this.dispose();
            }
        });
    }
    
    public void addEscapeDisposeSupport() {
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
              .put(escapeKey, "ESCAPE");
        this.getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DiscordFeedHubLogger.getLogger(ExtendedDialog.class.getName()).log(Level.INFO, "Closing popup dialog");
                ExtendedDialog.this.dispose();
            }
        });
    }
    
    
}
