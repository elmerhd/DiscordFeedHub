package com.junk.application.discordfeedhub.utils;

import java.awt.Component;

/**
 * The component accessor access the component class values and attributes
 * @author elmerhd
 */
public class ComponentAccessor implements aurelienribon.tweenengine.TweenAccessor<Component>{
    /**
     * the position X
     */
    public static final int POSITION_X = 0;
    /**
     * the position Y
     */
    public static final int POSITION_Y = 1;
    /**
     * the position X and Y
     */
    public static final int POSITION_XY = 2;
    /**
     * the width
     */
    public static final int LAYOUT_W = 3;
    /**
     * the height
     */
    public static final int LAYOUT_H = 4;
    /**
     * the width and height
     */
    public static final int LAYOUT_WH = 5;
    /**
     * gets all the values of the view class
     * @param t the view component target
     * @param tweenType the tweentype
     * @param retVal  the return values
     * @return the values returned
     */
    @Override
    public int getValues(Component t, int tweenType, float[] retVal) {
        switch(tweenType){
            case POSITION_X: retVal[0] = t.getX();return 1;
            case POSITION_Y: retVal[0] = t.getY();return 1;
            case POSITION_XY: retVal[0] = t.getX();
                retVal[1] = t.getY();return 2;
            case LAYOUT_W: retVal[0] = t.getWidth();return 1;
            case LAYOUT_H: retVal[0] = t.getHeight();return 1;
            case LAYOUT_WH: retVal[0] = t.getWidth();
                retVal[1] = t.getHeight();
                return 2;
                default:assert false;return 0;
        }
    }
    /**
     * Sets the values of the view
     * @param t the target value  
     * @param tweenType the tweentype
     * @param newVal  the newValue
     */
    @Override
    public void setValues(Component t, int tweenType, float[] newVal) {
        switch(tweenType){
            case POSITION_X:t.setBounds((int) newVal[0], t.getY(), t.getWidth(), t.getHeight());break;
            case POSITION_Y:t.setBounds(t.getX(),(int) newVal[0], t.getWidth(), t.getHeight());break;
            case POSITION_XY: t.setBounds((int)newVal[0], (int)newVal[1], t.getWidth(), t.getHeight());break;
            case LAYOUT_W:t.setBounds(t.getX(), t.getY(), (int) newVal[0], t.getHeight());break;
            case LAYOUT_H:t.setBounds(t.getX(), t.getY(), t.getWidth(), (int) newVal[0]);break;
            case LAYOUT_WH:t.setBounds(t.getX(), t.getY(), (int)newVal[0], (int) newVal[1]);break;
                default:assert false;break;
        }
    }
    
}