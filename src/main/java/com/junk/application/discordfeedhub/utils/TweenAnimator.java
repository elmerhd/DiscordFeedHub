package com.junk.application.discordfeedhub.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

/**
 * A tween manipulator manipulates every tween on swing and awt application(s) 
 * whats inside?
 * well actually a thread that updates {@link aurelienribon.tweenengine.TweenManager}
 * in every second of time.
 * @author elmerhd
 */
public class TweenAnimator{
    private TweenManager tweenManager;
    private boolean running = false;
    /**
     * Register all the attributes of the android components.
     * @param clasz the class to register.
     * @param tweenAccessor the accessor of the class that has been register.
     * @param tm the tweenmanager.
     */
    public  void registerClassAccessor(Class<?>clasz,TweenAccessor<?> tweenAccessor,TweenManager tm){
        Tween.registerAccessor(clasz, tweenAccessor);
        tweenManager = tm;
    }
    /**
     * Sets the attrib limits if you have a combination of XYWH just set 4
     * @param attribLimits the attribute limits.
     */
    public void setCombineAttrLimits(int attribLimits){
        Tween.setCombinedAttributesLimit(attribLimits);
    }
    /**
     * Starts the thread.
     */
    public void start() {
        running = true;
        
        Runnable runnable = () -> {
            long lastMillis = System.currentTimeMillis();
            long deltaLastMillis = System.currentTimeMillis();

            while (running) {
                long newMillis = System.currentTimeMillis();
                long sleep = 20 - (newMillis - lastMillis);
                lastMillis = newMillis;

                if (sleep > 1)
                    try {Thread.sleep(sleep);} catch (InterruptedException ex) {}

                long deltaNewMillis = System.currentTimeMillis();
                final float delta = (deltaNewMillis - deltaLastMillis) / 1000f;

                try {
                    SwingUtilities.invokeAndWait(() -> {
                        tweenManager.update(delta);
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                }

                deltaLastMillis = newMillis;
            }
        };

        new Thread(runnable).start();
    }
    
    /**
    * Stops the thread
    */
    public void stop() {
        running = false;
    }
    
    /**
    * Returns if the thread is running.
    * @return true if running else if not.
    */
    public boolean isRunning(){
        return running;
    }
}