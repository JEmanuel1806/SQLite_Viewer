

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.Vector;


public class Database {


    Connection conn = null;

    public boolean connect(String filename, JFileChooser fileChooser) {


        try {

            String url = "jdbc:sqlite:" + fileChooser.getSelectedFile().getAbsolutePath();
            if (fileChooser.getSelectedFile().getAbsolutePath().contains(".db")) {
                conn = DriverManager.getConnection(url);
                System.out.println("Connection to SQLite database " + filename + " has been established.");
                return true;
            } else {
                JOptionPane.showMessageDialog(new Frame(), "This is not a SQLite database file!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            JOptionPane.showMessageDialog(new Frame(), "Connection failed.");
            return false;
        }


    }

    public void getTables(JComboBox comboBox) throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        DatabaseMetaData md = conn.getMetaData();
        rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            comboBox.addItem(rs.getString(3));
        }

    }

    public void execute(String query, JTable table) {

        try {
            String sql = query;

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();


            DefaultTableModel model = new DefaultTableModel(new String[]{"Class Name", "Home work", "Due Date"}, 0);

            // names of columns
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            // data of the table
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

            DefaultTableModel defaultTableModel = new DefaultTableModel(data, columnNames);
            table.setModel(defaultTableModel);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(1200, 500));

            JOptionPane.showMessageDialog(null, scrollPane);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new Frame(), "Incorrect SQL Syntax!");
        }
    }

}





