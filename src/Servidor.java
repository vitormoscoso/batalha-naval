import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

    private DatagramSocket socket;
    private boolean running;
    private Map<String, ClienteInfo> clients;

    public Servidor() throws SocketException, UnknownHostException {
        InetAddress localAddress = InetAddress.getByName("192.168.0.2");
        this.socket = new DatagramSocket(5000, localAddress);
        this.clients = new HashMap<>();
    }

    public void start() {

        running = true;
        while (running) {
            try {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                String clientId = address.toString() + ":" + port;

                if (!clients.containsKey(clientId)) {
                    clients.put(clientId, new ClienteInfo(address, port));
                    System.out.println("Cliente conectado: " + clientId);
                }

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(
                        "Received packet from " + packet.getAddress() + ":" + packet.getPort() + ": " + received);

                if (received.equals("bye")) {
                    System.out.println("Client " + clientId + " sent bye.....EXITING");
                    clients.remove(clientId);
                } else {
                    // Enviar a mensagem para todos os clientes conectados, exceto para o remetente
                    for (ClienteInfo clientInfo : clients.values()) {
                        if (!clientInfo.getId().equals(clientId)) {
                            buf = (clientId + ": " + received).getBytes();
                            DatagramPacket resposta = new DatagramPacket(buf, buf.length, clientInfo.getAddress(),
                                    clientInfo.getPort());
                            socket.send(resposta);
                        }
                    }
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

    private static class ClienteInfo {
        private InetAddress address;
        private int port;
        private String id;

        public ClienteInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
            this.id = address.toString() + ":" + port;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }

        public String getId() {
            return id;
        }
    }
}
