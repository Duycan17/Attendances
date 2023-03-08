/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Student;

import java.sql.Statement;
import javax.swing.table.TableModel;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author duy
 */
public class StudentDAO {

    public ArrayList<Student> getData() {
//        Collect Data from Database Assign to Arraylist
        ArrayList<Student> ds = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();
        Statement stm = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from student";
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                ds.add(new Student(rs.getString("id"), rs.getString("name")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return ds;
    }

    public float getStatus(String id, String d) {
        Connection conn = JDBCUtil.getConnection();
        Statement stm = null;
        ResultSet rs = null;
        float result = 0;
        try {
            String sql = "Select status from attendance where student_id ='" + id + "' and date='" + d + "'";
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                result = rs.getFloat("status");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    public float getTotalAbsences(String id) {
        Connection conn = JDBCUtil.getConnection();
        Statement stm = null;
        ResultSet rs = null;
        float result = 0;
        try {
            String sql = "Select sum(status) as stt from attendance where student_id ='" + id + "'";
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                result = rs.getFloat("stt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    public void insertAbsence(String date, String id, float f) {
        Connection conn = JDBCUtil.getConnection();
        Statement stm = null;
        ResultSet rs = null;
        float result = 0;
        try {
            String sql = "insert into attendance (date, status, student_id) values (?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, date);
            pst.setFloat(2, f);
            pst.setString(3, id);
            pst.executeUpdate();
           
            System.out.println("insert Ok");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
