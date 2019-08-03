package fleamarket.taobao.com.xservicekit.handler;

public interface MessageDispatcher {

    void dispatch(Message msg, MessageResult result);

    void registerHandler(MessageHandler handler);

    void removeHandler(MessageHandler handler);

    void removeAll();

    Object getContext();

    void setContext(Object obj);

}
