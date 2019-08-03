package fleamarket.taobao.com.xservicekit.handler;

public interface MessageResult<T>  {

    void success(T var1);

    void error(String var1, String var2, Object var3);

    void notImplemented();
}
