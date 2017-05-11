package eu.openmos.msb.dds.topics;

public final class GeneralMethodMessageSeqHolder
{

    public eu.openmos.msb.dds.topics.GeneralMethodMessage value[] = null;

    public GeneralMethodMessageSeqHolder()
    {
    }

    public GeneralMethodMessageSeqHolder(eu.openmos.msb.dds.topics.GeneralMethodMessage[] initialValue)
    {
        value = initialValue;
    }

}
