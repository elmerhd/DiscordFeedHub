package com.junk.application.discordfeedhub.utils;

/**
 *
 * @author elmerhd
 */
public class Theme {
    private String name;
    private String className;

    public Theme(String name, String className) {
        this.name = name;
        this.className = className;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
