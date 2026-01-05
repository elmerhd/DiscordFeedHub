package com.junk.application.discordfeedhub;

import com.junk.application.discordfeedhub.ui.Main;
import com.junk.application.discordfeedhub.utils.ApplicationTaskbar;
import com.junk.application.discordfeedhub.utils.ApplicationTray;
import com.junk.application.discordfeedhub.utils.Constants;
import com.junk.application.discordfeedhub.utils.DiscordFeedHubLogger;
import com.junk.application.discordfeedhub.utils.TweenAnimationManager;
import com.junk.application.discordfeedhub.utils.Utility;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author elmerhd
 */
public class DiscordFeedHub {

    public static void main(String[] args){
        new DiscordFeedHub().launchApplication(args);
    }
    
    public void launchApplication(String [] args){
        Properties applicationProperty = null;
        try {
            List<String> argsList = Arrays.asList(args);
            DiscordFeedHubLogger.getLogger(DiscordFeedHub.class.getName()).log(Level.INFO,"app args :" + argsList.toString());
            applicationProperty = Utility.getApplicationProperty();
            TweenAnimationManager.registerTweenAccessors();
            
            Utility.installLookAndFeels();
            Utility.createApplicationFolder(applicationProperty);
            Utility.checkSettings();
            
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
            if (argsList != null && !argsList.isEmpty() && argsList.contains(Constants.STARTUP_ARGS_MINIMIZED)) {
                Utility.getScheduler().start(null);
            } else {
                mainUI.setVisible(true);
            }
            for (String arg : argsList) {
                if (arg.contains(Constants.STARTUP_ARGS_DELETE)) {
                    String path = arg.substring(Constants.STARTUP_ARGS_DELETE.length());
                    Utility.safeDeleteFile(new File(path));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Unable to start application", applicationProperty.getProperty("app.name"), JOptionPane.ERROR_MESSAGE);
            DiscordFeedHubLogger.getLogger(DiscordFeedHub.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }
}
