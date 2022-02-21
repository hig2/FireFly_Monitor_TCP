
import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;
import java.io.IOException;



public class SocketPostman {
    static private Socket client;
    private final short[] inArray;
    private short[] outArray;

    private boolean connectStatus = false;
    private boolean dataExchange = false;


    private final char startSymbol = '$';
    private final char finishSymbol = ';';
    private final char separatorSymbol = ' ';

    private final byte[] globalBuffer;
    private int indexGlobalBuffer = 0;
    private boolean startReadFlag = false;
    private int realByte = 0;

    public SocketPostman(String ipAddress, int port, short[] inArray, short[] outArray) throws IOException {
        this.inArray = inArray;
        this.outArray = outArray;
        globalBuffer = new byte[inArray.length * 10]; // динамический рост буффера относительно длины входного пакета
        client = new Socket(ipAddress, port);
        connectStatus = true;
        clientTask();

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

    public void clientTask() throws IOException {
        byte[] buffer = new byte[globalBuffer.length];
        DataInputStream in = new DataInputStream(client.getInputStream());
        AtomicLong t = new AtomicLong(0);
        int delay = 5000;

        Thread thread = new Thread(() -> {
            while (connectStatus) {
                try {
                    Thread.sleep(20);
                        t.set(System.currentTimeMillis());
                        int numByte = in.read(buffer);
                    if(parseBuffer(buffer, numByte)){
                        dataExchange = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    public Socket getClient() {
        return client;
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
                inArrayUpload(globalBuffer, realByte);
                realByte = 0;
                startReadFlag = false;
                indexGlobalBuffer = 0;
                return true;
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


    private void inArrayUpload(byte[] buffer, int realByte) {
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
                    return;
                }
                acc = 0;
                factor = 0;
            } else if ((buffer[i] - 48) >= 0 && (buffer[i] - 48) <= 9) {
                acc = ((acc * factor) + (buffer[i] - 48));
                factor = 10;
            } else {
                // была ошибка валидности пакета
                return;
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

            return;
        } else {
            // была ошибка crc
            return;
        }
    }


}
