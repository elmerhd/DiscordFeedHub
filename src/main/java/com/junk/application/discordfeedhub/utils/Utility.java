package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.DiscordFeedHub;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author elmerhd
 */
public class Utility {
    
    public static Toolkit getDefaultToolkit() {
        return Toolkit.getDefaultToolkit();
    }
    
    public static Image getApplicationIconImage() {
        try {
            return new ImageIcon(getSystemTrayImageURL()).getImage();
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Properties getApplicationProperty() throws IOException {
        InputStream inputstream = DiscordFeedHub.class.getResourceAsStream("app.properties");
        Properties applicationProperty = new Properties();
        applicationProperty.load(inputstream);
        return applicationProperty;
    }
    
    public static URL getMacApplicationImageURL() throws URISyntaxException {
        return DiscordFeedHub.class.getResource("/com/junk/application/discordfeedhub/logo-128.png");
    }
    
    public static URL getSystemTrayImageURL() throws URISyntaxException {
        return DiscordFeedHub.class.getResource("/com/junk/application/discordfeedhub/logo-64.png");
    }
    
    public static void checkStatus(ExtendedPanelModel extendedPanelModel, DatabaseStatementStatus databaseStatementStatus) {
        if (databaseStatementStatus.isSuccess()) {
            JOptionPane.showMessageDialog(extendedPanelModel, databaseStatementStatus.getStatusMessage(), "Status", JOptionPane.INFORMATION_MESSAGE);
            extendedPanelModel.closeParentDialog();
        } else {
            JOptionPane.showMessageDialog(extendedPanelModel, databaseStatementStatus.getStatusMessage(), "Status", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
