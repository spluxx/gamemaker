package gameObjects.category;

import javafx.collections.ObservableMap;

import java.util.Map;
import java.util.function.Supplier;

public class SimpleCategoryInstance implements CategoryInstance {
    private String className;
    private String instanceName;
    private int instanceId;

    private String imagePath;
    private Map<String, String> propertiesMap;
    private Supplier<CategoryClass> getCategoryClassFunc;

    public SimpleCategoryInstance(
            String className,
            String imagePath,
            Map<String, String> properties,
            Supplier<CategoryClass> getCategoryClassFunc) {
        this.className = className;
        this.instanceName = className; // ???
        this.imagePath = imagePath;
        this.propertiesMap = properties;
        this.getCategoryClassFunc = getCategoryClassFunc;
        instanceId = 0;
    }

    /**
     * @return
     */
    @Override
    public int getInstanceId() { return instanceId; }

    /**
     */
    @Override
    public void setInstanceId(int newId) { instanceId = newId; }

    /**
     * @return
     */
    @Override
    public String getClassName() { return className; }

    /**
     * @param name
     */
    @Override
    public void setClassName(String name) { className = name; }

    @Override
    public String getInstanceName() { return instanceName; }

    @Override
    public void setInstanceName(String newInstanceName) { instanceName = newInstanceName; }

    @Override
    public Map<String, String> getPropertiesMap() { return propertiesMap; }

    /**
     * @param propertyName
     * @param defaultValue
     * @return
     */
    @Override
    public void addProperty(String propertyName, String defaultValue) {
        propertiesMap.put(propertyName, defaultValue);
    }

    /**
     * @param propertyName
     * @return
     */
    @Override
    public void removeProperty(String propertyName) {
        propertiesMap.remove(propertyName);
    }

    @Override
    public boolean changePropertyValue(String propertyName, String newValue) {
        if (!propertiesMap.containsKey(propertyName)) {
            return false;
        }
        propertiesMap.put(propertyName, newValue);
        return true;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void setImagePath(String newImagePath) {
        imagePath = newImagePath;
    }

    @Override
    public CategoryClass getGameObjectClass() {
        return getCategoryClassFunc.get();
    }

}
