package fleamarket.taobao.com.xservicekit.handler.flutter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fleamarket.taobao.com.xservicekit.handler.MessageHandler;
import fleamarket.taobao.com.xservicekit.handler.MessageResult;

public class FlutterMessageHandler implements MessageHandler{

    private Object mContext;

    @Override
    public boolean onMethodCall(String name, Map args, MessageResult result){
        return false;
    }

    @Override
    public Object getContext() {
        return mContext;
    }

    @Override
    public void setContext(Object obj) {
        mContext = obj;
    }

    @Override
    public String service() {
        return "root";
    }

    @Override
    public List<String> handleMessageNames() {
        List<String> h = new ArrayList<>();
        h.add("");
        return h;
    }
}
