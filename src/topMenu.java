import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class topMenu {

    private MenuBar menuBar;

    public topMenu() {
        init();
    }

    private void init() {
        menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");

        /*add menu items*/
        MenuItem newFile = new MenuItem("New");
        fileMenu.getItems().add(newFile);

        fileMenu.getItems().add(new MenuItem("Save"));
        fileMenu.getItems().add(new SeparatorMenuItem());

        /*Sets on actions*/
        MenuItem exit = new MenuItem("Exit");
        fileMenu.getItems().add(exit);
        exit.setOnAction(e -> new AlertBox("Warning", "Exit program?", "Cancel").getAnswer());

        Menu editMenu = new Menu("Edit");

        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().add(new MenuItem("Tutorial"));
        MenuItem about = new MenuItem("About");
        helpMenu.getItems().add(about);
        about.setOnAction(e -> new AlertBox("About",
                "Christian Glissov and Nadja Riis.\n      01666 Project Work 2017", "Close").getAnswer());

        /*Add items to root*/
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
    }

    public MenuBar getMenu() {
        return menuBar;
    }
}
