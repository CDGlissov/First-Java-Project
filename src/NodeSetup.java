import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Objects;

public class NodeSetup {

    private static double orgSceneX;
    private static double orgSceneY;
    private Circle node;
    private TextField name;
    private Label number;
    private ContextMenu nodeMenu;
    private boolean click;
    private double scale = 1.0;
    private Circle drag;
    private Line line;
    private ArrayList<Circle> nodes;
    private boolean isFirstClick = true;
    private int i = 0;


    public NodeSetup(Color color, Pane nodePane, int count) {
        initNode(color,nodePane, count);
    }
    public NodeSetup(Pane nodePane, Circle node, TextField name, Label number, Circle drag) {
        initMenu(nodePane, node, name, number, drag);
    }

    private void initNode(Color color, Pane nodePane, int count) {
        node = new Circle(20, color);
        node.setCursor(Cursor.HAND);

        nodes = RightMenu.getNodes();

        number = new Label(Integer.toString(count));

        name = new TextField("Name");
        name.prefColumnCountProperty().bind(name.textProperty().length());
        name.setStyle("-fx-background-color: transparent;");

        drag = new Circle(5, Color.BLACK);
        drag.setCursor(Cursor.CROSSHAIR);

        /*sets start coords*/
        node.setLayoutX(nodePane.getWidth()/2);
        node.setLayoutY(nodePane.getHeight()/2);

        name.setLayoutX(nodePane.getWidth()/2 - 27);
        name.setLayoutY(nodePane.getHeight()/2 + 16);

        number.setLayoutX(nodePane.getWidth()/2 - 25);
        number.setLayoutY(nodePane.getHeight()/2 - 40);

        drag.setLayoutX(node.getLayoutX() + node.getRadius());
        drag.setLayoutY(node.getLayoutY());
        ArrayList<Circle> draggers = new ArrayList<>();
        draggers.add(drag);

        /*Gets coordinates when node is pressed*/
        node.setOnMousePressed(e -> nodePressed(e, nodePane));

        drag.setOnMouseClicked(e -> {
            dragClicked(e, nodePane, count);
        });

        /*Drag node*/
        node.setOnMouseDragged(e -> nodeDragged(e,nodePane));

        /*Scroll zoom of a node*/
        node.setOnScroll(e -> nodeScrollZoom(e));

        /*drag window*/
        node.setOnMouseReleased((MouseEvent e) -> {
            click = false;
            dragWindow(nodePane, click);
        });

        nodePane.setOnScroll(e -> {
            double delta = 1.1;
            if (e.getDeltaY() < 0)
                scale = 1/delta;
            else if (e.getDeltaY() > 0)
                scale = 1*delta;

            nodePane.setScaleX(nodePane.getScaleX()*scale);
            nodePane.setScaleY(nodePane.getScaleY()*scale);
        });

//        drag.setOnMouseReleased(e -> dragReleased(e, nodePane, count));

        name.toFront();
        nodePane.getChildren().addAll(name, number, drag);
    }


    public Circle getNode() {
        return node;
    }

    protected void nodePressed(MouseEvent e, Pane nodePane){
        click = true;
        dragWindow(nodePane, click);
        /* Gets node context menu */
        MouseButton button = e.getButton();
        if(button == MouseButton.SECONDARY) {
            new NodeSetup(nodePane, node, name, number, drag);
        }

        /*Start coordinates of node*/
        orgSceneX = e.getSceneX();
        orgSceneY = e.getSceneY();
    }

    private void dragClicked(MouseEvent e, Pane nodePane, int count){
        click = true;
        dragWindow(nodePane, click);
        line = new Line();

        if(isFirstClick) {
            System.out.println("You have clicked once");

//            line.setStartX(drag.getCenterX());
//            line.setStartY(drag.getCenterY());

            line.startXProperty().bind(drag.centerXProperty().add(drag.translateXProperty()));
            line.startYProperty().bind(drag.centerYProperty().add(drag.translateYProperty()));

            isFirstClick = false;
        }
        else {
            System.out.println("You have clicked twice");

            line.endXProperty().bind(drag.centerXProperty().add(drag.translateXProperty()));
            line.endYProperty().bind(drag.centerYProperty().add(drag.translateYProperty()));

//            line.setEndX(drag.getCenterX());
//            line.setEndY(drag.getCenterY());

            line.setLayoutX(nodePane.getWidth() / 2);
            line.setLayoutY(nodePane.getHeight() / 2);

            nodePane.getChildren().add(line);
            line.toBack();

            isFirstClick = true;
        }
    }

