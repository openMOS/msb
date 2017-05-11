package eu.openmos.msb.dds.topics;

public interface GeneralMethodMessageDataReaderOperations extends
    DDS.DataReaderOperations
{

    int read(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int take(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int read_w_condition(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            DDS.ReadCondition a_condition);

    int take_w_condition(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            DDS.ReadCondition a_condition);

    int read_next_sample(
            eu.openmos.msb.dds.topics.GeneralMethodMessageHolder received_data, 
            DDS.SampleInfoHolder sample_info);

    int take_next_sample(
            eu.openmos.msb.dds.topics.GeneralMethodMessageHolder received_data, 
            DDS.SampleInfoHolder sample_info);

    int read_instance(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples,
            long a_handle, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int take_instance(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int read_next_instance(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int take_next_instance(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int read_next_instance_w_condition(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            DDS.ReadCondition a_condition);

    int take_next_instance_w_condition(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            DDS.ReadCondition a_condition);

    int return_loan(
            eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq);

    int get_key_value(
            eu.openmos.msb.dds.topics.GeneralMethodMessageHolder key_holder, 
            long handle);
    
    long lookup_instance(
            eu.openmos.msb.dds.topics.GeneralMethodMessage instance);

}
