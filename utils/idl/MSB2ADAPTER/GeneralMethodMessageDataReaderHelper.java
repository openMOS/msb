package MSB2ADAPTER;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageDataReaderHelper {

    public static MSB2ADAPTER.GeneralMethodMessageDataReader narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2ADAPTER.GeneralMethodMessageDataReader) {
            return (MSB2ADAPTER.GeneralMethodMessageDataReader) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static MSB2ADAPTER.GeneralMethodMessageDataReader unchecked_narrow(java.lang.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2ADAPTER.GeneralMethodMessageDataReader) {
            return (MSB2ADAPTER.GeneralMethodMessageDataReader) obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
