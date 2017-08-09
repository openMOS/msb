package MSB2ADAPTER;

public class StringMessageTypeSupport extends org.opensplice.dds.dcps.TypeSupportImpl implements DDS.TypeSupportOperations
{
    private long copyCache;

    public StringMessageTypeSupport()
    {
        super("MSB2ADAPTER::StringMessage",
              "",
              "device",
              null,
              MSB2ADAPTER.StringMessageMetaHolder.metaDescriptor);
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

    @Override
    protected DDS.DataWriter create_datawriter ()
    {
        return new StringMessageDataWriterImpl(this);
    }

    @Override
    protected DDS.DataReader create_datareader ()
    {
        return new StringMessageDataReaderImpl(this);
    }

    @Override
    protected DDS.DataReaderView create_dataview ()
    {
        return new StringMessageDataReaderViewImpl(this);
    }
}
