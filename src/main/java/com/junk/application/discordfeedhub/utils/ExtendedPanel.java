package com.junk.application.discordfeedhub.utils;

import java.awt.Window;
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
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JDialog) {
            window.dispose();
        }
    }
    
}
