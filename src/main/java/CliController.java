import java.io.IOException;
import java.util.Scanner;

public class CliController {
    private static CliController cliController;
    private final SocketPostman socketPostman;
    private final Scanner scanner = new Scanner(System.in);
    private final short[] outArray;

    private CliController(SocketPostman socketPostman){
        this.socketPostman = socketPostman;
        outArray = socketPostman.getOutArrayLink();
        readerThread();
    }

    static CliController startCliController(SocketPostman socketPostman){
        if(cliController == null){
            cliController = new CliController(socketPostman);
        }
        return cliController;
    }

    private void readerThread()  {
        Thread thread = new Thread(() -> {
            while (socketPostman.isConnected()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (scanner.hasNextLine()) {
                    sendMessage(scanner.nextLine());
                }

            }
        });
        thread.start();
    }

    private void sendMessage(String command){
        short directiveNum;
        switch (command){
            case "/перезагрузка": directiveNum = 69;
                break;
            case "/стоп": directiveNum = 70;
                break;
            case "/выход": socketPostman.close();
                return;
            default: return;

        }

        outArray[0] = directiveNum;
        outArray[1] = socketPostman.getInArrayLink()[8]; //костыль на удержание температуры(необходим фикс на сервере)

        try {
            socketPostman.writeSymbolMessage();
        } catch (IOException e) {
            //e.printStackTrace();
        }

    }

}
