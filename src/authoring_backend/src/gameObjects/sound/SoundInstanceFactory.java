package gameObjects.sound;

import authoringUtils.exception.GameObjectTypeException;
import authoringUtils.exception.InvalidIdException;
import gameObjects.ThrowingConsumer;
import gameObjects.gameObject.GameObjectInstance;
import gameObjects.gameObject.GameObjectType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SoundInstanceFactory {
    private Consumer<GameObjectInstance> requestInstanceIdFunc;
    private ThrowingConsumer<GameObjectInstance, InvalidIdException> addInstanceToMapFunc;


    public SoundInstanceFactory(
            Consumer<GameObjectInstance> requestInstanceIdFunc,
            ThrowingConsumer<GameObjectInstance, InvalidIdException> addInstanceToMapFunc) {

        this.requestInstanceIdFunc = requestInstanceIdFunc;
        this.addInstanceToMapFunc = addInstanceToMapFunc;
    }

    public SoundInstance createInstance(SoundClass soundPrototype)
            throws GameObjectTypeException, InvalidIdException {

        if (soundPrototype.getType() != GameObjectType.SOUND) {
            throw new GameObjectTypeException(GameObjectType.SOUND);
        }
        SimpleStringProperty mediaFilePathCopy = new SimpleStringProperty();
        SimpleDoubleProperty durationCopy = new SimpleDoubleProperty();
        ObservableMap propertiesMapCopy = FXCollections.observableHashMap();
        mediaFilePathCopy.setValue(soundPrototype.getMediaFilePath().getValue());
        durationCopy.setValue((soundPrototype.getDuration().getValue()));
        propertiesMapCopy.putAll(soundPrototype.getPropertiesMap());
        Supplier<SoundClass> getCategoryClassFunc = () -> soundPrototype;
        SoundInstance categoryInstance = new SimpleSoundInstance(soundPrototype.getClassName().getValue(), mediaFilePathCopy, propertiesMapCopy, durationCopy, getCategoryClassFunc);
        requestInstanceIdFunc.accept(categoryInstance);
        addInstanceToMapFunc.accept(categoryInstance);
        return categoryInstance;

    }
}
