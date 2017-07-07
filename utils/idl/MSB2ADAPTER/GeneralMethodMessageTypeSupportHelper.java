package MSB2ADAPTER;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageTypeSupportHelper {

    public static MSB2ADAPTER.GeneralMethodMessageTypeSupport narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2ADAPTER.GeneralMethodMessageTypeSupport) {
            return (MSB2ADAPTER.GeneralMethodMessageTypeSupport) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static MSB2ADAPTER.GeneralMethodMessageTypeSupport unchecked_narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2ADAPTER.GeneralMethodMessageTypeSupport) {
            return (MSB2ADAPTER.GeneralMethodMessageTypeSupport) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
