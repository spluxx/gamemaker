package gameObjects.player;

        import gameObjects.gameObject.GameObjectClass;
        import gameObjects.gameObject.GameObjectType;

public interface PlayerClass {


    default GameObjectType getType() {
        return GameObjectType.PLAYER;
    }

    PlayerInstance createInstance();
}
