package eu.openmos.msb.dds.topics;

import org.opensplice.dds.dcps.Utilities;

public final class StringMessageDataWriterHelper
{

    public static eu.openmos.msb.dds.topics.StringMessageDataWriter narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.StringMessageDataWriter) {
            return (eu.openmos.msb.dds.topics.StringMessageDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static eu.openmos.msb.dds.topics.StringMessageDataWriter unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.StringMessageDataWriter) {
            return (eu.openmos.msb.dds.topics.StringMessageDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
