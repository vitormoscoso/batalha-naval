import java.io.IOException;
import java.net.*;

public class Cliente {

    private DatagramSocket socket;
    private InetAddress address;

    public Cliente(String address) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket();
        this.address = InetAddress.getByName(address);
    }

    public void send(String message, int port) throws IOException {
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        System.out.println("Sent packet to: " + address + ":" + port);
    }

    public String receive() throws IOException {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    public void close() {
        socket.close();
    }

    public static void main(String args[]) throws IOException {
        Cliente cliente = new Cliente("192.168.0.2");

        String inp;
        do {
            System.out.println("Digite sua jogada no formato 'x,y': ");
            inp = System.console().readLine();

            if (!inp.equals("bye")) {
                cliente.send(inp, 5000);
                String response = cliente.receive();
                System.out.println(response);
            }

        } while (!inp.equals("bye"));

        cliente.close();
    }
}
