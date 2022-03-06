import java.io.IOException;

public class FireFlyConnect {
    private static int connectStatus = 0;
    private static String messageCurrentStatus;
    private final static int port = 1801;
    private static SocketPostman serverConnect;
    private final String version = "v1.0";
    private static FireFlyConnect fireFlyConnect;


    private FireFlyConnect() throws IOException {
        connectStatusWatcher();
        autoReconnect();
    }

    public static int getPort() {
        return port;
    }

    public final short[] getInArrayLink(){
        return serverConnect.getInArrayLink();
    }

    public final String getAddressIP() {
        return serverConnect.getAddressIP();
    }

    public static void connect(String addressIP) {
        Thread connectingThread = new Thread(() -> {
            do {
                try {
                    connectStatus = 0;
                    serverConnect = new SocketPostman(addressIP, port, new short[12], new short[3], SocketPostmanTaskTypeList.READ_SYMBOL_ARRAY);
                } catch (Exception e) {
                    connectStatus = 4; // messageCurrentStatus = messageConnect_NotConnected;
                }
            } while (serverConnect == null || !serverConnect.isConnected());
            try {
                fireFlyConnect = new FireFlyConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        connectingThread.start();
    }

    public final void close() {
        serverConnect.close();
    }

    public final void setStopCommand() {
        sendMessage(70);
    }

    public final void setRestartCommand() {
        sendMessage(69);
    }

    public final void setOpenDirCommand(int numDir) {
        sendMessage(numDir);
    }

    private void sendMessage(int numDir) {
        serverConnect.getOutArrayLink()[0] = (short) numDir;
        serverConnect.getOutArrayLink()[1] = serverConnect.getInArrayLink()[8]; //костыль на удержание температуры(необходим фикс на сервере)
        try {
            serverConnect.writeSymbolMessage();
        } catch (IOException e) {
            System.out.println("Не отправлено!");
            //e.printStackTrace();
        }
    }

    public final boolean isConnected() {
        return serverConnect.isConnected();
    }
    public final boolean isDataExchange(){return serverConnect.isDataExchange();}

    private void autoReconnect() {
        int delay = 100;
        Thread autoReconnectThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!serverConnect.isConnected()) {
                    FireFlyConnect.connect(getAddressIP());
                    break;
                }

            }
        });
        autoReconnectThread.start();
    }


    private void connectStatusWatcher() {
        Thread connectStatusWatcherThread = new Thread(() -> {
            while (serverConnect.isConnected()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

                if (serverConnect.isConnected()) {
                    if (serverConnect.isDataExchange()) {
                        connectStatus = 1;
                    } else {
                        connectStatus = 2;
                    }
                } else {
                    connectStatus = 3;
                }

            }
        });
        connectStatusWatcherThread.start();
    }

    public static int getConnectStatus(){
        return connectStatus;
    }

    public static FireFlyConnect getFireFlyConnect() {
        return fireFlyConnect;
    }

    public String getVersion() {
        return version;
    }

    public static String getMessageCurrentStatus() {
        return messageCurrentStatus;
    }


}