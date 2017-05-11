package eu.openmos.msb.dds.topics;

public interface GeneralMethodMessageDataWriterOperations extends
    DDS.DataWriterOperations
{

    long register_instance(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data);

    long register_instance_w_timestamp(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data, 
            DDS.Time_t source_timestamp);

    int unregister_instance(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data, 
            long handle);

    int unregister_instance_w_timestamp(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data, 
            long handle, 
            DDS.Time_t source_timestamp);

    int write(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data, 
            long handle);

    int write_w_timestamp(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data, 
            long handle, 
            DDS.Time_t source_timestamp);

    int dispose(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data, 
            long instance_handle);

    int dispose_w_timestamp(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data, 
            long instance_handle, 
            DDS.Time_t source_timestamp);
    
    int writedispose(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data, 
            long instance_handle);

    int writedispose_w_timestamp(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data, 
            long instance_handle, 
            DDS.Time_t source_timestamp);

    int get_key_value(
            eu.openmos.msb.dds.topics.GeneralMethodMessageHolder key_holder, 
            long handle);
    
    long lookup_instance(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance_data);

}
