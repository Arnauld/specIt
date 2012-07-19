package specit.util;

import java.util.ResourceBundle;

/**
 *
 */
public class ResourceBundles {

    private ResourceBundles() {}

    @SuppressWarnings("unchecked")
    public static <T> T getOrDefault(ResourceBundle bundle, String key, T defaultValue) {
        if(bundle.containsKey(key)) {
            return (T)bundle.getObject(key);
        }
        else {
            return defaultValue;
        }
    }

}
