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
        System.out.println("Sent packet to: " + address + ": " + port);
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
        if (args.length < 1) {
            System.out.println("Usage: java Cliente <server_ip_address>");
            return;
        }

        String serverIpAddress = args[0];
        Cliente cliente = new Cliente(serverIpAddress);

        String inp;
        do {
            System.out.println("Digite sua jogada no formato 'x,y': ");
            inp = System.console().readLine();

            String[] coords = inp.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            if (!inp.equals("bye")) {
                if (x >= 0 && x < Tabuleiro.TAMANHO_TABULEIRO && y >= 0 && y < Tabuleiro.TAMANHO_TABULEIRO) {
                    cliente.send(inp, 5000);
                    String response = cliente.receive();
                    System.out.println(response);
                } else {
                    System.out.println("Coordenadas inválidas. Por favor, insira coordenadas entre 0 e "
                            + (Tabuleiro.TAMANHO_TABULEIRO - 1) + ".");
                }

            }

        } while (!inp.equals("bye"));

        cliente.close();
    }
}
