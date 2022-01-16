import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test extends JFrame{
  private JPanel mainPanel;
  private JComboBox namaField;
  private JComboBox mkField;
  private JComboBox ketField;
  private JComboBox pertemuanField;
  private JButton button2;
  private JLabel nameLabel;
  private JLabel mkLabel;
  private JLabel keteranganLabel;
  private JLabel pertemuanLabel;

  public void generateComboboxMK(){
//    daftar mk masing2 mahasiswa
    String mkBudi[]={"kalkulus", "PBO", "web programming"};
    String mkAco[]={"kalkulus", "fisika", "PBO"};
    String mkJoni[]={"kalkulus", "fisika", "web programming"};

    mkField.removeAllItems(); // hapus semua items/options

    switch (namaField.getSelectedIndex()) {
      case 0:
        System.out.println("tidak ada yang dipilih");
        break;
      case 1:
        for (int i = 0; i < 3; i++) {
          mkField.addItem(mkAco[i]);
        }
        break;
      case 2:
        for (int i = 0; i < 3; i++) {
          mkField.addItem(mkBudi[i]);
        }
        break;
      case 3:
        for (int i = 0; i < 3; i++) {
          mkField.addItem(mkJoni[i]);
        }
        break;
    }

  }

  public Test (String title) {
    super(title);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setContentPane(mainPanel);
    this.pack();
    namaField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("berhasil");
        generateComboboxMK();
      }
    });
  }

  public static void main(String args[]) {
    JFrame frame = new Test("my first java GUI app with swing");
    frame.setVisible(true);
  }
}
