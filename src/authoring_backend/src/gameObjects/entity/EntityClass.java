package gameObjects.entity;

import authoringUtils.exception.GameObjectTypeException;
import authoringUtils.exception.InvalidGameObjectInstanceException;
import authoringUtils.exception.InvalidIdException;
import gameObjects.gameObject.GameObjectClass;
import gameObjects.gameObject.GameObjectType;
import grids.Point;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

public interface EntityClass extends GameObjectClass {

    SimpleBooleanProperty isMovable();

    void setMovable(boolean move);

    EntityInstance createInstance(int playerId, Point point)
            throws InvalidGameObjectInstanceException, GameObjectTypeException, InvalidIdException;

    /**
     * This method adds the image path to the GameObject Class and to all instances of the class.
     * @param path file path of the image
     */
    void addImagePath(String path);

    /**
     * This method gets the image path list of the GameObject Class.
     * @return a list of the file paths of the images
     */
    ObservableList<String> getImagePathList();

    /**
     * This method removes the image path from the Entity Class and from all instances of the class.
     * @param index index of the image file path in the list
     * @return true if the image file path successfully removed
     */
    boolean removeImagePath(int index);

    /**
     * This method sets the GroovyCode for choosing the image to display from the list of images.
     * @param blockCode GroovyCode
     */
    void setImageSelector(String blockCode);

    /**
     * This method gets the image selector code.
     * @return image selector code
     */
    String getImageSelectorCode();


    @Override
    default GameObjectType getType() {
        return GameObjectType.ENTITY;
    }

    SimpleIntegerProperty getHeight();

    SimpleIntegerProperty getWidth();


    void setHeight(int newHeight);

    void setWidth(int newWidth);
}
