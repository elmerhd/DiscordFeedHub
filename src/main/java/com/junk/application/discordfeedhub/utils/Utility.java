package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.DmlResult;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import com.junk.application.discordfeedhub.DiscordFeedHub;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author elmerhd
 */
public class Utility {
    
    private static RSSScheduler scheduler = new RSSScheduler();
    private static String applicationFolder = null;
    private static Preference preference;
    
    public static Toolkit getDefaultToolkit() {
        return Toolkit.getDefaultToolkit();
    }
    
    public static Image getApplicationIconImage() {
        try {
            return new ImageIcon(getSystemTrayImageURL()).getImage();
        } catch (URISyntaxException ex) {
            DiscordFeedHubLogger.getLogger(Utility.class.getName()).log(Level.SEVERE, (String) null, ex);
            return null;
        }
    }
    
    public static Properties getApplicationProperty() throws IOException{
        InputStream inputstream = DiscordFeedHub.class.getResourceAsStream("app.properties");
        Properties applicationProperty = new Properties();
        applicationProperty.load(inputstream);
        return applicationProperty;
    }
    
    public static URL getLoadingImageURL() throws URISyntaxException {
        return DiscordFeedHub.class.getResource("/com/junk/application/discordfeedhub/logo-256.png");
    }
    
    public static URL getMacApplicationImageURL() throws URISyntaxException {
        return DiscordFeedHub.class.getResource("/com/junk/application/discordfeedhub/logo-128.png");
    }
    
    public static URL getSystemTrayImageURL() throws URISyntaxException {
        return DiscordFeedHub.class.getResource("/com/junk/application/discordfeedhub/logo-32.png");
    }
    
    public static String getAboutInfoHTMLFile() {
        return "/com/junk/application/discordfeedhub/about.html";
    }
    
    public static FlatSVGIcon getSettingIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/gear.svg");
    }
    
    public static FlatSVGIcon getCloseIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/close.svg");
    }
    
    public static FlatSVGIcon getSaveIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/save.svg");
    }
    
    public static FlatSVGIcon getNewIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/new.svg");
    }
    
    public static FlatSVGIcon getUpdateIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/update.svg");
    }
    
    public static FlatSVGIcon getStartIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/play.svg");
    }
    
    public static FlatSVGIcon getStopIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/stop.svg");
    }
    
    public static FlatSVGIcon getDownloadIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/download.svg");
    }
    
    public static FlatSVGIcon getDateIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/date.svg");
    }
    
    public static FlatSVGIcon getLogIcon() {
        return new FlatSVGIcon("com/junk/application/discordfeedhub/icons/log.svg");
    }
    
    public static void checkStatus(ExtendedPanel extendedPanelModel, DmlResult dmlResult) {
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
    
    public static String getApplicationFolder() {
        return applicationFolder;
    }
    
    public static JPopupMenu getTrayPopupMenu(JFrame parentFrame) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setFont(parentFrame.getFont());
        quitMenuItem.addActionListener((ActionEvent e) -> {
            int option = JOptionPane.showConfirmDialog(parentFrame, "Sure to quit " +parentFrame.getTitle()+ "?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        JMenuItem openItem = new JMenuItem("Open");
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
    
    public static void installLookAndFeels(){
        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme("Monocai", "com.formdev.flatlaf.intellijthemes.FlatMonocaiIJTheme"));
        themes.add(new Theme("Material Design Dark", "com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme"));
        themes.add(new Theme("Gradianto Nature Green", "com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme"));
        themes.add(new Theme("Gradianto Midnight Blue", "com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme"));
        themes.add(new Theme("Gradianto Nature Green", "com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme"));
        themes.add(new Theme("Gradianto Dark Fuchsia", "com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme"));
        themes.add(new Theme("Dark Purple", "com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme"));
        themes.add(new Theme("Cyan Light", "com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme"));
        themes.add(new Theme("Carbon", "com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme"));
        themes.add(new Theme("Arc Orange", "com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme"));
        themes.add(new Theme("Arc Dark", "com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme"));
        themes.add(new Theme("Flat Light", "com.formdev.flatlaf.FlatLightLaf"));
        themes.add(new Theme("Flat Dark", "com.formdev.flatlaf.FlatDarkLaf"));
        
        for(Theme theme: themes) {
            UIManager.installLookAndFeel(new FlatAllIJThemes.FlatIJLookAndFeelInfo(theme.getName(), theme.getClassName(), (theme.getClassName().contains("Dark")) ));
        }
    }
    
    public static void checkSettings() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        Properties applicationProperty = getApplicationProperty();
        String preferenceFile = applicationProperty.getProperty("app.settings");
        String template = "{0}\\{1}";
        String result = MessageFormat.format(
                template,
                Utility.getApplicationFolder(),
                preferenceFile
        );
        preference = new Preference(new File(result));
        preference.applyTheme();
    }
    
    public static void setPreference(Preference pref) {
        preference = pref;
    }
    
    public static Preference getPreference() {
        return preference;
    }
    
    /**
     * Gets the currently running JAR file
     */
    public static File getRunningJarFile() {
        try {
            File jarFile = new File(
                    Utility.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );
            if (jarFile.isFile()) {
                return jarFile; // running from JAR
            } else {
                return null; // running from IDE
            }
        } catch (URISyntaxException ex) {
            DiscordFeedHubLogger.getLogger(Utility.class.getName()).log(Level.SEVERE, (String) null, ex);
            return null;
        }
    }
    
    public static void safeDeleteFile(File fileToDelete) {
        new Thread(() -> {
            try {
                if (!fileToDelete.exists()) {
                    return;
                }
                while (!fileToDelete.delete()) {                
                    fileToDelete.delete();
                    DiscordFeedHubLogger.getLogger(Utility.class.getName()).log(Level.INFO, () -> "Deleting file  : " +fileToDelete.getAbsolutePath());
                }
            } catch (Exception ex) {
                DiscordFeedHubLogger.getLogger(Utility.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }).start();
    }
    
    public static String formatFileSize(long bytes) {
        if (bytes < 0) return "0 B";
        if (bytes < 1024) return bytes + " B";

        final String[] units = {"KB", "MB", "GB", "TB"};
        double size = bytes;
        int unitIndex = -1;

        do {
            size /= 1024;
            unitIndex++;
        } while (size >= 1024 && unitIndex < units.length - 1);

        return String.format("%.2f %s", size, units[unitIndex]);
    }

    // Helper to generate random Discord color
    public static int randomColor() {
        return ThreadLocalRandom.current().nextInt(0xFFFFFF);
    }
}
