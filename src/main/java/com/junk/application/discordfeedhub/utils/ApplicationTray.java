package com.junk.application.discordfeedhub.utils;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;

/**
 *
 * @author elmerhd
 */
public class ApplicationTray {
    
    private final SystemTray tray = SystemTray.getSystemTray();
    private TrayIcon trayIcon = null;
    
    public ApplicationTray(String applicationName, Image image) throws AWTException {
        trayIcon = new TrayIcon(image, applicationName);
        trayIcon.setImageAutoSize(true);
    }
    
    public ApplicationTray(String applicationName, Image image, ActionListener e) throws AWTException {
        this(applicationName, image);
        trayIcon.addActionListener(e);
    }
    
    public ApplicationTray(String applicationName, Image image, ActionListener e, PopupMenu popupMenu) throws AWTException {
        this(applicationName, image);
        trayIcon.addActionListener(e);
        trayIcon.setPopupMenu(popupMenu);
    }
    
    public void setUpTray() throws AWTException{
        tray.add(trayIcon);
    }
    
    public TrayIcon getTrayIcon() {
        return trayIcon;
    }
    
}
