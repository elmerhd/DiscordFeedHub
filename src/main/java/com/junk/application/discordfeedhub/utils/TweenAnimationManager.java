package com.junk.application.discordfeedhub.utils;

import aurelienribon.tweenengine.TweenManager;
import java.awt.Component;

/**
 *
 * @author elmerhd
 */
public class TweenAnimationManager {
    
    private static TweenManager tweenManager = new TweenManager();
    
    private static TweenAnimator tweenAnimator = new TweenAnimator();
    
    public static void registerTweenAccessors() {
        tweenAnimator.registerClassAccessor(Component.class, new ComponentAccessor(), tweenManager);
        tweenAnimator.setCombineAttrLimits(4);
        tweenAnimator.start();
    }
    
    
    public static TweenAnimator getTweenAnimator() {
        return tweenAnimator;
    }

    public static TweenManager getTweenManager() {
        return tweenManager;
    }
}
