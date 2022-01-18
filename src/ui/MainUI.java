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
  private JTable showTable;
  private JButton buttonEdit;
  private JButton buttonHapus;
  private JButton resetButton;
  private JButton button3;
  private JPanel rekapitulasiPanel;
  private JButton button1;
  private JButton button2;
  private JButton button4;

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

    showTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        System.out.println("berhasil klik tabel");
        int row = showTable.getSelectedRow();
        String noData = showTable.getModel().getValueAt(row, 0).toString();
        System.out.println(noData);
      }
    });
  }

  private void regenerateAllCombobox() {
    // comboboxNama
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
            {1, "joni", "fisika", 1, "hadir"},
            {2, "budi", "PBO", 1, "alpha"},
            {3, "aco", "web programming", 2, "sakit"}
    };
    // set table
    showTable.setModel(new DefaultTableModel(
            data,
            new String[] {"no", "nama", "mata kuliah", "pertemuan" ,"keterangan"} // column
    ));
    TableColumnModel columns =  showTable.getColumnModel();
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
