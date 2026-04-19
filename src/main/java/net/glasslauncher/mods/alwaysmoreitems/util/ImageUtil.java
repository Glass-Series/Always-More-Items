package net.glasslauncher.mods.alwaysmoreitems.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImageUtil {
    private static final Cache<String, Tooltip.Dimension> CACHE = Caffeine.newBuilder().build();

    /**
     * A modified version of <a href="https://stackoverflow.com/a/12164026">SO 12164026</a>
     * Gets image dimensions for given file
     * @param imgFile image file
     * @return dimensions of image
     */
    public static Tooltip.Dimension getImageDimension(File imgFile) {
        return CACHE.get(imgFile.getAbsolutePath(), k -> {
            int pos = imgFile.getName().lastIndexOf(".");
            String suffix = imgFile.getName().substring(pos + 1);
            Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
            if (!iter.hasNext()) {
                throw new RuntimeException(new IOException("Not a known image file: " + imgFile.getAbsolutePath()));
            }
            ImageReader reader = iter.next();

            try {
                ImageInputStream stream = new FileImageInputStream(imgFile);
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                reader.dispose();
                return new Tooltip.Dimension(width, height);
            } catch (IOException e) {
                reader.dispose();
                throw new RuntimeException(e);
            }
        });
    }
}
