
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Form extends JFrame {
  File file;
  ClientSocket clientSocket;

  public Form() {
    // Creamos el ClientSocket
    this.file = null;
    this.clientSocket = null;

    // Creamos el Frame
    JFrame frame = new JFrame("Sender Frame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(430, 120);

    // North Panel - Server, Port, Connect y Disconnect
    JPanel northPanel = new JPanel();
    JLabel lblServer = new JLabel("Server");
    JTextField tfServer = new JTextField("localhost", 10);
    JLabel lblPort = new JLabel("Port");
    JTextField tfPort = new JTextField("3000", 3);
    JButton btnConnect = new JButton("Connect");
    btnConnect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          if (clientSocket == null) {
            clientSocket = new ClientSocket(tfServer.getText(), Integer.parseInt(tfPort.getText()));
          } else {
            clientSocket.connect();
          }

          // clientSocket.coonnect();
        } catch (Exception ex) {
          ex.printStackTrace();
        }

      }
    });

    JButton btnDisconnect = new JButton("Disconn");
    btnDisconnect.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (clientSocket.isConnected()) {
          clientSocket.disconnect();
        }
      }

    });
    northPanel.add(lblServer);
    northPanel.add(tfServer);
    northPanel.add(lblPort);
    northPanel.add(tfPort);
    northPanel.add(btnConnect);
    northPanel.add(btnDisconnect);
    // ================================================================================

    // =============== File, Botones ... y Enviar ===================
    JPanel centerPanel = new JPanel();
    JLabel lblFile = new JLabel("File");
    JTextField tfFile = new JTextField("", 19);
    tfFile.setEditable(false);
    JButton btnFileSelector = new JButton("...");
    btnFileSelector.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (updateFile()) {
          tfFile.setText(file.getName());
        }
      }
    });
    JButton btnFileSend = new JButton("Enviar");
    btnFileSend.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (clientSocket.isConnected()) {
          if (file == null) {
            if (updateFile()) {
              tfFile.setText(file.getName());
            } else {
              return;
            }
          }
          clientSocket.sendFile(file);
        } else {
          System.out.println("btnFileSend.actionPerformed: Client is not connected.");
        }

      }
    });
    // ================================================================================
    centerPanel.add(lblFile);
    centerPanel.add(tfFile);
    centerPanel.add(btnFileSelector);
    centerPanel.add(btnFileSend);

    frame.getContentPane().add(BorderLayout.NORTH, northPanel);
    frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
    // frame.getContentPane().add(BorderLayout.CENTER, southPanel);
    frame.setVisible(true);
  }

  private boolean updateFile() {
    JFileChooser jfc = new JFileChooser(
        FileSystemView.getFileSystemView().getParentDirectory(new File("./data")));

    // System.out.println("Path: " + jfc.getSelectedFile().getAbsoluteFile());
    int returnValue = jfc.showOpenDialog(null);

    if (returnValue == JFileChooser.APPROVE_OPTION) {
      this.file = jfc.getSelectedFile();
    }
    return returnValue == JFileChooser.APPROVE_OPTION;
  }

  public static void main(String[] args) {
    new Form();
  }

}
