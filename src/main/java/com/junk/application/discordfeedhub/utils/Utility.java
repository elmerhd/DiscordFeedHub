package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.DiscordFeedHub;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author elmerhd
 */
public class Utility {
    
    private static RSSScheduler scheduler = new RSSScheduler();
    private static String applicationFolder = null;
    
    public static Toolkit getDefaultToolkit() {
        return Toolkit.getDefaultToolkit();
    }
    
    public static Image getApplicationIconImage() {
        try {
            return new ImageIcon(getSystemTrayImageURL()).getImage();
        } catch (URISyntaxException ex) {
            System.getLogger(Utility.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
    
    public static void checkStatus(ExtendedPanelModel extendedPanelModel, DmlResult dmlResult) {
        if (dmlResult.isSuccess()) {
            JOptionPane.showMessageDialog(extendedPanelModel, "Success!", "Status", JOptionPane.INFORMATION_MESSAGE);
            extendedPanelModel.closeParentDialog();
        } else {
            JOptionPane.showMessageDialog(extendedPanelModel, dmlResult.message(), "Status", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static RSSScheduler getScheduler() {
        return scheduler;
    }
    
    public static void createApplicationFolder(Properties props) throws IOException {
        String folder = props.getProperty("app.folder");
        Path appDir = Paths.get(
            System.getProperty("user.home"),
            folder
        );
        applicationFolder = appDir.toAbsolutePath().toString();
        Files.createDirectories(appDir);
    }
    
    public static void setupLogger() throws IOException, FileNotFoundException {
        Properties applicationProperty = Utility.getApplicationProperty();
        String applicationName = applicationProperty.getProperty("app.name");
        String template = "{0}\\{1}.log";
        String result = MessageFormat.format(
                template,
                Utility.getApplicationFolder(),
                applicationName.toLowerCase()
        );
        PrintStream fileOut = new PrintStream(result);
        System.setErr(fileOut);
    }
    
    public static String getApplicationFolder() {
        return applicationFolder;
    }
    
    public static PopupMenu getTrayPopupMenu(JFrame parentFrame) {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem quitMenuItem = new MenuItem("Quit");
        quitMenuItem.setFont(parentFrame.getFont());
        quitMenuItem.addActionListener((ActionEvent e) -> {
            int option = JOptionPane.showConfirmDialog(parentFrame, "Sure to quit " +parentFrame.getTitle()+ "?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        MenuItem openItem = new MenuItem("Open");
        openItem.setFont(parentFrame.getFont());
        openItem.addActionListener((ActionEvent e) -> {
            if (!parentFrame.isVisible()) {
                parentFrame.setVisible(true);
            }
        });
        popupMenu.add(openItem);
        popupMenu.add(quitMenuItem);
        return popupMenu;
    }
}
