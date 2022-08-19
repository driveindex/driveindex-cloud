package io.github.driveindex.common.dto.result;

/**
 * @author sgpublic
 * @Date 2022/8/3 12:54
 */
public class FailedResult extends ResponseData {
    public FailedResult(Integer code, String message) {
        super(code, message);
    }

    public static final FailedResult UNSUPPORTED_REQUEST = new FailedResult(-400, "不支持的请求方式");
    public static final FailedResult WRONG_PASSWORD = new FailedResult(-401, "密码错误");
    public static final FailedResult ANONYMOUS_DENIED = new FailedResult(-405, "请登陆后再试");
    public static final FailedResult ACCESS_DENIED = new FailedResult(-406, "非常抱歉，您暂时不能访问");
    public static final FailedResult EXPIRED_TOKEN = new FailedResult(-402, "无效的 token");
    public static final FailedResult NOT_FOUND = new FailedResult(-404, "您请求的资源不存在");

    public static final FailedResult SERVICE_UNAVAILABLE = new FailedResult(-500, "服务不可用");
    public static final FailedResult INTERNAL_SERVER_ERROR = new FailedResult(-500, "服务器内部错误");
    public static final FailedResult SERVER_PROCESSING_ERROR = new FailedResult(-500, "请求处理出错");
    public static final FailedResult NOT_IMPLEMENTATION_ERROR = new FailedResult(-500, "别买炒饭了，头发快掉光了(´╥ω╥`)");
}
