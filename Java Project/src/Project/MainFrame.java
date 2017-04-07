/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 *
 * @author Fudolig
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
     //color code 54 161 227
    MyConnection connect = new MyConnection();
    final JFileChooser chooser = new JFileChooser();
    private ResultSet main_rs = null;
    private Border resetborder;
    private String imagePath;
    private int studentTableId = -1;
    private int sectionTableId = -1;
    private int billingTableId = -1;
    public MainFrame() {
        //UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
        //UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        
        initComponents();
        paintTime();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images","jpg","gif","png");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        profileMenu.setSelected(true);
    }
    public MainFrame(ResultSet rs) {
        initComponents();
        this.main_rs = rs;
        resetborder = studentId.getBorder();
        //paint time
        paintTime();
        //fil info
        fillRegistrarInfo();
        //center
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        ((JSpinner.DefaultEditor)maxStud.getEditor()).getTextField().setEditable(false);
        SpinnerNumberModel model = (SpinnerNumberModel)maxStud.getModel();
        maxStud.setModel(model);
        
        //set menu selected
        profileMenu.setSelected(true);
        //JFile chooser
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images","jpg","gif","png");
        chooser.addChoosableFileFilter(filter);
        
    }
    
    public static boolean isInteger(String str) {
            if (str == null) {
                return false;
            }
            int length = str.length();
            if (length == 0) {
                return false;
            }
            int i = 0;
            if (str.charAt(0) == '-') {
                if (length == 1) {
                    return false;
                }
                i = 1;
            }
            for (; i < length; i++) {
                char c = str.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
            return true;
    }
    
    public void fillRegistrarInfo(){
        try{
        registrarId.setText(""+main_rs.getInt("registrarId"));
        registrarFirstn.setText(main_rs.getString("Firstname"));
        registrarMn.setText(main_rs.getString("Middlename"));
        registrarLastn.setText(main_rs.getString("Lastname"));
        registrarStatus.setText(main_rs.getString("Status"));
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void paintTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMMM dd, yyyy");
        Timer timer = new Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                hour.setText(dateFormat.format(new java.util.Date()));
                month.setText(dateFormat2.format(new java.util.Date()));
                hour.repaint();
                month.repaint();
            }
        });
        timer.start();
    }
    
    public ImageIcon resizeImage(String ImagePath,JLabel label){
        ImageIcon myimage = new ImageIcon(ImagePath);
        Image img = myimage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(),label.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }
    
    public void clearStudentForm(){
        studentId.setText("");
        studentFn.setText("");
        studentMn.setText("");
        studentLn.setText("");
        studentDbirth.setCalendar(null);
        student_gender_group.clearSelection();
        studentCitizenship.setText("");
        studentReligion.setText("");
        studentPassword.setText("");
        studentSection.removeAllItems();  
        createStudentYear.setSelectedIndex(0);
        Image image = null;
                try {
                  image = ImageIO.read(getClass().getResource("/images/user.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        imagePath = null;
        studentPhoto.setIcon(new ImageIcon(image));
        studentPhoto.repaint();
    }
    public void clearSectForm(){
        sectionName.setText("");
        sectionYearCombo.setSelectedIndex(0);
        maxStud.setValue(10);
        sectionRoom.setText("");
    }
    public void resetStudentCreateBorder(){
        studentFn.setBorder(resetborder);
        studentMn.setBorder(resetborder);
        studentLn.setBorder(resetborder);
        studentCitizenship.setBorder(resetborder);
        studentReligion.setBorder(resetborder);
        studentPassword.setBorder(resetborder);
        studentSection.setBorder(resetborder);
        studentDbirth.setBorder(resetborder);
    }
    public boolean checkStudentCreateForm(){
        boolean check = true;
        Border border = BorderFactory.createLineBorder(Color.red);
        String fn = studentFn.getText();
        String mn = studentMn.getText();
        String ln = studentLn.getText();
        String citizen = studentCitizenship.getText();
        String religion = studentReligion.getText();
        String pass = studentPassword.getText();
        resetStudentCreateBorder();
        Pattern p = Pattern.compile("[^a-zA-Z]");
        Pattern n = Pattern.compile("[0-9]+");
 
        if(p.matcher(fn).find()||fn.length() == 0){            
            studentFn.setBorder(border);
            check = false;
        }
        
        if(p.matcher(mn).find()||mn.length() == 0){            
            studentMn.setBorder(border);
            check = false;
        }
        if(p.matcher(ln).find()||ln.length() == 0){            
            studentLn.setBorder(border);
            check = false;
        }

        if(studentDbirth.getDate() == null){
            studentDbirth.setBorder(border);
            check = false;
        }
        if(student_gender_group.getSelection()==null){
            check = false;
        }
        if(p.matcher(citizen).find()||citizen.length() == 0){            
            studentCitizenship.setBorder(border);
            check = false;
        }
        if(p.matcher(religion).find()||religion.length() == 0){            
            studentReligion.setBorder(border);
            check = false;
        }
        if(studentSection.getSelectedIndex() == -1||Objects.equals(studentSection.getSelectedItem().toString(),"No sections available")){
            studentSection.setBorder(border);
            check = false;
        }        
        if(pass.length() == 0||!pass.matches("[a-zA-Z0-9]*")){            
            studentPassword.setBorder(border);
            check = false;
        }
        if(check){
            resetStudentCreateBorder();
        }
        return check;     
    }
    
    public boolean checkSectionCreateForm(){
        boolean check = true;
        Pattern p = Pattern.compile("[^a-zA-Z]");
        resetSectionCreateBorder();
        Border border = BorderFactory.createLineBorder(Color.red);
        
        if(p.matcher(sectionName.getText()).find()||sectionName.getText().length() == 0){            
            sectionName.setBorder(border);
            check = false;
        }
        if(!isInteger(sectionRoom.getText())){
            sectionRoom.setBorder(border);
            check = false;
        }
        if(check){
            resetSectionCreateBorder();
        }
        
        return check;
    }
    public void resetSectionCreateBorder(){
        sectionName.setBorder(resetborder);
        sectionRoom.setBorder(resetborder);
    }
     public void populateCreateSectCombo(){
        String query = "select sectionId,sectionName,sectionCurStud,sectionMaxStud from \n" +
                        "section \n" +
                        " WHERE sectionIsFull='No' AND yearLevel=? ORDER BY sectionId ASC";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            studentSection.removeAllItems();
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(createStudentYear.getSelectedItem().toString()));
            rs = pstmt.executeQuery();
            while(rs.next()){
            String item = rs.getInt("sectionId")+" - "+rs.getString("sectionName");
            if(studentSection.getItemCount() == 0){
                studentSection.addItem(item);
            }
            else if(studentSection.getItemCount() > 0){
                        Boolean found = false;
                        for(int c=0;c<studentSection.getItemCount();c++){
                            if(Objects.equals(item,studentSection.getItemAt(c).toString())){                   
                            found = true;
                            }
                        }
                        if(!found){
                            studentSection.addItem(item);
                        }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(rs!=null){
                try{
                    rs.close();
                }catch(SQLException e){}
            }
            if(pstmt!=null){
                try{
                    pstmt.close();
                }catch(SQLException e){}
            }
            if(con!=null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
     }
     public void populateEditSectCombo(){
         String getCurStudYear = "SELECT * FROM student WHERE studentId=?";
         String getCurrentSect = "SELECT section.sectionId,section.sectionName,section.yearLevel\n" +
                                " FROM section\n" +
                                "INNER JOIN student_section\n" +
                                "ON student_section.sectionId = section.sectionId AND student_section.studentId =?";
         String getOtherSections = "select sectionId,sectionName,sectionCurStud,sectionMaxStud from \n" +
                                   "section \n " +
                                   " WHERE sectionIsFull='No' AND yearLevel=? AND sectionId != ? ORDER BY sectionId ASC";
         String getOtherYearSect =  "select sectionId,sectionName,sectionCurStud,sectionMaxStud from \n" +
                                    "section \n" +
                                    " WHERE sectionIsFull='No' AND yearLevel=? ORDER BY sectionId ASC";
         Connection con = connect.getConnection();
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         int curSect = 0;
         int curYear = 0;
         try{
             studentSection.removeAllItems();
             //validate if there is a selected person
             if(studentId.getText().length() > 0){                         
             
                //get cur stud year
                 pstmt = con.prepareStatement(getCurStudYear);
                 pstmt.setInt(1,Integer.parseInt(studentId.getText().toString()));
                 rs = pstmt.executeQuery();
                    if(rs.next()){
                        curYear = rs.getInt("yearLevel");
                    }

                    //filter if current year is selected
                    if(curYear == Integer.parseInt(createStudentYear.getSelectedItem().toString())){    
                        //get Current sect
                        pstmt = con.prepareStatement(getCurrentSect);
                        pstmt.setInt(1, Integer.parseInt(studentId.getText().toString()));
                        rs = pstmt.executeQuery();
                        String item;
                           if(rs.next()){
                               curSect = rs.getInt("sectionId");
                               item = rs.getInt("sectionId")+" - "+rs.getString("sectionName");
                               studentSection.addItem(item);
                           }

                       //get other sections except current sect
                       pstmt = con.prepareStatement(getOtherSections);
                       pstmt.setInt(1,Integer.parseInt(createStudentYear.getSelectedItem().toString()));
                       pstmt.setInt(2, curSect);
                       rs = pstmt.executeQuery();
                        while(rs.next()){
                            item = rs.getInt("sectionId")+" - "+rs.getString("sectionName");
                            studentSection.addItem(item);
                        }
                    }
                    else{
                        pstmt = con.prepareStatement(getOtherYearSect);
                        pstmt.setInt(1,Integer.parseInt(createStudentYear.getSelectedItem().toString()));
                        rs = pstmt.executeQuery();
                            while(rs.next()){
                                String item = rs.getInt("sectionId")+" - "+rs.getString("sectionName");
                                    if(studentSection.getItemCount() == 0){
                                        studentSection.addItem(item);
                                    }
                                    else if(studentSection.getItemCount() > 0){
                                        Boolean found = false;
                                        for(int c=0;c<studentSection.getItemCount();c++){
                                            if(Objects.equals(item,studentSection.getItemAt(c).toString())){                   
                                                found = true;
                                            }
                                        }
                                        if(!found){
                                            studentSection.addItem(item);
                                            }
                                    }
                            }                    
                    }
             }
             
             else{
                 pstmt = con.prepareStatement(getOtherYearSect);
                        pstmt.setInt(1,Integer.parseInt(createStudentYear.getSelectedItem().toString()));
                        rs = pstmt.executeQuery();
                            while(rs.next()){
                                String item = rs.getInt("sectionId")+" - "+rs.getString("sectionName");
                                    if(studentSection.getItemCount() == 0){
                                        studentSection.addItem(item);
                                    }
                                    else if(studentSection.getItemCount() > 0){
                                        Boolean found = false;
                                        for(int c=0;c<studentSection.getItemCount();c++){
                                            if(Objects.equals(item,studentSection.getItemAt(c).toString())){                   
                                                found = true;
                                            }
                                        }
                                        if(!found){
                                            studentSection.addItem(item);
                                            }
                                    }
                            }
             }
             
         }catch(SQLException e){
             e.printStackTrace();
         }
         
     }
     public void populateBillCombo(){
         String query = "SELECT * FROM fee WHERE feeId != 10 AND feeId != 11 AND feeId != 15 AND feeId != 16 "
                 + "AND feeId != 17 AND feeId != 18";
         Connection con = connect.getConnection();
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         
         try{
             billingCombo.removeAllItems();
             pstmt = con.prepareStatement(query);
             rs = pstmt.executeQuery();
                while(rs.next()){
                    String item = rs.getString("feeName");
                    billingCombo.addItem(item);
                }
             
         }catch(SQLException e){
             e.printStackTrace();
         }finally{
             if(rs != null){
                try{
                    rs.close();
                }catch(SQLException e){}
            }
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch(SQLException e){}
            }
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
            
        }   
         
         
     }
    public void setSectionStatus(){
        String query = "UPDATE section set SectionIsFull ='Yes' where sectionCurStud = sectionMaxStud";
        String query2 = "UPDATE section set SectionIsFull ='No' where sectionCurStud != sectionMaxStud";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        try{
            pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(query2);
            pstmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch(SQLException e){}
            }
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }            
    }     
     public void drawStudentTable(){
         String query = "SELECT student.yearLevel,student.studentId,student.Firstname,student.Lastname,student.Gender,section.sectionId\n" +
                        "FROM student\n" +
                        "INNER JOIN student_section\n" +
                        "ON \n" +
                        "student.studentId = student_section.studentId\n" +
                        "INNER JOIN section\n" +
                        "ON section.sectionId = student_section.sectionId ORDER BY student.yearLevel ASC";
         //String query = "SELECT yearLevel,studentId,Firstname,Lastname,Gender,status FROM student";
         
         
         Color tableheader_color = new Color(15,79,157);
         Connection con = connect.getConnection();
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         try{
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            
            MyModel model = new MyModel(rs,"Student");
            studentTable.setModel(model);
            model.fireTableStructureChanged();
            model.fireTableDataChanged();
           // JTableHeader header = studentTable.getTableHeader();
           // header.setForeground(Color.white);
           // header.setBackground(tableheader_color);
         }catch(SQLException e){
             e.printStackTrace();
         }
         
     }
     public void drawSectionTable(){
         String query = "SELECT yearLevel, sectionId,sectionName,sectionCurStud,sectionMaxStud,sectionRoom,sectionIsFull FROM section ORDER BY yearLevel";
         Color tableheader_color = new Color(15,79,157);
         Connection con = connect.getConnection();
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         try{
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            
            MyModel model = new MyModel(rs,"Section");
            sectTable.setModel(model);
            model.fireTableStructureChanged();
            model.fireTableDataChanged();
           // JTableHeader header = studentTable.getTableHeader();
           // header.setForeground(Color.white);
           // header.setBackground(tableheader_color);
         }catch(SQLException e){
             e.printStackTrace();
         }
     }
     public void drawBillingTable(int id){
         String query = "select student_bill.billId, fee.feeName, fee.feeAmount,student_bill.billDate\n" +
                        "FROM student_bill\n" +
                        "INNER JOIN fee\n" +
                        "ON student_bill.paid='Not Paid' AND fee.feeId = student_bill.feeId and studentId =?";                   
         Connection con = connect.getConnection();
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         try{
             pstmt = con.prepareStatement(query);   
             pstmt.setInt(1,id);
             rs = pstmt.executeQuery();
             MyModel model = new MyModel(rs,"Billing");
             billingTable.setModel(model);
             model.fireTableStructureChanged();
             model.fireTableDataChanged();
         }catch(SQLException e){
             e.printStackTrace();
         }
         
     }
     public void drawBillingTables(){         
         MyModel model = new MyModel("BillBlank");
         billingTable.setModel(model);
         model.fireTableStructureChanged();
         model.fireTableDataChanged();
     }
     public void disableStudentTableButtons(){
         studentSave.setEnabled(false);
         studentEdit.setEnabled(false);
         studentRevert.setEnabled(false);
         studentDelete.setEnabled(false);
     }
     public void disableSectTableButtons(){
        sectShowList.setEnabled(false);
        sectSave.setEnabled(false);
        sectEdit.setEnabled(false);
        sectRevert.setEnabled(false);
        sectDelete.setEnabled(false);  
     }
     public void clearBillingForm(){
         billingStudId.setText("");
         billingStudName.setText("");
         studentTotalBalance.setText("");
         billingPayable.setText("");
         amtRecieved.setText("");
         totalPayable.setText("");
         
        try {                
            ImageIcon myimage = new ImageIcon(ImageIO.read(getClass().getResource("/images/user.png")));
            Image img = myimage.getImage();
            Image newImg = img.getScaledInstance(billIcon.getWidth(),billIcon.getHeight(),Image.SCALE_SMOOTH);
            ImageIcon image = new ImageIcon(newImg);        
            imagePath = null;
            billIcon.setIcon(image);
            billIcon.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
     }
     public void drawReportTable(){
         String query = "SELECT studentId,billId,amount,paid,changeAmount,date FROM transactions";
         String query2 ="select studentId,billId,amount,paid,changeAmount,date from transactions where MONTHNAME(date)=?";
         Connection con = connect.getConnection();
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         
            try{
                if(Objects.equals("All",reportMonth.getSelectedItem().toString())){
                    pstmt = con.prepareStatement(query);
                    rs = pstmt.executeQuery();
                    rs.next();
                    System.out.println(""+rs.getDouble("amount"));
                    MyModel model = new MyModel(rs,"Transactions");
                    reportTable.setModel(model);
                    model.fireTableStructureChanged();
                    model.fireTableDataChanged();
                }
                else{
                    pstmt = con.prepareStatement(query2);
                    pstmt.setString(1,reportMonth.getSelectedItem().toString());
                    rs = pstmt.executeQuery();
                    MyModel model = new MyModel(rs,"Transactions");
                    reportTable.setModel(model);
                    model.fireTableStructureChanged();
                    model.fireTableDataChanged();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
     }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        student_gender_group = new javax.swing.ButtonGroup();
        menubar_group = new javax.swing.ButtonGroup();
        payment_group = new javax.swing.ButtonGroup();
        menubarPanel = new javax.swing.JPanel();
        profileMenu = new javax.swing.JToggleButton();
        studentsMenu = new javax.swing.JToggleButton();
        sectionsMenu = new javax.swing.JToggleButton();
        reportsMenu = new javax.swing.JToggleButton();
        logoutMenu = new javax.swing.JToggleButton();
        billingMenu = new javax.swing.JToggleButton();
        mainPanel = new javax.swing.JPanel();
        profilePanel = new javax.swing.JPanel();
        registrarImage = new javax.swing.JLabel();
        registrarShowInfoTab = new javax.swing.JTabbedPane();
        registrarShowInfo = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        registrarFirstn = new javax.swing.JLabel();
        registrarMn = new javax.swing.JLabel();
        registrarLastn = new javax.swing.JLabel();
        registrarStatus = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        registrarId = new javax.swing.JLabel();
        registrarChangePass = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        currentPass = new javax.swing.JPasswordField();
        newPass = new javax.swing.JPasswordField();
        validateNewPass = new javax.swing.JPasswordField();
        changePass = new javax.swing.JButton();
        cancelChangePass = new javax.swing.JButton();
        studentPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        studentPhoto = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        studentId = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        studentFn = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        studentMn = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        studentLn = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        studentDbirth = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        studentMale = new javax.swing.JRadioButton();
        studentFemale = new javax.swing.JRadioButton();
        jLabel24 = new javax.swing.JLabel();
        studentCitizenship = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        studentReligion = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        studentPassword = new javax.swing.JTextField();
        studentCreate = new javax.swing.JButton();
        studentClear = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        studentSection = new javax.swing.JComboBox<String>();
        studentEnableEdit = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        studentSearch = new javax.swing.JTextField();
        studentSearchBtn = new javax.swing.JButton();
        studentSp = new javax.swing.JScrollPane();
        studentTable = new javax.swing.JTable();
        studentEdit = new javax.swing.JButton();
        studentSave = new javax.swing.JButton();
        studentRevert = new javax.swing.JButton();
        studentDelete = new javax.swing.JButton();
        studentImageBtn = new javax.swing.JButton();
        createStudentYear = new javax.swing.JComboBox<String>();
        sectionPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        sectionIdText = new javax.swing.JTextField();
        sectionName = new javax.swing.JTextField();
        sectionYearCombo = new javax.swing.JComboBox<String>();
        sectionRoom = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        sectCreate = new javax.swing.JButton();
        sectCancel = new javax.swing.JButton();
        sectionEnableEdit = new javax.swing.JToggleButton();
        sectSave = new javax.swing.JButton();
        sectEdit = new javax.swing.JButton();
        sectRevert = new javax.swing.JButton();
        sectDelete = new javax.swing.JButton();
        maxStud = new javax.swing.JSpinner();
        studentSp1 = new javax.swing.JScrollPane();
        sectTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        sectionSearchText = new javax.swing.JTextField();
        studentSearchBtn1 = new javax.swing.JButton();
        sectShowList = new javax.swing.JButton();
        billPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        billingTable = new javax.swing.JTable();
        jLabel36 = new javax.swing.JLabel();
        billingStudName = new javax.swing.JTextField();
        billingStudId = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        billingSearchText = new javax.swing.JTextField();
        billingSearchBtn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        billIcon = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        studentTotalBalance = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        billingPayable = new javax.swing.JTextField();
        amtRecieved = new javax.swing.JTextField();
        billChange = new javax.swing.JTextField();
        billingPay = new javax.swing.JButton();
        payAllBox = new javax.swing.JCheckBox();
        billingCombo = new javax.swing.JComboBox<String>();
        jLabel33 = new javax.swing.JLabel();
        addBill = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        totalPayable = new javax.swing.JTextField();
        reportsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        reportMonth = new javax.swing.JComboBox<String>();
        hour = new javax.swing.JLabel();
        month = new javax.swing.JLabel();
        Minimize = new javax.swing.JLabel();
        exit = new javax.swing.JLabel();
        schoolLogo = new javax.swing.JLabel();
        schoolBanner = new javax.swing.JLabel();
        mainbackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 0));
        setUndecorated(true);
        setSize(new java.awt.Dimension(1000, 750));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menubarPanel.setBackground(new java.awt.Color(255, 255, 255));
        menubarPanel.setOpaque(false);

        menubar_group.add(profileMenu);
        profileMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/myprofilebutton.png"))); // NOI18N
        profileMenu.setBorder(null);
        profileMenu.setBorderPainted(false);
        profileMenu.setContentAreaFilled(false);
        profileMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        profileMenu.setFocusPainted(false);
        profileMenu.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/myprofilebuttonrollover.png"))); // NOI18N
        profileMenu.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/myprofilebuttonselected.png"))); // NOI18N
        profileMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/myprofilebuttonselected.png"))); // NOI18N
        profileMenu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                profileMenuItemStateChanged(evt);
            }
        });

        menubar_group.add(studentsMenu);
        studentsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/studentsbutton.png"))); // NOI18N
        studentsMenu.setBorder(null);
        studentsMenu.setBorderPainted(false);
        studentsMenu.setContentAreaFilled(false);
        studentsMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        studentsMenu.setFocusPainted(false);
        studentsMenu.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/studentsbuttonrollover.png"))); // NOI18N
        studentsMenu.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/studentsbuttonselected.png"))); // NOI18N
        studentsMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/studentsbuttonselected.png"))); // NOI18N
        studentsMenu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                studentsMenuItemStateChanged(evt);
            }
        });

        menubar_group.add(sectionsMenu);
        sectionsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sectionsbutton.png"))); // NOI18N
        sectionsMenu.setBorder(null);
        sectionsMenu.setBorderPainted(false);
        sectionsMenu.setContentAreaFilled(false);
        sectionsMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sectionsMenu.setFocusPainted(false);
        sectionsMenu.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sectionsbuttonrollover.png"))); // NOI18N
        sectionsMenu.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sectionsbuttonselected.png"))); // NOI18N
        sectionsMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sectionsbuttonselected.png"))); // NOI18N
        sectionsMenu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sectionsMenuItemStateChanged(evt);
            }
        });

        menubar_group.add(reportsMenu);
        reportsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reportsbutton.png"))); // NOI18N
        reportsMenu.setBorder(null);
        reportsMenu.setBorderPainted(false);
        reportsMenu.setContentAreaFilled(false);
        reportsMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reportsMenu.setFocusPainted(false);
        reportsMenu.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reportsbuttonrollover.png"))); // NOI18N
        reportsMenu.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reportsbuttonselected.png"))); // NOI18N
        reportsMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reportsbuttonselected.png"))); // NOI18N
        reportsMenu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                reportsMenuItemStateChanged(evt);
            }
        });

        menubar_group.add(logoutMenu);
        logoutMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutbutton.png"))); // NOI18N
        logoutMenu.setBorder(null);
        logoutMenu.setBorderPainted(false);
        logoutMenu.setContentAreaFilled(false);
        logoutMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutMenu.setFocusPainted(false);
        logoutMenu.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutbuttonrollover.png"))); // NOI18N
        logoutMenu.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutbuttonselected.png"))); // NOI18N
        logoutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutMenuActionPerformed(evt);
            }
        });

        menubar_group.add(billingMenu);
        billingMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/billingbutton.png"))); // NOI18N
        billingMenu.setBorder(null);
        billingMenu.setBorderPainted(false);
        billingMenu.setContentAreaFilled(false);
        billingMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        billingMenu.setFocusPainted(false);
        billingMenu.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/billingbuttonrollover.png"))); // NOI18N
        billingMenu.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/billingbuttonselected.png"))); // NOI18N
        billingMenu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/billingbuttonselected.png"))); // NOI18N
        billingMenu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                billingMenuItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout menubarPanelLayout = new javax.swing.GroupLayout(menubarPanel);
        menubarPanel.setLayout(menubarPanelLayout);
        menubarPanelLayout.setHorizontalGroup(
            menubarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menubarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menubarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(profileMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentsMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sectionsMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(billingMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reportsMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menubarPanelLayout.setVerticalGroup(
            menubarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menubarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(profileMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(studentsMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sectionsMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(billingMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(reportsMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logoutMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(menubarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 120, 550));

        mainPanel.setBackground(new java.awt.Color(54, 161, 227));
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.CardLayout());

        profilePanel.setBackground(new java.awt.Color(255, 255, 255));
        profilePanel.setOpaque(false);

        registrarImage.setBackground(new java.awt.Color(51, 204, 0));
        registrarImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/person1.png"))); // NOI18N

        registrarShowInfoTab.setBackground(new java.awt.Color(54, 161, 227));
        registrarShowInfoTab.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N

        registrarShowInfo.setBackground(new java.awt.Color(160, 122, 72));

        jLabel9.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("First Name");

        jLabel10.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Middle Name");

        jLabel11.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Last Name");

        jLabel12.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Status");

        registrarFirstn.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        registrarFirstn.setForeground(new java.awt.Color(255, 255, 255));
        registrarFirstn.setText("Fn");

        registrarMn.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        registrarMn.setForeground(new java.awt.Color(255, 255, 255));
        registrarMn.setText("Mn");

        registrarLastn.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        registrarLastn.setForeground(new java.awt.Color(255, 255, 255));
        registrarLastn.setText("Ln");

        registrarStatus.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        registrarStatus.setForeground(new java.awt.Color(255, 255, 255));
        registrarStatus.setText("Status");

        jLabel7.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("RegistrarId");

        registrarId.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        registrarId.setForeground(new java.awt.Color(255, 255, 255));
        registrarId.setText("My Id");

        javax.swing.GroupLayout registrarShowInfoLayout = new javax.swing.GroupLayout(registrarShowInfo);
        registrarShowInfo.setLayout(registrarShowInfoLayout);
        registrarShowInfoLayout.setHorizontalGroup(
            registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrarShowInfoLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(registrarShowInfoLayout.createSequentialGroup()
                        .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addGap(30, 30, 30)
                        .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(registrarLastn, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(registrarStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(registrarShowInfoLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(registrarMn, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(registrarShowInfoLayout.createSequentialGroup()
                        .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7))
                        .addGap(30, 30, 30)
                        .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(registrarFirstn, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(registrarId))))
                .addContainerGap(306, Short.MAX_VALUE))
        );
        registrarShowInfoLayout.setVerticalGroup(
            registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrarShowInfoLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(registrarId))
                .addGap(18, 18, 18)
                .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(registrarFirstn))
                .addGap(18, 18, 18)
                .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(registrarMn))
                .addGap(18, 18, 18)
                .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(registrarLastn))
                .addGap(18, 18, 18)
                .addGroup(registrarShowInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(registrarStatus))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        registrarShowInfoTab.addTab("My Information", registrarShowInfo);

        registrarChangePass.setBackground(new java.awt.Color(160, 122, 72));
        registrarChangePass.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Old Password");

        jLabel14.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("New Password");

        jLabel15.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Verify Password");

        changePass.setText("Change");
        changePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePassActionPerformed(evt);
            }
        });

        cancelChangePass.setText("Cancel");
        cancelChangePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelChangePassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout registrarChangePassLayout = new javax.swing.GroupLayout(registrarChangePass);
        registrarChangePass.setLayout(registrarChangePassLayout);
        registrarChangePassLayout.setHorizontalGroup(
            registrarChangePassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrarChangePassLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(registrarChangePassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(registrarChangePassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(registrarChangePassLayout.createSequentialGroup()
                        .addComponent(changePass)
                        .addGap(18, 18, 18)
                        .addComponent(cancelChangePass))
                    .addGroup(registrarChangePassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(newPass)
                        .addComponent(currentPass)
                        .addComponent(validateNewPass, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)))
                .addContainerGap(456, Short.MAX_VALUE))
        );
        registrarChangePassLayout.setVerticalGroup(
            registrarChangePassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrarChangePassLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(registrarChangePassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(currentPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(registrarChangePassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(newPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(registrarChangePassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(validateNewPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(registrarChangePassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changePass)
                    .addComponent(cancelChangePass))
                .addContainerGap(92, Short.MAX_VALUE))
        );

        registrarShowInfoTab.addTab("Change Password", registrarChangePass);

        javax.swing.GroupLayout profilePanelLayout = new javax.swing.GroupLayout(profilePanel);
        profilePanel.setLayout(profilePanelLayout);
        profilePanelLayout.setHorizontalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(registrarShowInfoTab, javax.swing.GroupLayout.PREFERRED_SIZE, 754, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(registrarImage))
                .addGap(44, 44, 44))
        );
        profilePanelLayout.setVerticalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(registrarImage, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(registrarShowInfoTab, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainPanel.add(profilePanel, "profile");

        studentPanel.setBackground(new java.awt.Color(54, 161, 227));
        studentPanel.setOpaque(false);
        studentPanel.setLayout(null);

        jLabel16.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 30)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("STUDENT'S INFORMATION");
        studentPanel.add(jLabel16);
        jLabel16.setBounds(0, 0, 260, 33);

        studentPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/user.png"))); // NOI18N
        studentPhoto.setPreferredSize(new java.awt.Dimension(100, 100));
        studentPanel.add(studentPhoto);
        studentPhoto.setBounds(50, 40, 150, 150);

        jLabel18.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Student ID");
        studentPanel.add(jLabel18);
        jLabel18.setBounds(15, 206, 70, 16);

        studentId.setEditable(false);
        studentPanel.add(studentId);
        studentId.setBounds(94, 203, 40, 20);

        jLabel19.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("First Name");
        studentPanel.add(jLabel19);
        jLabel19.setBounds(15, 229, 60, 16);
        studentPanel.add(studentFn);
        studentFn.setBounds(94, 226, 150, 20);

        jLabel20.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Middle Name");
        studentPanel.add(jLabel20);
        jLabel20.setBounds(15, 258, 66, 16);
        studentPanel.add(studentMn);
        studentMn.setBounds(92, 255, 150, 20);

        jLabel21.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Last Name");
        studentPanel.add(jLabel21);
        jLabel21.setBounds(15, 287, 60, 16);
        studentPanel.add(studentLn);
        studentLn.setBounds(94, 284, 148, 20);

        jLabel22.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Date of Birth");
        studentPanel.add(jLabel22);
        jLabel22.setBounds(15, 319, 70, 16);

        studentDbirth.setDateFormatString("MMM d, yyyy");
        studentPanel.add(studentDbirth);
        studentDbirth.setBounds(94, 313, 154, 20);

        jLabel23.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Gender");
        studentPanel.add(jLabel23);
        jLabel23.setBounds(15, 339, 40, 16);

        student_gender_group.add(studentMale);
        studentMale.setForeground(new java.awt.Color(255, 255, 255));
        studentMale.setText("Male");
        studentMale.setContentAreaFilled(false);
        studentPanel.add(studentMale);
        studentMale.setBounds(94, 335, 47, 23);

        student_gender_group.add(studentFemale);
        studentFemale.setForeground(new java.awt.Color(255, 255, 255));
        studentFemale.setText("Female");
        studentFemale.setContentAreaFilled(false);
        studentPanel.add(studentFemale);
        studentFemale.setBounds(175, 335, 59, 23);

        jLabel24.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Citizenship");
        studentPanel.add(jLabel24);
        jLabel24.setBounds(15, 368, 60, 16);
        studentPanel.add(studentCitizenship);
        studentCitizenship.setBounds(94, 365, 154, 20);

        jLabel25.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Religion");
        studentPanel.add(jLabel25);
        jLabel25.setBounds(15, 394, 50, 16);
        studentPanel.add(studentReligion);
        studentReligion.setBounds(94, 391, 154, 20);

        jLabel26.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Password");
        studentPanel.add(jLabel26);
        jLabel26.setBounds(15, 420, 60, 16);
        studentPanel.add(studentPassword);
        studentPassword.setBounds(94, 417, 154, 20);

        studentCreate.setBackground(new java.awt.Color(54, 161, 227));
        studentCreate.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        studentCreate.setText("Create");
        studentCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentCreateActionPerformed(evt);
            }
        });
        studentPanel.add(studentCreate);
        studentCreate.setBounds(80, 510, 70, 30);

        studentClear.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        studentClear.setText("Clear");
        studentClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentClearActionPerformed(evt);
            }
        });
        studentPanel.add(studentClear);
        studentClear.setBounds(160, 510, 70, 30);

        jLabel27.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Year");
        studentPanel.add(jLabel27);
        jLabel27.setBounds(15, 451, 40, 16);

        jLabel28.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Section");
        studentPanel.add(jLabel28);
        jLabel28.setBounds(15, 477, 50, 16);

        studentPanel.add(studentSection);
        studentSection.setBounds(94, 474, 130, 20);

        studentEnableEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableeditbutton.png"))); // NOI18N
        studentEnableEdit.setBorder(null);
        studentEnableEdit.setBorderPainted(false);
        studentEnableEdit.setContentAreaFilled(false);
        studentEnableEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        studentEnableEdit.setFocusPainted(false);
        studentEnableEdit.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableeditrollover.png"))); // NOI18N
        studentEnableEdit.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableeditselected.png"))); // NOI18N
        studentEnableEdit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableeditselected.png"))); // NOI18N
        studentEnableEdit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                studentEnableEditItemStateChanged(evt);
            }
        });
        studentPanel.add(studentEnableEdit);
        studentEnableEdit.setBounds(290, 70, 30, 30);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(45, 30));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        studentSearch.setText("Enter student ID..");
        studentSearch.setBorder(null);
        studentSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studentSearchMouseClicked(evt);
            }
        });
        studentSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentSearchActionPerformed(evt);
            }
        });
        jPanel1.add(studentSearch);

        studentSearchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        studentSearchBtn.setBorder(null);
        studentSearchBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        studentSearchBtn.setPreferredSize(new java.awt.Dimension(45, 30));
        studentSearchBtn.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/searchrollover.png"))); // NOI18N
        studentSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentSearchBtnActionPerformed(evt);
            }
        });
        jPanel1.add(studentSearchBtn);

        studentPanel.add(jPanel1);
        jPanel1.setBounds(550, 70, 247, 32);

        studentTable.setBackground(new java.awt.Color(160, 122, 72));
        studentTable.setForeground(new java.awt.Color(255, 255, 255));
        studentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studentTableMouseClicked(evt);
            }
        });
        studentSp.setViewportView(studentTable);

        studentPanel.add(studentSp);
        studentSp.setBounds(290, 110, 505, 374);

        studentEdit.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        studentEdit.setText("Edit");
        studentEdit.setPreferredSize(new java.awt.Dimension(70, 23));
        studentEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentEditActionPerformed(evt);
            }
        });
        studentPanel.add(studentEdit);
        studentEdit.setBounds(460, 500, 80, 30);

        studentSave.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        studentSave.setText("Save");
        studentSave.setPreferredSize(new java.awt.Dimension(70, 23));
        studentSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentSaveActionPerformed(evt);
            }
        });
        studentPanel.add(studentSave);
        studentSave.setBounds(370, 500, 70, 30);

        studentRevert.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        studentRevert.setText("Revert");
        studentRevert.setPreferredSize(new java.awt.Dimension(70, 23));
        studentRevert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentRevertActionPerformed(evt);
            }
        });
        studentPanel.add(studentRevert);
        studentRevert.setBounds(550, 500, 70, 30);

        studentDelete.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        studentDelete.setText("Delete");
        studentDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentDeleteActionPerformed(evt);
            }
        });
        studentPanel.add(studentDelete);
        studentDelete.setBounds(640, 500, 90, 30);

        studentImageBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/uploadphoto.png"))); // NOI18N
        studentImageBtn.setBorder(null);
        studentImageBtn.setBorderPainted(false);
        studentImageBtn.setContentAreaFilled(false);
        studentImageBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        studentImageBtn.setFocusPainted(false);
        studentImageBtn.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/uploadphotorollover.png"))); // NOI18N
        studentImageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentImageBtnActionPerformed(evt);
            }
        });
        studentPanel.add(studentImageBtn);
        studentImageBtn.setBounds(210, 150, 40, 40);

        createStudentYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        createStudentYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createStudentYearActionPerformed(evt);
            }
        });
        studentPanel.add(createStudentYear);
        createStudentYear.setBounds(94, 448, 130, 20);

        mainPanel.add(studentPanel, "student");

        sectionPanel.setBackground(new java.awt.Color(54, 161, 227));
        sectionPanel.setOpaque(false);
        sectionPanel.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("SECTION INFORMATION");
        sectionPanel.add(jLabel2);
        jLabel2.setBounds(10, 11, 207, 33);

        jLabel3.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Section ID");
        sectionPanel.add(jLabel3);
        jLabel3.setBounds(0, 100, 80, 16);

        jLabel5.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Section Name");
        sectionPanel.add(jLabel5);
        jLabel5.setBounds(0, 126, 90, 30);

        jLabel6.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Section YearLevel");
        sectionPanel.add(jLabel6);
        jLabel6.setBounds(0, 170, 110, 20);

        jLabel8.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Section Room");
        sectionPanel.add(jLabel8);
        jLabel8.setBounds(0, 240, 90, 20);

        sectionIdText.setEditable(false);
        sectionPanel.add(sectionIdText);
        sectionIdText.setBounds(110, 100, 55, 20);
        sectionPanel.add(sectionName);
        sectionName.setBounds(110, 130, 150, 20);

        sectionYearCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        sectionPanel.add(sectionYearCombo);
        sectionYearCombo.setBounds(110, 170, 147, 20);
        sectionPanel.add(sectionRoom);
        sectionRoom.setBounds(110, 240, 146, 20);

        jLabel17.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Maximum Students");
        sectionPanel.add(jLabel17);
        jLabel17.setBounds(0, 210, 130, 16);

        sectCreate.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        sectCreate.setText("Create");
        sectCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectCreateActionPerformed(evt);
            }
        });
        sectionPanel.add(sectCreate);
        sectCreate.setBounds(110, 280, 70, 36);

        sectCancel.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        sectCancel.setText("Cancel");
        sectCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectCancelActionPerformed(evt);
            }
        });
        sectionPanel.add(sectCancel);
        sectCancel.setBounds(190, 280, 70, 36);

        sectionEnableEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableeditbutton.png"))); // NOI18N
        sectionEnableEdit.setBorder(null);
        sectionEnableEdit.setBorderPainted(false);
        sectionEnableEdit.setContentAreaFilled(false);
        sectionEnableEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sectionEnableEdit.setFocusPainted(false);
        sectionEnableEdit.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableeditrollover.png"))); // NOI18N
        sectionEnableEdit.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableeditselected.png"))); // NOI18N
        sectionEnableEdit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableeditselected.png"))); // NOI18N
        sectionEnableEdit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sectionEnableEditItemStateChanged(evt);
            }
        });
        sectionPanel.add(sectionEnableEdit);
        sectionEnableEdit.setBounds(299, 41, 30, 30);

        sectSave.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        sectSave.setText("Save");
        sectSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectSaveActionPerformed(evt);
            }
        });
        sectionPanel.add(sectSave);
        sectSave.setBounds(460, 510, 80, 40);

        sectEdit.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        sectEdit.setText("Edit");
        sectEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectEditActionPerformed(evt);
            }
        });
        sectionPanel.add(sectEdit);
        sectEdit.setBounds(550, 510, 60, 40);

        sectRevert.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        sectRevert.setText("Revert");
        sectRevert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectRevertActionPerformed(evt);
            }
        });
        sectionPanel.add(sectRevert);
        sectRevert.setBounds(620, 510, 80, 40);

        sectDelete.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        sectDelete.setText("Delete");
        sectDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectDeleteActionPerformed(evt);
            }
        });
        sectionPanel.add(sectDelete);
        sectDelete.setBounds(710, 510, 70, 40);

        maxStud.setModel(new javax.swing.SpinnerNumberModel(1, 1, 45, 1));
        sectionPanel.add(maxStud);
        maxStud.setBounds(212, 206, 40, 20);

        sectTable.setBackground(new java.awt.Color(160, 122, 72));
        sectTable.setFont(new java.awt.Font("Corbel", 0, 13)); // NOI18N
        sectTable.setForeground(new java.awt.Color(255, 255, 255));
        sectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        sectTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sectTableMouseClicked(evt);
            }
        });
        studentSp1.setViewportView(sectTable);

        sectionPanel.add(studentSp1);
        studentSp1.setBounds(269, 127, 530, 366);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setPreferredSize(new java.awt.Dimension(45, 32));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        sectionSearchText.setText("Enter section Id");
        sectionSearchText.setBorder(null);
        sectionSearchText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sectionSearchTextMouseClicked(evt);
            }
        });
        jPanel2.add(sectionSearchText);

        studentSearchBtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        studentSearchBtn1.setBorder(null);
        studentSearchBtn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        studentSearchBtn1.setFocusPainted(false);
        studentSearchBtn1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/searchrollover.png"))); // NOI18N
        studentSearchBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentSearchBtn1ActionPerformed(evt);
            }
        });
        jPanel2.add(studentSearchBtn1);

        sectionPanel.add(jPanel2);
        jPanel2.setBounds(549, 80, 250, 33);

        sectShowList.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        sectShowList.setText("Show Student List");
        sectShowList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectShowListActionPerformed(evt);
            }
        });
        sectionPanel.add(sectShowList);
        sectShowList.setBounds(320, 510, 130, 40);

        mainPanel.add(sectionPanel, "section");

        billPanel.setBackground(new java.awt.Color(54, 161, 227));
        billPanel.setForeground(new java.awt.Color(204, 204, 204));
        billPanel.setOpaque(false);

        billingTable.setBackground(new java.awt.Color(160, 122, 72));
        billingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        billingTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                billingTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(billingTable);

        jLabel36.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 30)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Student Account");

        billingStudName.setEditable(false);

        billingStudId.setEditable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setPreferredSize(new java.awt.Dimension(166, 30));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        billingSearchText.setText("Search student ID..");
        billingSearchText.setBorder(null);
        billingSearchText.setPreferredSize(new java.awt.Dimension(86, 30));
        billingSearchText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                billingSearchTextMouseClicked(evt);
            }
        });
        jPanel3.add(billingSearchText);

        billingSearchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        billingSearchBtn.setBorder(null);
        billingSearchBtn.setContentAreaFilled(false);
        billingSearchBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        billingSearchBtn.setDefaultCapable(false);
        billingSearchBtn.setFocusPainted(false);
        billingSearchBtn.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/searchrollover.png"))); // NOI18N
        billingSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billingSearchBtnActionPerformed(evt);
            }
        });
        jPanel3.add(billingSearchBtn);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(billIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jLabel30.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Balance");

        jLabel31.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Student ID");

        jLabel32.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Name");

        studentTotalBalance.setEditable(false);

        jPanel5.setBackground(new java.awt.Color(54, 161, 227));
        jPanel5.setOpaque(false);

        jLabel34.setFont(new java.awt.Font("Tw Cen MT Condensed", 1, 24)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Payments");

        jLabel35.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Amount Payable");

        jLabel37.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Amount Recieved");

        jLabel38.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Change");

        billingPayable.setEditable(false);

        billChange.setEditable(false);

        billingPay.setBackground(new java.awt.Color(54, 161, 227));
        billingPay.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        billingPay.setText("Pay");
        billingPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billingPayActionPerformed(evt);
            }
        });

        payAllBox.setBackground(new java.awt.Color(54, 161, 227));
        payAllBox.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        payAllBox.setForeground(new java.awt.Color(255, 255, 255));
        payAllBox.setText("Pay All");
        payAllBox.setOpaque(false);
        payAllBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                payAllBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(billingPay, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55))
                            .addComponent(billChange, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                            .addComponent(amtRecieved))
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(payAllBox)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(billingPayable, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel34)
                .addGap(95, 95, 95))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addComponent(payAllBox)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(billingPayable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(amtRecieved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(billChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addComponent(billingPay, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jLabel33.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Add Bill");

        addBill.setFont(new java.awt.Font("Corbel", 1, 12)); // NOI18N
        addBill.setText("Add");
        addBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBillActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Corbel", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Total Payable");

        totalPayable.setEditable(false);

        javax.swing.GroupLayout billPanelLayout = new javax.swing.GroupLayout(billPanel);
        billPanel.setLayout(billPanelLayout);
        billPanelLayout.setHorizontalGroup(
            billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPanelLayout.createSequentialGroup()
                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPanelLayout.createSequentialGroup()
                        .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(addBill, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel33)))
                        .addGap(236, 236, 236))
                    .addGroup(billPanelLayout.createSequentialGroup()
                        .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(billPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel36)
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(billPanelLayout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(billPanelLayout.createSequentialGroup()
                                                .addGap(102, 102, 102)
                                                .addComponent(totalPayable, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(billPanelLayout.createSequentialGroup()
                                                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(billPanelLayout.createSequentialGroup()
                                                        .addGap(20, 20, 20)
                                                        .addComponent(jLabel31))
                                                    .addGroup(billPanelLayout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jLabel32)))
                                                .addGap(30, 30, 30)
                                                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(billingStudName, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(billingStudId, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(billPanelLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel30)
                                                    .addComponent(jLabel29))
                                                .addGap(19, 19, 19)
                                                .addComponent(studentTotalBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(billPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPanelLayout.createSequentialGroup()
                                .addGap(189, 189, 189)
                                .addComponent(billingCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
        billPanelLayout.setVerticalGroup(
            billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPanelLayout.createSequentialGroup()
                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(billPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(billPanelLayout.createSequentialGroup()
                        .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(billPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, billPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(billPanelLayout.createSequentialGroup()
                                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(billPanelLayout.createSequentialGroup()
                                        .addGap(17, 17, 17)
                                        .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel31)
                                            .addComponent(billingStudId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(billPanelLayout.createSequentialGroup()
                                        .addGap(51, 51, 51)
                                        .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel32)
                                            .addComponent(billingStudName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(totalPayable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel29))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(studentTotalBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(billingCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addBill)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47))
        );

        mainPanel.add(billPanel, "bill");

        reportsPanel.setBackground(new java.awt.Color(160, 122, 72));
        reportsPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(204, 204, 204)));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Transactions");

        reportTable.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(reportTable);

        jLabel4.setFont(new java.awt.Font("Corbel", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Sort by Month");

        reportMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        reportMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportMonthActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout reportsPanelLayout = new javax.swing.GroupLayout(reportsPanel);
        reportsPanel.setLayout(reportsPanelLayout);
        reportsPanelLayout.setHorizontalGroup(
            reportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportsPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(reportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 747, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(reportsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(reportMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        reportsPanelLayout.setVerticalGroup(
            reportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportsPanelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(reportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(reportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addComponent(reportMonth, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        mainPanel.add(reportsPanel, "reports");

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, 820, 550));

        hour.setForeground(new java.awt.Color(255, 255, 255));
        hour.setText("hour");
        getContentPane().add(hour, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 70, -1, -1));

        month.setForeground(new java.awt.Color(255, 255, 255));
        month.setText("Month");
        getContentPane().add(month, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 90, -1, -1));

        Minimize.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Minimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MinimizeMouseClicked(evt);
            }
        });
        getContentPane().add(Minimize, new org.netbeans.lib.awtextra.AbsoluteConstraints(912, 11, 30, 30));

        exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitMouseClicked(evt);
            }
        });
        getContentPane().add(exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(959, 11, 30, 30));

        schoolLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/school logo.png"))); // NOI18N
        getContentPane().add(schoolLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, -1, 100));

        schoolBanner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/school banner.png"))); // NOI18N
        schoolBanner.setPreferredSize(new java.awt.Dimension(427, 90));
        getContentPane().add(schoolBanner, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 570, -1));

        mainbackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mainbackground.png"))); // NOI18N
        getContentPane().add(mainbackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 750));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void profileMenuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_profileMenuItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange()==ItemEvent.SELECTED){
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel,"profile");
      } else if(evt.getStateChange()==ItemEvent.DESELECTED){
      }
    }//GEN-LAST:event_profileMenuItemStateChanged

    private void studentsMenuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_studentsMenuItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange()==ItemEvent.SELECTED){            
            drawStudentTable();
            resetStudentCreateBorder();
            clearStudentForm();    
            disableStudentTableButtons();
            studentTableId = -1;
            studentEnableEdit.setSelected(false);
            
            CardLayout card = (CardLayout) mainPanel.getLayout();
            card.show(mainPanel,"student");
            //System.out.println("button is selected");
        } 
        else if(evt.getStateChange()==ItemEvent.DESELECTED){
            //System.out.println("button is not selected");
        }
    }//GEN-LAST:event_studentsMenuItemStateChanged

    private void studentCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentCreateActionPerformed
        // TODO add your handling code here:
        String insertIntoStud = "INSERT INTO student(Firstname,Middlename,Lastname,D_birth,Gender,Citizenship,Religion,Password,yearLevel,image) VALUES (?,?,?,?,?,?,?,?,?,?)";
        String getStudQuery = "SELECT * FROM student\n" +
                                "ORDER BY studentId DESC\n" +
                                "LIMIT 1;";
        String assignStudSect = "INSERT INTO student_section (sectionId,studentId) VALUES(?,?)";
        String getStudCountFromSect = "SELECT COUNT(*) AS 'studentCount' FROM student_section WHERE sectionId= ?";
        String updateSectStudCount ="update section set sectionCurStud= ? WHERE sectionId = ?";
        String createAccount = "INSERT INTO student_account (studentId,yearLevel) VALUES (?,?)";
        String generateBill  = "SELECT * FROM fee WHERE feeId=?";        
        String assignStudentBill = "INSERT INTO student_bill (studentId,feeId,amount) VALUES(?,?,?)";
        String updateStudentAccount = "UPDATE student_account SET totalPayable=?, balance=? WHERE studentId=?";        
        
        String splitStudSect[] = studentSection.getSelectedItem().toString().split("\\s+");
        String section_id = splitStudSect[0];
        
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean check = checkStudentCreateForm();
        int studId = 0;
        int studCount=0;
        int tuitionFeeId = 0;
        int miscId = 0;
        int year = 0;
        double miscFee = 0;
        double tuitionFee = 0;
        double balance = 0;
        double firstFee = 0;
        double secondFee = 0;
        double thirdFee = 0;
        double fourthFee = 0;
        double subjectFee = 0;
      if(!check){
                JOptionPane.showMessageDialog(null, "Please properly fill up the fields");               
                }
      else if(check){
                try{
                //insert into student
                pstmt = con.prepareStatement(insertIntoStud);
                pstmt.setString(1,studentFn.getText());
                pstmt.setString(2,studentMn.getText());
                pstmt.setString(3,studentLn.getText());
                java.sql.Date sqlDate = new java.sql.Date(studentDbirth.getDate().getTime());
                pstmt.setDate(4, sqlDate);
                if(studentMale.isSelected()){
                    pstmt.setString(5,"Male");
                }
                else if(studentFemale.isSelected()){
                    pstmt.setString(5,"Female");
                }
                pstmt.setString(6,studentCitizenship.getText());
                pstmt.setString(7,studentReligion.getText());
                pstmt.setString(8,studentPassword.getText());
                
                pstmt.setInt(9,Integer.parseInt(createStudentYear.getSelectedItem().toString()));
                    if(imagePath == null){
                        //File file = new File();
                        InputStream is = this.getClass().getResourceAsStream("/images/user.png");  
                        pstmt.setBlob(10,is);
                    }
                    else{
                        InputStream is = new FileInputStream(new File(imagePath));
                        pstmt.setBlob(10,is);
                    }
                    pstmt.executeUpdate();
               
                //get student id
                pstmt = con.prepareStatement(getStudQuery);
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        studId = rs.getInt("studentId");
                        year = rs.getInt("yearLevel");
                    }
                           
                //assign into section
                pstmt = con.prepareStatement(assignStudSect);
                pstmt.setInt(1,Integer.parseInt(section_id));
                pstmt.setInt(2,studId);
                pstmt.executeUpdate();
                
                //get student count from selected section in the student_section database
                pstmt = con.prepareStatement(getStudCountFromSect);
                pstmt.setInt(1,Integer.parseInt(section_id));
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        studCount = rs.getInt("studentCount");
                    }
                
                //update section student count
                pstmt = con.prepareStatement(updateSectStudCount);
                pstmt.setInt(1,studCount);
                pstmt.setInt(2,Integer.parseInt(section_id));
                pstmt.executeUpdate();
                          
                //create student account
                pstmt = con.prepareStatement(createAccount);
                pstmt.setInt(1,studId);
                pstmt.setInt(2,Integer.parseInt(createStudentYear.getSelectedItem().toString()));
                pstmt.executeUpdate();
                                
                //get fees from fee table
                pstmt = con.prepareStatement(generateBill);
                pstmt.setInt(1,10);
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        tuitionFee = rs.getDouble("feeAmount");
                        tuitionFeeId = rs.getInt("feeId");
                    }
                pstmt  = con.prepareStatement(generateBill);
                pstmt.setInt(1,11);
                rs = pstmt.executeQuery();
                     if(rs.next()){
                         miscFee = rs.getDouble("feeAmount");
                         miscId = rs.getInt("feeId");
                    }
                pstmt  = con.prepareStatement(generateBill);
                pstmt.setInt(1,15);
                rs = pstmt.executeQuery();
                     if(rs.next()){
                         firstFee = rs.getDouble("feeAmount");
                         subjectFee = firstFee;
                    }
                pstmt  = con.prepareStatement(generateBill);
                pstmt.setInt(1,16);
                rs = pstmt.executeQuery();
                     if(rs.next()){
                         secondFee = rs.getDouble("feeAmount");
                         subjectFee = secondFee;
                    }
                pstmt  = con.prepareStatement(generateBill);
                pstmt.setInt(1,17);
                rs = pstmt.executeQuery();
                     if(rs.next()){
                         thirdFee = rs.getDouble("feeAmount");
                         subjectFee = thirdFee;
                    }
                pstmt  = con.prepareStatement(generateBill);
                pstmt.setInt(1,18);
                rs = pstmt.executeQuery();
                     if(rs.next()){
                         fourthFee = rs.getDouble("feeAmount");
                         subjectFee = fourthFee;
                    }     
                     
                //assign bill to student
                pstmt = con.prepareStatement(assignStudentBill);
                pstmt.setInt(1,studId);
                pstmt.setInt(2,tuitionFeeId);
                pstmt.setDouble(3,tuitionFee);
                pstmt.executeUpdate();
                
                
                pstmt = con.prepareStatement(assignStudentBill);
                pstmt.setInt(1,studId);
                pstmt.setInt(2, miscId);
                pstmt.setDouble(3,miscFee);
                pstmt.executeUpdate();
                
                                pstmt = con.prepareStatement(assignStudentBill);
                    if(year == 1){
                        pstmt.setInt(1,studId);
                        pstmt.setInt(2,15);
                        pstmt.setDouble(3,firstFee);
                    }
                    else if(year ==2){
                        pstmt.setInt(1,studId);
                        pstmt.setInt(2,16);
                        pstmt.setDouble(3,secondFee);
                    }
                    else if(year ==3){
                        pstmt.setInt(1,studId);
                        pstmt.setInt(2,17);
                        pstmt.setDouble(3,thirdFee);
                    }
                    else{
                        pstmt.setInt(1,studId);
                        pstmt.setInt(2,18);
                        pstmt.setDouble(3,fourthFee);
                    }                    
                pstmt.executeUpdate();
                
                //update student account balance
                pstmt = con.prepareStatement(updateStudentAccount);
                balance = miscFee + tuitionFee +subjectFee;
                pstmt.setDouble(1,balance);
                pstmt.setDouble(2,balance);
                pstmt.setInt(3,studId);
                pstmt.executeUpdate();
                
                
                JOptionPane.showMessageDialog(null, "Student record successfully created");
                setSectionStatus();
                drawStudentTable();
                clearStudentForm();    
                }catch(SQLException e){
                    e.printStackTrace();
                } catch (FileNotFoundException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                if(rs != null){
                    try{
                        rs.close();
                    }catch(SQLException e){}
                }
                if(pstmt != null){
                    try{
                        pstmt.close();
                    }catch(SQLException e){}
                }
                if(con != null){
                    try{
                        con.close();
                    }catch(SQLException e){}
                }
            }
      }
    }//GEN-LAST:event_studentCreateActionPerformed

    private void studentClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentClearActionPerformed
        // TODO add your handling code here:
        clearStudentForm();
    }//GEN-LAST:event_studentClearActionPerformed

    private void createStudentYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createStudentYearActionPerformed
        // TODO add your handling code here:
        if(studentEnableEdit.isSelected()){
            
            populateEditSectCombo();
                if(studentSection.getItemCount() == 0){
                    studentSection.addItem("No sections available");
                    studentSection.setEnabled(false);
                }
                else{
                    studentSection.removeAllItems();
                    populateEditSectCombo();
                    studentSection.setEnabled(true);
                }                 
        }
        else{
            studentSection.removeAllItems();
            populateCreateSectCombo();
                if(studentSection.getItemCount() == 0){
                    studentSection.addItem("No sections available");
                    studentSection.setEnabled(false);
                }
                else{
                    studentSection.removeAllItems();
                    populateCreateSectCombo();    
                    studentSection.setEnabled(true);
                }            
        }
    }//GEN-LAST:event_createStudentYearActionPerformed

    private void studentImageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentImageBtnActionPerformed
        // TODO add your handling code here:
        int result = chooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = chooser.getSelectedFile();
            imagePath = selectedFile.getAbsolutePath();
            studentPhoto.setIcon(resizeImage(imagePath,studentPhoto));           
        }
        else if(result == JFileChooser.CANCEL_OPTION){
            System.out.println("No File selected");
        }
    }//GEN-LAST:event_studentImageBtnActionPerformed

    private void studentEnableEditItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_studentEnableEditItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange()==ItemEvent.SELECTED){
            studentSave.setEnabled(true);
            studentEdit.setEnabled(true);
            studentRevert.setEnabled(true);
            studentDelete.setEnabled(true);
            studentCreate.setEnabled(false);
            studentClear.setEnabled(false);
            resetStudentCreateBorder();
            clearStudentForm();
        } 
        else if(evt.getStateChange()==ItemEvent.DESELECTED){
            disableStudentTableButtons();
            studentCreate.setEnabled(true);
            studentClear.setEnabled(true);
            createStudentYear.setSelectedIndex(0);
            resetStudentCreateBorder();
            clearStudentForm();
        }
    }//GEN-LAST:event_studentEnableEditItemStateChanged

    private void studentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentTableMouseClicked
        // TODO add your handling code here:
        TableModel model = studentTable.getModel();
        int row =  studentTable.getSelectedRow();
            if(isInteger(model.getValueAt(row,1).toString())){
            studentTableId  = Integer.parseInt(model.getValueAt(row,1).toString());
            }
    }//GEN-LAST:event_studentTableMouseClicked

    private void studentEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentEditActionPerformed
        // TODO add your handling code here:
        String getStudInfo = "SELECT * FROM student where studentId =?";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if(studentTableId == -1){
            JOptionPane.showMessageDialog(null,"No student selected");
        }
        else{
            try{
                
            //get student information getStudInfo
            pstmt = con.prepareStatement(getStudInfo);
            pstmt.setInt(1,studentTableId);
            rs = pstmt.executeQuery();
                if(rs.next()){
                    imagePath =null;                    
                    byte[] img =rs.getBytes("image");
                    ImageIcon image = new ImageIcon(img);
                    Image im = image.getImage();
                    Image myImg = im.getScaledInstance(studentPhoto.getWidth(),studentPhoto.getHeight(),Image.SCALE_SMOOTH);
                    ImageIcon newImage = new ImageIcon(myImg);
                    studentPhoto.setIcon(newImage);
                    
                    studentId.setText(""+rs.getInt("studentId"));
                    studentFn.setText(rs.getString("Firstname"));
                    studentMn.setText(rs.getString("Middlename"));
                    studentLn.setText(rs.getString("Lastname"));
                    studentDbirth.setDate(rs.getDate("D_birth"));
                    String gender = rs.getString("Gender");
                        if(Objects.equals(gender, "Male")){
                            studentMale.setSelected(true);
                        }
                        else if(Objects.equals(gender,"Female")){
                            studentFemale.setSelected(true);
                        }
                    studentCitizenship.setText(rs.getString("Citizenship"));
                    studentReligion.setText(rs.getString("Religion"));
                    studentPassword.setText(rs.getString("Password"));
                    createStudentYear.setSelectedItem(rs.getInt("yearLevel"));                    
                }
                
                populateEditSectCombo();
                if(studentSection.getItemCount() == 0){
                    studentSection.addItem("No sections available");
                    studentSection.setEnabled(false);
                }
                else{
                    studentSection.removeAllItems();
                    populateEditSectCombo();
                    studentSection.setEnabled(true);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                if(rs != null){
                    try{
                        rs.close();
                    }catch(SQLException e){}
                }
                if(pstmt != null){
                    try{
                        pstmt.close();
                    }catch(SQLException e){}
                }
                if(con != null){
                    try{
                        con.close();
                    }catch(SQLException e){}
                }
            }
        }
    }//GEN-LAST:event_studentEditActionPerformed

    private void studentRevertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentRevertActionPerformed
        // TODO add your handling code here:
        String getStudInfo = "SELECT * FROM student where studentId =?";
        String getStudSect = "SELECT section.sectionId, section.sectionName \n" +
                            "FROM section\n" +
                            "INNER JOIN student_section\n" +
                            "ON section.sectionId = student_section.sectionId \n" +
                            "AND student_section.studentId = ?";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if(studentId.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "No student selected");
        }
        else{
            try{
                imagePath =null;
                //get student information getStudInfo
                pstmt = con.prepareStatement(getStudInfo);
                pstmt.setInt(1,studentTableId);
                rs = pstmt.executeQuery();
                    if(rs.next()){

                        byte[] img =rs.getBytes("image");
                        ImageIcon image = new ImageIcon(img);
                        Image im = image.getImage();
                        Image myImg = im.getScaledInstance(studentPhoto.getWidth(),studentPhoto.getHeight(),Image.SCALE_SMOOTH);
                        ImageIcon newImage = new ImageIcon(myImg);
                        studentPhoto.setIcon(newImage);

                        studentId.setText(""+rs.getInt("studentId"));
                        studentFn.setText(rs.getString("Firstname"));
                        studentMn.setText(rs.getString("Middlename"));
                        studentLn.setText(rs.getString("Lastname"));
                        studentDbirth.setDate(rs.getDate("D_birth"));
                        String gender = rs.getString("Gender");
                            if(Objects.equals(gender, "Male")){
                                studentMale.setSelected(true);
                            }
                            else if(Objects.equals(gender,"Female")){
                                studentFemale.setSelected(true);
                            }
                        studentCitizenship.setText(rs.getString("Citizenship"));
                        studentReligion.setText(rs.getString("Religion"));
                        studentPassword.setText(rs.getString("Password"));
                        createStudentYear.setSelectedItem(rs.getInt("yearLevel"));
                        
                        pstmt = con.prepareStatement(getStudSect);
                        pstmt.setInt(1,Integer.parseInt(studentId.getText().toString()));
                        rs = pstmt.executeQuery();
                            if(rs.next()){
                                String item = rs.getInt("sectionId")+" - "+rs.getString("sectionName");
                                studentSection.setSelectedItem(item);
                            }
                        
                    }
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                if(rs != null){
                    try{
                        rs.close();
                    }catch(SQLException e){}
                }
                if(pstmt != null){
                    try{
                        pstmt.close();
                    }catch(SQLException e){}
                }
                if(con != null){
                    try{
                        con.close();
                    }catch(SQLException e){}
                }
            }
        }
    }//GEN-LAST:event_studentRevertActionPerformed

    private void studentDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentDeleteActionPerformed
        // TODO add your handling code here:
        String getSect = "SELECT sectionId from student_section WHERE studentId=?";
        String deleteStud = "DELETE FROM student WHERE studentId=?";
        String getSectCurStud = "SELECT COUNT(*) as 'count' FROM student_section WHERE sectionId=?";
        String updateSection = "UPDATE section set sectionCurStud=? WHERE sectionId=?";
        
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int sectId = 0;
        int curStud = 0;
        if(studentTableId == -1){
            JOptionPane.showMessageDialog(null, "No student selected");
        }
        else{
            try{
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete student?", "WARNING", JOptionPane.YES_NO_OPTION);
                if(reply == JOptionPane.YES_OPTION){
                    //get current sect
                    pstmt = con.prepareStatement(getSect);
                    pstmt.setInt(1,studentTableId);
                    rs = pstmt.executeQuery();
                        if(rs.next()){
                            sectId = rs.getInt("sectionId");
                        }
                    //delete student    
                    pstmt = con.prepareStatement(deleteStud);
                    pstmt.setInt(1,studentTableId);
                    pstmt.executeUpdate();

                    //get sect cur stud
                    pstmt = con.prepareStatement(getSectCurStud);
                    pstmt.setInt(1, sectId);
                    rs = pstmt.executeQuery();
                        if(rs.next()){
                            curStud = rs.getInt("count");
                        }
                    System.out.println(""+curStud);
                    //update section stud count
                    pstmt = con.prepareStatement(updateSection);
                    pstmt.setInt(1,curStud);
                    pstmt.setInt(2,sectId);
                    pstmt.executeUpdate();
                    
                    JOptionPane.showMessageDialog(null,"Student has been deleted");
                    drawStudentTable();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_studentDeleteActionPerformed

    private void studentSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentSaveActionPerformed
        // TODO add your handling code here:
        if(studentId.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "No student selected");
        }
        else{
            String getCurrentSect = "SELECT section.sectionId,section.sectionName,section.yearLevel\n" +
                                " FROM section\n" +
                                "INNER JOIN student_section\n" +
                                "ON student_section.sectionId = section.sectionId AND student_section.studentId =?";
            String update_student = "UPDATE student set Firstname=?,Middlename=?,Lastname=?,D_birth=?,Gender=?,Citizenship=?,Religion=?,Password=?,yearLevel =? "
                                    + "WHERE studentId=?";
            String update_studentwithImage = "UPDATE student set Firstname=?,Middlename=?,Lastname=?,D_birth=?,Gender=?,Citizenship=?,Religion=?,Password=?,yearLevel =?,image=? "
                                + "WHERE studentId=?";
            String delete_stud_fromSect = "DELETE FROM student_section where studentId=?";
            String get_currentstud_query = "SELECT COUNT(*) AS 'studentCount' FROM student_section WHERE sectionId= ?";
            String update_section = "UPDATE section SET sectionCurStud=? WHERE sectionId=?";
            String insert_stud_sect ="INSERT INTO student_section(sectionId,studentId) VALUES(?,?)";
            
            int curSect = 0;
            int updatedSect = 0;
            int curStud = 0;            
            String splitSect[] = studentSection.getSelectedItem().toString().split("\\s+");
            updatedSect = Integer.parseInt(splitSect[0]);
            
            Connection con = connect.getConnection();
            PreparedStatement pstmt =null;
            ResultSet rs = null;
            
                try{
                    boolean check = checkStudentCreateForm();
                        if(check){
                            
                            pstmt = con.prepareStatement(getCurrentSect);
                            pstmt.setInt(1,Integer.parseInt(studentId.getText().toString()));
                            rs = pstmt.executeQuery();
                                if(rs.next()){
                                    curSect = rs.getInt("sectionId");
                                }
                            if(imagePath == null){
                                pstmt = con.prepareStatement(getCurrentSect);
                                pstmt.setInt(1,Integer.parseInt(studentId.getText().toString()));
                                rs = pstmt.executeQuery();
                                    if(rs.next()){
                                        curSect = rs.getInt("sectionId");
                                    }

                                //update student info    
                                pstmt = con.prepareStatement(update_student);
                                pstmt.setString(1,studentFn.getText());
                                pstmt.setString(2,studentMn.getText());
                                pstmt.setString(3,studentLn.getText());
                                java.sql.Date sqlDate = new java.sql.Date(studentDbirth.getDate().getTime());
                                pstmt.setDate(4,sqlDate);
                                    if(studentMale.isSelected()){
                                        pstmt.setString(5,"Male");
                                    }
                                    else if(studentFemale.isSelected()){
                                        pstmt.setString(5,"Female");
                                    }

                                pstmt.setString(6,studentCitizenship.getText());
                                pstmt.setString(7,studentReligion.getText());
                                pstmt.setString(8,studentPassword.getText());
                                pstmt.setInt(9,Integer.parseInt(createStudentYear.getSelectedItem().toString()));
                                pstmt.setInt(10,Integer.parseInt(studentId.getText().toString()));
                                pstmt.executeUpdate();  
                            }        
                            //update student info
                            else{
                                pstmt = con.prepareStatement(update_studentwithImage);
                                pstmt.setString(1,studentFn.getText());
                                pstmt.setString(2,studentMn.getText());
                                pstmt.setString(3,studentLn.getText());
                                java.sql.Date sqlDate = new java.sql.Date(studentDbirth.getDate().getTime());
                                pstmt.setDate(4,sqlDate);
                                    if(studentMale.isSelected()){
                                        pstmt.setString(5,"Male");
                                    }
                                    else if(studentFemale.isSelected()){
                                        pstmt.setString(5,"Female");
                                    }

                                pstmt.setString(6,studentCitizenship.getText());
                                pstmt.setString(7,studentReligion.getText());
                                pstmt.setString(8,studentPassword.getText());
                                pstmt.setInt(9,Integer.parseInt(createStudentYear.getSelectedItem().toString()));
                                InputStream is = new FileInputStream(new File(imagePath));
                                pstmt.setBlob(10,is);    
                                pstmt.setInt(11,Integer.parseInt(studentId.getText().toString()));
                                pstmt.executeUpdate();
                            }
                            //check if section changed
                            if(curSect == updatedSect){
                                //do nothing
                            }
                            else if(curSect != updatedSect){
                                //delete from previous sect
                                pstmt = con.prepareStatement(delete_stud_fromSect);
                                pstmt.setInt(1,Integer.parseInt(studentId.getText().toString()));
                                pstmt.executeUpdate();

                                //get prev sect stud count
                                pstmt =  con.prepareStatement(get_currentstud_query);
                                pstmt.setInt(1, curSect);
                                rs = pstmt.executeQuery();
                                    if(rs.next()){
                                        curStud = rs.getInt("studentCount");
                                    }

                                //update prev sect stud count
                                pstmt = con.prepareStatement(update_section);
                                pstmt.setInt(1,curStud);
                                pstmt.setInt(2,curSect);
                                pstmt.executeUpdate();

                                //insert into new section
                                pstmt = con.prepareStatement(insert_stud_sect);
                                pstmt.setInt(1,updatedSect);
                                pstmt.setInt(2,Integer.parseInt(studentId.getText().toString()));
                                pstmt.executeUpdate();

                                //get updated sect cur stud
                                pstmt = con.prepareStatement(get_currentstud_query);
                                pstmt.setInt(1, updatedSect);
                                rs = pstmt.executeQuery();
                                    if(rs.next()){
                                        curStud = rs.getInt("studentCount");
                                    }
                                //update updated sect stud count
                                pstmt = con.prepareStatement(update_section);
                                pstmt.setInt(1,curStud);
                                pstmt.setInt(2,updatedSect);
                                pstmt.executeUpdate();

                            }
                            JOptionPane.showMessageDialog(null,"Student has been updated");
                            imagePath =null;
                            drawStudentTable();
                            setSectionStatus();
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Please fill the form properly");
                        }
                }catch(SQLException e){
                    e.printStackTrace();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                if(rs != null){
                    try{
                        rs.close();
                    }catch(SQLException e){}
                }
                if(pstmt != null){
                    try{
                        pstmt.close();
                    }catch(SQLException e){}
                }
                if(con != null){
                    try{
                        con.close();
                    }catch(SQLException e){}
                }
            }
        }
    }//GEN-LAST:event_studentSaveActionPerformed

    private void studentSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentSearchMouseClicked
        // TODO add your handling code here:
        studentSearch.setText("");
    }//GEN-LAST:event_studentSearchMouseClicked

    private void studentSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentSearchBtnActionPerformed
        // TODO add your handling code here:
        String query = "SELECT student.yearLevel,student.studentId,student.Firstname,student.Lastname,section.sectionId,section.sectionName\n" +
                        "FROM student\n" +
                        "INNER JOIN student_section\n" +
                        "ON \n" +
                        "student.studentId = student_section.studentId\n" +
                        "INNER JOIN section\n" +
                        "ON section.sectionId = student_section.sectionId AND student.studentId = ?";
        Connection con = connect.getConnection();
        PreparedStatement pstmt =null;
        ResultSet rs = null;
        try{
            if(isInteger(studentSearch.getText())){
                pstmt = con.prepareStatement(query);
                pstmt.setInt(1,Integer.parseInt(studentSearch.getText()));
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        MyModel model = new MyModel(rs,"Student");
                        studentTable.setModel(model);
                        model.fireTableStructureChanged();
                        model.fireTableDataChanged();
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Student not found");
                    }
            }
            else{
                JOptionPane.showMessageDialog(null,"Invalid Id");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_studentSearchBtnActionPerformed

    private void cancelChangePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelChangePassActionPerformed
        // TODO add your handling code here:
        currentPass.setText("");
        newPass.setText("");
        validateNewPass.setText("");
    }//GEN-LAST:event_cancelChangePassActionPerformed

    private void changePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePassActionPerformed
        // TODO add your handling code here:
        Border border = BorderFactory.createLineBorder(Color.red);
        String query = "UPDATE registrar set Password=? WHERE registrarId=?";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        try{
            String old_pass = main_rs.getString("Password");
                if(Objects.equals(old_pass, currentPass.getText())){
                    currentPass.setBorder(resetborder);
                    if(Objects.equals(newPass.getText(),validateNewPass.getText())){
                         newPass.setBorder(resetborder);
                         validateNewPass.setBorder(resetborder);
                         
                         pstmt = con.prepareStatement(query);
                         pstmt.setString(1,validateNewPass.getText());
                         pstmt.setInt(2,main_rs.getInt("registrarId"));
                         pstmt.executeUpdate();
                         JOptionPane.showMessageDialog(null, "Password successfully changed");
                         currentPass.setText("");
                         newPass.setText("");
                         validateNewPass.setText("");
                    }
                    else{
                        newPass.setBorder(border);
                        validateNewPass.setBorder(border);
                        JOptionPane.showMessageDialog(null, "Please validate your password");
                    }
                }
                else{
                    currentPass.setBorder(border);
                    JOptionPane.showMessageDialog(null,"Incorrect Password");
                }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch(Exception e){}
            }
            if(con != null){
                try{
                    con.close();
                }catch(Exception e){}
            }
        }
    }//GEN-LAST:event_changePassActionPerformed

    private void sectCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectCancelActionPerformed
        // TODO add your handling code here:
        clearSectForm();
    }//GEN-LAST:event_sectCancelActionPerformed

    private void sectCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectCreateActionPerformed
        // TODO add your handling code here:
        String checkSectionName = "SELECT * FROM section where sectionName=? AND yearLevel=?";
        String checkSectionRoom = "SELECT * FROM section where sectionRoom=?";
        String createSection ="INSERT INTO section(yearLevel,sectionName,sectionMaxStud,sectionRoom) VALUES(?,?,?,?)";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            if(checkSectionCreateForm()){
                pstmt = con.prepareStatement(checkSectionName);
                pstmt.setString(1,sectionName.getText());
                pstmt.setInt(2,Integer.parseInt(sectionYearCombo.getSelectedItem().toString()));
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        JOptionPane.showMessageDialog(null,"Section name already exists");
                    }
                    else{
                        pstmt = con.prepareStatement(checkSectionRoom);
                        pstmt.setInt(1, Integer.parseInt(sectionRoom.getText()));
                        rs =  pstmt.executeQuery();
                            if(rs.next()){
                                JOptionPane.showMessageDialog(null,"Room is already assigned to a section");
                            }
                            else{
                                pstmt = con.prepareStatement(createSection);
                                pstmt.setInt(1,Integer.parseInt(sectionYearCombo.getSelectedItem().toString()));
                                pstmt.setString(2,sectionName.getText());
                                pstmt.setInt(3,(Integer) maxStud.getValue());
                                pstmt.setInt(4,Integer.parseInt(sectionRoom.getText()));
                                pstmt.executeUpdate();
                                JOptionPane.showMessageDialog(null,"Section has been created");
                                drawSectionTable();
                            }
                    }                                   
            }
            else{
                JOptionPane.showMessageDialog(null,"Please fill the form properly");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(rs != null){
                try{
                    rs.close();
                }catch(SQLException e){}
            }
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch(SQLException e){}
            }
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
        
        
    }//GEN-LAST:event_sectCreateActionPerformed

    private void sectionsMenuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sectionsMenuItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange()==ItemEvent.SELECTED){
        clearSectForm();
        drawSectionTable();
        disableSectTableButtons();
        sectionEnableEdit.setSelected(false);
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel,"section");
        sectionTableId = -1;
        } 
        else if(evt.getStateChange()==ItemEvent.DESELECTED){
        }
    }//GEN-LAST:event_sectionsMenuItemStateChanged

    private void sectTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sectTableMouseClicked
        // TODO add your handling code here:
        TableModel model = sectTable.getModel();
        int row =  sectTable.getSelectedRow();
            if(isInteger(model.getValueAt(row,1).toString())){
            sectionTableId  = Integer.parseInt(model.getValueAt(row,1).toString());
            }
    }//GEN-LAST:event_sectTableMouseClicked

    private void sectionEnableEditItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sectionEnableEditItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange()==ItemEvent.SELECTED){
            sectCreate.setEnabled(false);
            sectCancel.setEnabled(false);
            sectShowList.setEnabled(true);
            sectSave.setEnabled(true);
            sectEdit.setEnabled(true);
            sectRevert.setEnabled(true);
            sectDelete.setEnabled(true);
            resetSectionCreateBorder();
            clearSectForm();
        } 
        else if(evt.getStateChange()==ItemEvent.DESELECTED){
            sectCreate.setEnabled(true);
            sectCancel.setEnabled(true);
            disableSectTableButtons();
            resetSectionCreateBorder();
            clearSectForm();
        }
    }//GEN-LAST:event_sectionEnableEditItemStateChanged

    private void sectionSearchTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sectionSearchTextMouseClicked
        // TODO add your handling code here:
        sectionSearchText.setText("");
    }//GEN-LAST:event_sectionSearchTextMouseClicked

    private void studentSearchBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentSearchBtn1ActionPerformed
        // TODO add your handling code here:
        String query = "SELECT yearLevel, sectionId,sectionName,sectionCurStud,sectionMaxStud,sectionRoom,sectionIsFull FROM section WHERE sectionId=? ORDER BY yearLevel";
        Connection con = connect.getConnection();
        PreparedStatement pstmt =null;
        ResultSet rs = null;
        try{
            if(isInteger(sectionSearchText.getText())){
                pstmt = con.prepareStatement(query);
                pstmt.setInt(1,Integer.parseInt(sectionSearchText.getText()));
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        MyModel model = new MyModel(rs,"Section");
                        sectTable.setModel(model);
                        model.fireTableStructureChanged();
                        model.fireTableDataChanged();
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Section not found");
                    }
            }
            else{
                JOptionPane.showMessageDialog(null,"Invalid Id");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_studentSearchBtn1ActionPerformed

    private void sectEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectEditActionPerformed
        // TODO add your handling code here:
        String query = "SELECT * FROM section WHERE sectionId =?";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            if(sectionTableId == -1){
                JOptionPane.showMessageDialog(null,"No student selected");                                
            }
            else{
                pstmt = con.prepareStatement(query);
                pstmt.setInt(1,sectionTableId);
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        sectionIdText.setText(""+rs.getInt("sectionId"));
                        sectionName.setText(rs.getString("sectionName"));
                        sectionYearCombo.setSelectedItem(String.valueOf(rs.getInt("yearLevel")));
                        maxStud.setValue(rs.getInt("sectionMaxStud"));
                        sectionRoom.setText(""+rs.getInt("sectionRoom"));
                    }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(rs != null){
                try{
                    rs.close();
                }catch(SQLException e){}
            }
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch(SQLException e){}
            }
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }       
    }//GEN-LAST:event_sectEditActionPerformed

    private void sectSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectSaveActionPerformed
        // TODO add your handling code here:
        String checkSectionName = "SELECT * FROM section where sectionName=? AND yearLevel=? AND sectionId!= ?";
        String checkSectionRoom = "SELECT * FROM section where sectionRoom=? AND sectionId!= ?";
        String updateSection = "UPDATE section set sectionName=?, sectionMaxStud=?, sectionRoom=? WHERE sectionId=?";
        String getCurStud = "SELECT sectionCurStud from section where sectionId=?";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //String query = "UPDATE section set sectionName = ? section"
        try{
            if(sectionIdText.getText().length() == 0){
                JOptionPane.showMessageDialog(null, "No section selected");
            }
            else{
                pstmt = con.prepareStatement(checkSectionName);
                pstmt.setString(1,sectionName.getText());
                pstmt.setInt(2,Integer.parseInt(sectionYearCombo.getSelectedItem().toString()));
                pstmt.setInt(3,Integer.parseInt(sectionIdText.getText()));
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        JOptionPane.showMessageDialog(null,"Room name already exists");
                    }
                    else{
                        pstmt = con.prepareStatement(checkSectionRoom);
                        pstmt.setInt(1,Integer.parseInt(sectionRoom.getText()));
                        pstmt.setInt(2,Integer.parseInt(sectionIdText.getText()));
                        rs = pstmt.executeQuery();
                            if(rs.next()){
                                JOptionPane.showMessageDialog(null,"Room is already assigned to a section");
                            }
                            else{
                                pstmt = con.prepareStatement(getCurStud);
                                pstmt.setInt(1,sectionTableId);
                                rs = pstmt.executeQuery();
                                    if(rs.next()){
                                        if((Integer) maxStud.getValue() < rs.getInt("sectionCurStud")){
                                            JOptionPane.showMessageDialog(null,"Can't set maximum student number to below current students in room");
                                        }
                                        else{
                                            pstmt = con.prepareStatement(updateSection);
                                            pstmt.setString(1,sectionName.getText());
                                            pstmt.setInt(2,(Integer) maxStud.getValue());
                                            pstmt.setInt(3,Integer.parseInt(sectionRoom.getText()));
                                            pstmt.setInt(4,sectionTableId);
                                            pstmt.executeUpdate();
                                            JOptionPane.showMessageDialog(null,"Section has been updated");
                                            setSectionStatus();
                                            drawSectionTable();
                                        }
                                    }
                            }
                    }
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(rs != null){
                try{
                    rs.close();
                }catch(SQLException e){}
            }
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch(SQLException e){}
            }
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
    }//GEN-LAST:event_sectSaveActionPerformed

    private void sectRevertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectRevertActionPerformed
        // TODO add your handling code here:
        String query = "SELECT * FROM section where sectionId=?";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if(sectionIdText.getText().length() == 0){
            JOptionPane.showMessageDialog(null,"No section selected");
        }
        else{
            try{
                pstmt = con.prepareStatement(query);
                pstmt.setInt(1,sectionTableId);
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        sectionIdText.setText(""+rs.getInt("sectionId"));
                        sectionName.setText(rs.getString("sectionName"));
                        sectionYearCombo.setSelectedItem(rs.getInt("yearLevel"));
                        maxStud.setValue(rs.getInt("sectionMaxStud"));
                        sectionRoom.setText(""+rs.getInt("sectionRoom"));
                    }
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                if(rs != null){
                    try{
                        rs.close();
                    }catch(SQLException e){}
                }
                if(pstmt != null){
                    try{
                        pstmt.close();
                    }catch(SQLException e){}
                }
                if(con != null){
                    try{
                        con.close();
                    }catch(SQLException e){}
                }
            }
        }
        
    }//GEN-LAST:event_sectRevertActionPerformed

    private void sectDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectDeleteActionPerformed
        // TODO add your handling code here:
        String checkIfSectHasStud = "SELECT sectionCurStud FROM section WHERE sectionId=?";
        String deleteSection = "DELETE FROM section where sectionId=?";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if(sectionTableId == -1){
            JOptionPane.showMessageDialog(null,"No section selected");
        }
        else{
            try{
            pstmt = con.prepareStatement(checkIfSectHasStud);
            pstmt.setInt(1,sectionTableId);
            rs = pstmt.executeQuery();
                if(rs.next()){
                    if(rs.getInt("sectionCurStud") != 0){
                        JOptionPane.showMessageDialog(null,"Please transfer students first before deleting the section");
                    }
                    else{
                        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete section?", "WARNING", JOptionPane.YES_NO_OPTION);
                            if(reply == JOptionPane.YES_OPTION){
                                pstmt = con.prepareStatement(deleteSection);
                                pstmt.setInt(1,sectionTableId);
                                pstmt.executeUpdate();
                                JOptionPane.showMessageDialog(null,"Section has been deleted");
                                drawSectionTable();
                                sectionTableId = -1;
                            }
                    }
                }            
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                if(rs != null){
                    try{
                        rs.close();
                    }catch(SQLException e){}
                }
                if(pstmt != null){
                    try{
                        pstmt.close();
                    }catch(SQLException e){}
                }
                if(con != null){
                    try{
                        con.close();
                    }catch(SQLException e){}
                }
            }
        }
    }//GEN-LAST:event_sectDeleteActionPerformed

    private void sectShowListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectShowListActionPerformed
        // TODO add your handling code here:
        String getList = "SELECT student.yearLevel,student.studentId,student.Firstname,student.Lastname,student.Gender\n" +
                        "FROM student\n" +
                        "INNER JOIN student_section\n" +
                        "ON student.studentId = student_section.studentId AND student_section.sectionId =?";
        Connection con = connect.getConnection();
        PreparedStatement pstmt =null;
        ResultSet rs = null;
        if(sectionTableId == -1){
            JOptionPane.showMessageDialog(null,"No section selected");
        }
        else{
            try{
            pstmt = con.prepareStatement(getList);
            pstmt.setInt(1, sectionTableId);
            rs = pstmt.executeQuery();
                if(rs.next()){
                    ShowStudentListFrame  show = new ShowStudentListFrame(sectionTableId);
                    show.setVisible(true);
                    show.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    sectionTableId = -1;
                }
                else{
                    JOptionPane.showMessageDialog(null,"Section is empty");
                    sectionTableId = -1;
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_sectShowListActionPerformed

    private void studentSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studentSearchActionPerformed

    private void billingMenuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_billingMenuItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange()==ItemEvent.SELECTED){
            billIcon.setIcon(null);
            drawBillingTables();
            populateBillCombo();
            payAllBox.setSelected(false);
            payAllBox.setEnabled(false);
            amtRecieved.setEnabled(false);
            billingPay.setEnabled(false);
            billingTableId = -1;
            clearBillingForm();
            payAllBox.setSelected(false);
            billingSearchText.setText("Enter student ID: ");
            CardLayout card = (CardLayout) mainPanel.getLayout();
            card.show(mainPanel,"bill");
        } 
            else if(evt.getStateChange()==ItemEvent.DESELECTED){
        }
    }//GEN-LAST:event_billingMenuItemStateChanged

    private void billingSearchTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billingSearchTextMouseClicked
        // TODO add your handling code here:
        billingSearchText.setText("");
    }//GEN-LAST:event_billingSearchTextMouseClicked

    private void billingSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billingSearchBtnActionPerformed
        // TODO add your handling code here:
        //drawBillingTable(int id)
        String getStud = "select student.Firstname,student.Lastname, student_account.studentId,student_account.balance,student.image,student_account.totalPayable\n" +
                        "FROM student\n" +
                        "INNER JOIN student_account\n" +
                        "ON student.studentId = student_account.studentId and student.studentId = ?";
        String query = "select student_bill.billId,fee.feeName, fee.feeAmount,student_bill.billDate\n" +
                        "FROM student_bill\n" +
                        "INNER JOIN fee\n" +
                        "ON fee.feeId = student_bill.feeId and studentId =? AND student_bill.paid = 'Not Paid' ";         
        Connection con = connect.getConnection();
        PreparedStatement pstmt =null;
        ResultSet rs = null;
        try{
            if(isInteger(billingSearchText.getText())){
                pstmt = con.prepareStatement(getStud);
                pstmt.setInt(1,Integer.parseInt(billingSearchText.getText()));
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        byte[] img =rs.getBytes("image");
                        ImageIcon image = new ImageIcon(img);
                        Image im = image.getImage();
                        Image myImg = im.getScaledInstance(billIcon.getWidth(),billIcon.getHeight(),Image.SCALE_SMOOTH);
                        ImageIcon newImage = new ImageIcon(myImg);
                        billIcon.setIcon(newImage);
                        
                    studentPhoto.setIcon(newImage);
                        billingStudId.setText(""+rs.getInt("studentId"));
                        billingStudName.setText(rs.getString("Firstname")+" "+rs.getString("Lastname"));
                        studentTotalBalance.setText(""+rs.getDouble("balance"));
                        totalPayable.setText(""+rs.getDouble("totalPayable"));
                        payAllBox.setEnabled(true);
                        amtRecieved.setEnabled(true);
                        billingPay.setEnabled(true);
                        pstmt = con.prepareStatement(query);
                        pstmt.setInt(1,Integer.parseInt(billingSearchText.getText()));
                        rs = pstmt.executeQuery();
                            MyModel model = new MyModel(rs,"Billing");
                            billingTable.setModel(model);
                            model.fireTableStructureChanged();
                            model.fireTableDataChanged();
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Student not found");
                    }
            }
            else{
                JOptionPane.showMessageDialog(null,"Invalid Id");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_billingSearchBtnActionPerformed

    private void billingTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billingTableMouseClicked
        // TODO add your handling code here:
         TableModel model = billingTable.getModel();       
        int row =  billingTable.getSelectedRow();
        int col = billingTable.getSelectedColumn();
        
        double amt = 200 ;
            if(isInteger(model.getValueAt(row,0).toString())){
                if(!payAllBox.isSelected()){
                    billingTableId  = Integer.parseInt(model.getValueAt(row,0).toString());
                    amt = Double.parseDouble(model.getValueAt(row,2).toString());
                    billingPayable.setText(""+amt);
                }
            }
           

            //boolean selected;
            /*
            //if(isInteger(model.getValueAt(row,1).toString())){
            billingTableId  = Integer.parseInt(model.getValueAt(row,2).toString());
            selected = (boolean) model.getValueAt(row,4);
                if(col == 4){
                    if(selected){
                        System.out.println("Hello");
                        totalBill = totalBill + Double.parseDouble(model.getValueAt(row,2).toString());
                    }
                    else{

                        totalBill = totalBill - Double.parseDouble(model.getValueAt(row,2).toString());
                    }
                }
                 billingPayable.setText(""+totalBill);
           }
            */
            
            
    }//GEN-LAST:event_billingTableMouseClicked

    private void addBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBillActionPerformed
        // TODO add your handling code here:
        String getFeeId = "SELECT * FROM fee WHERE feeName=?";
        String addBill = "INSERT INTO student_bill(studentId,feeId,amount) VALUES(?,?,?)";
        String getTotalBalance = "SELECT * FROM student_bill WHERE studentId=? AND paid ='Not Paid'";
        String updateBalance = "UPDATE student_account set totalPayable= totalPayable + ? , balance=? WHERE studentId=?";
        String getAccount = "SELECT * FROM student_account where studentId=?";
        
        
        Connection con  = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;        
        int feeId = 0;
        int balance = 0;
        double feeAmt = 0;
        if(billingStudId.getText().length() == 0){
            JOptionPane.showMessageDialog(null,"No student selected");
        }
        else{
            try{
                pstmt = con.prepareStatement(getFeeId);
                pstmt.setString(1,billingCombo.getSelectedItem().toString());
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        feeId = rs.getInt("feeId");
                        feeAmt = rs.getDouble("feeAmount");
                    }
                pstmt = con.prepareStatement(addBill);
                pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                pstmt.setInt(2,feeId);
                pstmt.setDouble(3,feeAmt);
                pstmt.executeUpdate();
                
                
                pstmt = con.prepareStatement(getTotalBalance);
                pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                rs = pstmt.executeQuery();
                    while(rs.next()){
                        balance += rs.getDouble("amount");
                    }
                
                pstmt = con.prepareStatement(updateBalance);
                pstmt.setDouble(1,feeAmt);
                pstmt.setDouble(2,balance);
                pstmt.setInt(3,Integer.parseInt(billingStudId.getText()));
                pstmt.executeUpdate();
                
                
                pstmt = con.prepareStatement(getAccount);
                pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                rs = pstmt.executeQuery();
                    if(rs.next()){
                        studentTotalBalance.setText(""+rs.getDouble("balance"));
                        totalPayable.setText(""+rs.getDouble("totalPayable"));
                    }
                drawBillingTable(Integer.parseInt(billingStudId.getText()));
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_addBillActionPerformed

    private void payAllBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_payAllBoxItemStateChanged
        // TODO add your handling code here:
        String query = "SELECT * FROM student_bill WHERE studentId=? AND paid = 'Not Paid' ";
        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double payable = 0;
        if(evt.getStateChange()==ItemEvent.SELECTED){
            try{
                pstmt = con.prepareStatement(query);
                pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                rs = pstmt.executeQuery();
                if(rs.next()){
                    rs.beforeFirst();
                        while(rs.next()){
                            payable += rs.getDouble("amount");
                        }
                }
                else{
                    JOptionPane.showMessageDialog(null,"No bill to be paid");
                }
                    billingPayable.setText(""+payable);
            }catch(SQLException e){
                e.printStackTrace();
            }
            
        } 
            else if(evt.getStateChange()==ItemEvent.DESELECTED){
                billingPayable.setText("");
        }
        
    }//GEN-LAST:event_payAllBoxItemStateChanged

    private void billingPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billingPayActionPerformed

        Connection con = connect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if(billingPayable.getText().length() == 0){
            JOptionPane.showMessageDialog(null,"No bill selected");
        }
        else{
           // if(billingPayable.getText())
            if(payAllBox.isSelected()){
                String updateStudBill = "UPDATE student_bill set amountPaid = amount,paid='Paid' WHERE studentId =?";
                String updateAccount = "UPDATE student_account set balance = 0, paid =paid+ ? WHERE studentId=?";
                String checkBill = "SELECT * from student_bill where studentId=? and paid ='Not Paid'";
                String getAccount = "SELECT * FROM student_account where studentId=?";
                String getReport = "INSERT INTO transactions(billId,studentId,amount,paid,changeAmount) VALUES(?,?,?,?,?)";
                double change = 0;
                try{
                    if(isInteger(amtRecieved.getText()) && amtRecieved.getText().length() > 0){
                        if(Double.parseDouble(amtRecieved.getText().toString()) >= Double.parseDouble(billingPayable.getText().toString())){
                            pstmt = con.prepareStatement(checkBill);
                            pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                            rs = pstmt.executeQuery();    
                                if(rs.next()){
                                    change =  Double.parseDouble(amtRecieved.getText())  - Double.parseDouble(billingPayable.getText()) ;
                                    billChange.setText(""+change);
                                    pstmt = con.prepareStatement(checkBill);
                                    pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                                    rs = pstmt.executeQuery();
                                        while(rs.next()){                                   
                                            pstmt = con.prepareStatement(getReport);
                                            pstmt.setInt(1,rs.getInt("billId"));
                                            pstmt.setInt(2,Integer.parseInt(billingStudId.getText()));
                                            pstmt.setDouble(3,rs.getDouble("amount"));
                                            pstmt.setDouble(4,Double.parseDouble(amtRecieved.getText()));
                                            pstmt.setDouble(5,change);
                                            pstmt.executeUpdate();
                                        }
                                    pstmt = con.prepareStatement(updateStudBill);
                                    pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                                    pstmt.executeUpdate();

                                    pstmt = con.prepareStatement(updateAccount);
                                    pstmt.setDouble(1, Double.parseDouble(billingPayable.getText()));
                                    pstmt.setInt(2,Integer.parseInt(billingStudId.getText()));
                                    pstmt.executeUpdate();                     
                                    pstmt = con.prepareStatement(getAccount);
                                    pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                                    rs = pstmt.executeQuery();
                                        if(rs.next()){
                                            studentTotalBalance.setText(""+rs.getDouble("balance"));
                                            drawBillingTable(Integer.parseInt(billingStudId.getText()));
                                            JOptionPane.showMessageDialog(null,"Bill has been paid");
                                            
                                            amtRecieved.setText("");
                                            billingPayable.setText("");
                                            payAllBox.setSelected(false);
                                        }

                                    //get report
                                }
                                else{
                                    JOptionPane.showMessageDialog(null,"No bill selected");
                                }
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Recieved amount is not enough");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Invalid input");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            else if(!payAllBox.isSelected()){
                String updateStudBill = "UPDATE student_bill set amountPaid = amount, paid='Paid' WHERE studentId=? AND billId =?";
                String updateAccount = "UPDATE student_account set balance=?, paid = paid + ? where studentId=?";
                String getAccount = "SELECT * FROM student_account where studentId=?";
                String getBillAmt = "SELECT * from student_bill where billId=?";
                String getReport = "INSERT INTO transactions(billId,studentId,amount,paid,changeAmount) VALUES(?,?,?,?,?)";
                double currentBal = 0;
                double updatedBal  = 0;
                double amt = 0;
                double change = 0;
                try{
                    //get bil amount
                    if(isInteger(amtRecieved.getText()) && amtRecieved.getText().length() > 0){
                        if(Double.parseDouble(amtRecieved.getText().toString()) >= Double.parseDouble(billingPayable.getText().toString())){   
                            change = Double.parseDouble(amtRecieved.getText())  - Double.parseDouble(billingPayable.getText()) ;
                            billChange.setText(""+change);
                            
                            pstmt = con.prepareStatement(getBillAmt);
                            pstmt.setInt(1,billingTableId);
                            rs = pstmt.executeQuery();
                                if(rs.next()){
                                    amt = rs.getDouble("amount");
                                }

                            //get student account
                            pstmt = con.prepareStatement(getAccount);
                            pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                            rs = pstmt.executeQuery();
                                if(rs.next()){
                                    currentBal = rs.getDouble("balance");                       
                                }

                            //update student bills    
                            pstmt = con.prepareStatement(updateStudBill);
                            pstmt.setInt(1,Integer.parseInt(billingStudId.getText()));
                            pstmt.setInt(2,billingTableId);
                            pstmt.executeUpdate();

                            //update student 
                            updatedBal = currentBal - Double.parseDouble(billingPayable.getText().toString());
                            pstmt = con.prepareStatement(updateAccount);
                            pstmt.setDouble(1,updatedBal);
                            pstmt.setDouble(2,amt);
                            pstmt.setInt(3,Integer.parseInt(billingStudId.getText()));
                            pstmt.executeUpdate();

                            //getrecord
                            pstmt = con.prepareStatement(getReport);
                            pstmt.setInt(1,billingTableId);
                            pstmt.setInt(2,Integer.parseInt(billingStudId.getText()));
                            pstmt.setDouble(3,amt);
                            pstmt.setDouble(4,Double.parseDouble(amtRecieved.getText()));
                            pstmt.setDouble(5,change);
                            pstmt.executeUpdate();

                            studentTotalBalance.setText(""+updatedBal);
                            billChange.setText(""+change);
                            drawBillingTable(Integer.parseInt(billingStudId.getText()));
                            JOptionPane.showMessageDialog(null,"Bill has been paid");
                            amtRecieved.setText("");
                            billingPayable.setText("");
                            payAllBox.setSelected(false);
                        }
                        else{
                        JOptionPane.showMessageDialog(null,"Received amount is not enough");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Invalid input");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_billingPayActionPerformed

    private void logoutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutMenuActionPerformed
        // TODO add your handling code here:
        try{
            main_rs.close();
            this.dispose();
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_logoutMenuActionPerformed

    private void MinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimizeMouseClicked
        // TODO add your handling code here:
        this.setState(LoginFrame.ICONIFIED);
    }//GEN-LAST:event_MinimizeMouseClicked

    private void exitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitMouseClicked

    private void reportsMenuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_reportsMenuItemStateChanged
        // TODO add your handling code here:
        
        if(evt.getStateChange()==ItemEvent.SELECTED){
            drawReportTable();
            reportMonth.setSelectedIndex(0);
            CardLayout card = (CardLayout) mainPanel.getLayout();
            card.show(mainPanel,"reports");
            //System.out.println("button is selected");
        } 
        else if(evt.getStateChange()==ItemEvent.DESELECTED){
            //System.out.println("button is not selected");
        }
    }//GEN-LAST:event_reportsMenuItemStateChanged

    private void reportMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportMonthActionPerformed
        // TODO add your handling code here:
        drawReportTable();
    }//GEN-LAST:event_reportMonthActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Minimize;
    private javax.swing.JButton addBill;
    private javax.swing.JTextField amtRecieved;
    private javax.swing.JTextField billChange;
    private javax.swing.JLabel billIcon;
    private javax.swing.JPanel billPanel;
    private javax.swing.JComboBox<String> billingCombo;
    private javax.swing.JToggleButton billingMenu;
    private javax.swing.JButton billingPay;
    private javax.swing.JTextField billingPayable;
    private javax.swing.JButton billingSearchBtn;
    private javax.swing.JTextField billingSearchText;
    private javax.swing.JTextField billingStudId;
    private javax.swing.JTextField billingStudName;
    private javax.swing.JTable billingTable;
    private javax.swing.JButton cancelChangePass;
    private javax.swing.JButton changePass;
    private javax.swing.JComboBox<String> createStudentYear;
    private javax.swing.JPasswordField currentPass;
    private javax.swing.JLabel exit;
    private javax.swing.JLabel hour;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton logoutMenu;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel mainbackground;
    private javax.swing.JSpinner maxStud;
    private javax.swing.JPanel menubarPanel;
    private javax.swing.ButtonGroup menubar_group;
    private javax.swing.JLabel month;
    private javax.swing.JPasswordField newPass;
    private javax.swing.JCheckBox payAllBox;
    private javax.swing.ButtonGroup payment_group;
    private javax.swing.JToggleButton profileMenu;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JPanel registrarChangePass;
    private javax.swing.JLabel registrarFirstn;
    private javax.swing.JLabel registrarId;
    private javax.swing.JLabel registrarImage;
    private javax.swing.JLabel registrarLastn;
    private javax.swing.JLabel registrarMn;
    private javax.swing.JPanel registrarShowInfo;
    private javax.swing.JTabbedPane registrarShowInfoTab;
    private javax.swing.JLabel registrarStatus;
    private javax.swing.JComboBox<String> reportMonth;
    private javax.swing.JTable reportTable;
    private javax.swing.JToggleButton reportsMenu;
    private javax.swing.JPanel reportsPanel;
    private javax.swing.JLabel schoolBanner;
    private javax.swing.JLabel schoolLogo;
    private javax.swing.JButton sectCancel;
    private javax.swing.JButton sectCreate;
    private javax.swing.JButton sectDelete;
    private javax.swing.JButton sectEdit;
    private javax.swing.JButton sectRevert;
    private javax.swing.JButton sectSave;
    private javax.swing.JButton sectShowList;
    private javax.swing.JTable sectTable;
    private javax.swing.JToggleButton sectionEnableEdit;
    private javax.swing.JTextField sectionIdText;
    private javax.swing.JTextField sectionName;
    private javax.swing.JPanel sectionPanel;
    private javax.swing.JTextField sectionRoom;
    private javax.swing.JTextField sectionSearchText;
    private javax.swing.JComboBox<String> sectionYearCombo;
    private javax.swing.JToggleButton sectionsMenu;
    private javax.swing.JTextField studentCitizenship;
    private javax.swing.JButton studentClear;
    private javax.swing.JButton studentCreate;
    private com.toedter.calendar.JDateChooser studentDbirth;
    private javax.swing.JButton studentDelete;
    private javax.swing.JButton studentEdit;
    private javax.swing.JToggleButton studentEnableEdit;
    private javax.swing.JRadioButton studentFemale;
    private javax.swing.JTextField studentFn;
    private javax.swing.JTextField studentId;
    private javax.swing.JButton studentImageBtn;
    private javax.swing.JTextField studentLn;
    private javax.swing.JRadioButton studentMale;
    private javax.swing.JTextField studentMn;
    private javax.swing.JPanel studentPanel;
    private javax.swing.JTextField studentPassword;
    private javax.swing.JLabel studentPhoto;
    private javax.swing.JTextField studentReligion;
    private javax.swing.JButton studentRevert;
    private javax.swing.JButton studentSave;
    private javax.swing.JTextField studentSearch;
    private javax.swing.JButton studentSearchBtn;
    private javax.swing.JButton studentSearchBtn1;
    private javax.swing.JComboBox<String> studentSection;
    private javax.swing.JScrollPane studentSp;
    private javax.swing.JScrollPane studentSp1;
    private javax.swing.JTable studentTable;
    private javax.swing.JTextField studentTotalBalance;
    private javax.swing.ButtonGroup student_gender_group;
    private javax.swing.JToggleButton studentsMenu;
    private javax.swing.JTextField totalPayable;
    private javax.swing.JPasswordField validateNewPass;
    // End of variables declaration//GEN-END:variables
}
