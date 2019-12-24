import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientDestHost {
    private static String directoryName = "/home/";
    private static FileOutputStream fos = null;
    private static String serverHostAddr = "152.136.61.24";
    private static String serverHostPort = "4001";
    public static void main(String[] args) throws IOException {
        Socket receiver = new Socket();
        receiver.connect(new InetSocketAddress(serverHostAddr, Integer.parseInt(serverHostPort)));
        System.out.println("[dest] get connection");
        MessageDispatcher md = new MessageDispatcher(receiver.getInputStream());
        File file = null;
        while(true){
            byte[] dataPackage = md.getPackage();
            Message m = new Message(dataPackage);
            if(m.getTypeOfMessage() == Message.CONTROL_SIGNAL){

                List<String> content = new ArrayList<>();
                m.getControlMessage(content);
                String c = content.get(0);
                //System.out.println(m.getTypeOfMessage() + " " + c + " " + " " + m.getLengthOfLoad() + "" + dataPackage[1] + " " +  dataPackage[2]+ " "
                //        + dataPackage[3] + " " + dataPackage[4]);
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
                //System.out.println("received file package --- " + m.getLengthOfLoad());
                if(fos == null){
                    System.out.println("[FATAL]: FOS OBJECT SHOULD NOT BE NULL");
                    System.exit(-1);
                }
                List<byte[]> t = new ArrayList<>();
                m.getFilePackage(t);
//                byte[] rrr = t.get(0);
//                for(int i = 0; i < rrr.length; ++ i){
//                    System.out.print(rrr[i] + " ");
//                }
//                System.out.println();
                fos.write(t.get(0));
            }
        }
    }
}
