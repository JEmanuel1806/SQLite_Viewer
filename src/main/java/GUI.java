import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;

public class GUI extends JFrame {

    Database database = new Database();

    public GUI() {

        setTitle("SQLite Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.white);


        JTextField textField = new JTextField();
        textField.setName("FileNameTextField");
        textField.setBounds(10, 50, 550, 30);


        JTextArea textArea = new JTextArea();
        textArea.setName("QueryTextArea");
        textArea.setBounds(10, 150, 500, 100);
        textArea.setEnabled(false);

        JComboBox comboBox = new JComboBox();
        comboBox.setName("TablesComboBox");
        comboBox.setBackground(Color.white);
        comboBox.setBounds(10, 100, 550, 30);
        comboBox.addActionListener(e -> switchingElement(e, comboBox, textArea));
        comboBox.setEnabled(true);


        JTable table = new JTable();
        table.setBounds(10, 270, 500, 150);
        table.setName("Table");

        JButton button2 = new JButton("Execute");
        button2.setName("ExecuteQueryButton");
        button2.setBounds(570, 150, 95, 30);
        button2.addActionListener(e -> {
            try {
                execute(e, textArea, table);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        button2.setEnabled(false);

        JButton button = new JButton("Open");
        button.setName("OpenFileButton");
        button.setBounds(570, 50, 95, 30);
        button.addActionListener(e -> {
            try {
                open(e, comboBox, button2, textArea);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });


        add(button);
        add(button2);
        add(comboBox);
        add(textArea);
        add(textField);
        add(table);


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    private void switchingElement(ActionEvent e, JComboBox comboBox, JTextArea textArea) {
        textArea.setText("SELECT * FROM " + comboBox.getSelectedItem() + ";");
    }

    private void open(ActionEvent e,  JComboBox comboBox, JButton button2, JTextArea textArea) throws SQLException {

        boolean connected = false;

        comboBox.removeAllItems();

        JFileChooser fileChooser = new JFileChooser();

        int response = fileChooser.showOpenDialog(null);

        if(response == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            connected = database.connect(file.getName(), fileChooser);
        }


        if (connected) {
            comboBox.setEnabled(true);
            database.getTables(comboBox);

            button2.setEnabled(true);
            textArea.setEnabled(true);
        } else {
            comboBox.setEnabled(false);
            button2.setEnabled(false);
            textArea.setEnabled(false);
        }


    }

    private void execute(ActionEvent e, JTextArea textArea, JTable table) throws SQLException {
        String query = textArea.getText();
        database.execute(query, table);

    }


}