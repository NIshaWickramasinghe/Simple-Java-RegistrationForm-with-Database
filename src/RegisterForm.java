import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterForm extends JDialog{
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField tfPassword;
    private JPasswordField tfConfirm;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public RegisterForm(Frame parent) {
        super(parent);
        setTitle("Create a new Account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,475));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(tfPassword.getPassword());
        String confirmPassword = String.valueOf(tfConfirm.getPassword());

        if(name.isEmpty() || email.isEmpty()|| phone.isEmpty() || address.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "please enter all feilds",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(name,email,phone,address,password);

        if(user != null){
            dispose();
        }else{
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public User user;
    private User addUserToDatabase(String name, String email, String phone, String address, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/registerstore";
        final String USERNAME = "root";
        final String PASSWORD = "1234";

        try{
            Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement statement = con.createStatement();
            String sql = "INSERT INTO users (name,email,phone,address,password)" + "VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,phone);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5,password);

            //Insert raw into the table
            int addedRows = preparedStatement.executeUpdate();
            if(addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;

            }

            statement.close();
            con.close();


        }catch (Exception e){
            e.printStackTrace();
        }


        return user;
    }

    public static void main(String[] args) {
        RegisterForm myForm = new RegisterForm(null);
        User user = myForm.user;
        if(user != null){
            System.out.println("Successfull Registration of :" + user.name);
        }else{
            System.out.println("Registration cancelled");
        }

    }
}
