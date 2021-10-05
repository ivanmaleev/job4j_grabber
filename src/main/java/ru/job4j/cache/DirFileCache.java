package ru.job4j.cache;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DirFileCache extends AbstractCache<String, String> {

    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    @Override
    protected String load(String key) {
        String textFile = "";
        try {
            textFile = Files.readString(Paths.get(cachingDir.concat(key)));
            cache.put(key, new SoftReference<>(textFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return textFile;
    }

}