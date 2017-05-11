package MSB2Adapter;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageDataReaderViewHelper
{

    public static MSB2Adapter.GeneralMethodMessageDataReaderView narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2Adapter.GeneralMethodMessageDataReaderView) {
            return (MSB2Adapter.GeneralMethodMessageDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static MSB2Adapter.GeneralMethodMessageDataReaderView unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof MSB2Adapter.GeneralMethodMessageDataReaderView) {
            return (MSB2Adapter.GeneralMethodMessageDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
