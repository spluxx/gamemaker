package authoringInterface.spritechoosingwindow;

import authoringInterface.sidebar.Entity;
import authoringInterface.sidebar.ObjectManager;
import authoringInterface.sidebar.SideView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

/**
 * Pop-up window for the user to add settings for Entity
 *
 * @author jl729
 */

public class EntityWindow {
    private final Stage primaryStage;
    private final ObjectManager objectManager;
    private final SideView mySideView;
    private final Integer id;
    private final TreeItem item;
    private GridPane myPane;
    private Button imageBtn;
    private ImageView imageView;
    private Button applyBtn;
    private Image mySprite;
    private TextField nameField;
    private VBox imageBox;
    private Stage dialog;

    public EntityWindow(Stage primaryStage, ObjectManager objectManager, SideView mySideView, Integer id, TreeItem item){
        this.primaryStage = primaryStage;
        this.objectManager = objectManager;
        this.mySideView = mySideView;
        this.item = item;
        this.id = id;
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);

        initializeGridPane();
        initializeElements();
        addElementsGridPane();

        Scene dialogScene = new Scene(myPane, 600, 500);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void initializeElements() {
        nameField = new TextField();
        // imageBox contains an imageView, which contains an image that can be changed
        imageView = new ImageView();
        imageBox = new VBox(imageView);

        imageBtn = new Button("Choose Sprite");
        imageBtn.setOnMouseClicked(e -> {
            final FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                mySprite = new Image(file.toURI().toString());
                imageView.setImage(mySprite);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
            }
        });
        
        applyBtn = new Button("Apply");
        applyBtn.setOnMouseClicked(e -> {
            // TODO: 11/13/18 add Events to somewhere
            Entity myEntity = new Entity(mySprite, id, nameField.getText());
            objectManager.getEntityList().add(myEntity);
            mySideView.addCell(myEntity, item);
            dialog.close();
        });
    }

    private GridPane initializeGridPane() {
        myPane = new GridPane();
        var row1 = new RowConstraints();
        row1.setPercentHeight(10);
        var row2 = new RowConstraints();
        row2.setPercentHeight(15);
        var row3 = new RowConstraints();
        row3.setPercentHeight(65);
        var row4 = new RowConstraints();
        row4.setPercentHeight(10);
        var col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        var col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        myPane.getColumnConstraints().addAll(col1, col2);
        myPane.getRowConstraints().addAll(row1, row2, row3, row4);
        return myPane;
    }

    private void addElementsGridPane() {
        myPane.add(nameField, 0, 0);
        myPane.add(imageBtn, 0, 1);
        myPane.add(imageBox, 0, 2);
        myPane.add(new Label("Events"), 1, 1);
        myPane.add(new VBox(new Label("Player Events go here")), 1, 2);
        myPane.add(applyBtn, 1, 3);
    }
}
