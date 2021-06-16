
package com.ragemachine.moon.impl.manager;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.impl.client.modules.Module;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager
        extends Hack {
    private final Path base = this.getMkDirectory(this.getRoot(), "moonhack");
    private final Path config = this.getMkDirectory(this.base, "config");

    private String[] expandPath(String fullPath) {
        return fullPath.split(":?\\\\\\\\|\\/");
    }

    private Stream<String> expandPaths(String ... paths) {
        return Arrays.stream(paths).map(this::expandPath).flatMap(Arrays::stream);
    }

    private Path lookupPath(Path root, String ... paths) {
        return Paths.get(root.toString(), paths);
    }

    private Path getRoot() {
        return Paths.get("", new String[0]);
    }

    private void createDirectory(Path dir) {
        try {
            if (Files.isDirectory(dir, new LinkOption[0])) return;
            if (Files.exists(dir, new LinkOption[0])) {
                Files.delete(dir);
            }
            Files.createDirectories(dir, new FileAttribute[0]);
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getMkDirectory(Path parent, String ... paths) {
        if (paths.length < 1) {
            return parent;
        }
        Path dir = this.lookupPath(parent, paths);
        this.createDirectory(dir);
        return dir;
    }

    public FileManager() {
        this.getMkDirectory(this.base, "util");
        Iterator<Module.Category> iterator = MoonHack.getModuleManager.getCategories().iterator();
        while (iterator.hasNext()) {
            Module.Category category = iterator.next();
            this.getMkDirectory(this.config, category.getName());
        }
    }

    public Path getBasePath() {
        return this.base;
    }

    public Path getBaseResolve(String ... paths) {
        String[] names = (String[])this.expandPaths(paths).toArray(x$0 -> new String[x$0]);
        if (names.length >= 1) return this.lookupPath(this.getBasePath(), names);
        throw new IllegalArgumentException("missing path");
    }

    public Path getMkBaseResolve(String ... paths) {
        Path path = this.getBaseResolve(paths);
        this.createDirectory(path.getParent());
        return path;
    }

    public Path getConfig() {
        return this.getBasePath().resolve("config");
    }

    public Path getCache() {
        return this.getBasePath().resolve("cache");
    }

    public Path getMkBaseDirectory(String ... names) {
        return this.getMkDirectory(this.getBasePath(), this.expandPaths(names).collect(Collectors.joining(File.separator)));
    }

    public Path getMkConfigDirectory(String ... names) {
        return this.getMkDirectory(this.getConfig(), this.expandPaths(names).collect(Collectors.joining(File.separator)));
    }

    public static boolean appendTextFile(String data, String file) {
        try {
            Path path = Paths.get(file, new String[0]);
            Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, Files.exists(path, new LinkOption[0]) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
            return true;
        }
        catch (IOException e) {
            System.out.println("WARNING: Unable to write file: " + file);
            return false;
        }
    }

    public static List<String> readTextFileAllLines(String file) {
        try {
            Path path = Paths.get(file, new String[0]);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            System.out.println("WARNING: Unable to read file, creating new file: " + file);
            FileManager.appendTextFile("", file);
            return Collections.emptyList();
        }
    }
}

