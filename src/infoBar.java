import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


public class infoBar {

    private HBox infoBar;
    public double scale =1;

    public infoBar(Pane nodePane) {
        init(nodePane);
    }


    private void init(Pane nodePane) {
        infoBar = new HBox();

        /*Sets padding and style*/
        infoBar.setPadding(new Insets(3, 12, 3, 8));
        infoBar.setSpacing(10);
        infoBar.setStyle("-fx-background-color: #494646;");
        infoBar.widthProperty();

        /*Adds text field*/
        TextField text = new TextField();
        text.setMaxWidth(80);

        /*Adds button next to text field*/
        Button zoomButton = new Button("Zoom");
        zoomButton.setOnAction(e -> {
                if (isInt(text, text.getText())) {
                    scale = Double.parseDouble(text.getText());
                    nodePane.setScaleX(nodePane.getScaleX()*scale);
                    nodePane.setScaleY(nodePane.getScaleY()*scale);
                }
            });

        /*add children to root*/
        infoBar.getChildren().addAll(text, zoomButton);
    }

    /*Check if text field scale is a number*/
    private boolean isInt(TextField input, String message){
        try{
            double number = Double.parseDouble(input.getText());
            System.out.println(number);
            return true;
        }catch(NumberFormatException e){
            System.out.print("Error: " +  message + " is not a number");
            return false;
        }

    }

    public HBox getBar() {
        return infoBar;
    }

    public double getScale(){
        return scale;
    }


}
