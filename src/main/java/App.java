import Album.ImageGallery;
import List.List;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class App extends JFrame {
    JButton button ;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;
    JButton button7;
    JLabel label;
    JTextField textID;
    JTextField textSize;
    JTextField textLong;
    JTextField textLat;
    String s;

    //connection DB
    String driverName = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/";
    String dbName = "imagesdb";
    String userName = "root";
    String password = "hhhhhhh";
    Connection con = null;

    public App() {
        super("Save Image and Display Data");

        button = new JButton("Add");
        button.setBounds(200,300,100,40);

        button2 = new JButton("Browse");
        button2.setBounds(80, 300, 100, 40);

        button3 = new JButton("Data");
        button3.setBounds(80, 350, 100, 40);

        button4 = new JButton("Retrieve");
        button4.setBounds(80,400, 100, 40);

        button5 = new JButton("List");
        button5.setBounds(200,350 ,100, 40);

        button7 = new JButton("Album");
        button7.setBounds(200, 400, 100, 40);

        textID = new JTextField("ID");
        textID.setBounds(370,310,100,20);

        textSize = new JTextField("Size");
        textSize.setBounds(370,340,100,20);

        textLong = new JTextField("Longitude");
        textLong.setBounds(370, 370,100, 20);

        textLat = new JTextField("Latitude");
        textLat.setBounds(370, 400, 100,20);

        label = new JLabel();
        label.setBounds(10,10,670,250);




        //button2 Browse
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg","gif","png");
                fileChooser.addChoosableFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                //int result = fileChooser.showSaveDialog(null);

                if(returnValue == JFileChooser.APPROVE_OPTION){
                    File selectedFile = fileChooser.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();
                    label.setIcon(ResizeImage(path));
                    s = path;
                }
                else if(returnValue == JFileChooser.CANCEL_OPTION){
                    System.out.println("No Data");
                }
            }

        });

        //button Add
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Class.forName(driverName);
                    con = DriverManager.getConnection(url+dbName,userName,password);
                    Statement st = con.createStatement();

                    InputStream is = new FileInputStream(new File(s));

                    PreparedStatement pre = con.prepareStatement("INSERT INTO Image VALUES(?,?,?)");

                    pre.setString(1,textID.getText());
                    pre.setInt(2, Integer.parseInt(textSize.getText()));
                    pre.setBinaryStream(3,is);
                    pre.executeUpdate();
                    System.out.println("Successfully inserted the file into the database!");

                    pre.close();
                    con.close();
                }catch (Exception e1){
                    System.out.println(e1.getMessage());
                }
            }
        });

        //button3 Data
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //detecting the file type
                BodyContentHandler handler = new BodyContentHandler();
                Metadata metadata = new Metadata();
                FileInputStream inputstream = null;
                try {
                    inputstream = new FileInputStream(new File(s));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                ParseContext pcontext = new ParseContext();

                //Jpeg Parse
                JpegParser JpegParser = new JpegParser();
                try {
                    JpegParser.parse(inputstream, handler, metadata,pcontext);
                } catch (IOException | SAXException | TikaException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("Contents of the document:" + handler.toString());
                System.out.println("Metadata of the document:");
                String[] metadataNames = metadata.names();

                for(String name : metadataNames) {
                    System.out.println(name + ": " + metadata.get(name));
                }
            }
        });

        //ButtonRetrieve
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Class.forName(driverName);
                    con = DriverManager.getConnection(url+dbName,userName,password);

                    File file = new File("C:\\Users\\Kostas\\Desktop\\Μαθήματα\\Μεθοδολογία Προγραμματισμού\\TheOnlyProject\\TheOnlyProject\\src\\main\\java\\Photos\\"+textID.getText()+"_"+textSize.getText()+".jpg");
                    FileOutputStream fos = new FileOutputStream(file);

                    byte b[];
                    Blob blob;

                    PreparedStatement pre = con.prepareStatement("SELECT * from image where id='" + textID.getText() +"' and size='" + textSize.getText() + "'");
                    ResultSet rs = pre.executeQuery();
                    while(rs.next())
                    {
                        /**
                        byte[] img = rs.getBytes("image");
                        ImageIcon image = new ImageIcon(img);
                        Image im = image.getImage();
                        Image myImg = im.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
                        ImageIcon newImage = new ImageIcon(myImg);
                        label.setIcon(newImage);
                         */

                        blob = rs.getBlob("image");
                        b = blob.getBytes(1,(int)blob.length());
                        ImageIcon image = new ImageIcon(b);
                        Image im = image.getImage();
                        Image myImg = im.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
                        ImageIcon newImage = new ImageIcon(myImg);
                        fos.write(b);

                        System.out.println("The image saved in Folder Photos\\"+textID.getText()+"_"+textSize.getText()+".jpg");
                    }
                    JOptionPane.showMessageDialog(null,"The image saved in Folder Photos\\"+textID.getText()+"_"+textSize.getText()+".jpg");
                    pre.close();
                    con.close();
                }catch (Exception e4){

                    e4.printStackTrace();
                    System.out.println("There is not an image");

                }
            }
        });


        //ButtonList
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new List();
                //frame.setTitle("Album");
                //frame.setSize(1200, 800);
                //frame.setLocationRelativeTo(null);
                //frame.setVisible(true);
                //new Album();
            }
        });

        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new ImageGallery();
            }
        });


        //Directions, adders
        add(label);
        add(textID);
        add(textSize);
        add(textLong);
        add(textLat);
        add(button);
        add(button2);
        add(button3);
        add(button4);
        add(button5);
        add(button7);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,500);
        setVisible(true);
    }


    //Method To Resize The ImageIcon
    public ImageIcon ResizeImage(String imgPath){
        ImageIcon MyImage = new ImageIcon(imgPath);
        Image img = MyImage.getImage();
        Image newImage = ((Image) img).getScaledInstance(label.getWidth(), label.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImage);
        return image;
    }





}
