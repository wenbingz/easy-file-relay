import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Message {
    public static final int FILE_PACKAGE = 0;
    public static final int CONTROL_SIGNAL = 1;
    private int typeOfMessage = -1;
    private int lengthOfLoad = 0;
    private static int lengthOfPackage = 1024;
    private int overallSize = 0;
    private byte[] dataPackage = null;
    private int offset = 0;
    private static int headLength = 5;

    public static int getOverallSize(){
        return lengthOfPackage + headLength;
    }
    public int getTypeOfMessage(){
        return typeOfMessage;
    }
    public Message(int signal){
        this.typeOfMessage = signal;
        this.overallSize = lengthOfPackage + headLength;
        this.dataPackage = new byte[this.overallSize];
        dataPackage[offset ++] = (byte) signal;
    }
    public Message(byte[] dataPackage){
        this.dataPackage = dataPackage;
        if(((int)dataPackage[0]) == FILE_PACKAGE){
            this.typeOfMessage = FILE_PACKAGE;
        }else if(((int)dataPackage[0]) == CONTROL_SIGNAL){
            this.typeOfMessage = CONTROL_SIGNAL;
        }
        this.lengthOfLoad = getIntegerFromBytes(Arrays.copyOfRange(this.dataPackage, 1, 5));
    }
    public boolean getControlMessage(List<String> res){
        if(this.typeOfMessage != CONTROL_SIGNAL){
            return false;
        }
        res.add(new String(Arrays.copyOfRange(this.dataPackage, 5, 5 + lengthOfLoad)));
        return true;
    }
    public boolean getFilePackage(byte[] res){
        if(this.typeOfMessage != FILE_PACKAGE){
            return false;
        }
        res = Arrays.copyOfRange(this.dataPackage, 5, 5 + lengthOfLoad);
        return true;
    }
    private byte[] getIntegerInBytes(int integer){
        byte[] result = new byte[4];
        result[0] = (byte)((integer >> 24) & 0xFF);
        result[1] = (byte)((integer >> 16) & 0xFF);
        result[2] = (byte)((integer >> 8) & 0xFF);
        result[3] = (byte)(integer & 0xFF);
        return result;
    }
    private int getIntegerFromBytes(byte[] bytes){
        if(bytes.length != 4){
            return -1;
        }
        return (((int)(bytes[0])) << 24) + (((int)(bytes[1])) << 16) + (((int)(bytes[2])) << 8) + ((int)(bytes[0]));
    }
    public boolean setControlMessage(String controlMessage){
        if(typeOfMessage != CONTROL_SIGNAL){
            return false;
        }
        byte[] message = controlMessage.getBytes();
        lengthOfLoad = message.length;
        byte[] lengthBytes = getIntegerInBytes(lengthOfLoad);
        for(byte b: lengthBytes){
            dataPackage[offset ++] = b;
        }
        if(offset + lengthOfLoad >= overallSize) {
            return false;
        }
        System.arraycopy(message, 0, dataPackage, offset, message.length);
        offset += message.length;
        return true;
    }
    public boolean setFilePackage(byte[] message, int length){
        if(typeOfMessage != FILE_PACKAGE){
            return false;
        }
        lengthOfLoad = length;
        byte[] lengthBytes = getIntegerInBytes(lengthOfLoad);
        for(byte b: lengthBytes){
            dataPackage[offset ++] = b;
        }
        if(offset + lengthOfLoad >= overallSize){
            return false;
        }
        System.arraycopy(message, 0, dataPackage, offset, length);
        offset += length;
        return true;
    }

    public byte[] getDataPackage(){
        return this.dataPackage;
    }

}
