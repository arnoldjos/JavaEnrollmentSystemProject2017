/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.awt.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Fudolig
 */
public class MyModel extends AbstractTableModel{
    private ResultSet rs = null;
    private String user_type = null;
    private String[] student_headers = {"Year Level","Student ID","Student Name","Gender","Section"};
    private String[] section_headers = {"Year Level","Section ID","Name","No. of Studs","Maximum ","Room","Room Status"};
    private String[] section_list_headers = {"Year Level","Student ID","Student's Firstname","Student's Lastname","Gender"};
    private String[] bill_headers = {"Bill ID","Fee Category","Fee Amount","Date of Bill"};
    private String[] transaction_headers={"Student","Bill ID","Amount Payable","Amount Paid","Change","Transaction Date"};
    private ArrayList<Boolean> rowList;
    
        public MyModel(ResultSet rs,String type){
            this.rs = rs;
            this.user_type = type;
            int size = 0;
                try{
                rs.last();
                size =rs.getRow();
                rs.beforeFirst();
                }catch(SQLException e){}
            rowList = new ArrayList<Boolean>(size);
            for(int i = 0;i< size;i++){
                rowList.add(Boolean.FALSE);
            }
        }
        public MyModel(String type){
            this.user_type = type;
        }
    @Override
    public int getRowCount() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int size =0 ;
        if(Objects.equals(user_type,"BillBlank")){
            size = 0;
        }
        else if(Objects.equals(user_type,"FeeBlank")){
            size = 0;
        }
        else{
            try{
                rs.last();
                size = rs.getRow();
                rs.beforeFirst();            
                return size;
                }catch(SQLException e){
                e.printStackTrace();
                }
        }
        return size;
    }

    /*
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //return super.isCellEditable(rowIndex, columnIndex); //To change body of generated methods, choose Tools | Templates.
        if(Objects.equals(user_type,"Billing")){
            if(columnIndex == 4){
                return true;
            }
        }
        return false;
    } 
    */
    @Override
    public int getColumnCount() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int col = 0;
        if(Objects.equals(user_type,"Student")){
            col =  student_headers.length;
        }
        else if(Objects.equals(user_type,"Section")){
            col =  section_headers.length;
        }
        else if(Objects.equals(user_type,"SectionList")){
            col =  section_list_headers.length;
        }
        else if(Objects.equals(user_type,"Billing")){
            col = bill_headers.length;
        }
        else if(Objects.equals(user_type,"BillBlank")){
            col = bill_headers.length;
        }
        else if(Objects.equals(user_type,"Transactions")){
            col = transaction_headers.length;
        }
        return col;
    }

    
    @Override
    public String getColumnName(int column) {
        //return super.getColumnName(column); //To change body of generated methods, choose Tools | Templates.
        String name= null;
        if(Objects.equals(user_type,"Student")){
            name =  student_headers[column];
        }
        else if(Objects.equals(user_type,"Section")){
            name =  section_headers[column];
        }
        else if(Objects.equals(user_type,"SectionList")){
            name =  section_list_headers[column];
        }
        else if(Objects.equals(user_type,"Billing")||Objects.equals(user_type,"BillBlank")){
            name = bill_headers[column];
        }
        else if(Objects.equals(user_type,"Transactions")){
            name = transaction_headers[column];
        }
        return name;
    }

    /*
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //return super.getColumnClass(columnIndex); //To change body of generated methods, choose Tools | Templates.
        if(Objects.equals(user_type,"Billing")||Objects.equals(user_type,"BillBlank")){
            switch(columnIndex){
                case 4:
                    return Boolean.class;
                default:
                    return Object.class;
            }
        }
        return String.class;
    }
    */
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try{
            rs.absolute(rowIndex+1);
            if(Objects.equals(user_type,"Student")){
                columnIndex +=1;
                if(columnIndex == 1){
                    return rs.getObject(columnIndex).toString();
                }
                else if(columnIndex == 2){
                    return rs.getObject(columnIndex).toString();
                }
                else if(columnIndex == 3){
                    return rs.getString("Firstname")+" "+rs.getString("Lastname");
                }
                else if(columnIndex == 4){
                    return rs.getObject(columnIndex+1).toString();
                }
                else if(columnIndex ==5){
                    return rs.getObject(columnIndex+1).toString();
                }
                else if (columnIndex == 6){
                    return rs.getObject(columnIndex+1).toString();
                }
            }
            else if(Objects.equals(user_type,"Section")){
                columnIndex++;
                if(columnIndex == 7){
                    String availability = rs.getString("sectionIsFull");
                        if(Objects.equals(availability,"No")){
                            return "Available";
                        }
                        else{
                            return "Full";
                        }
                }
                else{
                    return rs.getObject(columnIndex).toString();
                }
            }
            else if(Objects.equals(user_type,"Billing")){
                columnIndex +=1;
                if(columnIndex == 1){
                    return rs.getObject(columnIndex).toString();
                }
                else if(columnIndex == 2){
                    return rs.getObject(columnIndex).toString();
                }
                else if(columnIndex == 3){
                    return rs.getObject(columnIndex).toString();
                }
                else if(columnIndex == 4){
                     return rs.getObject(columnIndex).toString();
                }
            }
            else{
                return rs.getObject(columnIndex+1).toString();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return "Failed";
    }        
}
