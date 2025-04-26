package clueGame;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.imageio.ImageIO;

public final class Art {
	private Art() {}

    public static BufferedImage load(String path) {
        try (InputStream in = Art.class.getResourceAsStream("/art/" + path)) {
            if (in == null) throw new IllegalArgumentException("Missing art: " + path);
            return ImageIO.read(in);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load " + path, ex);
        }
    }
}
