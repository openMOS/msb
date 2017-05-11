package MSB2Adapter;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageDataWriterHelper
{

    public static MSB2Adapter.GeneralMethodMessageDataWriter narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2Adapter.GeneralMethodMessageDataWriter) {
            return (MSB2Adapter.GeneralMethodMessageDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static MSB2Adapter.GeneralMethodMessageDataWriter unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2Adapter.GeneralMethodMessageDataWriter) {
            return (MSB2Adapter.GeneralMethodMessageDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
