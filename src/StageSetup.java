import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StageSetup {

    private Stage stage;
    private Scene scene;


    public StageSetup(Stage stage) {
        this.stage = stage;
        init();
    }

    private void init() {
        BorderPane mainPane = new BorderPane();

        /*Creates node pane*/
        Pane nodePane = new Pane();
        nodePane.setStyle("-fx-border-color: #3f3f3f");


        /*makes node pane*/
        mainPane.setCenter(nodePane);


        /*Sets right part of mainPane, rightMenu*/
        mainPane.setRight(new RightMenu(nodePane).getRightLayout());


        /*Makes upper menu bar and adds to main root*/
        VBox topBar = new VBox();
        topBar.getChildren().add(new topMenu().getMenu());
        mainPane.setTop(topBar);

        /*Sets bottom part of mainPane, infoBar*/
        mainPane.setBottom(new infoBar(nodePane).getBar());
        /*gets scale from text field in infoBar*/
        System.out.println(new infoBar(nodePane).getScale());

        /*Adds close option to main stage*/
        stage.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });


        /*sets main scene onto main stage, root is mainPane*/
        scene = new Scene(mainPane, 800, 800);
        stage.setScene(scene);
        stage.setTitle("TREsPASS");
        scene.getStylesheets().add("theme.css");

    }


    public void showStage() {
        stage.show();
    }

    private void closeProgram() {
        boolean answer = new AlertBox("Warning","Exit program?", "Cancel").getAnswer();
        if(answer) {
            stage.close();
        }
    }

}
