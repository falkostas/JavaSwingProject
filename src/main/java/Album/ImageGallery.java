package Album;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.TimerTask;


public class ImageGallery extends JFrame {
    JLabel imglabel;
    JButton btnNext;
    JButton btnPrevious;

    //connection DB
    String driverName = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/";
    String dbName = "imagesdb";
    String userName = "root";
    String password = "hhhhhhh";
    Connection con = null;


    public ImageGallery(){
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,500);
        setVisible(true);

        imglabel = new JLabel();
        imglabel.setBounds(10,10,670,250);
        add(imglabel);

        btnPrevious= new JButton("Previous");
        btnPrevious.setBounds(80,400, 100, 40);
        add(btnPrevious);

        btnNext = new JButton("Next");
        btnNext.setBounds(200, 400, 100, 40);
        add(btnNext);

        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = 7;
                byte b[];
                Blob blob;
                String asta= "Asta";
                try{Class.forName(driverName);
                    con = DriverManager.getConnection(url + dbName, userName, password);
                    PreparedStatement pre = con.prepareStatement("SELECT image from image WHERE size=7 and id='Asta'        ");
                    ResultSet rs = pre.executeQuery();

                    while (rs.next()){
                        byte[] img = rs.getBytes("IMAGE");
                        ImageIcon image = new ImageIcon(img);
                        Image im = image.getImage();
                        Image myImg = im.getScaledInstance(imglabel.getWidth(), imglabel.getHeight(), Image.SCALE_SMOOTH);    //Display Image
                        ImageIcon newImage = new ImageIcon(myImg);                                                      //Display Image
                        imglabel.setIcon(newImage);                                                                        //Display Image
                    }




                }catch (Exception eNext){
                    System.out.println("F");

                }
            }
        });
    }
}
