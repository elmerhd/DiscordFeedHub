package com.junk.application.discordfeedhub;

import com.formdev.flatlaf.FlatLightLaf;
import com.junk.application.discordfeedhub.ui.Main;
import com.junk.application.discordfeedhub.utils.ApplicationTaskbar;
import com.junk.application.discordfeedhub.utils.ApplicationTray;
import com.junk.application.discordfeedhub.utils.TweenAnimationManager;
import com.junk.application.discordfeedhub.utils.Utility;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Properties;

/**
 *
 * @author elmerhd
 */
public class DiscordFeedHub {

    public static void main(String[] args){
        new DiscordFeedHub().launchApplication(args);
    }
    
    public void launchApplication(String [] args){
        try {
            Properties applicationProperty = Utility.getApplicationProperty();
            FlatLightLaf.setup();
            TweenAnimationManager.registerTweenAccessors();
            Utility.createApplicationFolder(applicationProperty);
            Utility.setupLogger();
            
            Main mainUI = new Main();
            String applicationName = applicationProperty.getProperty("app.name");
            // setting system properties for mac os
            System.setProperty( "apple.laf.useScreenMenuBar", "true");
            System.setProperty( "apple.awt.application.name", applicationName);
            System.setProperty( "apple.awt.application.appearance", "system");
            
            Toolkit defaultToolkit = Utility.getDefaultToolkit();
            Image macImageLogo = defaultToolkit.getImage(Utility.getMacApplicationImageURL());
            Image systemTrayImageLogo = defaultToolkit.getImage(Utility.getSystemTrayImageURL());
            
            // setup tray icon
            
            new ApplicationTray(
                    applicationName, 
                    systemTrayImageLogo, 
                    (ActionEvent e) -> {
                        if (!mainUI.isVisible()) {
                            mainUI.setVisible(true);
                        }
                    },
                    Utility.getTrayPopupMenu(mainUI)
            ).setUpTray();
            
            // setup task bar for mac os
            new ApplicationTaskbar(macImageLogo).setUpTaskBar();
            
            if (args.length != 0 && args[0] != null && args[0] == "--no-ui") {
                
            } else {
                mainUI.setVisible(true);
            }
        } catch (Exception ex) {
            System.getLogger(DiscordFeedHub.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
