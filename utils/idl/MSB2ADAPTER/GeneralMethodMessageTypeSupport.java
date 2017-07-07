package MSB2ADAPTER;

public class GeneralMethodMessageTypeSupport extends org.opensplice.dds.dcps.TypeSupportImpl implements DDS.TypeSupportOperations {

    private long copyCache;

    public GeneralMethodMessageTypeSupport() {
        super("MSB2ADAPTER::GeneralMethodMessage",
                "",
                "device",
                null,
                MSB2ADAPTER.GeneralMethodMessageMetaHolder.metaDescriptor);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected DDS.DataWriter create_datawriter() {
        return new GeneralMethodMessageDataWriterImpl(this);
    }

    @Override
    protected DDS.DataReader create_datareader() {
        return new GeneralMethodMessageDataReaderImpl(this);
    }

    @Override
    protected DDS.DataReaderView create_dataview() {
        return new GeneralMethodMessageDataReaderViewImpl(this);
    }
}
