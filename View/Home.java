/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author duy
 */
import Controller.StudentDAO;
import Model.Student;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Home extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private JButton saveButton;
    private JButton showButton;
    private int totalS = 0;

    public Home() {
        setTitle("Check Attendances");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        // Create the table with 4 columns
        String[] columnNames = {"ID", "Name", "Status", "Total Absences"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        showButton = new JButton("Show");
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    String formattedDate = dateFormat.format(jDateChooser1.getDate());
                    DefaultTableModel tableModel;
                    model.setRowCount(0);
                    StudentDAO stdDAO = new StudentDAO();

                    ArrayList<Student> s = stdDAO.getData();
                    for (Student std : s) {
                        totalS++;
                        String stt = "";
                        float totalAbsences = stdDAO.getTotalAbsences(std.getId());
                        float result = stdDAO.getStatus(std.getId(), formattedDate);
                        if (result == 0) {
                            stt = "Có mặt";
                        } else if (result == 1) {
                            stt = "Vắng";
                        } else if (result == 0.5) {
                            stt = "Có phép";
                        }
                        model.addRow(new Object[]{std.getId(), std.getName(), stt, totalAbsences});

                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Date must not be null!");
                }

            }
        });
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    for (int i = 0; i < totalS; i++) {
                        float result = 0;
                        int id = Integer.parseInt(model.getValueAt(i, 0).toString());
                        String status = model.getValueAt(i, 2).toString();
                        System.out.println(status);
                        if (status.equals("Vắng")) {
                            result = 1;
                        }
                        if (status.equals("Có phép")) {
                            result = (float) 0.5;
                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        String formattedDate = dateFormat.format(jDateChooser1.getDate());
                        StudentDAO std = new StudentDAO();
                        if (result != 0) {
                            std.insertAbsence(formattedDate, id + "", result);
                            JOptionPane.showMessageDialog(null, "Succesfully");
                        }
                    }
                } catch (Exception ex) {

                }

            }
        });

        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        JPanel panel = new JPanel();
        panel.add(jDateChooser1);
        panel.add(saveButton);
        panel.add(showButton);
        add(panel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        TableColumn statusColumn = table.getColumn("Status");
        statusColumn.setCellRenderer((TableCellRenderer) new ButtonRenderer());
        statusColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        setVisible(true);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                int i = 0;

                public void actionPerformed(ActionEvent e) {
                    i++;
                    if (i == 1) {
                        button.setText("Vắng");
                    } else if (i == 2) {
                        button.setText("Có phép");
                    } else {
                        button.setText("Có mặt");
                        i = 0;
                    }
                    fireEditingStopped();
                }

            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }

            button.setText((value == null) ? "" : value.toString());
            return button;
        }

        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    public static void main(String[] args) {
        new Home();
    }
}
