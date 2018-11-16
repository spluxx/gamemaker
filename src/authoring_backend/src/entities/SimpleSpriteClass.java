package entities;

import groovy.api.BlockGraph;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SimpleSpriteClass implements SpriteClass {

    private String CONST_DEFAULTHEIGHT = "defaultHeight";
    private String CONST_DEFAULTWIDTH = "defaultWidth";
    private String CONST_ID = "id";
    private String CONST_MOVABLE = "movable";

    private ReadOnlyIntegerWrapper classId;
    private IdManager myIdManager;
    private SimpleIntegerProperty height;
    private SimpleIntegerProperty width;
    private SimpleBooleanProperty movable;
    private ObservableList<String> imagePathList;
    private ObservableMap<String, BlockGraph> propertiesMap;
    private BlockGraph imageSelector;

    private SimpleSpriteClass() {
        classId = new ReadOnlyIntegerWrapper(this, CONST_ID);
        height = new SimpleIntegerProperty(this, CONST_DEFAULTHEIGHT);
        width = new SimpleIntegerProperty(this, CONST_DEFAULTWIDTH);
        movable = new SimpleBooleanProperty(this, CONST_MOVABLE);
        imagePathList = FXCollections.observableArrayList();
        propertiesMap = FXCollections.observableHashMap();
        myIdManager = new IdManagerClass();
    }

    SimpleSpriteClass(Consumer<SimpleIntegerProperty> setFunc) {
        this();
        setClassId(setFunc);
    }


    @Override
    public ReadOnlyIntegerProperty getClassId() {
        return classId.getReadOnlyProperty();
    }

    @Override
    public void setClassId(Consumer<SimpleIntegerProperty> setFunc) {
        setFunc.accept(classId);
    }

    @Override
    public Supplier<ReadOnlyIntegerProperty> returnClassId() {
        return this::getClassId;
    }

    @Override
    public ObservableMap getPropertiesMap() {
        return propertiesMap;
    }

    @Override
    public boolean addProperty(String propertyName, BlockGraph defaultValue) {
        if (!propertiesMap.containsKey(propertyName)) {
            propertiesMap.put(propertyName, defaultValue);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeProperty(String propertyName) {
        return propertiesMap.remove(propertyName) != null;
    }

    @Override
    public void setDefaultHeightWidth(int defaultHeight, int defaultWidth) {
        height.setValue(defaultHeight);
        width.setValue(defaultWidth);
    }

    @Override
    public SimpleIntegerProperty getDefaultHeight() {
        return height;
    }

    @Override
    public SimpleIntegerProperty getDefaultWidth() {
        return width;
    }

    @Override
    public ObservableList getImagePathList() {
        return imagePathList;
    }

    @Override
    public void addImagePath(String path) {
        imagePathList.add(path);
    }


    @Override
    public boolean removeImagePath(int index) {
        try {
            imagePathList.remove(index);
            return true;
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public void setImageSelector(BlockGraph blockCode) {
        imageSelector = blockCode;
    }

    @Override
    public BlockGraph getImageSelectorCode() {
        return imageSelector;
    }

    @Override
    public EntityInstance createInstance() {
        EntityInstance spriteInstance = new SimpleSpriteInstance();

    }

    @Override
    public SimpleBooleanProperty isMovable() {
        return movable;
    }

    @Override
    public void setMovable(boolean move) {
        movable.setValue(move);
    }
}
