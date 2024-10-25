package net.glasslauncher.mods.alwaysmoreitems.network;

import net.minecraft.nbt.NbtElement;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NetworkHelper {

    public static int getNbtLength(NbtElement element) {
        // Semi-wasteful, but better than an actual output stream.
        return writeAndGetNbtLength(element, DataOutputStream.nullOutputStream());
    }

    public static int writeAndGetNbtLength(NbtElement element, OutputStream dataOutput) {
        DataOutputStream outputStream = new DataOutputStream(dataOutput);
        element.write(outputStream);
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.size();
    }
}
