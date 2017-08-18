package MSB2ADAPTER;

import org.opensplice.dds.dcps.Utilities;

public final class StringMessageDataReaderHelper
{

  public static MSB2ADAPTER.StringMessageDataReader narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    } else if (obj instanceof MSB2ADAPTER.StringMessageDataReader)
    {
      return (MSB2ADAPTER.StringMessageDataReader) obj;
    } else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }

  public static MSB2ADAPTER.StringMessageDataReader unchecked_narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    } else if (obj instanceof MSB2ADAPTER.StringMessageDataReader)
    {
      return (MSB2ADAPTER.StringMessageDataReader) obj;
    } else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }

}
