import java.io.IOException;
import java.io.InputStream;

public class MessageDispatcher {
    private InputStream reader = null;
    private int writeoffset = 0;
    private int readoffset = 0;
    public MessageDispatcher(InputStream reader){
        this.reader = reader;
    }
    public byte[] getPackage() throws IOException {
        int spaceToWrite = 0;
        int spaceToRead = 0;
        byte[] m = new byte[Message.getOverallSize()];
        int length = 0;
        while(length < Message.getOverallSize()){
            length += reader.read(m, length, m.length - length);
        }
        return m;
    }

}
