package eu.openmos.msb.dds.topics;

import org.opensplice.dds.dcps.Utilities;

public final class StringMessageDataReaderViewHelper
{

    public static eu.openmos.msb.dds.topics.StringMessageDataReaderView narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.StringMessageDataReaderView) {
            return (eu.openmos.msb.dds.topics.StringMessageDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static eu.openmos.msb.dds.topics.StringMessageDataReaderView unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.StringMessageDataReaderView) {
            return (eu.openmos.msb.dds.topics.StringMessageDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
