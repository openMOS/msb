package eu.openmos.msb.dds.topics;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageDataWriterHelper
{

    public static eu.openmos.msb.dds.topics.GeneralMethodMessageDataWriter narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.GeneralMethodMessageDataWriter) {
            return (eu.openmos.msb.dds.topics.GeneralMethodMessageDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static eu.openmos.msb.dds.topics.GeneralMethodMessageDataWriter unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.GeneralMethodMessageDataWriter) {
            return (eu.openmos.msb.dds.topics.GeneralMethodMessageDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
