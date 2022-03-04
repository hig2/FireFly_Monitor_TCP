import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.text.Text;

public class Controller implements Initializable {
    private void showStatus(){
        Thread statusMassageWatcherThread = new Thread(()->{
            while (true){
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //textStatus.setText(FireFlyConnect.getMessageCurrentStatus());
                try {
                    if(FireFlyConnect.getMessageCurrentStatus() != null && FireFlyConnect.getFireFlyConnect().isConnected()){

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        statusMassageWatcherThread.start();
    }

    @FXML
    private Button rebootButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button saveButton;

    /*
    @FXML
    private Text textStatus;

     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showStatus();
    }

    public void pressedStopButton(ActionEvent event) {
        if(FireFlyConnect.getFireFlyConnect() != null){

        }
    }

    public void pressedRebootButton(ActionEvent event) {
        if(FireFlyConnect.getFireFlyConnect() != null){

        }
    }

    public void pressedSaveIPButton(ActionEvent event) {
        if(FireFlyConnect.getFireFlyConnect() != null){

        }
    }
}