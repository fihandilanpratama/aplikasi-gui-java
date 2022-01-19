package fihan.uas.ui;

import java.sql.*;

public class Koneksi {
    private Connection connect;
    private String driverName = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/absensi_mahasiswa";
    private String username = "root";
    private String password = "";


    public Connection getConnect() {
        if(connect == null) {
            try {
                Class.forName(driverName);
                System.out.println("class driver ditemukan");

                connect = DriverManager.getConnection(url, username, password);
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from t_mahasiswa");

//                while (resultSet.next()) {
//                    System.out.println(resultSet.getString("nama"));
//                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connect;
    }

    public static void main(String[] args) {
        Koneksi koneksi = new Koneksi();
        koneksi.getConnect();

    }
}
