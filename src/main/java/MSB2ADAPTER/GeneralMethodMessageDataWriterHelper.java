package MSB2ADAPTER;

import org.opensplice.dds.dcps.Utilities;

public final class GeneralMethodMessageDataWriterHelper
{

  public static MSB2ADAPTER.GeneralMethodMessageDataWriter narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    } else if (obj instanceof MSB2ADAPTER.GeneralMethodMessageDataWriter)
    {
      return (MSB2ADAPTER.GeneralMethodMessageDataWriter) obj;
    } else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }

  public static MSB2ADAPTER.GeneralMethodMessageDataWriter unchecked_narrow(java.lang.Object obj)
  {
    if (obj == null)
    {
      return null;
    } else if (obj instanceof MSB2ADAPTER.GeneralMethodMessageDataWriter)
    {
      return (MSB2ADAPTER.GeneralMethodMessageDataWriter) obj;
    } else
    {
      throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
    }
  }

}
