package com.junk.application.discordfeedhub.panel;

import com.junk.application.discordfeedhub.model.UpdateInfo;
import com.junk.application.discordfeedhub.utils.Constants;
import com.junk.application.discordfeedhub.utils.DiscordFeedHubLogger;
import com.junk.application.discordfeedhub.utils.ExtendedPanel;
import com.junk.application.discordfeedhub.utils.GitHubUpdateChecker;
import com.junk.application.discordfeedhub.utils.JarUpdateInstaller;
import com.junk.application.discordfeedhub.utils.StartupHelper;
import com.junk.application.discordfeedhub.utils.Utility;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.logging.Level;

/**
 *
 * @author elmerhd
 */
public class CheckUpdatePanel extends ExtendedPanel {
    
    private boolean checkingUpdates = false;
    private UpdateInfo info = null;
    
    /**
     * Creates new form CheckUpdatePanel
     */
    public CheckUpdatePanel() throws IOException {
        super();
        versionCheckerThread.start();
    }
    
    @Override
    public void onInitializedPanel() {
        DiscordFeedHubLogger.getLogger(CheckUpdatePanel.class.getName()).log(Level.INFO, "Initializing Components");
        initComponents();
        setUpdateInfo(null);
    }
    
    public Thread versionCheckerThread = new Thread(() -> {
        try {
            setCheckingUpdates(true);
            this.labelStatus.setText("Checking for updates...");
            String currentVersion = Utility.getApplicationProperty().getProperty("app.version");
            info = GitHubUpdateChecker.checkForUpdate(currentVersion);
            if (info != null) {
                DiscordFeedHubLogger.getLogger(CheckUpdatePanel.class.getName()).log(Level.INFO, () -> info.version() + " is available!!!");
                this.labelStatus.setText("An update is available!");
                this.buttonDownloadRestart.setEnabled(hasDownloadURL());
            } else {
                this.labelStatus.setText("App is up to date!");
            }
            setCheckingUpdates(false);
            setUpdateInfo(info);
        } catch (IOException ex) {
            DiscordFeedHubLogger.getLogger(CheckUpdatePanel.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    });
    
    public Thread exitDelayThread = new Thread(() -> {
        try {
            Thread.sleep(Duration.ofSeconds(1));
            System.exit(0);
        } catch (InterruptedException ex) {
            DiscordFeedHubLogger.getLogger(CheckUpdatePanel.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    });
    
    public Thread downloadThread = new Thread(() -> {
        try {
            File tempJar = JarUpdateInstaller.downloadJar(info , (long downloaded, long total) -> {
            updateProgress(downloaded, total);
            });
            if (StartupHelper.removeFromStartup() && Utility.getPreference().isRunningAtStartup()) {
                StartupHelper.addToStartup(tempJar);
            }

            if (tempJar != null) {

                new ProcessBuilder(
                        "javaw",
                        "-jar",
                        tempJar.getAbsolutePath(),
                        Constants.STARTUP_ARGS_DELETE + Utility.getRunningJarFile().getAbsolutePath(),
                        Constants.STARTUP_ARGS_UPDATE
                ).start();

                exitDelayThread.start();
            }
        } catch (Exception ex) {
            DiscordFeedHubLogger.getLogger(CheckUpdatePanel.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    });
    
    public void setUpdateInfo(UpdateInfo info) {
        if (info != null) {
            labelVersion.setText("Version : "+info.version());
            labelDate.setText("Date : "+info.publishedDate());
            labelFileSize.setText("File Size : "+Utility.formatFileSize(info.fileSize()));
            labelDownloads.setText("Downloads : "+info.downloadCount());
        } else {
            labelVersion.setText(" ");
            labelDate.setText(" ");
            labelFileSize.setText(" ");
            labelDownloads.setText(" ");
        }
    }
    
    public boolean isCheckingUpdates() {
        return checkingUpdates;
    }
    
    public void setCheckingUpdates(boolean checking) {
        this.checkingUpdates = checking;
        progressBarStatus.setVisible(checking);
        if (checking) {
            DiscordFeedHubLogger.getLogger(CheckUpdatePanel.class.getName()).log(Level.INFO, "Checking for new version");
        } else {
            DiscordFeedHubLogger.getLogger(CheckUpdatePanel.class.getName()).log(Level.INFO, "Checking done");
        }
    }
    
    public void updateProgress(long downloaded, long total) {
        progressBarStatus.setVisible(true);
        progressBarStatus.setIndeterminate(false);
        int percent = (int) ((downloaded * 100) / total);
        progressBarStatus.setValue(percent);
        progressBarStatus.setString(percent + "%");
        String infoProgressText = "Downloading . . . " +Utility.formatFileSize(downloaded)+" / " +Utility.formatFileSize(total);
        DiscordFeedHubLogger.getLogger(CheckUpdatePanel.class.getName()).log(Level.INFO, infoProgressText);
        labelStatus.setText(infoProgressText);
        if (percent == 100) {
            progressBarStatus.setVisible(false);
        }
    }
    
    public boolean hasDownloadURL() {
        return info != null && info.downloadUrl() != null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBarStatus = new javax.swing.JProgressBar();
        buttonClose = new javax.swing.JButton();
        buttonDownloadRestart = new javax.swing.JButton();
        labelStatus = new javax.swing.JLabel();
        labelVersion = new javax.swing.JLabel();
        labelDate = new javax.swing.JLabel();
        labelFileSize = new javax.swing.JLabel();
        labelDownloads = new javax.swing.JLabel();

        progressBarStatus.setIndeterminate(true);
        progressBarStatus.setVisible(isCheckingUpdates());

        buttonClose.setIcon(Utility.getCloseIcon());
        buttonClose.setText("Close");
        buttonClose.addActionListener(this::buttonCloseActionPerformed);

        buttonDownloadRestart.setIcon(Utility.getDownloadIcon());
        buttonDownloadRestart.setText("Download & Restart");
        buttonDownloadRestart.setEnabled(hasDownloadURL());
        buttonDownloadRestart.addActionListener(this::buttonDownloadRestartActionPerformed);

        labelStatus.setText(" ");

        labelVersion.setText("Version : ");

        labelDate.setText("Date :");

        labelFileSize.setText("File Size : ");

        labelDownloads.setText("Downloads :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(289, Short.MAX_VALUE)
                .addComponent(buttonDownloadRestart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonClose)
                .addGap(5, 5, 5))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBarStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelVersion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelFileSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelDownloads, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(labelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(labelVersion)
                .addGap(10, 10, 10)
                .addComponent(labelDate)
                .addGap(10, 10, 10)
                .addComponent(labelFileSize)
                .addGap(10, 10, 10)
                .addComponent(labelDownloads)
                .addGap(10, 10, 10)
                .addComponent(progressBarStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonClose)
                    .addComponent(buttonDownloadRestart))
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCloseActionPerformed
        closeParentDialog();
    }//GEN-LAST:event_buttonCloseActionPerformed

    private void buttonDownloadRestartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownloadRestartActionPerformed
        buttonDownloadRestart.setEnabled(false);
        progressBarStatus.setIndeterminate(false);
        progressBarStatus.setStringPainted(true);
        progressBarStatus.setVisible(true);
        downloadThread.start();
    }//GEN-LAST:event_buttonDownloadRestartActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClose;
    private javax.swing.JButton buttonDownloadRestart;
    private javax.swing.JLabel labelDate;
    private javax.swing.JLabel labelDownloads;
    private javax.swing.JLabel labelFileSize;
    private javax.swing.JLabel labelStatus;
    private javax.swing.JLabel labelVersion;
    private javax.swing.JProgressBar progressBarStatus;
    // End of variables declaration//GEN-END:variables
}
