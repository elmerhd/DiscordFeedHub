package com.junk.application.discordfeedhub;

import com.junk.application.discordfeedhub.ui.Main;
import com.junk.application.discordfeedhub.utils.ApplicationTaskbar;
import com.junk.application.discordfeedhub.utils.ApplicationTray;
import com.junk.application.discordfeedhub.utils.Constants;
import com.junk.application.discordfeedhub.utils.TweenAnimationManager;
import com.junk.application.discordfeedhub.utils.Utility;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
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
        try {
            List<String> argsList = Arrays.asList(args);
            Properties applicationProperty = Utility.getApplicationProperty();
            TweenAnimationManager.registerTweenAccessors();
            
            Utility.installLookAndFeels();
            Utility.createApplicationFolder(applicationProperty);
            Utility.setupLogger();
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
            JOptionPane.showMessageDialog(null, ex);
            System.getLogger(DiscordFeedHub.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
