package MSB2Adapter;

public final class GeneralMethodMessage {

    public short id;
    public java.lang.String function = "";
    public java.lang.String args = "";
    public java.lang.String feedback = "";

    public GeneralMethodMessage() {
    }

    public GeneralMethodMessage(
        short _id,
        java.lang.String _function,
        java.lang.String _args,
        java.lang.String _feedback)
    {
        id = _id;
        function = _function;
        args = _args;
        feedback = _feedback;
    }

}
