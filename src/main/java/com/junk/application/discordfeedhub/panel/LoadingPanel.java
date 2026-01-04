package com.junk.application.discordfeedhub.panel;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Quart;
import com.junk.application.discordfeedhub.ui.Main;
import com.junk.application.discordfeedhub.utils.ComponentAccessor;
import com.junk.application.discordfeedhub.utils.DiscordFeedHubLogger;
import com.junk.application.discordfeedhub.utils.TweenAnimationManager;
import com.junk.application.discordfeedhub.utils.Utility;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author elmerhd
 */
public class LoadingPanel extends javax.swing.JPanel {
    
    private JPanel parentPanel;
    private Main mainUI;
    
    private final java.util.List<JLabel> logoSlices = new java.util.ArrayList<>();
    private BufferedImage logoImage;
    // will use random to make it dynamic everytime the app loads
    
    private int sliceRows = ThreadLocalRandom.current().nextInt(4, 26);
    private int sliceCols = ThreadLocalRandom.current().nextInt(4, 26);
    
    /**
     * Creates new form LoadingPanel
     */
    public LoadingPanel(Main mainUI) {
        initComponents();
        this.mainUI = mainUI;
        this.parentPanel = mainUI.mainPanel;
        initComponentsPosition();
        startAnimations();
    }
    
    public void initComponentsPosition() {
        this.setPreferredSize(parentPanel.getPreferredSize());
        labelAppLogo.setPreferredSize(new Dimension(256, 256));
    }
    
    public void startAnimations() {
        
        float appLogoCenterX = (float) ((this.parentPanel.getWidth() / 2) - (labelAppLogo.getPreferredSize().getWidth() / 2) ) ;
        float appLogoCenterY = (float) ((this.parentPanel.getHeight() / 2) - (labelAppLogo.getPreferredSize().getHeight() / 2));
        
        TweenCallback animationDone = (type, source) ->
        SwingUtilities.invokeLater(this::explodeLogo);
        
        Timeline.createSequence()
            .beginParallel()
                .push(Tween.set(labelAppLogo, ComponentAccessor.POSITION_X).target(appLogoCenterX))
            .end()
            .delay(2)
            // Fast drop
            .beginSequence()
                .push(Tween.to(labelAppLogo, ComponentAccessor.POSITION_XY, 1.5f)
                    .target(appLogoCenterX, appLogoCenterY)
                    .ease(Bounce.OUT))
            .end()
            .setCallback(animationDone)
            .start(TweenAnimationManager.getTweenManager());
    }
    /**
     * slices the image
     */
    private void explodeLogo() {
        try {
            labelAppLogo.setVisible(false);

            logoImage = ImageIO.read(Utility.getLoadingImageURL());

            int pw = logoImage.getWidth() / sliceCols;
            int ph = logoImage.getHeight() / sliceRows;

            int baseX = labelAppLogo.getX();
            int baseY = labelAppLogo.getY();

            // Create slices
            for (int y = 0; y < sliceRows; y++) {
                for (int x = 0; x < sliceCols; x++) {

                    BufferedImage piece = logoImage.getSubimage(
                            x * pw, y * ph, pw, ph
                    );

                    JLabel slice = new JLabel(new ImageIcon(piece));
                    slice.setBounds(
                            baseX + x * pw,
                            baseY + y * ph,
                            pw,
                            ph
                    );

                    logoSlices.add(slice);
                    add(slice);
                }
            }

            repaint();
            animateExplosion(baseX, baseY);

        } catch (Exception ex) {
            DiscordFeedHubLogger.getLogger(LoadingPanel.class.getName()).log(Level.SEVERE, "No row selected from the table!", ex);
        }
    }
    
    private void animateExplosion(int baseX, int baseY) {

        Timeline explode = Timeline.createParallel();
        Timeline reform  = Timeline.createParallel();

        int pw = logoImage.getWidth() / sliceCols;
        int ph = logoImage.getHeight() / sliceRows;

        int i = 0;
        for (JLabel slice : logoSlices) {

            int col = i % sliceCols;
            int row = i / sliceCols;

            int targetX = baseX + col * pw;
            int targetY = baseY + row * ph;

            int offX = targetX + (int) (Math.random() * 500 - 200);
            int offY = targetY + (int) (Math.random() * 500 - 200);

            explode.push(
                    Tween.to(slice, ComponentAccessor.POSITION_XY, 2f)
                            .target(offX, offY)
                            .ease(Back.OUT)
            );

            reform.push(
                    Tween.to(slice, ComponentAccessor.POSITION_XY, 1.2f)
                            .target(targetX, targetY)
                            .ease(Quart.OUT)
            );

            i++;
        }

        Timeline.createSequence()
                .push(explode)
                .delay(0.2f)
                .push(reform)
                .setCallback((t, s) -> {
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                            mainUI.showDataPanel();
                        } catch (InterruptedException ignored) {
                        }
                    }).start();
                })
                .start(TweenAnimationManager.getTweenManager());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelAppLogo = new javax.swing.JLabel();

        setLayout(null);

        labelAppLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAppLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/junk/application/discordfeedhub/logo-256.png"))); // NOI18N
        labelAppLogo.setPreferredSize(new java.awt.Dimension(128, 128));
        add(labelAppLogo);
        labelAppLogo.setBounds(5, -500, 256, 256);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelAppLogo;
    // End of variables declaration//GEN-END:variables
}
