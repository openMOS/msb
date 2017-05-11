package eu.openmos.msb.dds.topics;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageDataReaderHelper
{

    public static eu.openmos.msb.dds.topics.GeneralMethodMessageDataReader narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.GeneralMethodMessageDataReader) {
            return (eu.openmos.msb.dds.topics.GeneralMethodMessageDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static eu.openmos.msb.dds.topics.GeneralMethodMessageDataReader unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.GeneralMethodMessageDataReader) {
            return (eu.openmos.msb.dds.topics.GeneralMethodMessageDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
