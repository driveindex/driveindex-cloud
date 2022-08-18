package io.github.driveindex.common.util;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Stack;
import java.util.StringJoiner;

/**
 * 一个用于解析标准路径的工具类
 * @author sgpublic
 * @Date 2022/8/15 17:15
 */
public class CanonicalPath implements Cloneable, Serializable {
    /** CanonicalPath 不可变，同 String */
    private final Stack<String> pathStack;
    private final String path;

    protected CanonicalPath(String path) {
        this.pathStack = new Stack<>();
        String[] files = path.split("/");
        for (String file : files) {
            if ("".equals(file) || ".".equals(file))
                continue;
            if ("..".equals(file)) {
                if (!this.pathStack.empty()) this.pathStack.pop();
                continue;
            }
            this.pathStack.push(file);
        }
        this.path = generatePath();
    }

    /**
     * 直接传入目录栈创建 CanonicalPath 对象
     * @param pathStack 目录栈
     * @see CanonicalPath#clone()
     */
    private CanonicalPath(Stack<String> pathStack) {
        this.pathStack = pathStack;
        this.path = generatePath();
    }

    private String generatePath() {
        if (pathStack.empty()) return ROOT_PATH;
        StringJoiner ans = new StringJoiner("/", "/", "");
        for (String s : pathStack) ans.add(s);
        return ans.toString();
    }

    public static final String ROOT_PATH = "/";

    /**
     * 利用路径文本创建 CanonicalPath 对象
     * @param path 路径文本
     * @return CanonicalPath 对象
     */
    public static CanonicalPath of(String path) {
        path = path.replace('\\', '/');
        path = path.replaceAll(":", "");
        return new CanonicalPath(path);
    }

    /**
     * 获取父级文件对象，可能为空
     * @return 父级文件对象，若当前目录已经为根目录，则返回 null
     */
    @Nullable
    public CanonicalPath getParentPath() {
        Stack<String> path = clonePath();
        if (path.isEmpty()) return null;
        else path.pop();
        return new CanonicalPath(path);
    }

    /**
     * 将新文件添加到当前路径后面
     * @param newFile 要添加的文件名
     * @return 添加后的新路径
     */
    public CanonicalPath append(String newFile) {
        Stack<String> path = clonePath();
        path.add(newFile);
        return new CanonicalPath(path);
    }

    /**
     * 将指定 path 添加到当前路径后面
     * @param newPath 要添加的路径
     * @return 添加后的新路径
     */
    public CanonicalPath append(CanonicalPath newPath) {
        Stack<String> pathStack = clonePath();
        pathStack.addAll(newPath.pathStack);
        return new CanonicalPath(pathStack);
    }

    /**
     * 将新文件添加到当前路径前面
     * @param newFile 要添加的文件名
     * @return 添加后的新路径
     */
    public CanonicalPath push(String newFile) {
        Stack<String> path = new Stack<>();
        path.add(newFile);
        path.addAll(this.pathStack);
        return new CanonicalPath(path);
    }

    /**
     * 将指定 path 添加到当前路径前面
     * @param newPath 要添加的路径
     * @return 添加后的新路径
     */
    public CanonicalPath push(CanonicalPath newPath) {
        Stack<String> pathStack = newPath.clonePath();
        pathStack.addAll(this.pathStack);
        return new CanonicalPath(pathStack);
    }

    /**
     * 获取当前规范路径文本
     * @return 规范路径文本
     */
    public String getPath() {
        return path;
    }

    /**
     * 获取当前文件名
     * @return 文件名，若当前路径为根目录则返回 null
     */
    @Nullable
    public String getCurrentFile() {
        return pathStack.isEmpty() ? null : pathStack.peek();
    }

    /**
     * 返回当前规范路径文本
     * @see CanonicalPath#getPath()
     * @return 规范路径文本
     */
    @Override
    public String toString() {
        return getPath();
    }

    /**
     * 扩展：转为 Microsoft Graph 接口中需要的路径参数。
     * <br/>- 若当前路径为根目录，则返回空文本；
     * <br/>- 若当前路径为不根目录，则在首位添加英文冒号返回，即：":${CanonicalPath#getPath()}:"。
     * @return 转换后的文本
     */
    public String toAzureCanonicalizePath() {
        String canonicalizePath = getPath();
        if (ROOT_PATH.equals(canonicalizePath)) return "";
        return ":" + canonicalizePath + ":";
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public CanonicalPath clone() {
        return new CanonicalPath(clonePath());
    }

    private Stack<String> clonePath() {
        Stack<String> newStack = new Stack<>();
        newStack.addAll(pathStack);
        return newStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CanonicalPath that)) return false;
        return Objects.equals(pathStack, that.pathStack);
    }

    @Override
    public int hashCode() {
        return pathStack.hashCode();
    }
}
