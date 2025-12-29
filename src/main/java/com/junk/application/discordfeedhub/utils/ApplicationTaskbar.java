package com.junk.application.discordfeedhub.utils;

import java.awt.Image;
import java.awt.Taskbar;

/**
 *
 * @author elmerhd
 */
public class ApplicationTaskbar {
    
    private Taskbar taskbar = Taskbar.getTaskbar();
    
    private Image image = null;
    
    public ApplicationTaskbar(Image image) {
        this.image = image;
    }
    
    public void setUpTaskBar(){
        if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
            taskbar.setIconImage(this.image);
        }
    }
    
}
