package com.junk.application.discordfeedhub.utils;

import com.formdev.flatlaf.FlatLaf;
import java.awt.Font;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author elmerhd
 */
public class Preference {
    
    private File file;
    private Theme theme;
    private String fontName;
    private Integer fontSize;
    private Properties prefProperties;
    private OutputStream outputStream;
    private InputStream inputStream;
    // app specific cfg
    private Integer schedulerTimerInterval;
    private Integer webhookQueueDelay;
    private Boolean runningAtStartup;
    
    public Preference(File file) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        prefProperties = new Properties();
        this.theme = new Theme(Constants.PREF_DEFAULT_LAF_NAME_VALUE, Constants.PREF_DEFAULT_LAF_CLASS_NAME_VALUE);
        this.fontName = Constants.PREF_DEFAULT_FONT_NAME_VALUE;
        this.fontSize = Constants.PREF_DEFAULT_FONT_SIZE_VALUE;
        this.schedulerTimerInterval = Constants.PREF_DEFAULT_SCHEDULER_TIMER_VALUE;
        this.webhookQueueDelay = Constants.PREF_DEFAULT_WEBHOOK_DELAY_VALUE;
        this.runningAtStartup = Constants.PREF_DEFAULT_RUN_STARTUP_VALUE;
        this.file = file;
        if (file.exists()) {
            inputStream = new FileInputStream(file.getAbsolutePath());
            prefProperties.load(inputStream);
            String themeName = prefProperties.getProperty(Constants.PREF_DEFAULT_LAF_NAME_KEY);
            String themeClassName = prefProperties.getProperty(Constants.PREF_DEFAULT_LAF_CLASS_NAME_KEY);
            this.fontName = prefProperties.getProperty(Constants.PREF_DEFAULT_FONT_NAME_KEY);
            this.fontSize = Integer.valueOf(prefProperties.getProperty(Constants.PREF_DEFAULT_FONT_SIZE_KEY));
            this.theme = new Theme(themeName, themeClassName);
            this.schedulerTimerInterval = Integer.valueOf(prefProperties.getProperty(Constants.PREF_DEFAULT_SCHEDULER_TIMER_KEY));
            this.webhookQueueDelay = Integer.valueOf(prefProperties.getProperty(Constants.PREF_DEFAULT_WEBHOOK_DELAY_KEY));
            this.runningAtStartup = Boolean.valueOf(prefProperties.getProperty(Constants.PREF_DEFAULT_RUN_STARTUP_KEY));
            inputStream.close();
        } else {
            savePreference();
        }
    }
    
    public void savePreference() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
        if (!file.exists()) {
            file.createNewFile();
        }
        outputStream = new FileOutputStream(file.getAbsolutePath());
        prefProperties.setProperty(Constants.PREF_DEFAULT_LAF_NAME_KEY, this.theme.getName());
        prefProperties.setProperty(Constants.PREF_DEFAULT_LAF_CLASS_NAME_KEY, this.theme.getClassName());
        prefProperties.setProperty(Constants.PREF_DEFAULT_FONT_NAME_KEY, this.fontName);
        prefProperties.setProperty(Constants.PREF_DEFAULT_FONT_SIZE_KEY, String.valueOf(this.fontSize));
        prefProperties.setProperty(Constants.PREF_DEFAULT_SCHEDULER_TIMER_KEY, String.valueOf(this.schedulerTimerInterval));
        prefProperties.setProperty(Constants.PREF_DEFAULT_WEBHOOK_DELAY_KEY, String.valueOf(this.webhookQueueDelay));
        prefProperties.setProperty(Constants.PREF_DEFAULT_RUN_STARTUP_KEY, String.valueOf(this.runningAtStartup));
        prefProperties.store(outputStream, null);
        outputStream.close();
        if (this.runningAtStartup) {
            StartupHelper.addToStartup(Utility.getRunningJarFile());
        } else {
            StartupHelper.removeFromStartup();
        }
        this.applyTheme();
    }
    
    public void applyTheme() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
        UIManager.setLookAndFeel(this.theme.getClassName());
        UIManager.put("defaultFont", new Font(fontName, Font.PLAIN, fontSize));
        refreshAllWindows();
        FlatLaf.updateUI();
    }
    
    public static void refreshAllWindows() {
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
            window.pack();
            window.revalidate();
            window.repaint();
        }
    }
    
    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public File getFile() {
        return file;
    }

    public String getFontName() {
        return fontName;
    }

    public Integer getFontSize() {
        return fontSize;
    }
    
    public Font getFont() {
        return new Font(fontName, Font.PLAIN, fontSize);
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getSchedulerTimerInterval() {
        return schedulerTimerInterval;
    }

    public Integer getWebhookQueueDelay() {
        return webhookQueueDelay;
    }

    public Boolean isRunningAtStartup() {
        return runningAtStartup;
    }

    public void setSchedulerTimerInterval(Integer schedulerTimerInterval) {
        this.schedulerTimerInterval = schedulerTimerInterval;
    }

    public void setWebhookQueueDelay(Integer webhookQueueDelay) {
        this.webhookQueueDelay = webhookQueueDelay;
    }

    public void setRunningAtStartup(Boolean runningAtStartup) {
        this.runningAtStartup = runningAtStartup;
    }
}
