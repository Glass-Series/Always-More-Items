package net.glasslauncher.mods.alwaysmoreitems.util;

import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;

import javax.imageio.*;
import javax.imageio.stream.*;
import java.io.*;
import java.util.*;

public class ImageUtil {
    /**
     * A modified version of <a href="https://stackoverflow.com/a/12164026">SO 12164026</a>
     * Gets image dimensions for given file
     * @param imgFile image file
     * @return dimensions of image
     * @throws IOException if the file is not a known image
     */
    public static Tooltip.Dimension getImageDimension(File imgFile) throws IOException {
        int pos = imgFile.getName().lastIndexOf(".");
        if (pos == -1) {
            throw new IOException("No extension for file: " + imgFile.getAbsolutePath());
        }

        String suffix = imgFile.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (!iter.hasNext()) {
            throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
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
            throw e;
        }
    }
}
