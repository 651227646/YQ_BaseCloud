package com.yq.bc.utils;

import com.yq.bc.pojo.nio.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description 文件工具包
 * @date 2020/1/19
 */
public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
    private static String currentPath = "";
    private static String rootPath;
    private static String location = "";
    public static String TMP_PATH = System.getProperty("java.io.tmpdir");
    public static String classPath = "";

    private FileUtils() {
    }

    public static String getRootPath(boolean isDevModel) {
        return isDevModel ? getCurrentPath() : getAppLocation();
    }

    public static String getCurrentPath() {
        if (StringUtils.notBlank(rootPath)) {
            return rootPath;
        } else {
            if (StringUtils.isEmpty(currentPath)) {
                try {
                    File file = new File("");
                    currentPath = file.getAbsolutePath();
                    currentPath = currentPath.endsWith(File.separator) ? currentPath.substring(0, currentPath.length() - 1) : currentPath;
                    LOGGER.debug(String.format("当前程序路径为:[%s]", currentPath));
                    if (!currentPath.endsWith(classPath)) {
                        LOGGER.debug(String.format("已设置CLASS_PATH，当前程序根据CLASS_PATH[%s]格式化后路径为:[%s]", classPath, currentPath));
                        currentPath = currentPath + classPath;
                    }
                } catch (Exception var1) {
                    LOGGER.error("获取当前文件路径失败。", var1);
                }
            }

            return buildPath4EveryOS(currentPath);
        }
    }

    public static boolean mkdir(String path) {
        try {
            if (StringUtils.isBlank(path)) {
                return true;
            } else {
                String tmpPath = buildPath4EveryOS(path);
                File file = new File(tmpPath);
                if (file.exists()) {
                    AssertUtils.isTrue(file.isDirectory(), String.format("路径[%s]不是一个目录", path));
                }

                return file.mkdirs();
            }
        } catch (Exception var3) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(String.format("创建目录[%s]失败", path), var3);
            }

            return false;
        }
    }

    public static boolean del(File file, boolean all) {
        String path = "unknown";

        try {
            path = file.getAbsolutePath();
            if (file != null && file.exists()) {
                if (!file.isFile() && file.listFiles().length >= 1) {
                    if (file.isDirectory() && file.listFiles().length > 0) {
                        File[] var3 = file.listFiles();
                        int var4 = var3.length;

                        for(int var5 = 0; var5 < var4; ++var5) {
                            File subFile = var3[var5];
                            del(subFile, all);
                            if (!Files.deleteIfExists(Paths.get(subFile.getAbsolutePath()))) {
                                return false;
                            }
                        }
                    }

                    return true;
                } else {
                    return file.delete();
                }
            } else {
                return true;
            }
        } catch (Exception var7) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(String.format("删除文件/目录[%s]失败", path), var7);
            }

            return false;
        }
    }

    public static boolean del(String path, boolean all) {
        try {
            if (StringUtils.isBlank(path)) {
                return true;
            } else {
                String tmpPath = buildPath4EveryOS(path);
                File file = new File(tmpPath);
                return del(file, all);
            }
        } catch (Exception var4) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(String.format("删除文件/目录[%s]失败", path), var4);
            }

            return false;
        }
    }

    public static boolean renameTo(String path, String targetPath) {
        try {
            if (!StringUtils.isBlank(path) && !StringUtils.isBlank(targetPath)) {
                File file = new File(path);
                if (!file.exists()) {
                    return false;
                } else {
                    File target = new File(targetPath);
                    return file.renameTo(target);
                }
            } else {
                return false;
            }
        } catch (Exception var4) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(String.format("重命名文件/目录[%s]为[%s]失败", path, targetPath), var4);
            }

            return false;
        }
    }

    public static boolean mv(String path, String targetPath, CopyOption... options) {
        try {
            if (!StringUtils.isBlank(path) && !StringUtils.isBlank(targetPath)) {
                File file = new File(path);
                if (!file.exists()) {
                    return false;
                } else {
                    if (!ArraysUtils.hasItems(options)) {
                        options = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES};
                    }

                    Path result = Files.move(Paths.get(path), Paths.get(targetPath), options);
                    return Files.exists(result, new LinkOption[0]);
                }
            } else {
                return false;
            }
        } catch (Exception var5) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(String.format("重命名文件/目录[%s]为[%s]失败", path, targetPath), var5);
            }

            return false;
        }
    }

    public static String getAppLocation() {
        if (StringUtils.isBlank(location)) {
            try {
                location = URLDecoder.decode(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8");
                File path = new File(location);
                location = path.getPath();
                location = location.toLowerCase().trim().endsWith(".jar") ? path.getParent() : location;
                LOGGER.debug(String.format("当前文件路径:[%s/当前目录路径[%s]", path, location));
            } catch (UnsupportedEncodingException var1) {
                location = "";
            }
        }

        return location;
    }

    public static String getRelativePath(String basePath, String filePath) {
        String newBasePath = basePath;
        String newFilePath = "";
        if (StringUtils.isBlank(basePath)) {
            newBasePath = getCurrentPath();
        }

        newBasePath = newBasePath + (basePath.endsWith(File.separator) ? "" : File.separator);
        if (filePath.startsWith(newBasePath)) {
            newFilePath = filePath.substring(newBasePath.length());
        }

        return buildPath4EveryOS(newFilePath);
    }

    public static String getAbsolutePath(String subPath) {
        String path = "";
        File file = new File(subPath);
        path = getCurrentPath();
        if (!subPath.startsWith(path) && !file.isAbsolute()) {
            path = path + (path.endsWith(File.separator) ? subPath : File.separator + subPath);
        } else {
            path = subPath;
        }

        return buildPath4EveryOS(path);
    }

    public static String getAbsolutePath(String rootPath, String subPath) {
        String path = "";
        if (subPath.startsWith(rootPath)) {
            path = subPath;
        } else {
            path = rootPath + (rootPath.endsWith(File.separator) ? subPath : File.separator + subPath);
        }

        return buildPath4EveryOS(path);
    }

    public static File getFile(String path) {
        String filePath = getAbsolutePath(path);
        return new File(filePath);
    }

    public static String getFileName(FileInfo fileInfo) {
        if (fileInfo == null) {
            return "";
        } else if (!StringUtils.isBlank(fileInfo.getFileName())) {
            return fileInfo.getFileName();
        } else if (!StringUtils.isBlank(fileInfo.getOriFileName())) {
            return fileInfo.getOriFileName();
        } else if (!StringUtils.isBlank(fileInfo.getKeyId())) {
            return fileInfo.getKeyId();
        } else {
            File file = getFile(fileInfo);
            return file != null && !StringUtils.isBlank(file.getName()) ? file.getName() : "unknown";
        }
    }

    public static File[] getFiles(FileInfo... fileInfos) {
        File[] files = null;
        if (ArraysUtils.hasNotNullItems(fileInfos)) {
            files = new File[fileInfos.length];
            int i = 0;

            for(int length = fileInfos.length; i < length; ++i) {
                files[i] = getFile(fileInfos[i]);
            }
        }

        return files;
    }

    public static File getFile(FileInfo fileInfo) {
        AssertUtils.notNull(fileInfo, "FileInfo对象不能为null");
        File file = fileInfo.getFile();
        if (file == null) {
            String storePath = fileInfo.getStorePath();
            AssertUtils.notBlank(storePath, "FileInfo对象storePath不能为空");
            String fileName = fileInfo.getFileName();
            if (isDirectory(storePath)) {
                storePath = StringUtils.notBlank(fileName) ? getAbsolutePath(storePath, fileName) : storePath;
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("根据路径构造[{}]文件对象", storePath);
            }

            file = new File(storePath);
        }

        return file;
    }

    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    public static boolean isDirectory(File file) {
        return file.exists() && file.isDirectory();
    }

    public static boolean exists(String path) {
        return exists(getFile(path));
    }

    public static boolean isFile(String path) {
        return isFile(getFile(path));
    }

    public static boolean isDirectory(String path) {
        return isDirectory(getFile(path));
    }

    public static String package2path(String packageName) {
        String packagePath = packageName.replace(".", File.separator);
        return buildPath4EveryOS(getAbsolutePath(packagePath));
    }

    public static String getClassName(File root, File file) {
        String basePath = root != null ? root.getAbsolutePath() : getCurrentPath();
        String clazzName = getRelativePath(basePath, file.getAbsolutePath());
        if (clazzName.endsWith(".class")) {
            clazzName = clazzName.substring(0, clazzName.lastIndexOf(".class"));
        }

        return clazzName.replace(File.separator, ".");
    }

    public static List<String> listFilesName(String basePackage) {
        AssertUtils.notBlank(basePackage, "文件根路径为空(如需扫描所有，请设置为*)");
        File packageFile = getFile(package2path(basePackage));
        AssertUtils.isTrue(isDirectory(packageFile), String.format("目录[%s]对应目录不存在", basePackage));
        List<File> fileList = listFiles(packageFile);
        List<String> result = new ArrayList();
        fileList.forEach((file) -> {
            result.add(getClassName((File)null, file));
        });
        return result;
    }

    public static String buildPath4EveryOS(String path) {
        String newPath = path;

        try {
            newPath = StringUtils.replace(path, "/", File.separator, -1);
            newPath = StringUtils.replace(newPath, "\\", File.separator, -1);
            newPath = StringUtils.replace(newPath, "//", File.separator, -1);
        } catch (Exception var3) {
            LOGGER.warn(String.format("执行[%s]失败", "buildPath4EveryOS"), var3);
        }

        return newPath;
    }

    public static List<File> listFiles(File rootPath) {
        List<File> files = new ArrayList();
        File[] tmpFileList = rootPath.listFiles();
        return (List)(tmpFileList != null && tmpFileList.length > 0 ? Arrays.asList(tmpFileList) : files);
    }

    public static String getClassFilePath(Class<?> cls) {
        String filePath = "";

        try {
            filePath = URLDecoder.decode(cls.getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8");
            File path = new File(filePath);
            filePath = path.getPath();
            filePath = filePath.toLowerCase().trim().endsWith(".jar") ? path.getParent() : filePath;
        } catch (UnsupportedEncodingException var3) {
            LOGGER.error("获取class文件绝对路径失败", var3);
        }

        return filePath;
    }

    public static void copyFolder(File src, File dest, CopyOption... options) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }

            String[] files = src.list();
            String[] var4 = files;
            int var5 = files.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String file = var4[var6];
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
            }
        } else {
            Files.copy(Paths.get(src.getAbsolutePath()), Paths.get(dest.getAbsolutePath()), options);
        }

    }

    public static void setRootPath(String rootPath) {
        FileUtils.rootPath = rootPath;
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }
}
