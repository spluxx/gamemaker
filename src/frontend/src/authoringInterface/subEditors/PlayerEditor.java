package authoringInterface.subEditors;

import authoringInterface.subEditors.exception.IllegalGameObjectNamingException;
import authoringUtils.exception.DuplicateGameObjectClassException;
import authoringUtils.exception.GameObjectClassNotFoundException;
import authoringUtils.exception.GameObjectInstanceNotFoundException;
import authoringUtils.exception.InvalidOperationException;
import gameObjects.crud.GameObjectsCRUDInterface;
import gameObjects.entity.EntityClass;
import gameObjects.player.PlayerClass;
import gameObjects.player.PlayerInstance;
import grids.PointImpl;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.exception.PreviewUnavailableException;
import utils.imageManipulation.ImageManager;
import utils.imageManipulation.JavaFxOperation;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class PlayerEditor extends AbstractGameObjectEditor<PlayerClass, PlayerInstance> {
    private static final double ICON_WIDTH = 50;
    private static final double ICON_HEIGHT = 50;
    private static final double REMOVE_OPACITY = 0.5;
    private static final double IMAGE_PANEL_GAP = 10;
    private Label imageText;
    private Button chooseImage;
    private HBox imagePanel;
    private Set<ImageView> toRemoveImage;
    private Set<String> toRemovePath;
    private String imagePath;
    private Label entityText;
    private CheckBox entitybox;

    PlayerEditor(GameObjectsCRUDInterface manager) {
        super(manager);
        toRemoveImage = new HashSet<>();
        toRemovePath = new HashSet<>();
        nameLabel.setText("Your Player Name");
        nameField.setPromptText("Player 0");

        imageText = new Label("Add an image to your player");
        chooseImage = new Button("Choose image");
        chooseImage.setStyle("-fx-text-fill: white;"
                + "-fx-background-color: #343a40;");
        imagePanel = new HBox(10);
        chooseImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                String imagefilePath = file.toURI().toString();
                imagePath = imagefilePath;
                presentImages();
            }
        });
        imagePanel = new HBox(IMAGE_PANEL_GAP);

        entityText = new Label("Assign Entities");
        entitybox = new CheckBox();

        confirm.setStyle("-fx-text-fill: white;"
                + "-fx-background-color: #343a40;");
        layout.addRow(0, imageText, chooseImage);
        layout.addRow(1, imagePanel);
        layout.addRow(2, entityText);
        layout.addRow(3, entitybox);

        rootPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.BACK_SPACE) {
                toRemovePath.remove(imagePath);
            }
        });
    }

    private void presentImages() {
        imagePanel.getChildren().clear();
        ImageView preview = new ImageView(imagePath);
        preview.setFitWidth(ICON_WIDTH);
        preview.setFitHeight(ICON_HEIGHT);
        imagePanel.getChildren().add(preview);
        preview.setOnMouseClicked(e -> {
            if (!toRemoveImage.remove(preview)) {
                toRemoveImage.add(preview);
                toRemovePath.add(imagePath);
                preview.setOpacity(REMOVE_OPACITY);
            } else {
                toRemovePath.remove(imagePath);
                preview.setOpacity(1);
            }
        });
    }

    @Override
    protected void readGameObjectInstance() {
        readInstanceProperties();
        nameField.setText(gameObjectInstance.getClassName().getValue());
        imagePath = gameObjectInstance.getImagePath().get();
        System.out.print(imagePath);
    }

    @Override
    protected void readGameObjectClass() {
        readClassProperties();
        nameField.setText(gameObjectClass.getClassName().getValue());
        imagePath = gameObjectClass.getImagePath().get();
        System.out.print(imagePath);
    }

    /**
     * This method sets up the confirm logic of adding new TreeItem.
     */
    @Override
    protected void confirmAddTreeItem() throws PreviewUnavailableException, IllegalGameObjectNamingException, DuplicateGameObjectClassException {
        PlayerClass playerClass = gameObjectManager.createPlayerClass(getValidClassName());
        assert playerClass != null;
        TreeItem<String> newItem = new TreeItem<>(playerClass.getClassName().getValue());
        playerClass.setImagePath(imagePath);
        ImageView icon = new ImageView(ImageManager.getPreview(playerClass));
        JavaFxOperation.setWidthAndHeight(icon, ICON_WIDTH, ICON_HEIGHT);
        newItem.setGraphic(icon);
        treeItem.getChildren().add(newItem);
        System.out.print(treeItem.getChildren());
    }

    /**
     * This method sets up the confirm logic of editing existing TreeItem.
     */
    @Override
    protected void confirmEditTreeItem() throws IllegalGameObjectNamingException, InvalidOperationException, PreviewUnavailableException {
        gameObjectClass.setClassName(getValidClassName());
        try {
            ImageManager.removeClassImage(gameObjectClass);
        } catch (GameObjectClassNotFoundException ignored) {
        }
        gameObjectClass.setImagePath(imagePath);
        gameObjectManager.changeGameObjectClassName(gameObjectClass.getClassName().getValue(), nameField.getText());
        ImageView icon2 = new ImageView(ImageManager.getPreview(gameObjectClass));
        JavaFxOperation.setWidthAndHeight(icon2, ICON_WIDTH, ICON_HEIGHT);
        treeItem.setValue(nameField.getText());
        treeItem.setGraphic(icon2);
    }

    /**
     * This method sets up the confirm logic of editing existing Node.
     */
    @Override
    protected void confirmEditNode() throws IllegalGameObjectNamingException, PreviewUnavailableException {
        gameObjectInstance.setInstanceName(getValidInstanceName());
        try {
            ImageManager.removeInstanceImage(gameObjectInstance);
        } catch (GameObjectInstanceNotFoundException ignored) {
        }
        gameObjectInstance.setImagePath(imagePath);
        ((ImageView) nodeEdited).setImage(ImageManager.getPreview(gameObjectInstance));
    }
}
