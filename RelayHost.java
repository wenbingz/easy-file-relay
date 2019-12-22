import java.io.IOException;
        import java.io.OutputStream;
        import java.net.InetAddress;
        import java.net.InetSocketAddress;
        import java.net.ServerSocket;
        import java.net.Socket;

public class RelayHost {
    private static String nextHopAdd = "";
    private static String nextHopPort = "";
    private static String localHostAddr = "";
    private static String localHostPort = "";
    private static Socket s = null;
    private static OutputStream writer = null;
    private static byte[] buffer = null;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(localHostAddr, Integer.parseInt(localHostPort)));
        Socket reader = ss.accept();
        Socket sender = new Socket();
        sender.bind(new InetSocketAddress(nextHopAdd, Integer.parseInt(nextHopPort)));
        MessageDispatcher md = new MessageDispatcher(reader.getInputStream());
        while(true){
            byte[] toSend = md.getPackage();
            sender.getOutputStream().write(toSend);
        }
    }
}
