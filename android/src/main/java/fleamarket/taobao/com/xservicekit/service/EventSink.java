package fleamarket.taobao.com.xservicekit.service;

public interface EventSink {
    void success(Object var1);
    void error(String var1, String var2, Object var3);
    void endOfStream();
}
