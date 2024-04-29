package ija.project.robot;

import ija.project.robot.logic.room.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws InterruptedException {
        System.Logger logger = System.getLogger(HelloController.class.getName());
        logger.log(System.Logger.Level.INFO, "Button clicked");
        Room room = Room.getInstance();
        room.tick();
        Thread.sleep(100);
        welcomeText.setText(room.toString());
        System.out.println(room);
    }
}