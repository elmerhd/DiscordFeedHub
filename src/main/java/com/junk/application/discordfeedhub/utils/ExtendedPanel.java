package com.junk.application.discordfeedhub.utils;

import java.awt.Window;
import java.util.logging.Level;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author elmerhd
 */
public abstract class ExtendedPanel extends JPanel implements IExtendedPanel {

    public ExtendedPanel() {
        onInitializedPanel();
    }
    
    @Override
    public void closeParentDialog() {
        DiscordFeedHubLogger.getLogger(ExtendedPanel.class.getName()).log(Level.INFO, "Closing parent dialog");
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JDialog) {
            window.dispose();
        }
    }
    
}
