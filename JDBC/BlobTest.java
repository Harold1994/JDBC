package JDBC;

import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class BlobTest {

    JFrame jf = new JFrame("PICTURE MANAGER");
    private static Connection conn;
    private static PreparedStatement insert;
    private static PreparedStatement query;
    private static PreparedStatement queryAll;

    private DefaultListModel<ImageHolder> imageModel = new DefaultListModel<>();
    private JList<ImageHolder> imageList = new JList<>(imageModel);
    private JTextField filePath = new JTextField(26);
    private JButton uploadBn = new JButton("upload");
    private JButton browserBn = new JButton("...");
    private JLabel imageLabel = new JLabel();

    static JFileChooser chooser = new JFileChooser(".");
    static ExtensionFileFilter filter = new ExtensionFileFilter();

    static {
        try{
            Properties props = new Properties();
            props.load(new FileInputStream("mysql.ini"));
            String driver = props.getProperty("driver");
            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String pass = props.getProperty("pass");

            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,pass);
            insert = conn.prepareStatement("insert into img_table"
            +" values(null,?,?)", Statement.RETURN_GENERATED_KEYS);
            query = conn.prepareStatement("select img_data from img_table"
            +" where img_id = ?");
            queryAll = conn.prepareStatement("select img_id, img_name from img_table");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() throws SQLException{
        filter.addExtension("jpg");
        filter.addExtension("jpeg");
        filter.addExtension("gif");
        filter.addExtension("png");
        chooser.setAcceptAllFileFilterUsed(false);
        fillListModel();
        filePath.setEditable(false);
        imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel jp = new JPanel();
        jp.add(filePath);
        jp.add(browserBn);

        browserBn.addActionListener(event ->{
            int result = chooser.showDialog(jf,"browse pic to upload");
            if(result == JFileChooser.APPROVE_OPTION){
                filePath.setText(chooser.getSelectedFile().getPath());
            }
        });
        jp.add(uploadBn);
        uploadBn.addActionListener(avt->{
            if(filePath.getText().trim().length()>0){
                upload(filePath.getText());
                filePath.setText("");
            }
        });
        JPanel left = new JPanel();
        left.setLayout(new BorderLayout());
        left.add(new JScrollPane(imageLabel),BorderLayout.CENTER);
        left.add(jp,BorderLayout.SOUTH);
        jf.add(left);
        imageList.setFixedCellWidth(160);
        jf.add(new JScrollPane(imageList),BorderLayout.EAST);
        imageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() >=2){
                    ImageHolder cur = (ImageHolder) imageList.getSelectedValue();
                    try{
                        showImage(cur.getId());
                    }catch (SQLException sqle){
                        sqle.printStackTrace();
                    }
                }
            }


        });
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(620,400);
    }

    public void upload(String text) {
        String imageName = text.substring(text.lastIndexOf('/') + 1,text.lastIndexOf('.'));
        File f = new File(text);
        try(
                InputStream is = new FileInputStream(f)){
            insert.setString(1,imageName);
            insert.setBinaryStream(2,is,(int)f.length());
            int affect = insert.executeUpdate();
            if(affect == 1){
                fillListModel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillListModel() throws SQLException {
        try(
            ResultSet rs = queryAll.executeQuery();)
        {
           imageModel.clear();
           while(rs.next()){
               imageModel.addElement(new ImageHolder(rs.getInt(1),rs.getString(2)));
           }
        }
    }
    public void showImage(int id) throws SQLException {
        query.setInt(1,id);
        try(

                ResultSet rs = query.executeQuery())
        {
            if(rs.next()){
                Blob imgBlob = rs.getBlob(1);
                ImageIcon icon = new ImageIcon(imgBlob.getBytes(1L,(int)imgBlob.length()));
                imageLabel.setIcon(icon);
            }
        }
    }


    public static void main(String[] args) throws SQLException {
        new BlobTest().init();
    }
}

class ExtensionFileFilter extends FileFilter{

    private String description = "";
    private ArrayList<String> extensions = new ArrayList<>();

    public void addExtension(String extension){
        extension = '.' + extension;
        extensions.add(extension.toLowerCase());
    }

    public void setDescription(String aDescription){
        description = aDescription;
    }

    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
            return true;
        String name = f.getName().toLowerCase();
        for(String extension :extensions){
            if(name.endsWith(extension))
                return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }
}


class ImageHolder{
    private int id;
    private String name;

    public ImageHolder() {}

    public ImageHolder(int id,String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

