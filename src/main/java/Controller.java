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
                    errorData.setText("?????? ????????????.");
                    break;
                case 1:
                    errorData.setText("???? ?????????????? ??????????????.");
                    break;
                case 2:
                    errorData.setText("???? ?????????????? ?????????????????? ?????? ????????????????.");
                    break;
                case 3:
                    errorData.setText("???????????????? ??????????????.");
                    break;
                case 4:
                    errorData.setText("????????????????????.");
                    break;
                case 5:
                    errorData.setText("???????? ?????????????????? ?????????????????????? ????????????.");
                    break;
                case 6:
                    errorData.setText("???????????? ?????????????? ?????????????? ?? ??????????????.");
                    break;
                case 7:
                    errorData.setText("?????????? ?????????????? ?????????????????????? ??????????.");
                    break;
                default:
                    errorData.setText("???? ????????????????????.");
                    break;
            }
        }else{
            errorData.setText("?????? ????????????.");
        }
    }

    private void showConnectStatus() {
            switch (FireFlyConnect.getConnectStatus()) {
                case 0:
                    connectStatus.setFill(Color.ORANGE);
                    connectStatus.setText("?????????????????????? ?? ??????????????.");
                    break;
                case 1:
                    connectStatus.setFill(Color.GREEN);
                    connectStatus.setText("??????????????????.");
                    break;
                case 2:
                    connectStatus.setFill(Color.ORANGE);
                    connectStatus.setText("?????????????????????????? ??????????.");
                    break;
                case 3:
                    connectStatus.setFill(Color.RED);
                    connectStatus.setText("???????????? ???? ????????????????!");
                    break;
                default:
                    connectStatus.setFill(Color.BLACK);
                    connectStatus.setText("???? ????????????????????");
                    break;
            }

    }

    private void showFireFlyStatus(){
        if(isCanRenderData()) {
            switch (FireFlyConnect.getFireFlyConnect().getInArrayLink()[0]) {
                case 0:
                    statusData.setText("????????????????.");
                    break;
                case 1:
                    statusData.setText("????????????.");
                    break;
                case 2:
                    statusData.setText("??????????????.");
                    break;
                default:
                    statusData.setText("???? ??????????????????.");
            }
        }else{
            statusData.setText("?????? ????????????.");
        }
    }

    private void showFuelTemperatureSensor(){
        if(isCanRenderData()) {
            fuelTemperatureSensorData.setText(String.valueOf(FireFlyConnect.getFireFlyConnect().getInArrayLink()[1]));
        }else{
            fuelTemperatureSensorData.setText("?????? ????????????.");
        }
    }

    private void showDepulsatorLevelSensor(){
        if(isCanRenderData()) {
            depulsatorLevelSensorData.setText(FireFlyConnect.getFireFlyConnect().getInArrayLink()[2] == 1 ? "??????????????." : "???? ??????????????.");
        }else{
            depulsatorLevelSensorData.setText("?????? ????????????.");
        }
    }

    private void showBufferTankLevelSensor(){
        if(isCanRenderData()) {
            bufferTankLevelSensorData.setText(FireFlyConnect.getFireFlyConnect().getInArrayLink()[3] == 1 ? "??????????????." : "???? ??????????????.");
        }else{
            bufferTankLevelSensorData.setText("?????? ????????????.");
        }
    }

    private void showFireSensor(){
        if(isCanRenderData()) {
            fireSensorData.setText(FireFlyConnect.getFireFlyConnect().getInArrayLink()[4] == 1 ? "???????? ??????????." : "?????? ??????????????.");
        }else{
            fireSensorData.setText("?????? ????????????.");
        }
    }

    private void showHoldingFuelTemperature(){
        if(isCanRenderData()) {
            holdingFuelTemperatureData.setText(String.valueOf(FireFlyConnect.getFireFlyConnect().getInArrayLink()[8]));
        }else {
            holdingFuelTemperatureData.setText("?????? ????????????.");
        }
    }

    private void showSoftWareVersion(){
        if(isCanRenderData()) {
            softWareVersionData.setText(String.valueOf(FireFlyConnect.getFireFlyConnect().getInArrayLink()[9]));
        }else {
            softWareVersionData.setText("?????? ????????????.");
        }
    }

    private boolean isCanRenderData(){
        return (FireFlyConnect.getFireFlyConnect() != null && FireFlyConnect.getFireFlyConnect().isConnected() && FireFlyConnect.getFireFlyConnect().isDataExchange());
    }

}

