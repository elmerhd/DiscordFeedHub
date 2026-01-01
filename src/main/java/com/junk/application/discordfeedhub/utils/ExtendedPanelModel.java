package com.junk.application.discordfeedhub.utils;

import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author elmerhd
 */
public class ExtendedPanelModel extends JPanel implements IExtendedPanel {

    @Override
    public void closeParentDialog() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JDialog) {
            window.dispose();
        }
    }
    
}
