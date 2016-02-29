package lang;

public class Native {
    public double numValue;
    public String strValue;
    public boolean boolValue;

    public NativeType type;

    public Native(double numValue) {
        type = NativeType.NUMBER;
        this.numValue = numValue;
    }
    public Native(String strValue) {
        type = NativeType.STRING;
        this.strValue = strValue;
    }
    public Native(boolean boolValue) {
        type = NativeType.BOOLEAN;
        this.boolValue = boolValue;
    }

    public Object getValue() {
        return type == NativeType.NUMBER ? numValue :
                type == NativeType.STRING ? strValue :
                        boolValue;
    }

    public enum NativeType {
        NUMBER, STRING, BOOLEAN
    }
}
