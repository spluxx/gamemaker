package authoringInterface.editor.menuBarView;

import api.SubView;
import authoring.AuthoringTools;
import authoringInterface.MainAuthoringProgram;
import authoringInterface.View;
import authoringInterface.editor.editView.EditView;
import authoringInterface.editor.memento.Editor;
import authoringInterface.editor.memento.EditorCaretaker;
import authoringInterface.editor.menuBarView.subMenuBarView.*;
import conversion.authoring.CRUDConverterAuthoring;
import gameplay.Initializer;
import graphUI.graphData.PhaseGraphXMLParser;
import graphUI.graphData.SinglePhaseData;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import runningGame.GameWindow;
import utils.serializer.SerializerTestCRUD;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * MenuBarView class
 *
 * @author Haotian
 * @author Amy
 * @author jl729
 */
public class EditorMenuBarView implements SubView<MenuBar> {
    private EditView editView;
    private MenuBar menuBar;
    private GameWindow gameWindow;
    private AuthoringTools authTools;
    private String fileName; //TODO: temp var, will be changed
    private SerializerTestCRUD serializer;

    private SoundView soundView;

    private final EditorCaretaker editorCaretaker = new EditorCaretaker();
    private final Editor editor = new Editor();
    private File myFile = null;
    private Integer currentMemento = 0;
    private Runnable closeWindow; //For each window closable

    public EditorMenuBarView(
            AuthoringTools authTools,
            Runnable closeWindow,
            BiConsumer<Integer, Integer> updateGridDimension,
            EditView editView
    ) {
        serializer = new SerializerTestCRUD();
        this.authTools = authTools;
        this.closeWindow = closeWindow;
        this.editView = editView;
        fileName = "TicTacToe.xml";

        menuBar = new MenuBar();
        menuBar.setPrefHeight(View.MENU_BAR_HEIGHT);

        soundView = new SoundView();

        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu settings = new Menu("Settings");
        Menu run = new Menu("Run");
        Menu help = new Menu("Help");

        MenuItem newFile = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem export = new MenuItem("Export");
        MenuItem save = new MenuItem("Save");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem close = new MenuItem("Close");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        MenuItem runProject = new MenuItem("Run");
        MenuItem resizeGrid = new MenuItem("Resize Grid");
        MenuItem setBGM = new MenuItem("BGM");
        MenuItem helpDoc = new MenuItem("Help");
        MenuItem about = new MenuItem("About");
        MenuItem saveGameObjects = new MenuItem("Save GameObjects");

        save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        resizeGrid.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));
        helpDoc.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));

        newFile.setOnAction(e -> new NewWindowView());
        open.setOnAction(this::handleOpen);
        export.setOnAction(this::handleExport);

        saveGameObjects.setOnAction(e -> new SaveGridAndGameObjectsView(serializer.getXMLString(authTools.entityDB())));

        save.setOnAction(e -> {
            if (myFile != null)
            handleSave(e, myFile);
        });
        saveAs.setOnAction(this::handleSaveAs);
        close.setOnAction(e -> new CloseFileView(closeWindow));
        undo.setOnAction(this::handleUndo);
        redo.setOnAction(this::handleRedo);
        runProject.setOnAction(this::handleRunProject);
        resizeGrid.setOnAction(e -> new ResizeGridView().showAndWait().ifPresent(dimension ->
                updateGridDimension.accept(dimension.getKey(), dimension.getValue())
        ));
        setBGM.setOnAction(e -> soundView.show());
        helpDoc.setOnAction(this::handleHelpDoc);
        about.setOnAction(this::handleAbout);

        file.getItems().addAll(newFile, open, export, save, saveAs, saveGameObjects, close);
        edit.getItems().addAll(undo, redo);
        run.getItems().addAll(runProject);
        settings.getItems().addAll(resizeGrid, setBGM);
        help.getItems().addAll(helpDoc, about);

        menuBar.getMenus().addAll(file, edit, settings, run, help);
    }

    void handleSave(ActionEvent event, File file) {
        // TODO: 11/17/18 Enable and Disable the undo and redo button (handleUndo + handleRedo function)
        editView.getPhaseView().saveXML(file);
        System.out.println("New Content is saved");
        editorCaretaker.addMemento(editor.save());
        editor.setState(editorCaretaker.getMemento(currentMemento++).getSavedState());
    }

    void handleSaveAs(ActionEvent event) {
        myFile = editView.getPhaseView().generateXML();
        handleSave(event, myFile);
    }

    void handleExport(ActionEvent event) {
        new SaveFileView(authTools::toEngineXML);
    }

    void handleOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open project files");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            var parser = new PhaseGraphXMLParser();
            try {
                Map<String, SinglePhaseData> phaseDataMap = parser.parseFile(file);
                regeneratePhaseGraph(phaseDataMap);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
    }

    private void regeneratePhaseGraph(Map<String, SinglePhaseData> phaseDataMap) {
        editView.getPhaseView().reset(phaseDataMap);
    }

    void handleUndo(ActionEvent event) {
        if (currentMemento < 2) return;
        editor.restoreToState(editorCaretaker.getMemento(--currentMemento));
        // need to scan through the map and find out which ones need update
    }
    void handleRedo(ActionEvent event) {
        editor.restoreToState(editorCaretaker.getMemento(++currentMemento));
        // need to scan through the map and find out which ones need update
    }
    void handleRunProject(ActionEvent event) {
        Stage newWindow = new Stage();
        newWindow.setTitle("Your Game");
        gameWindow = new GameWindow();
        try{
            Initializer initializer =
                new Initializer(new File(getClass().getClassLoader().getResource(fileName).getFile()));
            Scene newScene = new Scene(initializer.getRoot(), View.GAME_WIDTH, View.GAME_HEIGHT);
            newScene.addEventFilter(KeyEvent.KEY_RELEASED, initializer::keyFilter);
            newWindow.setScene(newScene);
            newWindow.setX(MainAuthoringProgram.SCREEN_WIDTH*0.5 - View.GAME_WIDTH*0.5);
            newWindow.setY(MainAuthoringProgram.SCREEN_HEIGHT*0.5 - View.GAME_HEIGHT*0.5);
            initializer.setScreenSize(View.GAME_WIDTH, View.GAME_HEIGHT);
            newWindow.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    void handleHelpDoc(ActionEvent event) {}
    void handleAbout(ActionEvent event) {}

    @Override
    public MenuBar getView() {
        return menuBar;
    }
}
