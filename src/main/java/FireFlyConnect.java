import java.io.IOException;

public class FireFlyConnect {
    private static final String messageConnect_Connecting = "Подключение к серверу.";
    private static final String messageConnect_Connected = "Подключен.";
    private static final String messageExchange_NotExchange = "Устанавливаем обмен.";
    private static final String messageConnect_NotConnected = "Сервер не доступен!";
    private static final String messageNotData = "нет данных.";
    private static String messageCurrentStatus;
    private final static int port = 1801;
    private static SocketPostman serverConnect;
    private final String version = "v1.0";
    private static FireFlyConnect fireFlyConnect;


    private FireFlyConnect() throws IOException {
        connectStatusWatcher();
        autoReconnect();
    }

    public final String getAddressIP(){
        return serverConnect.getAddressIP();
    }

    public static void connect(String addressIP){
        Thread connectingThread = new Thread(()->{
            do{
                try {
                    messageCurrentStatus = messageConnect_Connecting;
                    serverConnect = new SocketPostman(addressIP, port, new short[12], new short[3], SocketPostmanTaskTypeList.READ_SYMBOL_ARRAY);
                }catch (Exception e){
                    messageCurrentStatus = messageConnect_NotConnected;
                }
            }while(serverConnect == null || !serverConnect.isConnected());
            try {
                fireFlyConnect = new FireFlyConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        connectingThread.start();
    }

    public final void setStopCommand(){
        sendMessage(70);
    }

    public final void setRestartCommand(){
        sendMessage(69);
    }

    public final void setOpenDirCommand(int numDir){
        sendMessage(numDir);
    }

    private void sendMessage(int numDir){
        serverConnect.getOutArrayLink()[0] = (short)numDir;
        serverConnect.getOutArrayLink()[1] = serverConnect.getInArrayLink()[8]; //костыль на удержание температуры(необходим фикс на сервере)
        try {
            serverConnect.writeSymbolMessage();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public final boolean isConnected(){
        return serverConnect.isConnected();
    }

    private void autoReconnect(){
        int delay = 100;
        Thread autoReconnectThread = new Thread(()->{
            while (true){
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!serverConnect.isConnected()){
                    FireFlyConnect.connect(getAddressIP());
                    break;
                }

            }
        });
        autoReconnectThread.start();
    }


    private void connectStatusWatcher(){
        Thread connectStatusWatcherThread = new Thread(()->{
            while(serverConnect.isConnected()){
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

                if(serverConnect.isConnected()){
                    if(serverConnect.isDataExchange()){
                        messageCurrentStatus = messageConnect_Connected;
                    }else{
                        messageCurrentStatus = messageExchange_NotExchange;
                    }
                }else{
                    messageCurrentStatus = messageConnect_NotConnected;
                }

            }
        });
        connectStatusWatcherThread.start();
    }


    public static FireFlyConnect getFireFlyConnect(){
        return fireFlyConnect;
    }

    public String getVersion(){
        return version;
    }

    public static String getMessageCurrentStatus(){
        return messageCurrentStatus;
    }


}