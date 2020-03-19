import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;


public class RightMenu {

    private Button addButton;
    private VBox rightMenu;
    private ComboBox<String> comboBox = new ComboBox<>();
    private static ArrayList<Circle> nodes = new ArrayList<>();
    private int count = 0;
    private boolean allowed;
    private TextField text1, text2;
    private Line line;
    private static ArrayList<Line> connections = new ArrayList<>();
    private Text title;
    private Text info[];
    private Color color;
    private String type = "";
    private String fill = "";
    private Circle node;

    public RightMenu(Pane nodePane) {
        init(nodePane);
    }


    private void init(Pane nodePane) {
        rightMenu = new VBox();

        /*sets padding for right menu*/
        rightMenu.setPadding(new Insets(15, 25, 15, 25));
        rightMenu.setSpacing(10);
        rightMenu.setStyle("-fx-background-color: #494646;");
        rightMenu.widthProperty();
        rightMenu.setPrefWidth(180);

        /*adds button to right menu*/
        addButton = new Button("Add Node");

        /*makes combo box for right menu and add items*/
        comboBox = new ComboBox();
        comboBox.getItems().addAll("Attacker", "Actor", "Location", "Asset", "Default");
        comboBox.setPromptText("Select Node");
        comboBox.setMaxWidth(125);

        addButton.setOnAction(e -> handleButtonPressed(nodePane));
        addButton.setMaxWidth(125);

        Button connectButton = new Button("Connect Nodes");
        connectButton.setMaxWidth(125);
        text1 = new TextField();
        text1.setMaxWidth(125);
        text2 = new TextField();
        text2.setMaxWidth(125);
        text1.setPromptText("Connect node 1");
        text2.setPromptText("Connect node 2");

        connectButton.setOnAction(e -> {
            allowed = true;

            /* Checks if the inputs are empty */
            if(Objects.equals(text1.getText(), "") || Objects.equals(text2.getText(), "")) allowed = false;

            /*Checks if input is integer*/
            if(isInt(text1,text1.getText()) && isInt(text2,text2.getText())) getLine(nodePane);
            if(!allowed){
                new AlertBox("Warning", "ERROR: You may try to connect an invalid node\n", "Try again");
            }
        });

        /*add button and box to pane*/
        rightMenu.getChildren().addAll(comboBox, addButton, connectButton, text1, text2);

    }

    /*gets choice of combo box*/
    public static String getChoice(ComboBox<String> comboBox) {
        return comboBox.getValue();
    }

    private void getInfo(Paint fill, double centerX, double centerY) {
        title = new Text();
        title.setText("Node information");
        title.setFill(Color.WHITESMOKE);
        title.setStyle("-fx-font-weight: bold");

        info = new Text[] {
                new Text(),
                new Text(),
                new Text(),
        };

        /*Implement for all types of nodes!*/
        if(fill == Color.LIGHTSTEELBLUE) type = "Default";
        else if(fill == Color.SILVER) type = "Actor";
        else if(fill == Color.DIMGRAY) type = "Location";
        else if(fill == Color.LIGHTSLATEGRAY) type = "Asset";

        // Set the number format so we get at most 2 digits after the comma
        DecimalFormat numberFormat = new DecimalFormat("#.##");

        info[0].setText("Type: " + type);
        info[1].setText("Center: " + numberFormat.format(centerX) + ", " + numberFormat.format(centerY));

        rightMenu.getChildren().add(title);
        for (int i = 0; i < 2; i++) {
            info[i].setFont(Font.font("", FontWeight.NORMAL, 11));
            info[i].setFill(Color.WHITE);
            VBox.setMargin(info[i], new Insets(0, 0, 0, 8));
            rightMenu.getChildren().add(info[i]);
        }
    }

    private void removeInfo() {
        rightMenu.getChildren().remove(title);
        for (int i = 0; i < 2; i++) {
            rightMenu.getChildren().remove(info[i]);
        }
        type = "";
    }


    public Line getLine(Pane nodePane) {
        line = new Line();
        //line.setStroke(Color.RED);

        if(allowed) {
            int input1 = Integer.parseInt(text1.getText()) - 1;
            int input2 = Integer.parseInt(text2.getText()) - 1;
                /* Checks if the inputs are the same */
            if (input1 == input2) allowed = false;
                /* Checks if the inputs are larger than the number of nodes currently present*/
            if (nodes.size() < input1 || nodes.size() < input2) allowed = false;
            if(nodes.get(input1) == null || nodes.get(input2) == null) allowed =false;
            if (input1 < 0 || input2 < 0) allowed = false;

            if(allowed) {
                line.startXProperty().bind((nodes.get(input1)).centerXProperty().add((nodes.get(input1)).translateXProperty()));
                line.startYProperty().bind((nodes.get(input1)).centerYProperty().add((nodes.get(input1)).translateYProperty()));
                line.endXProperty().bind((nodes.get(input2)).centerXProperty().add((nodes.get(input2)).translateXProperty()));
                line.endYProperty().bind((nodes.get(input2)).centerYProperty().add((nodes.get(input2)).translateYProperty()));

                line.setLayoutX(nodePane.getWidth() / 2);
                line.setLayoutY(nodePane.getHeight() / 2);
                connections.add(line);

                nodePane.getChildren().add(line);
                line.toBack();
            }
        }

        return line;
        }

    /*Handle addButton pressed*/
    protected void handleButtonPressed(Pane nodePane){
            /*sets default color*/
            color = Color.DIMGRAY;
            String choice = getChoice(comboBox);

            /*Get node color*/
            if(Objects.equals("Attacker", choice)) {
                color = Color.FIREBRICK;
            }
            else if(Objects.equals("Actor", choice)) {
                color = Color.SILVER;
            }
            else if(Objects.equals("Location", choice)) {
                color = Color.DIMGRAY;
            }
            else if(Objects.equals("Asset", choice)) {
                color = Color.LIGHTSLATEGRAY;
            }
            else {
                color = Color.LIGHTSTEELBLUE;
            }

            count++;
            Circle node = new NodeSetup(color, nodePane, count).getNode();

            node.setOnMouseEntered(e -> {
                getInfo(node.getFill(), node.getCenterX(), node.getCenterY());
            });
            nodes.add(node);
            node.setOnMouseExited(e -> {
                removeInfo();
            });
            nodePane.getChildren().add(node);
    }

    /*Check if text field scale is a number*/
    private boolean isInt(TextField input, String message){
        try{
            double number = Double.parseDouble(input.getText());
            System.out.println(number);
            return true;
        }catch(NumberFormatException e){
            System.out.println("Error: " +  message + " is not a number");
            return false;
        }
    }

    public VBox getRightLayout() {
        return rightMenu;
    }
    public static ArrayList<Line> getConnections() {
        return connections;
    }
    public static ArrayList<Circle> getNodes() {
        return nodes;
    }
    public static void removeNode(boolean remove, Circle node){
        int index;
        if(remove){
            index = nodes.indexOf(node);
            nodes.set(index, null);
        }
    }
}
