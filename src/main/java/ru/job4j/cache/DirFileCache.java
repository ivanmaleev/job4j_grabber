package ru.job4j.cache;

import java.io.FileInputStream;
import java.lang.ref.SoftReference;

public class DirFileCache extends AbstractCache<String, String> {

    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    @Override
    protected String load(String key) {
        String textFile = "";
        try (FileInputStream in = new FileInputStream(cachingDir.concat(key))) {
            StringBuilder text = new StringBuilder();
            int read;
            while ((read = in.read()) != -1) {
                text.append((char) read);
            }
            textFile = text.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cache.put(key, new SoftReference<>(textFile));
        return textFile;
    }

}