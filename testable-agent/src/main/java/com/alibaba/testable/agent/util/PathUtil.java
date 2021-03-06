package com.alibaba.testable.agent.util;

import java.io.File;

public class PathUtil {

    /**
     * Join a path text and a file name to full file path
     * @param folder path text
     * @param file file name
     * @return joined full file path
     */
    public static String join(String folder, String file) {
        return (folder.endsWith(File.separator) ? folder : (folder + File.separator)) + file;
    }

    /**
     * Get the absolute path of the first sub-folder from root folder to target path
     * @param rootFolder specify root folder path
     * @param targetPath any sub path inside root folder
     * @return first sub-folder path
     */
    public static String getFirstLevelFolder(String rootFolder, String targetPath) {
        if (!targetPath.startsWith(rootFolder) || targetPath.length() <= rootFolder.length() + 1) {
            return "";
        }
        char separator = targetPath.charAt(rootFolder.length());
        int pos = targetPath.indexOf(separator, rootFolder.length() + 1);
        return pos > 0 ? targetPath.substring(0, pos) : "";
    }
}
