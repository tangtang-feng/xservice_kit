package fleamarket.taobao.com.xservicekit.service;

import java.util.Map;

public interface ServiceEventListner {
    void onEvent(String name , Map params);
}
