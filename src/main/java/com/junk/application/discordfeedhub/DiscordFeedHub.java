package com.junk.application.discordfeedhub;

import com.formdev.flatlaf.FlatLightLaf;
import com.junk.application.discordfeedhub.ui.Main;
import com.junk.application.discordfeedhub.utils.ApplicationTaskbar;
import com.junk.application.discordfeedhub.utils.ApplicationTray;
import com.junk.application.discordfeedhub.utils.TweenAnimationManager;
import com.junk.application.discordfeedhub.utils.Utility;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author elmerhd
 */
public class DiscordFeedHub {

    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException, AWTException {
        new DiscordFeedHub().launchApplication(args);
    }
    
    public void launchApplication(String [] args) throws IOException, URISyntaxException, AWTException{
        try {
            
            FlatLightLaf.setup();
            TweenAnimationManager.registerTweenAccessors();
            
            Main mainUI = new Main();
            
            Properties applicationProperty = Utility.getApplicationProperty();
            String applicationName = applicationProperty.getProperty("app.name");
            // setting system properties for mac os
            System.setProperty( "apple.laf.useScreenMenuBar", "true");
            System.setProperty( "apple.awt.application.name", applicationName);
            System.setProperty( "apple.awt.application.appearance", "system");
            
            Toolkit defaultToolkit = Utility.getDefaultToolkit();
            Image macImageLogo = defaultToolkit.getImage(Utility.getMacApplicationImageURL());
            Image systemTrayImageLogo = defaultToolkit.getImage(Utility.getSystemTrayImageURL());
            
            // setup tray icon
            new ApplicationTray(applicationName, systemTrayImageLogo, (ActionEvent e) -> {
                if (!mainUI.isVisible()) {
                    mainUI.setVisible(true);
                }
            }).setUpTray();
            // setup task bar for mac os
            new ApplicationTaskbar(macImageLogo).setUpTaskBar();
            
            if (args.length != 0 && args[0] != null && args[0] == "--no-ui") {
            
            } else {
                mainUI.setVisible(true);
//                RSSReader rSSReader = new RSSReader("https://techcrunch.com/feed/");
//                
//                for (SyndEntry entry : rSSReader.getList()) {
//                    System.out.println("title : " + entry.getTitle());
//                    System.out.println("link : " + entry.getLink());
//                    System.out.println("uri : " + entry.getUri());
//                    System.out.println("author : " + entry.getAuthor());
//                    System.out.println("category : " +entry.getCategories());
//                    break;
//                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
}
