package authoringInterface.editor.menuBarView;

import api.SubView;
import authoring.AuthoringTools;
import authoringInterface.MainAuthoringProgram;
import authoringInterface.View;
import authoringInterface.editor.menuBarView.subMenuBarView.LoadFileView;
import graphUI.groovy.GroovyPane;
import graphUI.phase.GraphPane;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import runningGame.GameWindow;
public class EditorMenuBarView implements SubView<MenuBar> {

    private MenuBar menuBar;
    private GameWindow gameWindow;
    private LoadFileView newFile;
    private AuthoringTools authTools;

    public EditorMenuBarView(AuthoringTools authTools) {
        this.authTools = authTools;

        menuBar = new MenuBar();
        menuBar.setPrefHeight(View.MENU_BAR_HEIGHT);

        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu tools = new Menu("Tools");
        Menu run = new Menu("Run");
        Menu help = new Menu("Help");

        MenuItem newFile = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem close = new MenuItem("Close");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        MenuItem runProject = new MenuItem("Run");
        MenuItem helpDoc = new MenuItem("Help");
        MenuItem about = new MenuItem("About");
        MenuItem graph = new MenuItem("Graph");
        MenuItem groovyGraph = new MenuItem("GroovyGraph");

        save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        newFile.setOnAction(this::handleNewFile);
        open.setOnAction(this::handleOpen);
        save.setOnAction(this::handleSave);
        saveAs.setOnAction(this::handleSaveAs);
        close.setOnAction(this::handleClose);
        undo.setOnAction(this::handleUndo);
        redo.setOnAction(this::handleRedo);
        runProject.setOnAction(this::handleRunProject);
        helpDoc.setOnAction(this::handleHelpDoc);
        about.setOnAction(this::handleAbout);
        graph.setOnAction(this::handleGraph);
        groovyGraph.setOnAction(this::handleGroovyGraph);

        file.getItems().addAll(newFile, open, save, saveAs, close);
        edit.getItems().addAll(undo, redo, graph, groovyGraph);
        run.getItems().addAll(runProject);
        help.getItems().addAll(helpDoc, about);

        menuBar.getMenus().addAll(file, edit, tools, run, help);
    }
    private void handleGroovyGraph(ActionEvent actionEvent) {
        new GroovyPane(new Stage(), authTools.factory());
    }

    private void handleGraph(ActionEvent e) {
        new GraphPane(new Stage());
    }


    void handleOpen(ActionEvent event) {
        newFile = new LoadFileView();
    }
    void handleNewFile(ActionEvent event) { }
    void handleSave(ActionEvent event) {}
    void handleSaveAs(ActionEvent event) {}
    void handleClose(ActionEvent event) {}
    void handleUndo(ActionEvent event) {}
    void handleRedo(ActionEvent event) {}
    void handleRunProject(ActionEvent event) {
        Stage newWindow = new Stage();
        newWindow.setTitle("Your Game");
        gameWindow = new GameWindow();
        Scene newScene = new Scene(gameWindow.getView(), View.GAME_WIDTH, View.GAME_HEIGHT);
        newWindow.setScene(newScene);
        newWindow.setX(MainAuthoringProgram.SCREEN_WIDTH*0.5 - View.GAME_WIDTH*0.5);
        newWindow.setY(MainAuthoringProgram.SCREEN_HEIGHT*0.5 - View.GAME_HEIGHT*0.5);
        newWindow.show();
    }
    void handleHelpDoc(ActionEvent event) {}
    void handleAbout(ActionEvent event) {}

    @Override
    public MenuBar getView() {
        return menuBar;
    }
}
