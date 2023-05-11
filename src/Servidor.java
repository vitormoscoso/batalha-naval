import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Servidor {

    private DatagramSocket socket;
    private boolean running;
    private Map<String, ClienteInfo> clients;
    private Map<String, Tabuleiro> tabuleiros;
    private Pattern pattern;
    private Matcher matcher;

    public Servidor(String ipAddress, int port) throws SocketException, UnknownHostException {
        InetAddress localAddress = InetAddress.getByName(ipAddress);
        this.socket = new DatagramSocket(port, localAddress);
        this.clients = new HashMap<>();
        this.tabuleiros = new HashMap<>();
        this.pattern = Pattern.compile("^(\\d),(\\d)$");
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
                    tabuleiros.put(clientId, new Tabuleiro());
                    System.out.println("Cliente conectado: " + clientId);
                }

                String received = new String(packet.getData(), 0, packet.getLength());
                matcher = pattern.matcher(received);
                System.out.println(
                        "Received packet from " + packet.getAddress() + ":" + packet.getPort() + ": " + received);

                if (matcher.matches()) {
                    int x = Integer.parseInt(matcher.group(1));
                    int y = Integer.parseInt(matcher.group(2));

                    if (received.equals("bye")) {
                        System.out.println("Client " + clientId + " sent bye.....EXITING");
                        clients.remove(clientId);
                    } else {
                        // Enviar a mensagem para todos os clientes conectados, exceto para o remetente
                        for (ClienteInfo clientInfo : clients.values()) {
                            if (!clientInfo.getId().equals(clientId)) {
                                boolean acertou = tabuleiros.get(clientId).realizarTiro(x, y);
                                String resultado = acertou ? "Acertou" : "Errou";
                                buf = (clientId + ": " + resultado).getBytes();
                                DatagramPacket resposta = new DatagramPacket(buf, buf.length, clientInfo.getAddress(),
                                        clientInfo.getPort());
                                socket.send(resposta);

                                if (tabuleiros.get(clientId).verificarFimDeJogo()) {
                                    buf = ("Fim de jogo, " + clientId + " venceu!").getBytes();
                                    resposta = new DatagramPacket(buf, buf.length, clientInfo.getAddress(),
                                            clientInfo.getPort());
                                    socket.send(resposta);
                                    stop();
                                }
                            }
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
        if (args.length < 2) {
            System.out.println("Usage: java Servidor <ip_address> <port>");
            return;
        }

        String ipAddress = args[0];
        int port = Integer.parseInt(args[1]);

        Servidor server = new Servidor(ipAddress, port);
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
