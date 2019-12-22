import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DestHost {
    private static String directoryName = "";
    private static FileOutputStream fos = null;
    private static String localHostAddr = null;
    private static String localHostPort = null;
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(localHostAddr, Integer.parseInt(localHostPort)));
        Socket receiver = ss.accept();
        MessageDispatcher md = new MessageDispatcher(receiver.getInputStream());
        File file = null;
        while(true){
            byte[] dataPackage = md.getPackage();
            Message m = new Message(dataPackage);
            if(m.getTypeOfMessage() == Message.CONTROL_SIGNAL){
                List<String> content = new ArrayList<>();
                m.getControlMessage(content);
                String c = content.get(0);
                if(c.startsWith("BEGIN")){
                    if(file != null){
                        System.out.println("EXPECT NULL FILE OBJECT");
                    }
                    file = new File(directoryName + "/" + c.substring("BEGIN-".length()));
                    if(fos != null){
                        System.out.println("EXPECT NULL FOS OBJECT");
                    }
                    fos = new FileOutputStream(file);
                }
                if(c.startsWith("END")){
                    fos.close();
                    file = null;
                    fos = null;
                }
            }
            if(m.getTypeOfMessage() == Message.FILE_PACKAGE){
                if(fos == null){
                    System.out.println("[FATAL]: FOS OBJECT SHOULD NOT BE NULL");
                }
                byte[] t = null;
                m.getFilePackage(t);
                fos.write(t);
            }
        }
    }
}
