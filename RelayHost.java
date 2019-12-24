import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;
        import java.io.OutputStream;
        import java.net.InetAddress;
        import java.net.InetSocketAddress;
        import java.net.ServerSocket;
        import java.net.Socket;

public class RelayHost {
    private static String nextHopAdd = "192.168.100.238";
    private static String nextHopPort = "14003";
    private static String localHostAddr = "222.85.139.245";
    private static String localHostPort = "4002";
    private static Socket s = null;
    private static OutputStream writer = null;
    private static byte[] buffer = null;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(localHostAddr, Integer.parseInt(localHostPort)));
        Socket reader = ss.accept();
        System.out.println("[relay] get connection from " + reader.getInetAddress().getHostName() + " " + reader.getInetAddress().getCanonicalHostName());
        Socket sender = new Socket();
        sender.connect(new InetSocketAddress(nextHopAdd, Integer.parseInt(nextHopPort)));
        System.out.println("[relay] connect to next");
        MessageDispatcher md = new MessageDispatcher(reader.getInputStream());
        while(true){
            byte[] toSend = md.getPackage();
            System.out.println("get a package " + toSend[0]);
            for(int i = 0; i < toSend.length; ++ i){
                System.out.print(toSend[i] + " ");
            }
            System.out.println();
            sender.getOutputStream().write(toSend);
        }
    }
}
