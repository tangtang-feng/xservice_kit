package fleamarket.taobao.com.xservicekit.handler;

import java.util.List;
import java.util.Map;

public interface MessageHandler<T> {

    boolean onMethodCall(String name, Map args, MessageResult<T> result);

    Object getContext();

    void setContext(Object obj);

    List<String> handleMessageNames();

    String service();

}
