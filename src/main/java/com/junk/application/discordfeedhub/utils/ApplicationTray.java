package com.junk.application.discordfeedhub.utils;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author elmerhd
 */
public class ApplicationTray {
    
    private final SystemTray tray = SystemTray.getSystemTray();
    private TrayIcon trayIcon = null;
    private JWindow anchor = null;
    private JPopupMenu popup = null;
    
    public ApplicationTray(String applicationName, Image image) throws AWTException {
        trayIcon = new TrayIcon(image, applicationName);
        trayIcon.setToolTip(applicationName);
        trayIcon.setImageAutoSize(true);
        anchor = new JWindow();
        anchor.setFocusableWindowState(true);
        anchor.setSize(1, 1);
        anchor.setType(Window.Type.POPUP);
        anchor.addWindowFocusListener(anchorWindowFocusListener);
    }
    
    public ApplicationTray(String applicationName, Image image, ActionListener e) throws AWTException {
        this(applicationName, image);
        trayIcon.addActionListener(e);
    }
    
    public ApplicationTray(String applicationName, Image image, ActionListener e, JPopupMenu popupMenu) throws AWTException {
        this(applicationName, image);
        this.popup = popupMenu;
        trayIcon.addActionListener(e);
        trayIcon.addMouseListener(trayMouseAdapter);
        this.popup.addPopupMenuListener(popupMenuListener);
        popup.requestFocusInWindow();
    }
    
    public void setUpTray() throws AWTException{
        tray.add(trayIcon);
    }
    
    public TrayIcon getTrayIcon() {
        return trayIcon;
    }
    
    private PopupMenuListener popupMenuListener = new PopupMenuListener() {
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            if (anchor != null && anchor.isVisible()) {
                anchor.setVisible(false);
            }
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {}
    };
    
    private MouseAdapter trayMouseAdapter = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                SwingUtilities.invokeLater(() -> {
                    // Move anchor to cursor
                    anchor.setLocation(e.getXOnScreen(), e.getYOnScreen());
                    anchor.setVisible(true);

                    popup.show(anchor, 0, 0);
                });
            }
        }
    };
    
    private WindowFocusListener anchorWindowFocusListener = new WindowFocusListener() {
        @Override
        public void windowLostFocus(WindowEvent e) {
            popup.setVisible(false);
            anchor.setVisible(false);
        }

        @Override
        public void windowGainedFocus(WindowEvent e) {}
    };
}
