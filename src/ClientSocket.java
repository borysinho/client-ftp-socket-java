import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class ClientSocket {
  private Socket socket;
  private DataOutputStream dataOutputStream;
  // private DataInputStream dataInputStream;
  private String serverURI;
  private int serverPort;

  public ClientSocket(String serverURI, int serverPort) {
    try {
      this.serverURI = serverURI;
      this.serverPort = serverPort;
      this.socket = new Socket(serverURI, serverPort);
      this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
      // TODO: handle exception
    }

  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

  public int getServerPort() {
    return this.serverPort;
  }

  public void setServerURI(String serverURI) {
    this.serverURI = serverURI;
  }

  public String getServerURI() {
    return this.serverURI;
  }

  public void connect() {
    try {
      if (!socket.isConnected()) {
        System.out.println("No está conectado");
        this.socket.connect(socket.getRemoteSocketAddress());
      }
      if (socket.isClosed()) {
        System.out.println("Socket cerrado");
        this.socket = new Socket(serverURI, serverPort);
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
      }

    } catch (Exception e) {
      // TODO: handle exception
    }

  }

  public boolean isConnected() {
    return socket.isConnected();
  }

  public boolean isClosed() {
    return socket.isClosed();
  }

  public void disconnect() {
    try {
      this.socket.close();
      this.dataOutputStream.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void sendFile(File file) {
    // String URI = "AutoClicker-3.0.exe";
    if (socket != null && socket.isConnected()) {
      try (FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath())) {
        // Enviar nombre del archivo
        byte[] fileNameBytes = file.getName().getBytes(StandardCharsets.UTF_8);
        this.dataOutputStream.writeInt(fileNameBytes.length);
        this.dataOutputStream.write(fileNameBytes);

        // Enviar longitud del archivo
        this.dataOutputStream.writeLong(file.length());

        // Enviar datos del archivo en bloques de 64 KB
        byte[] buffer = new byte[64 * 1024];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
          this.dataOutputStream.write(buffer, 0, bytesRead);
        }

        System.out.println("Archivo " + file.getName() + " enviado.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("ClientSocket.sendFile: Socket is not initialized or is not connected.");
    }

  }

  public static void main(String[] args) {

  }
}
