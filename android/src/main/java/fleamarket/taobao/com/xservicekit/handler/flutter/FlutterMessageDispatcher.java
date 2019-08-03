package fleamarket.taobao.com.xservicekit.handler.flutter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fleamarket.taobao.com.xservicekit.handler.Message;
import fleamarket.taobao.com.xservicekit.handler.MessageDispatcher;
import fleamarket.taobao.com.xservicekit.handler.MessageHandler;
import fleamarket.taobao.com.xservicekit.handler.MessageResult;

public class FlutterMessageDispatcher implements MessageDispatcher {

    private Map<String,MessageHandler> _handlers = new HashMap<>();

    private Object mContenxt;

    @Override
    public void dispatch(Message msg, MessageResult result) {
        if (msg != null && result != null){
            MessageHandler  handler = _handlers.get(msg.name());
            if(handler != null){
                handler.onMethodCall(msg.name(),(Map) msg.arguments(),result);
            }
        }
    }

    @Override
    public void registerHandler(MessageHandler handler) {
        if (handler == null){
            return;
        }

        handler.setContext(this.getContext());

        List<String> messages = handler.handleMessageNames();
        for(String name : messages){
            if(_handlers.get(name) == null){
                _handlers.put(name,handler);
            }else{
                assert(false):"Register mutiple handlers for same key result in undeined error!";
            }
        }

    }

    @Override
    public void removeHandler(MessageHandler handler) {
        if (handler != null){
            List<String> messages = handler.handleMessageNames();
            for(String name : messages) {
                _handlers.remove(name);
            }
        }
    }

    @Override
    public void removeAll() {
        _handlers.clear();;
    }

    @Override
    public Object getContext() {
        return mContenxt;
    }

    @Override
    public void setContext(Object obj) {
        mContenxt = obj;
    }
}
