package io.github.driveindex.azure.core;

/**
 * @author sgpublic
 * @Date 2022/8/16 16:01
 */
public enum SpecialFile {
    README_MD("readme.md"),
    HEADER_MD("header.md"),
    _PASSWORD(".password");

    private final String name;
    SpecialFile(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean match(String fileName) {
        return name.equals(fileName.toLowerCase());
    }
}
