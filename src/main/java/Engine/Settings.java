package Engine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    public static String getProperty(String key) throws IOException {

        File file = new File("settings/GlobalWindowSettings");

        Properties properties = new Properties();
        properties.load(new FileReader(file));

       return properties.getProperty(key);
    }
}
