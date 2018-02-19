package ru.iate.gak.util;

import java.io.File;

public class FilesUtil {

    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }
        directory.delete();
    }
}
