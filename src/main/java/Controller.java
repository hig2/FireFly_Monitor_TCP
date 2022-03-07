import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Controller implements Initializable {

    @FXML
    private Button rebootButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button saveButton;


    @FXML
    private TextField addressIPTextField;

    @FXML
    private Text statusData;

    @FXML
    private Text errorData;

    @FXML
    private Text fuelTemperatureSensorData;

    @FXML
    private Text depulsatorLevelSensorData;

    @FXML
    private Text bufferTankLevelSensorData;

    @FXML
    private Text softWareVersionData;

    @FXML
    private Text holdingFuelTemperatureData;

    @FXML
    private Text fireSensorData;

    @FXML
    private Text connectAddressIP;

    @FXML
    private Text connectStatus;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String addressIP = ConfigFile.getIP();
        FireFlyConnect.connect(addressIP);
        showTextFieldIPAddress(addressIP);
        showThread();
    }

    public void showTextFieldIPAddress(String addressIP) {
        addressIPTextField.setText(addressIP);
    }


    public void pressedStopButton(ActionEvent event) {
        if (FireFlyConnect.getFireFlyConnect() != null) {
            FireFlyConnect.getFireFlyConnect().setStopCommand();
        }
    }

    public void pressedRebootButton(ActionEvent event) {
        if (FireFlyConnect.getFireFlyConnect() != null) {
            FireFlyConnect.getFireFlyConnect().setRestartCommand();
        }
    }

    public void pressedSaveIPButton(ActionEvent event) {
        String addressIP = addressIPTextField.getText();

        if (!addressIP.equals(ConfigFile.getIP())) {
            if (ConfigFile.setIP(addressIP)) {
                String newAddressIP = ConfigFile.getIP();
                FireFlyConnect.connect(newAddressIP);
                showTextFieldIPAddress(newAddressIP);
            }
        }
    }

    private void showThread() {
        Thread statusMassageWatcherThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showConnectStatus();
                showAddress();
                showFireFlyStatus();
                showErrorStatus();
                showFuelTemperatureSensor();
                showDepulsatorLevelSensor();
                showBufferTankLevelSensor();
                showFireSensor();
                showHoldingFuelTemperature();
                showSoftWareVersion();

            }
        });
        statusMassageWatcherThread.start();
    }

    private void showAddress() {
        connectAddressIP.setText(ConfigFile.getIP() + ":" + FireFlyConnect.getPort());
    }


    private void showErrorStatus() {
        if(isCanRenderData()) {
            switch (FireFlyConnect.getFireFlyConnect().getInArrayLink()[7]) {
                case 0:
                    errorData.setText("Нет ошибки.");
                    break;
                case 1:
                    errorData.setText("Не удалось расжечь.");
                    break;
                case 2:
                    errorData.setText("Не удалось заполнить бак топливом.");
                    break;
                case 3:
                    errorData.setText("Перегрев топлива.");
                    break;
                case 4:
                    errorData.setText("Блокировка.");
                    break;
                case 5:
                    errorData.setText("Клин двигателя внутреннего насоса.");
                    break;
                case 6:
                    errorData.setText("Разрыв обменна данными с пультом.");
                    break;
                case 7:
                    errorData.setText("Обрыв датчика температуры котла.");
                    break;
                default:
                    errorData.setText("Не определена.");
                    break;
            }
        }else{
            errorData.setText("нет данных.");
        }
    }

    private void showConnectStatus() {
            switch (FireFlyConnect.getConnectStatus()) {
                case 0:
                    connectStatus.setFill(Color.ORANGE);
                    connectStatus.setText("Подключение к серверу.");
                    break;
                case 1:
                    connectStatus.setFill(Color.GREEN);
                    connectStatus.setText("Подключен.");
                    break;
                case 2:
                    connectStatus.setFill(Color.ORANGE);
                    connectStatus.setText("Устанавливаем обмен.");
                    break;
                case 3:
                    connectStatus.setFill(Color.RED);
                    connectStatus.setText("Сервер не доступен!");
                    break;
                default:
                    connectStatus.setFill(Color.BLACK);
                    connectStatus.setText("Не определено");
                    break;
            }

    }

    private void showFireFlyStatus(){
        if(isCanRenderData()) {
            switch (FireFlyConnect.getFireFlyConnect().getInArrayLink()[0]) {
                case 0:
                    statusData.setText("Ожидание.");
                    break;
                case 1:
                    statusData.setText("Розжиг.");
                    break;
                case 2:
                    statusData.setText("Горение.");
                    break;
                default:
                    statusData.setText("Не определен.");
            }
        }else{
            statusData.setText("нет данных.");
        }
    }

    private void showFuelTemperatureSensor(){
        if(isCanRenderData()) {
            fuelTemperatureSensorData.setText(String.valueOf(FireFlyConnect.getFireFlyConnect().getInArrayLink()[1]));
        }else{
            fuelTemperatureSensorData.setText("нет данных.");
        }
    }

    private void showDepulsatorLevelSensor(){
        if(isCanRenderData()) {
            depulsatorLevelSensorData.setText(FireFlyConnect.getFireFlyConnect().getInArrayLink()[2] == 1 ? "Накачал." : "Не накачал.");
        }else{
            depulsatorLevelSensorData.setText("нет данных.");
        }
    }

    private void showBufferTankLevelSensor(){
        if(isCanRenderData()) {
            bufferTankLevelSensorData.setText(FireFlyConnect.getFireFlyConnect().getInArrayLink()[3] == 1 ? "Накачал." : "Не накачал.");
        }else{
            bufferTankLevelSensorData.setText("нет данных.");
        }
    }

    private void showFireSensor(){
        if(isCanRenderData()) {
            fireSensorData.setText(FireFlyConnect.getFireFlyConnect().getInArrayLink()[4] == 1 ? "Есть пламя." : "Нет пламени.");
        }else{
            fireSensorData.setText("нет данных.");
        }
    }

    private void showHoldingFuelTemperature(){
        if(isCanRenderData()) {
            holdingFuelTemperatureData.setText(String.valueOf(FireFlyConnect.getFireFlyConnect().getInArrayLink()[8]));
        }else {
            holdingFuelTemperatureData.setText("нет данных.");
        }
    }

    private void showSoftWareVersion(){
        if(isCanRenderData()) {
            softWareVersionData.setText(String.valueOf(FireFlyConnect.getFireFlyConnect().getInArrayLink()[9]));
        }else {
            softWareVersionData.setText("нет данных.");
        }
    }

    private boolean isCanRenderData(){
        return (FireFlyConnect.getFireFlyConnect() != null && FireFlyConnect.getFireFlyConnect().isConnected() && FireFlyConnect.getFireFlyConnect().isDataExchange());
    }

}

