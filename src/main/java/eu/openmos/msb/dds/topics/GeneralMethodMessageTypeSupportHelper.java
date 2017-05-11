package eu.openmos.msb.dds.topics;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageTypeSupportHelper
{

    public static eu.openmos.msb.dds.topics.GeneralMethodMessageTypeSupport narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.GeneralMethodMessageTypeSupport) {
            return (eu.openmos.msb.dds.topics.GeneralMethodMessageTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static eu.openmos.msb.dds.topics.GeneralMethodMessageTypeSupport unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof eu.openmos.msb.dds.topics.GeneralMethodMessageTypeSupport) {
            return (eu.openmos.msb.dds.topics.GeneralMethodMessageTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