    private void nodeDragged(MouseEvent e, Pane nodePane){
        click = true;
        dragWindow(nodePane, click);
        /*offset of node*/
        double offsetX = e.getSceneX() - orgSceneX;
        double offsetY = e.getSceneY() - orgSceneY;

        node = (Circle) (e.getSource());

        /*new center of node*/
        node.setCenterX(node.getCenterX() + offsetX/nodePane.getScaleX());
        node.setCenterY(node.getCenterY() + offsetY/nodePane.getScaleY());

        name.setLayoutX(name.getLayoutX() + offsetX/nodePane.getScaleX());
        name.setLayoutY(name.getLayoutY() + offsetY/nodePane.getScaleY());

        number.setLayoutX(number.getLayoutX() + offsetX/nodePane.getScaleX());
        number.setLayoutY(number.getLayoutY() + offsetY/nodePane.getScaleY());

        drag.setCenterX(drag.getCenterX() + offsetX/nodePane.getScaleX());
        drag.setCenterY(drag.getCenterY() + offsetY/nodePane.getScaleY());

        /*Update coordinates*/
        orgSceneX = e.getSceneX();
        orgSceneY = e.getSceneY();
    }

    /* Should we delete this method? */
    private void nodeScrollZoom(ScrollEvent e){
        double zoom = 1.1;
        double deltaY = e.getDeltaY();
        if (deltaY < 0) {
            zoom = 2.0 - zoom;
        }
        node.setScaleX(node.getScaleX() * zoom);
        node.setScaleY(node.getScaleY() * zoom);
        e.consume();
    }

    /*PROTOTYPE*/
    private void dragWindow(Pane nodePane, boolean click){
        if(!click) {
            nodePane.setOnMousePressed(e -> {
                orgSceneX = e.getSceneX();
                orgSceneY = e.getSceneY();
            });

            nodePane.setOnMouseDragged(e -> {
                double xOffset = e.getSceneX() - orgSceneX;
                double yOffset = e.getSceneY() - orgSceneY;

                nodePane.setTranslateX(nodePane.getTranslateX() + xOffset);
                nodePane.setTranslateY(nodePane.getTranslateY() + yOffset);
                orgSceneX = e.getSceneX();
                orgSceneY = e.getSceneY();

                e.consume();
            });
        }
    }
    /*PROTOTYPE*/

    private void initMenu(Pane nodePane, Circle node, TextField name, Label number, Circle drag) {
        nodeMenu = new ContextMenu();
        ArrayList<Line> connections = RightMenu.getConnections();

        for (Line connection : connections) {
            System.out.println(connection);
            System.out.println(node.getCenterX());
        }

        /*Remove node*/
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> {
            nodePane.getChildren().remove(node);
            nodePane.getChildren().remove(name);
            nodePane.getChildren().remove(number);
            nodePane.getChildren().remove(drag);
            RightMenu.removeNode(true, node);

            /*A for each loop*/
            for(Line line: connections){
                double startX = line.startXProperty().get();
                double startY = line.startYProperty().get();
                double endX = line.endXProperty().get();
                double endY = line.endYProperty().get();
                if(Objects.equals(node.getCenterX(), startX) && Objects.equals(node.getCenterY(), startY) ||
                        Objects.equals(node.getCenterX(), endX) && Objects.equals(node.getCenterY(), endY)) {
                    nodePane.getChildren().remove(line);
                }
            }

        });

        /*Rename node*/
        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(e -> {

        });

        MenuItem cancel = new MenuItem("Cancel");
        cancel.setOnAction(e -> nodeMenu.hide());

        nodeMenu.getItems().addAll(delete, rename, cancel);

        nodePane.setOnContextMenuRequested(e ->
                nodeMenu.show(nodePane, e.getScreenX(), e.getScreenY()));
    }

}