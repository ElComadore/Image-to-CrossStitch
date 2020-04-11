package reducer.Events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum EventBus {

    INSTANCE;

    private final List<IEventHandler> handlers = Collections.synchronizedList(new ArrayList<>());
    private boolean trace = false;

    public void register(IEventHandler handler){handlers.add(handler);}
    public void unRegister(IEventHandler handler){handlers.remove(handler);}

    public void publish(ModelEvent evt){
        if (trace){
            System.out.println(evt);
        }
        synchronized (handlers){
            handlers.forEach((evh) -> {
                evh.onModelEvent(evt);
            });
        }
    }
    public void publish(ModelEvent.Type tag){
        publish((new ModelEvent(tag)));
    }
}
