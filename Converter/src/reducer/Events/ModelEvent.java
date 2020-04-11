package reducer.Events;

public class ModelEvent {
    public enum Type {
        CONVERT,
        REDUCING,
        REPLACING
    }
    public final ModelEvent.Type type;
    public final Object data;

    public ModelEvent(ModelEvent.Type type, Object data){
        this.type = type;
        this.data = data;
    }

    public ModelEvent(ModelEvent.Type type){
        this.type = type;
        this.data = null;
    }
    @Override
    public String toString(){ return this.type.toString();}

}
