package MSB2ADAPTER;

import org.opensplice.dds.dcps.Utilities;

public final class StringMessageTypeSupportHelper
{

    public static MSB2ADAPTER.StringMessageTypeSupport narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2ADAPTER.StringMessageTypeSupport) {
            return (MSB2ADAPTER.StringMessageTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static MSB2ADAPTER.StringMessageTypeSupport unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2ADAPTER.StringMessageTypeSupport) {
            return (MSB2ADAPTER.StringMessageTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
