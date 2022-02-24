
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.io.IOException;



public class SocketPostman {
    static private Socket client;
    private final short[] inArray;
    private final short[] outArray;
    private final SocketPostmanTaskTypeList typeTask;
    AtomicLong t = new AtomicLong(0);
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private int timeOutToWriteMessage = 1000;

    private boolean connectStatus = false;
    private boolean dataExchange = false;


    private final char startSymbol = '$';
    private final char finishSymbol = ';';
    private final char separatorSymbol = ' ';

    private final byte[] globalBuffer;
    private int indexGlobalBuffer = 0;
    private boolean startReadFlag = false;
    private int realByte = 0;

    public SocketPostman(String ipAddress, int port, short[] inArray, short[] outArray, SocketPostmanTaskTypeList typeTask) throws IOException {
        this.inArray = inArray;
        this.outArray = outArray;
        globalBuffer = new byte[inArray.length * 10]; // динамический рост буффера относительно длины входного пакета
        client = new Socket(ipAddress, port);
        connectStatus = true;
        this.typeTask = typeTask;
        dataInputStream = new DataInputStream(client.getInputStream());
        dataOutputStream = new DataOutputStream(client.getOutputStream());
        startTask();
    }

    public final boolean isConnected() {
        return connectStatus;
    }

    public final  boolean isDataExchange(){
        return dataExchange;
    }

    public final short[] getInArrayLink(){
        return inArray;
    }

    public final short[] getOutArrayLink(){
        return outArray;
    }

    public final void setTimeOutToWriteMessage(int timeOutToWriteMessage){
        this.timeOutToWriteMessage = timeOutToWriteMessage;
    }

    public void writeSymbolMessage() throws IOException {
        dataOutputStream.writeUTF(getMessage());
    }

    private String getMessage() throws IOException {
        short crc = 0;
        String result = String.valueOf(startSymbol);

        for (int i = 0; i < (outArray.length - 1); i++) {
            crc += outArray[i];
        }

        for (int i = 0; i < outArray.length; i++) {
            result = i == (outArray.length - 1) ? result + crc + finishSymbol + '\n' : result + outArray[i] + separatorSymbol;
        }

        return result;
    }

    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readSymbolArrayMod(byte[] buffer) throws IOException {
        t.set(System.currentTimeMillis());
        int numByte = dataInputStream.read(buffer);
       if(parseBuffer(buffer, numByte)){
            
       }
    }

    private void readSymbolArrayBoomerangSlaveMod(byte[] buffer) throws IOException {
        t.set(System.currentTimeMillis());
        int numByte = dataInputStream.read(buffer);
        if(parseBuffer(buffer, numByte)){
            dataExchange = true;
            dataOutputStream.writeUTF(getMessage());
        }
    }



    private void startTask() throws IOException {
        byte[] buffer = new byte[inArray.length * 10];
        final int delay = 3500;

        Thread thread = new Thread(() -> {
            while (connectStatus) {
                try {
                    Thread.sleep(20);
                    switch (typeTask){
                        case READ_SYMBOL_ARRAY: readSymbolArrayMod(buffer); // только чтение (символьный поток)
                            break;
                        case READ_SYMBOL_ARRAY_BOOMERANG_SLAVE: readSymbolArrayBoomerangSlaveMod(buffer); // чтение и запись (полноценный режим) в роли ведомого (символьный поток)
                            break;
                        case READ_SYMBOL_ARRAY_BOOMERANG_MASTER: // чтение и запись (полноценный режим) в роли ведущего (символьный поток)
                            break;
                        case  READ_BYTE_ARRAY: // только чтение (байт поток)
                            break;
                        case  READ_BYTE_ARRAY_BOOMERANG_SLAVE: // чтение и запись (байт поток) в роли ведомого
                            break;
                        case  READ_BYTE_ARRAY_BOOMERANG_MASTER: // чтение и запись (байт поток) в роли ведущего
                            break;
                    }
                } catch (Exception e) {
                    connectStatus = false;
                    dataExchange = false;
                }
            }
        });

        Thread timerWatcher = new Thread(() -> {
            while (connectStatus) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if((System.currentTimeMillis() - t.get()) > delay){
                    dataExchange = false;
                }
            }
        });

        thread.start();
        timerWatcher.start();
    }




    private boolean parseBuffer(byte[] buffer, int numByte) throws IOException {

        for (int i = 0; i < numByte; i++) {
            if (buffer[i] == startSymbol) {
                indexGlobalBuffer = 0;
                startReadFlag = true;
                realByte = 0;
                continue;
            } else if (buffer[i] == finishSymbol && startReadFlag) {
                //обновляем глобальное состояние
                if(inArrayUpload(globalBuffer, realByte)){
                    realByte = 0;
                    startReadFlag = false;
                    indexGlobalBuffer = 0;
                    return true;
                }else{
                    return false;
                }

            }
            if (startReadFlag) {
                if (indexGlobalBuffer == globalBuffer.length) {
                    realByte = 0;
                    startReadFlag = false;
                    indexGlobalBuffer = 0;
                    return false;
                }
                globalBuffer[indexGlobalBuffer++] = buffer[i];
                realByte++;
            }
        }
    return false;
    }


    private boolean inArrayUpload(byte[] buffer, int realByte) {
        short[] bufferArray = new short[inArray.length];

        for (int i = 0, acc = 0, factor = 0, indexOfBufferArray = 0; i < realByte + 1; i++) {
            if (i == realByte) {
                bufferArray[indexOfBufferArray] = (short) acc;
                break;
            }

            if (buffer[i] == separatorSymbol) {
                bufferArray[indexOfBufferArray] = (short) acc;
                indexOfBufferArray++;
                if (indexOfBufferArray == (bufferArray.length)) {
                    // пришедший пакет больше ожидаемого
                    return false;
                }
                acc = 0;
                factor = 0;
            } else if ((buffer[i] - 48) >= 0 && (buffer[i] - 48) <= 9) {
                acc = ((acc * factor) + (buffer[i] - 48));
                factor = 10;
            } else {
                // была ошибка валидности пакета
                return false;
            }

        }

        // начало проверки контрольной суммы
        short crc = 0;

        for (int n = 0; n < bufferArray.length - 1; n++) {
            crc += bufferArray[n];
        }

        if (bufferArray[bufferArray.length - 1] == crc) {
            //все ок
            System.arraycopy(bufferArray, 0, inArray, 0, inArray.length);
            return true;
        } else {
            // была ошибка crc
            return false;
        }
    }


}
