package MSB2ADAPTER;

public interface StringMessageDataWriterOperations extends
        DDS.DataWriterOperations {

    long register_instance(
            MSB2ADAPTER.StringMessage instance_data);

    long register_instance_w_timestamp(
            MSB2ADAPTER.StringMessage instance_data,
            DDS.Time_t source_timestamp);

    int unregister_instance(
            MSB2ADAPTER.StringMessage instance_data,
            long handle);

    int unregister_instance_w_timestamp(
            MSB2ADAPTER.StringMessage instance_data,
            long handle,
            DDS.Time_t source_timestamp);

    int write(
            MSB2ADAPTER.StringMessage instance_data,
            long handle);

    int write_w_timestamp(
            MSB2ADAPTER.StringMessage instance_data,
            long handle,
            DDS.Time_t source_timestamp);

    int dispose(
            MSB2ADAPTER.StringMessage instance_data,
            long instance_handle);

    int dispose_w_timestamp(
            MSB2ADAPTER.StringMessage instance_data,
            long instance_handle,
            DDS.Time_t source_timestamp);

    int writedispose(
            MSB2ADAPTER.StringMessage instance_data,
            long instance_handle);

    int writedispose_w_timestamp(
            MSB2ADAPTER.StringMessage instance_data,
            long instance_handle,
            DDS.Time_t source_timestamp);

    int get_key_value(
            MSB2ADAPTER.StringMessageHolder key_holder,
            long handle);

    long lookup_instance(
            MSB2ADAPTER.StringMessage instance_data);

}
