import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;


public class CliRun {
    SocketPostman socketPostman;

    public CliRun(SocketPostman socketPostman){
        this.socketPostman = socketPostman;
            //init controller
            //run thread
    }

    public void showLocalDataTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);

        System.out.println("Время: " + formatDateTime);
    }


    public void showErrorStatus(int errorCode){
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
            default: System.out.println("Не опознана.");

        }
    }

    public void showDivLine(){
        System.out.println("_______________________________");
    }

    public void showStatus(int status){
        System.out.println("Статус: ");
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
        System.out.println("Датчик уровня топлива буферного бака: " + (fireSensor == 1 ? "Есть пламя." : "Нет пламени."));
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
        short[] inArray = new short[15];
        short[] outArray = new short[3];

        try {
            System.out.println("Подключение к " + ipAddress + " на порт " + port);
            SocketPostman socketPostman = new SocketPostman(ipAddress, port, inArray, outArray);
            System.out.println("Связь с сервером установлена !");

            while (socketPostman.isConnected()) {
                Thread.sleep(1000);
                if (socketPostman.isDataExchange()) { // проверка на наличие обмена

                } else {

                }

            }
            System.out.println("Произошел разрыв соединения!");

        }catch (Exception e){
            System.out.println("Сервер недоступен!");
        }
    }
}

/*
 if (scanner.hasNextLine()) {
                    String str = scanner.nextLine();
                }
 */