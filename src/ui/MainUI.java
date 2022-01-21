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
  private JPanel rekapitulasiPanel;
  private JTextField idField;
  private JComboBox comboBoxNamaRekap;
  private JButton rekapButton;
  private JTable tableRekapitulasi;
  private JComboBox comboBoxJenisRekap;
  private JComboBox comboBoxRekap;

  private boolean databaru;
  private final Koneksi koneksi = new Koneksi();


  public JPanel getMainPanel() {
    return mainPanel;
  }


  private Object getDataAbsensi() {
    try {
      Connection conn = koneksi.getConnect();
      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery("select * from t_mahasiswa");
      DefaultTableModel model = (DefaultTableModel) tableDataAbsensi.getModel();
      // System.out.println(model);

      model.setRowCount(0);  // reset data table

      String dataAbsensi[][] = new String[100][5]; // 100 jumlah row, 5 jumlah column

      int i = 0;
      while(result.next()) {  // looping untuk mengisi data dari tabel ke array
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
      return "ada yang error saat query data absensi ke database";
    }
  }


  private Object getDataRekap(String nama, String jenisRekap, String satuanRekap) {
    try {
      Connection conn = koneksi.getConnect();
      Statement statement = conn.createStatement();
      String sql = "";
      switch (jenisRekap) {
        case "mingguan":
          sql = "select mata_kuliah as mk, find_in_set('hadir', keterangan) as hadir, find_in_set('izin', keterangan) as izin, find_in_set('alpha', keterangan) as alpha FROM t_mahasiswa WHERE nama = '%s' and pertemuan = '%s'";
          sql = String.format(
                  sql,
                  nama,
                  satuanRekap
          );
          break;
        case "bulanan":
          if(satuanRekap == "1") {  // bulan 1
            sql = "SELECT mata_kuliah as mk, sum(find_in_set('hadir', keterangan)) as 'hadir', sum(find_in_set('izin', keterangan)) as izin, sum(find_in_set('alpha', keterangan)) as alpha FROM t_mahasiswa WHERE nama = '%s' and pertemuan >= 1 AND pertemuan <= 4 GROUP BY mk";
          } else {  // bulan 2
            sql = "SELECT mata_kuliah as mk, sum(find_in_set('hadir', keterangan)) as 'hadir', sum(find_in_set('izin', keterangan)) as izin, sum(find_in_set('alpha', keterangan)) as alpha FROM t_mahasiswa WHERE nama = '%s' and pertemuan >= 5 AND pertemuan <= 8 GROUP BY mk";
          }
          sql = String.format(sql, nama);
          break;
      }

      ResultSet result = statement.executeQuery(sql);
      DefaultTableModel model = (DefaultTableModel) tableDataAbsensi.getModel();

      model.setRowCount(0);  // reset data table

      String dataRekap[][] = new String[3][4]; // 3 jumlah row, 4 jumlah column

      int i = 0;
      while(result.next()) {
        String mata_kuliah = result.getString("mk");
        String hadir = result.getString("hadir");
        String izin = result.getString("izin");
        String alpha = result.getString("alpha");

        dataRekap[i][0] = mata_kuliah;
        dataRekap[i][1] = hadir;
        dataRekap[i][2] = izin;
        dataRekap[i][3] = alpha;

        i++;
      }
      return dataRekap;

    } catch (SQLException e) {
      e.printStackTrace();
      return "ada yang error saat rekap data dari database";
    }
  }


  // constructor
  public MainUI() {
    createTableAbsensi();

    // event ketika pilih nama (input absensi panel)
    namaField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("berhasil");
        generateComboboxMK();
      }
    });

    // event ketika pilih nama (rekapitulasi panel)
    comboBoxJenisRekap.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        generateComboboxRekapitulasi();
      }
    });

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
          createTableAbsensi();
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
          createTableAbsensi();
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
        createTableAbsensi(); // update data di tabel
      }
    });

    // event tombol rekap
    rekapButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        String nama = (String) comboBoxNamaRekap.getSelectedItem();
        String jenisRekap = (String) comboBoxJenisRekap.getSelectedItem();
        String satuanRekap = (String) comboBoxRekap.getSelectedItem();
        createTableRekapitulasi(nama, jenisRekap, satuanRekap);
        createTableAbsensi();
      }
    });
  }

  private void regenerateAllCombobox() {
    idField.setText("");

    namaField.removeAllItems();
    String itemNama[] = {"-pilih-", "aco", "budi", "joni"};
    namaField.setModel(new DefaultComboBoxModel(itemNama));

    mkField.removeAllItems();
    mkField.addItem("-pilih-");

    ketField.removeAllItems();
    String itemKet[] = {"-pilih-", "hadir", "alpha", "izin"};
    ketField.setModel(new DefaultComboBoxModel(itemKet));

    pertemuanField.removeAllItems();
    String itemPertemuan[] = {"-pilih-", "1", "2", "3", "4", "5", "6", "7", "8"};
    pertemuanField.setModel(new DefaultComboBoxModel(itemPertemuan));
  }


  private void createTableAbsensi() {
//    Object[][] data = {
//            {123, "joni", "fisika", 1, "hadir"},
//            {234, "budi", "PBO", 1, "alpha"},
//            {345, "aco", "web programming", 2, "sakit"}
//    };
    Object[][] data = (Object[][]) getDataAbsensi();

    // set table
    tableDataAbsensi.setModel(new DefaultTableModel(
            data,
            new String[] {"ID", "nama", "mata kuliah", "pertemuan" ,"keterangan"} // nama column
    ));
    databaru = true;
  }


  private void createTableRekapitulasi(String nama, String jenisRekap, String satuanRekap) {
//    Object[][] data = {
//            {"fisika", "1", "0", "0"},
//            {"PBO", "1", "0", "0"},
//            {"kalkulus", "0", "0", "1"}
//    };
     Object[][] data = (Object[][]) getDataRekap(nama, jenisRekap, satuanRekap);

    // set table
    tableRekapitulasi.setModel(new DefaultTableModel(
            data,
            new String[] {"mata kuliah", "hadir", "izin", "alpha"} // nama column
    ));

    TableColumnModel columns =  tableRekapitulasi.getColumnModel();
    columns.getColumn(0).setMinWidth(15);
    columns.getColumn(3).setMinWidth(10);
  }


  private void generateComboboxMK () {
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


  private void generateComboboxRekapitulasi() {
    String[] rekapMingguanItems = {"1", "2", "3", "4", "5", "6", "7", "8"};
    String[] rekapBulananItems = {"1", "2"};

    comboBoxRekap.removeAllItems(); // hapus semua items/options

    switch (comboBoxJenisRekap.getSelectedIndex()) {
      case 0:
        System.out.println("tidak ada yang dipilih");
        break;
      case 1:
        comboBoxRekap.setModel(new DefaultComboBoxModel(rekapMingguanItems));
        break;
      case 2:
        comboBoxRekap.setModel(new DefaultComboBoxModel(rekapBulananItems));
        break;
    }
  }
}
