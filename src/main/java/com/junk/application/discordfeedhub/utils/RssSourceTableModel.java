package com.junk.application.discordfeedhub.utils;

/**
 *
 * @author elmerhd
 */
import com.junk.application.discordfeedhub.model.RssSource;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RssSourceTableModel extends AbstractTableModel {
    
    public RssSourceTableModel() {
        
    }

    private final String[] columns = {
        "ID", "Title", "Website URL", "RSS URL", "Discord Webhook", "Enabled"
    };

    private List<RssSource> data = new ArrayList<>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        RssSource r = data.get(row);

        return switch (col) {
            case 0 -> r.getId();
            case 1 -> r.getTitle();
            case 2 -> r.getWebsiteUrl();
            case 3 -> r.getRssUrl();
            case 4 -> r.getDiscordWebhookUrl();
            case 5 -> r.isEnabled();
            default -> null;
        };
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return col == 5 ? Boolean.class : String.class;
    }

    public void setData(List<RssSource> list) {
        this.data = list;
        fireTableDataChanged();
    }

    public RssSource getAt(int row) {
        return data.get(row);
    }
}
