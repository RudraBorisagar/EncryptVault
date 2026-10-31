package filemanager;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * EncryptVault - My Files
 * Displays all files for the logged in user.
 */
public class MyFiles extends JFrame {

    private final String username;
    private final String aesKey;

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public MyFiles(String username, String aesKey) {
        this.username = username;
        this.aesKey = aesKey;

        setTitle("EncryptVault - My Files");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color bg = new Color(18,18,18);

        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBackground(bg);
        root.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel top = new JPanel(new BorderLayout(10,0));
        top.setBackground(bg);

        JLabel title = new JLabel("My Files");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,22));

        searchField = new JTextField();

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadFiles(searchField.getText()));

        top.add(title,BorderLayout.WEST);
        top.add(searchField,BorderLayout.CENTER);
        top.add(refresh,BorderLayout.EAST);

        model = new DefaultTableModel(
            new String[]{"File Name","Stored Name","Size (Bytes)"},0){
            @Override
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        JPanel bottom = new JPanel();
        JButton read = new JButton("Read");
        JButton download = new JButton("Download");
        JButton delete = new JButton("Delete");
        JButton close = new JButton("Close");

        read.addActionListener(e -> FileRead.read(username,aesKey));
        download.addActionListener(e -> FileDownload.download(username,aesKey));
        delete.addActionListener(e -> {
            FileDelete.delete(username);
            loadFiles(searchField.getText());
        });
        close.addActionListener(e -> dispose());

        bottom.add(read);
        bottom.add(download);
        bottom.add(delete);
        bottom.add(close);

        root.add(top,BorderLayout.NORTH);
        root.add(scroll,BorderLayout.CENTER);
        root.add(bottom,BorderLayout.SOUTH);

        add(root);

        searchField.addActionListener(e -> loadFiles(searchField.getText()));

        loadFiles("");
        setVisible(true);
    }

    private void loadFiles(String keyword){

        model.setRowCount(0);

        try(Connection con = DBConnection.getConnection()){

            PreparedStatement user = con.prepareStatement(
                    "SELECT id FROM users WHERE username=?");
            user.setString(1, username);

            ResultSet ur = user.executeQuery();

            if(!ur.next()) return;

            int userId = ur.getInt("id");

            PreparedStatement ps = con.prepareStatement(
                "SELECT original_name,stored_name,file_size " +
                "FROM user_files " +
                "WHERE user_id=? AND original_name LIKE ? " +
                "ORDER BY id DESC");

            ps.setInt(1,userId);
            ps.setString(2,"%"+keyword+"%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("original_name"),
                    rs.getString("stored_name"),
                    rs.getLong("file_size")
                });
            }

        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,ex.getMessage());
        }
    }
}
