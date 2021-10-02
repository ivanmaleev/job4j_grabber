package ru.job4j.cache;

public class CashFabric<K, V> {
    public AbstractCache<K, V> createCash(String cashName, String param) {
        switch (cashName) {
            case "DirFile":
                return (AbstractCache<K, V>) new DirFileCache(param);
            default:
                return null;
        }
    }
}
