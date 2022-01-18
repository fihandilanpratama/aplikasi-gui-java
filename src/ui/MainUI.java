package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainUI extends JFrame{
  private JPanel mainPanel;
  private JComboBox namaField;
  private JComboBox mkField;
  private JComboBox ketField;
  private JComboBox pertemuanField;
  private JButton buttonTambah;
  private JLabel nameLabel;
  private JLabel mkLabel;
  private JLabel keteranganLabel;
  private JLabel pertemuanLabel;
  private JPanel listAbsensiPanel;
  private JPanel inputAbsensiPanel;
  private JTable tableDataAbsensi;
  private JButton buttonEdit;
  private JButton buttonHapus;
  private JButton resetButton;
  private JButton button3;
  private JPanel rekapitulasiPanel;
  private JButton button1;
  private JButton button2;
  private JButton button4;
  private JTextField idField;

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
      }
    });

    tableDataAbsensi.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        System.out.println("berhasil klik tabel");
        int row = tableDataAbsensi.getSelectedRow();
        String noData = tableDataAbsensi.getModel().getValueAt(row, 0).toString();
        System.out.println(noData);
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
    Object[][] data = {
            {123, "joni", "fisika", 1, "hadir"},
            {234, "budi", "PBO", 1, "alpha"},
            {345, "aco", "web programming", 2, "sakit"}
    };
    // set table
    tableDataAbsensi.setModel(new DefaultTableModel(
            data,
            new String[] {"ID", "nama", "mata kuliah", "pertemuan" ,"keterangan"} // nama column
    ));
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
