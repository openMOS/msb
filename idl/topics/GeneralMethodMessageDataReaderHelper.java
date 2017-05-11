package MSB2Adapter;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageDataReaderHelper
{

    public static MSB2Adapter.GeneralMethodMessageDataReader narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2Adapter.GeneralMethodMessageDataReader) {
            return (MSB2Adapter.GeneralMethodMessageDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static MSB2Adapter.GeneralMethodMessageDataReader unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2Adapter.GeneralMethodMessageDataReader) {
            return (MSB2Adapter.GeneralMethodMessageDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
