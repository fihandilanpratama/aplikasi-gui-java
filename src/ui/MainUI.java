package ui;

import fihan.uas.ui.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.*;

public class MainUI extends JFrame{
  private JPanel mainPanel;
  private JComboBox namaField;
  private JComboBox mkField;
  private JComboBox ketField;
  private JComboBox pertemuanField;
  private JButton buttonSimpan;
  private JLabel nameLabel;
  private JLabel mkLabel;
  private JLabel keteranganLabel;
  private JLabel pertemuanLabel;
  private JPanel listAbsensiPanel;
  private JPanel inputAbsensiPanel;
  private JTable tableDataAbsensi;
  private JButton buttonHapus;
  private JButton resetButton;
  private JButton button3;
  private JPanel rekapitulasiPanel;
  private JButton button1;
  private JButton button2;
  private JButton button4;
  private JTextField idField;

  private boolean databaru;
  private final Koneksi koneksi = new Koneksi();

  private Object getData() {
    try {
      Connection conn = koneksi.getConnect();
      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery("select * from t_mahasiswa");
      DefaultTableModel model = (DefaultTableModel) tableDataAbsensi.getModel();
      System.out.println(model);

      model.setRowCount(0);  // reset data table

      String dataAbsensi[][] = new String[24][5]; // 24 jumlah row, 5 jumlah column
      int i = 0;
      while(result.next()) {
        String id = result.getString("id");
        String nama = result.getString("nama");
        String mata_kuliah = result.getString("mata_kuliah");
        String keterangan = result.getString("keterangan");
        String pertemuan = result.getString("pertemuan");

        dataAbsensi[i][0] = id;
        dataAbsensi[i][1] = nama;
        dataAbsensi[i][2] = mata_kuliah;
        dataAbsensi[i][3] = pertemuan;
        dataAbsensi[i][4] = keterangan;

        i++;
      }
      return dataAbsensi;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return "";
  }

  // constructor
  public MainUI() {


    // event ketika pilih nama
    namaField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("berhasil");
        generateComboboxMK();
      }
    });
    createTable();

    // event tombol reset
    resetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        regenerateAllCombobox();
        databaru = true;
      }
    });

    // event ketika row dalam tabel diklik
    tableDataAbsensi.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        try {
          databaru = false;  // data diambil dari tabel
          int row = tableDataAbsensi.getSelectedRow();
          String id_absensi = tableDataAbsensi.getModel().getValueAt(row, 0).toString();

          Connection conn = koneksi.getConnect();
          Statement statement = conn.createStatement();
          ResultSet sql = statement.executeQuery("select * from t_mahasiswa where id='" + id_absensi + "'");

          if(sql.next()) {
            idField.setText(sql.getString("id"));
            namaField.setSelectedItem(sql.getString("nama"));
            mkField.setSelectedItem(sql.getString("mata_kuliah"));
            pertemuanField.setSelectedItem(sql.getString("pertemuan"));
            ketField.setSelectedItem(sql.getString("keterangan"));
          }

        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    // event tombol simpan
    buttonSimpan.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {

        System.out.println("id : " + idField.getText());
        System.out.println("nama : " + namaField.getSelectedItem());

        if(databaru) {
          try {
            String sql = "insert into t_mahasiswa values ('%s', '%s', '%s', '%s', '%s')";
            sql = String.format(
                    sql,
                    idField.getText(),
                    namaField.getSelectedItem(),
                    mkField.getSelectedItem(),
                    ketField.getSelectedItem(),
                    pertemuanField.getSelectedItem()
            );

            Connection conn = koneksi.getConnect();
            PreparedStatement pstatement = conn.prepareStatement(sql);
            pstatement.execute();

            // tampilkan pesan jika berhasi insert data
            JOptionPane.showMessageDialog(null, "data berhasil disimpan");

          } catch (SQLException e) {
            // e.printStackTrace();
            // tampilkan pesan jika berhasi insert data
            JOptionPane.showMessageDialog(null, "data gagal disimpan" + e);
          }
          createTable();
        }

        // jika tidak ada data baru (update/edit)
        else {
          try {
            String sql = "update t_mahasiswa set nama='%s', mata_kuliah='%s', keterangan='%s', pertemuan='%s' where id='%s'";
            sql = String.format(
                    sql,
                    namaField.getSelectedItem(),
                    mkField.getSelectedItem(),
                    ketField.getSelectedItem(),
                    pertemuanField.getSelectedItem(),
                    idField.getText()
            );

            Connection conn = koneksi.getConnect();
            PreparedStatement pstatement = conn.prepareStatement(sql);
            pstatement.execute();

            // tampilkan pesan jika berhasi insert data
            JOptionPane.showMessageDialog(null, "data berhasil diedit");

          } catch (SQLException e) {
            // e.printStackTrace();
            // tampilkan pesan jika berhasi insert data
            JOptionPane.showMessageDialog(null, "data gagal diedit" + e);
          }
          createTable();
        }
      }
    });

    // event tombol hapus
    buttonHapus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        try {
          String sql = "delete from t_mahasiswa where id='"+ idField.getText() +"'";
          Connection conn = koneksi.getConnect();
          PreparedStatement preparedStatement = conn.prepareStatement(sql);
          preparedStatement.execute();

          JOptionPane.showMessageDialog(null, "data berhasil dihapus");
        } catch (SQLException e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(null, "data gagal dihapus" + e);
        }
        regenerateAllCombobox();  // reset form
        createTable(); // update data di tabel
      }
    });
  }

  private void regenerateAllCombobox() {
    // id field
    idField.setText("");

    // combobox Nama
    namaField.removeAllItems();
    String itemNama[] = {"-pilih-", "aco", "budi", "joni"};
    namaField.setModel(new DefaultComboBoxModel(itemNama));

    // combobox MK
    mkField.removeAllItems();
    mkField.addItem("-pilih-");

    // combobox keterangan
    ketField.removeAllItems();
    String itemKet[] = {"-pilih-", "hadir", "alpha", "izin"};
    ketField.setModel(new DefaultComboBoxModel(itemKet));

    // combobox
    pertemuanField.removeAllItems();
    String itemPertemuan[] = {"-pilih-", "1", "2", "3", "4", "5", "6", "7", "8"};
    pertemuanField.setModel(new DefaultComboBoxModel(itemPertemuan));
  }


  private void createTable() {
//    Object[][] data = {
//            {123, "joni", "fisika", 1, "hadir"},
//            {234, "budi", "PBO", 1, "alpha"},
//            {345, "aco", "web programming", 2, "sakit"}
//    };
    Object[][] data = (Object[][]) getData();

    // set table
    tableDataAbsensi.setModel(new DefaultTableModel(
            data,
            new String[] {"ID", "nama", "mata kuliah", "pertemuan" ,"keterangan"} // nama column
    ));

    databaru = true;//

    TableColumnModel columns =  tableDataAbsensi.getColumnModel();
    columns.getColumn(0).setMinWidth(15);
    columns.getColumn(3).setMinWidth(10);

  }

  public JPanel getMainPanel() {
    return mainPanel;
  }

  private void generateComboboxMK(){
    //    daftar mk masing2 mahasiswa
    String mkBudi[] = {"kalkulus", "PBO", "web programming"};
    String mkAco[] = {"kalkulus", "fisika", "PBO"};
    String mkJoni[] = {"kalkulus", "fisika", "web programming"};

    mkField.removeAllItems(); // hapus semua items/options

    switch (namaField.getSelectedIndex()) {
      case 0:
        System.out.println("tidak ada yang dipilih");
        break;
      case 1:
          mkField.setModel(new DefaultComboBoxModel(mkAco));
        break;
      case 2:
          mkField.setModel(new DefaultComboBoxModel(mkBudi));
        break;
      case 3:
          mkField.setModel(new DefaultComboBoxModel(mkJoni));
        break;
    }
  }
}
