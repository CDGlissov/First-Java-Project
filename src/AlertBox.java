import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class AlertBox {

    private static boolean answer;

    public AlertBox(String title, String message, String type) {
        init(title, message, type);
    }

    private void init(String title, String message, String type) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);
        window.setMinHeight(120);

        Label label = new Label(message);
        label.setAlignment(Pos.CENTER);

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            answer = true;
            Platform.exit();
            System.exit(0);
        });

        exitButton.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                answer = true;
                Platform.exit();
                System.exit(0);
            }
        });

        Button cancelButton = new Button(type);
        cancelButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        AnchorPane anchorpane = new AnchorPane();

        HBox hb = new HBox();
        hb.setPadding(new Insets(10, 10, 10, 10));
        hb.setSpacing(10);

        if(Objects.equals("Close", type)) {
            hb.getChildren().addAll(cancelButton);
        }
        else {
            hb.getChildren().addAll(exitButton, cancelButton);
        }

        anchorpane.getChildren().addAll(hb, label);

        AnchorPane.setBottomAnchor(hb, 10.0);
        AnchorPane.setTopAnchor(hb, 40.0);
        AnchorPane.setRightAnchor(hb, 100.0);
        AnchorPane.setLeftAnchor(hb, 100.0);

        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        AnchorPane.setTopAnchor(label, 10.0);

        Scene scene = new Scene(anchorpane);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();
    }

    public boolean getAnswer() {
        return answer;
    }

}
