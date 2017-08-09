package MSB2ADAPTER;

public final class GeneralMethodMessage {

    public java.lang.String device = "";
    public java.lang.String function = "";
    public java.lang.String args = "";
    public java.lang.String feedback = "";

    public GeneralMethodMessage() {
    }

    public GeneralMethodMessage(
        java.lang.String _device,
        java.lang.String _function,
        java.lang.String _args,
        java.lang.String _feedback)
    {
        device = _device;
        function = _function;
        args = _args;
        feedback = _feedback;
    }

}
