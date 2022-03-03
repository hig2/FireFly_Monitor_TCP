// Название файла GreetingServer.java
import java.net.*;
import java.io.*;

public class GreetingServer extends Thread {
    private ServerSocket serverSocket;

    public GreetingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(20000);
    }

    public void run() {
        byte[] buffer = new byte[1024];
        String strFrame = "{\"COM\":{\"ttymxc0\":1800,\"ttymxc1\":1801,\"ttymxc2\":1802,\"ttymxc3\":1803,\"ttymxc4\":1804},\"Connected\":1,\"ReSET\":{\"Streams\":null,\"Telemetry\":\"6d727473e04beda91528421676629b1a8b7edefe00000003e20700001300638063d3085600000000ca570000000000000200264c0000fb126e109f01d2bc04007c83000040361100c0d8a7006842914014080824080400080008100406008400000202090000308040a2290804c1000001044000000000002400300000000000000000000000000000000000000000000000000000000000000000000000000000000000008000000000820403800100202000441000010000200148000001521000400000082508030000400080400010000048144421180400004001982020404042000100000800842804200802482014000320400008000000001a000000\"},\"Temperature\":0,\"Version\":\"1.8.4\",\"Watchdog\":{\"CRC\":603,\"activeCommand\":0,\"debugStatus\":0,\"lastPacketNum\":501,\"mainPower\":0,\"modemPower\":0,\"modemSwitchState\":0,\"powerChannelNum\":0,\"reservePower\":0,\"reserved1\":0,\"reserved3\":0,\"reserved4\":0,\"skStatus\":1,\"state\":1,\"watchdogVersion\":100}}\n" +
                "{\"COM\":{\"ttymxc0\":1800,\"ttymxc1\":1801,\"ttymxc2\":1802,\"ttymxc3\":1803,\"ttymxc4\":1804},\"Connected\":1,\"ReSET\":{\"Streams\":null,\"Telemetry\":\"6d727473e04beda91528421676629b1a8b7edefe00000003e20700001300638064d3085600000000c16e0000000000000200264c0000f7126e109f0188fb0400f882000040361100c0d8a700684a91001408182c080400080008100406008400000a02090080308840a2290804c100000104400000000000000041000000000000000000000000000000000000000000000000000000000000000000000000000000000000800004000c8204038101002020404c30001000000001480000015a100040000008254c0300000000844000100800481444211804080040019820206040660001000008208428042008024820100009204020080000000031000000\"},\"Temperature\":0,\"Version\":\"1.8.4\",\"Watchdog\":{\"CRC\":605,\"activeCommand\":0,\"debugStatus\":0,\"lastPacketNum\":503,\"mainPower\":0,\"modemPower\":0,\"modemSwitchState\":0,\"powerChannelNum\":0,\"reservePower\":0,\"reserved1\":0,\"reserved3\":0,\"reserved4\":0,\"skStatus\":1,\"state\":1,\"watchdogVersion\":100}}\n" +
                "{\"COM\":{\"ttymxc0\":1800,\"ttymxc1\":1801,\"ttymxc2\":1802,\"ttymxc3\":1803,\"ttymxc4\":1804},\"Connected\":1,\"ReSET\":{\"Streams\":null,\"Telemetry\":\"6d727473e04beda91528421676629b1a8b7edefe00000003e20700001300638065d308560000000086470000000000000200264c0000fb126e109f0149d90400f782000040361100c0d8a7006842914014080824080400080008100406008400000202090000308040a2290804c10000010440000000000000005200000000000000000000000000000000000000000000000000000000000000000000000000000000000080000000008204038001002020004410000100002001480000015210004000000825080300004000804000100000481444211804000040019820204040420001000008008428042008024820140003204000080000000048000000\"},\"Temperature\":0,\"Version\":\"1.8.4\",\"Watchdog\":{\"CRC\":606,\"activeCommand\":0,\"debugStatus\":0,\"lastPacketNum\":504,\"mainPower\":0,\"modemPower\":0,\"modemSwitchState\":0,\"powerChannelNum\":0,\"reservePower\":0,\"reserved1\":0,\"reserved3\":0,\"reserved4\":0,\"skStatus\":1,\"state\":1,\"watchdogVersion\":100}}\n" +
                "{\"COM\":{\"ttymxc0\":1800,\"ttymxc1\":1801,\"ttymxc2\":1802,\"ttymxc3\":1803,\"ttymxc4\":1804},\"Connected\":1,\"ReSET\":{\"Streams\":null,\"Telemetry\":\"6d727473e04beda91528421676629b1a8b7edefe00000003e20700001300638066d3085600000000d9510000000000000200264c0000fb126e10a001a8cd04000d82000040361100c0d8a7006842914014080824080400080008100406008400000202090000308040a2290804c1000001044000000000000000620000000000000000000000000000000000000000000000000000000000000000000000000000000000008000000000820403800100202000441000010000200148000001521000400000082508030000400080400010000048144421180400004001982020404042000100000800842804200802482014000320400008000000005e000000\"},\"Temperature\":0,\"Version\":\"1.8.4\",\"Watchdog\":{\"CRC\":607,\"activeCommand\":0,\"debugStatus\":0,\"lastPacketNum\":505,\"mainPower\":0,\"modemPower\":0,\"modemSwitchState\":0,\"powerChannelNum\":0,\"reservePower\":0,\"reserved1\":0,\"reserved3\":0,\"reserved4\":0,\"skStatus\":1,\"state\":1,\"watchdogVersion\":100}}\n" +
                "{\"COM\":{\"ttymxc0\":1800,\"ttymxc1\":1801,\"ttymxc2\":1802,\"ttymxc3\":1803,\"ttymxc4\":1804},\"Connected\":1,\"ReSET\":{\"Streams\":null,\"Telemetry\":\"6d727473e04beda91528421676629b1a8b7edefe00000003e20700001300638067d3085600000000e97c0100000000000200244c0000fb127010a001d1ce04005482000040361100c0d8a7006842914014080824080400080008100406008400000202090000308040a2290804c10000010440000000000056007300000000000000000000000000000000000000000000000000000000000000000000000000000000000080000000008204038001002020004410000100002001480000015210004000000825080300004000804000100000481444211804000040019820204040420001000008008428042008024820140003204000080000000076000000\"},\"Temperature\":0,\"Version\":\"1.8.4\",\"Watchdog\":{\"CRC\":608,\"activeCommand\":0,\"debugStatus\":0,\"lastPacketNum\":506,\"mainPower\":0,\"modemPower\":0,\"modemSwitchState\":0,\"powerChannelNum\":0,\"reservePower\":0,\"reserved1\":0,\"reserved3\":0,\"reserved4\":0,\"skStatus\":1,\"state\":1,\"watchdogVersion\":100}}";


        System.out.println("Ожидание клиента на порт " +
                serverSocket.getLocalPort() + "...");
        Socket server = null;
        try {
            server = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Просто подключается к " + server.getRemoteSocketAddress());
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(in.available() > 0){
                    int read = in.read(buffer);
                    for (int i = 0; i < read;i++){
                        System.out.print(buffer[i] + " ");
                    }
                    System.out.println("");

                }
                out.writeUTF(strFrame);


            } catch (SocketTimeoutException s) {
                System.out.println("Время сокета истекло!");
                break;
            } catch (IOException e) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String [] args) {
        int port = 1777;
        try {
            Thread t = new GreetingServer(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}