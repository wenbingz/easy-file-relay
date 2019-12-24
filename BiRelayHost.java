import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BiRelayHost {
    private static String localHostAddr = "0.0.0.0";
    private static String port1 = "4001";
    private static String port2 = "4002";
    public static void main(String args[]) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(localHostAddr, Integer.parseInt(port1)));
        Socket one = ss.accept();
        System.out.println("[relay] get connection from " + one.getInetAddress().getHostName() + " " + one.getInetAddress().getCanonicalHostName());
        ServerSocket sss = new ServerSocket();
        sss.bind(new InetSocketAddress(localHostAddr, Integer.parseInt(port2)));
        Socket another = sss.accept();

        System.out.println("[relay] connect to next");
        MessageDispatcher md = new MessageDispatcher(another.getInputStream());
        while(true){
            byte[] toSend = md.getPackage();
//            System.out.println("get a package " + toSend[0]);
//            for(int i = 0; i < toSend.length; ++ i){
//                System.out.print(toSend[i] + " ");
//            }
//            System.out.println();
            one.getOutputStream().write(toSend);
            one.getOutputStream().flush();
        }
    }
}
