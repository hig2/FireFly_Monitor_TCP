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
                if (FireFlyConnect.getFireFlyConnect() != null) {
                    FireFlyConnect.getFireFlyConnect().close();
                }
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

                try {
                    /*
                    if(FireFlyConnect.getMessageCurrentStatus() != null && FireFlyConnect.getFireFlyConnect().isConnected()){

                    }

                     */
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        statusMassageWatcherThread.start();
    }

    private void showAddress() {
        connectAddressIP.setText(ConfigFile.getIP() + ":" + FireFlyConnect.getPort());
    }


    private void showErrorStatus() {
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

}