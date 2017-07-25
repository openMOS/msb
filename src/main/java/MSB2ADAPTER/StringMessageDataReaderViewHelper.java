package MSB2ADAPTER;

import org.opensplice.dds.dcps.Utilities;


public final class StringMessageDataReaderViewHelper
{

  public static MSB2ADAPTER.StringMessageDataReaderView narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    }
    else if (obj instanceof MSB2ADAPTER.StringMessageDataReaderView)
    {
      return (MSB2ADAPTER.StringMessageDataReaderView) obj;
    }
    else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }


  public static MSB2ADAPTER.StringMessageDataReaderView unchecked_narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    }
    else if (obj instanceof MSB2ADAPTER.StringMessageDataReaderView)
    {
      return (MSB2ADAPTER.StringMessageDataReaderView) obj;
    }
    else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }

}
