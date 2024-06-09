package local.studentmgmtsystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
public  class StudentManagementSystem extends JFrame {
    private DatabaseManager dbManager;
    private JPanel mainPanel, addPanel,updatePanel, removePanel, searchPanel, displayPanel;
    public StudentManagementSystem() {
        dbManager = new DatabaseManager();
        dbManager.createTables();
        setTitle("Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem addStudentItem = new JMenuItem("Add Student");
        JMenuItem removeStudentItem = new JMenuItem("Remove Student");
        JMenuItem searchStudentItem = new JMenuItem("Search Student");
        JMenuItem updateStudentItem = new JMenuItem("Update Student");
        JMenuItem displayAllStudentsItem = new JMenuItem("Display All Students");
        addStudentItem.addActionListener(new MenuActionListener());
        removeStudentItem.addActionListener(new MenuActionListener());
        searchStudentItem.addActionListener(new MenuActionListener());
        updateStudentItem.addActionListener(new MenuActionListener());
        displayAllStudentsItem.addActionListener(new MenuActionListener());
        menu.add(addStudentItem);
        menu.add(removeStudentItem);
        menu.add(searchStudentItem);
        menu.add(updateStudentItem);
        menu.add(displayAllStudentsItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        mainPanel = new JPanel(new CardLayout());
        add(mainPanel, BorderLayout.CENTER);
        addPanel = createAddPanel();
        mainPanel.add(addPanel, "Add Student");
        removePanel = createRemovePanel();
        mainPanel.add(removePanel, "Remove Student");
        searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, "Search Student");
        updatePanel = createUpdatePanel(); 
        mainPanel.add(updatePanel, "Update Student"); 
        displayPanel = createDisplayPanel();
        mainPanel.add(displayPanel, "Display All Students");
    }
    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextField nameField = new JTextField();
        JTextField rollNumberField = new JTextField();
        JTextField gradeField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Roll Number:"));
        panel.add(rollNumberField);
        panel.add(new JLabel("Grade:"));
        panel.add(gradeField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String rollNumber = rollNumberField.getText();
            String grade = gradeField.getText();
            String ageText = ageField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty() || ageText.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int age = Integer.parseInt(ageText);
                if (age < 4 || age > 99) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid age between 4 and 99.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid age.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!grade.matches("(A\\+|A\\-|A[1-9]|10\\.0|\\d(\\.\\d)?)")) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid grade.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid 10-digit phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
   
            if (dbManager.doesRollNumberExist(rollNumber)) {
                JOptionPane.showMessageDialog(panel, "Roll number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dbManager.doesPhoneNumberExist(phone)) {
                JOptionPane.showMessageDialog(panel, "Phone number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dbManager.doesEmailExist(email)) {
                JOptionPane.showMessageDialog(panel, "Email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student student = new Student(name, rollNumber, grade, Integer.parseInt(ageText), email, address, phone);
            dbManager.insertStudent(student);
            JOptionPane.showMessageDialog(panel, "Student added successfully!");
        });
        panel.add(new JLabel());
        panel.add(addButton);
        return panel;
    }

 private JPanel createRemovePanel() {
	    JPanel panel = new JPanel(new BorderLayout(10, 10));
	    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    DefaultTableModel model = new DefaultTableModel(new Object[]{"Name", "Roll Number", "Grade", "Age", "Email", "Address", "Phone"}, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false; 
	        }
	    };
	    JTable table = new JTable(model);
	    JScrollPane tableScrollPane = new JScrollPane(table);
	    panel.add(tableScrollPane, BorderLayout.CENTER);
	    List<Student> allStudents = dbManager.getAllStudents();
	    for (Student student : allStudents) {
	        model.addRow(new Object[]{student.getName(), student.getRollNumber(), student.getGrade(), student.getAge(), student.getEmail(), student.getAddress(), student.getPhoneNumber()});
	    }
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    JButton removeButton = new JButton("Remove");
	    removeButton.setPreferredSize(new Dimension(100, 30));
	    removeButton.addActionListener(e -> {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(panel, "Please select a student to remove.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        String rollNumber = (String) model.getValueAt(selectedRow, 1);
	        int confirmation = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete the selected student?", "Confirmation", JOptionPane.YES_NO_OPTION);
	        if (confirmation == JOptionPane.YES_OPTION) {
	            int rowsAffected = dbManager.deleteStudent(rollNumber);
	            if (rowsAffected > 0) {
	                model.removeRow(selectedRow);
	                JOptionPane.showMessageDialog(panel, "Student removed successfully!");
	            } else {
	                JOptionPane.showMessageDialog(panel, "No record found with Roll Number: " + rollNumber);
	            }
	        }
	    });
	    buttonPanel.add(removeButton);
	    panel.add(buttonPanel, BorderLayout.SOUTH);
	    return panel;
	}
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        String[] columnNames = {"Name", "Roll Number", "Grade", "Age", "Email", "Address", "Phone"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(table);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField searchField = new JTextField(20); 
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText();
            List<Student> students = dbManager.searchStudent(keyword);
            if (!students.isEmpty()) {
                model.setRowCount(0); 
                for (Student student : students) {
                    Object[] row = {student.getName(), student.getRollNumber(), student.getGrade(), student.getAge(), student.getEmail(), student.getAddress(), student.getPhoneNumber()};
                    model.addRow(row);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "No students found with the keyword: " + keyword);
            }
        });
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String rollNumber = (String) table.getValueAt(selectedRow, 1); 
                Student student = dbManager.getStudentByRollNumber(rollNumber);
                if (student != null) {
                    showUpdatePanelWithOldData(panel, student);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a student from the table to update.");
            }
        });
        panel.add(updateButton, BorderLayout.SOUTH);
        return panel;
    }
    private void showUpdatePanelWithOldData(JPanel parentPanel, Student student) {
        JPanel updatePanel = new JPanel(new GridLayout(0, 2, 10, 10));
        updatePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel rollNumberLabel = new JLabel("Roll Number:");
        JTextField rollNumberField = new JTextField(student.getRollNumber());
        rollNumberField.setEditable(false); 
        updatePanel.add(rollNumberLabel);
        updatePanel.add(rollNumberField);
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(student.getName());
        updatePanel.add(nameLabel);
        updatePanel.add(nameField);
        JLabel gradeLabel = new JLabel("Grade:");
        JTextField gradeField = new JTextField(student.getGrade());
        updatePanel.add(gradeLabel);
        updatePanel.add(gradeField);
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(String.valueOf(student.getAge()));
        updatePanel.add(ageLabel);
        updatePanel.add(ageField);
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(student.getEmail());
        updatePanel.add(emailLabel);
        updatePanel.add(emailField);
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(student.getAddress());
        updatePanel.add(addressLabel);
        updatePanel.add(addressField);
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        JTextField phoneNumberField = new JTextField(student.getPhoneNumber());
        updatePanel.add(phoneNumberLabel);
        updatePanel.add(phoneNumberField);
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String name = nameField.getText();
            String grade = gradeField.getText();
            String ageText = ageField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();
            try {
                int age = Integer.parseInt(ageText);
                if (age < 4 || age > 99) {
                    JOptionPane.showMessageDialog(updatePanel, "Please enter a valid age between 4 and 99.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updatePanel, "Please enter a valid age.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                JOptionPane.showMessageDialog(updatePanel, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!grade.matches("[0-9]+(\\.[0-9]+)?|[A-Za-z]+[\\+\\-]?[0-9]*")) {
                JOptionPane.showMessageDialog(updatePanel, "Please enter a valid grade between 0.0 and 10.0 or letter grades.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!phoneNumber.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(updatePanel, "Please enter a valid 10-digit phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student updatedStudent = new Student(
                name,
                student.getRollNumber(),
                grade,
                Integer.parseInt(ageText),
                email,
                address,
                phoneNumber
            );
            dbManager.updateStudent(student.getRollNumber(), updatedStudent);
            JOptionPane.showMessageDialog(updatePanel, "Student updated successfully!");
        });
        updatePanel.add(updateButton);
        parentPanel.removeAll();
        parentPanel.add(updatePanel);
        parentPanel.revalidate();
        parentPanel.repaint();
    }
private JPanel createSearchPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    JPanel searchInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    JTextField searchField = new JTextField(20); 
    searchInputPanel.add(new JLabel("Enter Keyword to Search:"));
    searchInputPanel.add(searchField);
    panel.add(searchInputPanel, BorderLayout.NORTH);
    String[] columnNames = {"Name", "Roll Number", "Grade", "Age", "Email", "Address", "Phone"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    JTable table = new JTable(model);
    JScrollPane tableScrollPane = new JScrollPane(table);
    panel.add(tableScrollPane, BorderLayout.CENTER);
    JButton searchButton = new JButton("Search");
    searchButton.setPreferredSize(new Dimension(100, 30)); 
    searchButton.addActionListener(e -> {
        String keyword = searchField.getText();
        List<Student> students = dbManager.searchStudent(keyword);
        if (!students.isEmpty()) {
            model.setRowCount(0); 
            for (Student student : students) {
                Object[] row = {student.getName(), student.getRollNumber(), student.getGrade(), student.getAge(), student.getEmail(), student.getAddress(), student.getPhoneNumber()};
                model.addRow(row);
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Student not found.");
        }
    });
    searchInputPanel.add(searchButton);
    return panel;
}
private JPanel createDisplayPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    String[] columnNames = {"Name", "Roll Number", "Grade", "Age", "Email", "Address", "Phone"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    JTable table = new JTable(model);
    JScrollPane tableScrollPane = new JScrollPane(table);
    panel.add(tableScrollPane, BorderLayout.CENTER);
    List<Student> allStudents = dbManager.getAllStudents();
    for (Student student : allStudents) {
        Object[] row = {student.getName(), student.getRollNumber(), student.getGrade(), student.getAge(), student.getEmail(), student.getAddress(), student.getPhoneNumber()};
        model.addRow(row);
    }
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    JTextField searchField = new JTextField(20); 
    JButton searchButton = new JButton("Search");
    searchButton.addActionListener(e -> {
        String keyword = searchField.getText();
        List<Student> students = dbManager.searchStudent(keyword);
        if (!students.isEmpty()) {
            model.setRowCount(0); 
            for (Student student : students) {
                Object[] row = {student.getName(), student.getRollNumber(), student.getGrade(), student.getAge(), student.getEmail(), student.getAddress(), student.getPhoneNumber()};
                model.addRow(row);
            }
        } else {
            JOptionPane.showMessageDialog(panel, "No students found with the keyword: " + keyword);
        }
    });
    searchPanel.add(new JLabel("Search: "));
    searchPanel.add(searchField);
    searchPanel.add(searchButton);
    panel.add(searchPanel, BorderLayout.NORTH);
    return panel;
}
private void updateRemovePanel() {
    DefaultTableModel model = (DefaultTableModel) ((JTable) ((JScrollPane) removePanel.getComponent(0)).getViewport().getView()).getModel();
    model.setRowCount(0);
    List<Student> allStudents = dbManager.getAllStudents();
    for (Student student : allStudents) {
        model.addRow(new Object[]{student.getName(), student.getRollNumber(), student.getGrade(), student.getAge(), student.getEmail(), student.getAddress(), student.getPhoneNumber()});
    }
}


    private void updateDisplayPanel() {
    DefaultTableModel model = (DefaultTableModel) ((JTable) ((JScrollPane) displayPanel.getComponent(0)).getViewport().getView()).getModel();
    model.setRowCount(0);
    List<Student> allStudents = dbManager.getAllStudents();
    for (Student student : allStudents) {
        model.addRow(new Object[]{student.getName(), student.getRollNumber(), student.getGrade(), student.getAge(), student.getEmail(), student.getAddress(), student.getPhoneNumber()});
    }
}
private class MenuActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        switch (command) {
            case "Add Student":
                cl.show(mainPanel, "Add Student");
                break;
            case "Remove Student":
                updateRemovePanel();
                cl.show(mainPanel, "Remove Student");
                break;
            case "Search Student":
                cl.show(mainPanel, "Search Student");
                break;
            case "Update Student":
                cl.show(mainPanel, "Update Student");
                break;
            case "Display All Students":
                updateDisplayPanel();
                cl.show(mainPanel, "Display All Students");
                break;
        }
    }
}
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        StudentManagementSystem app = new StudentManagementSystem();
        app.setLocationRelativeTo(null); 
        app.setVisible(true);
    });
}
}