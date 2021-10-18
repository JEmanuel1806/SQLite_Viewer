import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.Objects;

public class GUI extends JFrame {


    Database database = new Database();


    public GUI() {


        setTitle("SQLite Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("server.png")));
        setIconImage(logo.getImage());

        JLabel label = new JLabel();
        label.setText("Choose database file:");
        label.setBounds(10, 20, 550, 30);

        JLabel label2 = new JLabel();
        label2.setText("Choose table from database:");
        label2.setBounds(10, 75, 550, 30);


        JTextField textField = new JTextField();
        textField.setName("FileNameTextField");
        textField.setEnabled(false);
        textField.setBounds(10, 50, 550, 30);


        JTextArea textArea = new JTextArea();
        textArea.setName("QueryTextArea");
        textArea.setBounds(10, 150, 500, 100);
        textArea.setEnabled(false);

        JComboBox comboBox = new JComboBox();
        comboBox.setName("TablesComboBox");
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
        add(label);
        add(label2);


        setVisible(true);
    }

    private void switchingElement(ActionEvent e, JComboBox comboBox, JTextArea textArea) {
        textArea.setText("SELECT * FROM " + comboBox.getSelectedItem() + ";");
    }

    private void open(ActionEvent e, JComboBox comboBox, JButton button2, JTextArea textArea) throws SQLException {

        boolean connected = false;

        comboBox.removeAllItems();

        JFileChooser fileChooser = new JFileChooser();


        FileNameExtensionFilter dbfilter = new FileNameExtensionFilter(
                "sqlite database files (*.db)", "db");

        fileChooser.setFileFilter(dbfilter);

        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
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