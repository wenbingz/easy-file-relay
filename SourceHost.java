import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;


public class SourceHost {
    private static String[] filenames = new String[]{""};
    private static String nextHopAdd = "";
    private static String nextHopPort = "";
    private static Socket s = null;
    private static OutputStream writer = null;
    private static byte[] buffer = null;


    public static void main(String[] args) throws IOException {
        buffer = new byte[Message.getOverallSize()];
        Socket s = new Socket();
        s.bind(new InetSocketAddress(nextHopAdd, Integer.parseInt(nextHopPort)));
        writer = s.getOutputStream();
        for(String fn: filenames){
            File f = new File(fn);
            if(!f.exists() || f.isDirectory()){
                continue;
            }
            String fileName = f.getName();
            Message m = new Message(Message.CONTROL_SIGNAL);
            m.setControlMessage("BEGIN@" + fileName);
            writer.write(m.getDataPackage());

            FileInputStream fin = new FileInputStream(f);
            int readLength = -1;
            while((readLength = fin.read(buffer, 0, buffer.length)) > 0){
                Message mFile = new Message(Message.FILE_PACKAGE);
                mFile.setFilePackage(buffer, readLength);
                writer.write(mFile.getDataPackage());
            }
            fin.close();
            m = new Message(Message.CONTROL_SIGNAL);
            m.setControlMessage("END@" + fileName);
            writer.write(m.getDataPackage());
        }
    }
}
