import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class Servidor {

    private DatagramSocket socket;
    private boolean running;

    public Servidor() throws SocketException, UnknownHostException {
        InetAddress localAddress = InetAddress.getByName("192.168.0.2");
        this.socket = new DatagramSocket(5000, localAddress);
    }

    public void start() {

        running = true;
        while (running) {
            try {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                System.out.println("Received packet from: " + packet.getAddress() + ":" + packet.getPort());

                String received = new String(packet.getData(), 0, packet.getLength());

                if (received.equals("bye")) {
                    System.out.println("Client sent bye.....EXITING");
                    stop();
                } else {
                    // Processar a mensagem e responder ao cliente
                    System.out.println("Mensagem recebida: " + received);
                    buf = received.getBytes();
                    DatagramPacket resposta = new DatagramPacket(buf, buf.length, packet.getAddress(),
                            packet.getPort());
                    socket.send(resposta);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        socket.close();
    }

    public static void main(String[] args)
            throws IOException {
        Servidor server = new Servidor();
        System.out.println(
                "Server listening on: " + server.socket.getLocalSocketAddress());
        server.start();
    }
}
