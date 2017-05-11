package MSB2Adapter;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageTypeSupportHelper
{

    public static MSB2Adapter.GeneralMethodMessageTypeSupport narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2Adapter.GeneralMethodMessageTypeSupport) {
            return (MSB2Adapter.GeneralMethodMessageTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static MSB2Adapter.GeneralMethodMessageTypeSupport unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2Adapter.GeneralMethodMessageTypeSupport) {
            return (MSB2Adapter.GeneralMethodMessageTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
