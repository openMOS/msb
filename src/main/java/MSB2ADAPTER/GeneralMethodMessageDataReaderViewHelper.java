package MSB2ADAPTER;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageDataReaderViewHelper
{

  public static MSB2ADAPTER.GeneralMethodMessageDataReaderView narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    } else if (obj instanceof MSB2ADAPTER.GeneralMethodMessageDataReaderView)
    {
      return (MSB2ADAPTER.GeneralMethodMessageDataReaderView) obj;
    } else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }

  public static MSB2ADAPTER.GeneralMethodMessageDataReaderView unchecked_narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    } else if (obj instanceof MSB2ADAPTER.GeneralMethodMessageDataReaderView)
    {
      return (MSB2ADAPTER.GeneralMethodMessageDataReaderView) obj;
    } else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }

}
