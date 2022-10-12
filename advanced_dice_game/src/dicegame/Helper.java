package dicegame;

import javafx.scene.image.Image;

import java.io.File;

public class Helper {

    /**
     * Retrieves an image resource.
     *
     * @param imageName The name of the image file.
     * @param format The format of the image file.
     * @return Image
     */
    public static Image getImage(String imageName, String format)
    {
        File file = new File("src/resources/images/" + imageName + "." + format);
        return new Image(file.toURI().toString());
    }
}
