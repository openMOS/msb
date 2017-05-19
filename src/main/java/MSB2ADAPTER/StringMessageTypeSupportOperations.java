package MSB2ADAPTER;

public interface StringMessageTypeSupportOperations extends
    DDS.TypeSupportOperations
{

    int register_type(
            DDS.DomainParticipant participant, 
            java.lang.String type_name);

}
