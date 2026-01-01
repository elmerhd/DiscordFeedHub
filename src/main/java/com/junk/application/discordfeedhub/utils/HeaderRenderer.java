/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.junk.application.discordfeedhub.utils;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author elmer
 */
public class HeaderRenderer extends JLabel implements TableCellRenderer {
 
    public HeaderRenderer() {
        setHorizontalAlignment(SwingConstants.LEFT);
    }
     
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((String)value);
        return this;
    }
 
}
