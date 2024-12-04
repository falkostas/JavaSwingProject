package List;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class List extends JFrame {
    JLabel label2;
    JButton button1;
    JTextField text1;
    JTextField text2;

    //connection DB
    String driverName = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/";
    String dbName = "imagesdb";
    String userName = "root";
    String password = "hhhhhhh";
    Connection con = null;

    //JTable
    DefaultTableModel model = new DefaultTableModel();
    Container cnt = this.getContentPane();
    JTable jtbl = new JTable(model);


    public List() {
        super("List");
        setLayout(null);
        setSize(1000, 800);
        setVisible(true);

        label2 = new JLabel();
        label2.setBounds(200,400,500,250);
        add(label2);

        button1 = new JButton("GO");
        button1.setBounds(850, 400, 100, 40);
        add(button1);

        text1 = new JTextField("ID");
        text1.setBounds(850,450,100,20);
        add(text1);

        text2 = new JTextField("Size");
        text2.setBounds(850,480,100,20);
        add(text2);

        //JTable
        cnt.setLayout(new BorderLayout());


        JScrollPane pg = new JScrollPane(jtbl);
        jtbl.setFillsViewportHeight(true);
        cnt.add(pg);



        //cnt.setLayout(new BorderLayout());
        //cnt.add(jtbl.getTableHeader(), BorderLayout.PAGE_START);
        //cnt.add(jtbl, BorderLayout.CENTER);



        //model.addColumn("id");
        //model.addColumn("size");
        //model.addColumn("image");


        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url + dbName, userName, password);
            PreparedStatement pre = con.prepareStatement("SELECT * from image");
            ResultSet rs = pre.executeQuery();


            String[]title = {"id","size"};//,"image"};
            Object[]Column = new Object[3];
            byte b[];
            Blob blob;

            model = new DefaultTableModel(null,title) {
                @Override
                public Class<?> getColumnClass(int column) {
                    if (column == 2) return ImageIcon.class;
                    return Object.class;
                }
            };

            while (rs.next()) {
                Column[0] = rs.getString("id");
                Column[1] = rs.getString("size");
                /**blob = rs.getBlob("image");
                b = blob.getBytes(1,(int)blob.length());
                ImageIcon image = new ImageIcon(b);
                Image im = image.getImage();
                Image myImg = im.getScaledInstance(label2.getWidth(), label2.getHeight(), Image.SCALE_SMOOTH);
                Column[2] = new ImageIcon(myImg);
                //Column[2] = new ImageIcon(rs.getBytes("image"));
                */model.addRow(Column);

                //model.addRow(new Object[]{rs.getString(1), rs.getInt(2), rs.getBlob(3)});
            }
            jtbl.setModel(model);
            rs.close();
        } catch (Exception ex) {
            System.out.println("Could not connect to database");
        }

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Class.forName(driverName);
                    con = DriverManager.getConnection(url+dbName,userName,password);
                    PreparedStatement pre = con.prepareStatement("SELECT * from image where id='" + text1.getText() +"' and size='" + text2.getText() + "'");
                    ResultSet rs = pre.executeQuery();
                    byte b[];
                    Blob blob;
                    while (rs.next()){
                        byte[] img = rs.getBytes("IMAGE");
                        ImageIcon image = new ImageIcon(img);
                        Image im = image.getImage();
                        Image myImg = im.getScaledInstance(label2.getWidth(), label2.getHeight(), Image.SCALE_SMOOTH);    //Display Image
                        ImageIcon newImage = new ImageIcon(myImg);                                                      //Display Image
                        label2.setIcon(newImage);

                    }
                    pre.close();
                    con.close();

                }catch(Exception e2){

                }
            }
        });
    }

}





/**byte[] img = rs.getBytes("image");
 ImageIcon image = new ImageIcon(img);
 Image im = image.getImage();
 Image myImg = im.getScaledInstance(label2.getWidth(), label2.getHeight(), Image.SCALE_SMOOTH);
 ImageIcon newImage = new ImageIcon(myImg);
 label2.setIcon(newImage);
 */
//model.addRow(new Object[]{rs.getString(1), rs.getInt(2),rs.getBytes(3)});
/**class ImageRenderer extends DefaultTableCellRenderer {
 JLabel lbl = new JLabel();

 ImageIcon icon = new ImageIcon();

 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
 lbl.setText((String) value);
 lbl.setIcon(icon);
 return lbl;
 }
 }
 */

