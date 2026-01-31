package com.junk.application.discordfeedhub.panel;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import com.junk.application.discordfeedhub.utils.ComponentAccessor;
import com.junk.application.discordfeedhub.utils.DiscordFeedHubLogger;
import com.junk.application.discordfeedhub.utils.ExtendedPanel;
import com.junk.application.discordfeedhub.utils.TweenAnimationManager;
import com.junk.application.discordfeedhub.utils.Utility;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author elmerhd
 */
public class AboutPanel extends ExtendedPanel {

    /**
     * Creates new form AboutPanel
     */
    public AboutPanel() {
        super();
    }
    
    @Override
    public void onInitializedPanel() {
        try {
            DiscordFeedHubLogger.getLogger(AboutPanel.class.getName()).log(Level.INFO, "Initializing Components");
            initComponents();
            setupInfo();
            startAnimation();
        } catch (IOException ex) {
            DiscordFeedHubLogger.getLogger(AboutPanel.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }
    
    public void startAnimation() {
        DiscordFeedHubLogger.getLogger(AboutPanel.class.getName()).log(Level.INFO, "Starting Animation");
        TweenCallback callback = (int x, BaseTween<?> bt) -> {
            int snowCount = 20;
            for (int i = 0; i < snowCount; i++) {
                JLabel snow = createSnowflake(random(4, 8));
                add(snow);
                setComponentZOrder(snow, 0); // behind logo/text
                animateSnowflake(snow);
            }
        };
        
        Timeline.createSequence()
            .beginParallel()
                .push(Tween.set(labelLogo, ComponentAccessor.POSITION_X).target(-1000))
                .push(Tween.set(scrollPaneInfo, ComponentAccessor.POSITION_X).target(scrollPaneInfo.getWidth() + 1000))
            .end()
            .beginParallel()
                .push(Tween.to(labelLogo, ComponentAccessor.POSITION_X, 1f).target(5))
                .push(Tween.to(scrollPaneInfo, ComponentAccessor.POSITION_X, 1f).target(5))
            .end()
            .setCallback(callback)
            .start(TweenAnimationManager.getTweenManager());
    }
    
    private JLabel createSnowflake(int size) {
        JLabel snow = new JLabel();
        snow.setOpaque(false);
        snow.setSize(size, size);

        snow.setIcon(new ImageIcon(
            new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB) {{
                Graphics2D g = createGraphics();
                g.setColor(new Color(255, 255, 255, 180));
                g.fillOval(0, 0, size, size);
                g.dispose();
            }}
        ));

        return snow;
    }
    
    private void animateSnowflake(JLabel snow) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int startX = random(0, panelWidth);
        int endX = startX + random(-40, 40);

        int startY = -random(20, 100);
        int endY = panelHeight + 50;

        float duration = randomFloat(6f, 12f);

        snow.setLocation(startX, startY);

        Timeline.createSequence()
            .push(Tween.to(snow, ComponentAccessor.POSITION_XY, duration)
                .target(endX, endY)
                .ease(Linear.INOUT))
            .setCallback((type, source) -> animateSnowflake(snow)) // loop
            .start(TweenAnimationManager.getTweenManager());
    }
    
    public static int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static float randomFloat(float min, float max) {
        return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
    }
    
    public void setupInfo() throws IOException {
        DiscordFeedHubLogger.getLogger(AboutPanel.class.getName()).log(Level.INFO, "Setting up application information");
        String htmlContent = loadHtmlContent();
        editorPaneInfo.setText(htmlContent);
        editorPaneInfo.setCaretPosition(0);
        editorPaneInfo.addHyperlinkListener(hyperlinkListener);
    }
    
    public HyperlinkListener hyperlinkListener = (HyperlinkEvent e) -> {
        if (e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
            try {
                DiscordFeedHubLogger.getLogger(AboutPanel.class.getName()).log(Level.INFO, () -> "Link clicked : " + e.getURL().toString());
                Desktop.getDesktop().browse(new URI(e.getURL().toString()));
            } catch (Exception ex) {
                DiscordFeedHubLogger.getLogger(AboutPanel.class.getName()).log(Level.SEVERE, (String) null, ex);
            }
        }
    };
    
    private String loadHtmlContent() throws IOException {
        String html = "";
        Properties applicationProperty = Utility.getApplicationProperty();
        try (InputStream is = getClass().getResourceAsStream(Utility.getAboutInfoHTMLFile());
            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            html = scanner.useDelimiter("\\A").next();
        } catch (IOException ex) {
            html = "<html><body><p>Error loading About content.</p></body></html>";
            System.getLogger(AboutPanel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        // Replace placeholders with actual system info
        String javaVersion = System.getProperty("java.version") + " (" + System.getProperty("java.vm.name") + ")";
        String osInfo = System.getProperty("os.name") + " (" + System.getProperty("os.version") + ") on " + System.getProperty("os.arch");
        String appVersion = applicationProperty.getProperty("app.version");
        html = html.replace("${appVersion}", appVersion)
                   .replace("${javaVersion}", javaVersion)
                   .replace("${osInfo}", osInfo)
                   .replace("${userDir}", Utility.getApplicationFolder());
        
        return html;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPaneInfo = new javax.swing.JScrollPane();
        editorPaneInfo = new javax.swing.JEditorPane();
        labelLogo = new javax.swing.JLabel();

        editorPaneInfo.setEditable(false);
        editorPaneInfo.setContentType("text/html"); // NOI18N
        scrollPaneInfo.setViewportView(editorPaneInfo);

        labelLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/junk/application/discordfeedhub/logo-256.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneInfo)
                    .addComponent(labelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(labelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(scrollPaneInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane editorPaneInfo;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JScrollPane scrollPaneInfo;
    // End of variables declaration//GEN-END:variables
}
