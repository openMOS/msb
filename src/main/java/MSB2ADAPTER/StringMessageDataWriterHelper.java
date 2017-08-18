package MSB2ADAPTER;

import org.opensplice.dds.dcps.Utilities;

public final class StringMessageDataWriterHelper
{

  public static MSB2ADAPTER.StringMessageDataWriter narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    } else if (obj instanceof MSB2ADAPTER.StringMessageDataWriter)
    {
      return (MSB2ADAPTER.StringMessageDataWriter) obj;
    } else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }

  public static MSB2ADAPTER.StringMessageDataWriter unchecked_narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    } else if (obj instanceof MSB2ADAPTER.StringMessageDataWriter)
    {
      return (MSB2ADAPTER.StringMessageDataWriter) obj;
    } else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }

}
