import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ClientSourceHost {
    private static String[] filenames = new String[]{
                "/Users/wenbingzhang/Documents/easy-file-relay/DestHost.java",
                "/Users/wenbingzhang/Documents/easy-file-relay/Message.java",
                "/Users/wenbingzhang/Downloads/generative-query-network-pytorch-2gpus.tar.gz"
                                                        };
    private static String nextHopAdd = "152.136.61.24";
    private static String nextHopPort = "4002";
    private static Socket s = null;
    private static OutputStream writer = null;
    private static byte[] buffer = null;


    public static void main(String[] args) throws IOException, InterruptedException {
        buffer = new byte[Message.getOverallSize() - Message.getHeadLength()];
        Socket s = new Socket();
        s.connect(new InetSocketAddress(nextHopAdd, Integer.parseInt(nextHopPort)));
        System.out.println("connect to next relay");
        writer = s.getOutputStream();
        for(String fn: filenames){
//            System.out.println("prepare to send file " + fn);
            File f = new File(fn);
            if(!f.exists() || f.isDirectory()){
                continue;
            }
            String fileName = f.getName();
            Message m = new Message(Message.CONTROL_SIGNAL);
            m.setControlMessage("BEGIN@" + fileName);
//            System.out.println("send begin package");
            writer.write(m.getDataPackage());

            FileInputStream fin = new FileInputStream(f);
            int readLength = -1;
            while((readLength = fin.read(buffer, 0, buffer.length)) >= 0){
                Message mFile = new Message(Message.FILE_PACKAGE);
//                System.out.println("send file package ---- " + readLength);
//                for(int i = 0; i < readLength; ++ i){
//                    System.out.print(buffer[i] + " ");
//                }
//                System.out.println();
                mFile.setFilePackage(buffer, readLength);
                byte[] rrr = mFile.getDataPackage();
//                for(int i = 0; i < rrr.length; ++ i){
//                    System.out.print(rrr[i] + " ");
//                }
//                System.out.println();
                writer.write(mFile.getDataPackage());
            }
            fin.close();
            m = new Message(Message.CONTROL_SIGNAL);
            m.setControlMessage("END@" + fileName);
//            System.out.println("send end package");

            writer.write(m.getDataPackage());
            writer.flush();
        }
        Thread.sleep(1000 * 60 * 60 * 24 * 5);
    }
}
