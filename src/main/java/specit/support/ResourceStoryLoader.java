package specit.support;

import specit.SpecItRuntimeException;
import specit.StoryLoader;
import specit.util.IO;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 */
public class ResourceStoryLoader implements StoryLoader {

    private final String basePackage;
    private final IO io;
    private String charset = "UTF-8";

    public ResourceStoryLoader(String basePackage) {
        this(basePackage, new IO());
    }

    public ResourceStoryLoader(String basePackage, IO io) {
        this.basePackage = basePackage;
        this.io = io;
    }

    public ResourceStoryLoader withCharset(String charset) {
        this.charset = charset;
        return this;
    }

    @Override
    public String loadStoryAsText(String storyPath) {
        String resourceName = (basePackage + '/' + storyPath).replaceAll("[/]+", "/");
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }

        InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);
        if (stream == null) {
            throw new SpecItRuntimeException("Missing story resource <" + resourceName + ">");
        }

        try {
            return io.toString(stream, charset);
        }
        catch (IOException e) {
            throw new SpecItRuntimeException("Failed to load story <" + storyPath + ">", e);
        }
        finally {
            IO.closeQuietly(stream);
        }
    }
}
