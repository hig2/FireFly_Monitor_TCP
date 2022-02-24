import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

//$1 0 0 0 0 0 0 0 0 0 0 0 0 0 1;
//$1 0 1 0 532 0 0 0 0 0 0 0 0 100 634;
public class CliRun {
    private static CliRun cliRun;
    SocketPostman socketPostman;

    private CliRun(SocketPostman socketPostman){
        this.socketPostman = socketPostman;
        renderThread();
    }
    public static CliRun startListInfo(SocketPostman socketPostman){
        if(cliRun == null){
            cliRun = new CliRun(socketPostman);
        }
        return cliRun;
    }

    private void showLocalDataTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);

        System.out.println("Время: " + formatDateTime);
    }


    private void showErrorStatus(int errorCode){
        System.out.print("Ошибка №"+ errorCode + ": ");
        switch (errorCode){
            case 0: System.out.println("Нет ошибки.");
                break;
            case 1: System.out.println("Не удалось расжечь.");
                break;
            case 2: System.out.println("Не удалось заполнить бак топливом.");
                break;
            case 3: System.out.println("Перегрев топлива.");
                break;
            case 4: System.out.println("Блокировка.");
                break;
            case 5: System.out.println("Клин двигателя внутреннего насоса.");
                break;
            case 6: System.out.println("Разрыв обменна данными с пультом.");
                break;
            case 7: System.out.println("Обрыв датчика температуры котла.");
                break;
            default: System.out.println("Не определена.");

        }
    }

    private void showDivLine(){
        System.out.println("_______________________________");
    }

    private void showStatus(int status){
        System.out.print("Статус: ");
        switch (status){
            case 0: System.out.println("Ожидание.");
                break;
            case 1: System.out.println("Росжиг.");
                break;
            case 2: System.out.println("Горение.");
                break;
            default: System.out.println("Не определен.");

        }
    }

    private void renderNoDataMessage(){
        showDivLine();
        System.out.println("Нет данных.");
    }

    private void renderThread()  {
        Thread thread = new Thread(() -> {
            while (socketPostman.isConnected()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (socketPostman.isDataExchange()) { // проверка на наличие обмена
                    renderListInfo();
                } else {
                    renderNoDataMessage();
                }
            }
            System.out.println("Произошел разрыв соединения!");
        });
        thread.start();
    }

    private void renderListInfo(){
        showDivLine();
        showStatus(socketPostman.getInArrayLink()[0]);
        showFuelTemperatureSensor(socketPostman.getInArrayLink()[1]);
        showErrorStatus(socketPostman.getInArrayLink()[7]);
        showDepulsatorLevelSensor(socketPostman.getInArrayLink()[2]);
        showBufferTankLevelSensor(socketPostman.getInArrayLink()[3]);
        showFireSensor(socketPostman.getInArrayLink()[4]);
        showHoldingFuelTemperature(socketPostman.getInArrayLink()[8]);
        showSoftWareVersion(socketPostman.getInArrayLink()[9]);
        showLocalDataTime();

    }

    private void showFuelTemperatureSensor(int fuelTempSensor){
        System.out.println("Температура топлива: " + fuelTempSensor);
    }

    private void showDepulsatorLevelSensor(int depulsatorLevelSensor){
        System.out.println("Датчик уровня топлива депульстора: " + (depulsatorLevelSensor == 1 ? "Накачал." : "Не накачал."));
    }

    private void showBufferTankLevelSensor(int bufferTankLevelSensor){
        System.out.println("Датчик уровня топлива буферного бака: " + (bufferTankLevelSensor == 1 ? "Накачал." : "Не накачал."));
    }

    private void showFireSensor(int fireSensor){
        System.out.println("Датчик пламени: " + (fireSensor == 1 ? "Есть пламя." : "Нет пламени."));
    }

    private void showHoldingFuelTemperature(int HoldingFuelTemperature){
        System.out.println("Удерживаемая температура топлива: " + HoldingFuelTemperature);
    }

    private void showSoftWareVersion(int showSoftWareVersion){
        System.out.println("Версия ПО: " + showSoftWareVersion);
    }

    public static void main(String[] args) {
        String ipAddress = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            System.out.println("Подключение к " + ipAddress + " на порт " + port);
            SocketPostman socketPostman = new SocketPostman(ipAddress, port, new short[15], new short[3], SocketPostmanTaskTypeList.READ_SYMBOL_ARRAY);
            System.out.println("Связь с сервером установлена !");
            CliRun.startListInfo(socketPostman);
            CliController.startCliController(socketPostman);
        }catch (Exception e){
            System.out.println("Сервер недоступен!");
        }
    }
}
