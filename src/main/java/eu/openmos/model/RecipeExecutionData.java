package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.bson.Document;

/**
 * Object that represents data regarding recipe execution and that then are passed on to the optimizer.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class RecipeExecutionData extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757401L;
    
    private String productId;
    private String recipeId;
    private List<String> kpiIds;
    private List<String> parameterIds;

//    private static final int FIELDS_COUNT = 5;
    
    // reflection stuff
    public RecipeExecutionData() { super();
    }

    public RecipeExecutionData(
            String productId, 
            String recipeId, 
            List<String> kpiIds, 
            List<String> parameterIds, 
            Date registeredTimestamp) 
    {
        super(registeredTimestamp);
        
        this.productId = productId;
        this.recipeId = recipeId;
        this.kpiIds = kpiIds;
        this.parameterIds = parameterIds;
//        this.registered = registered;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public List<String> getKpiIds() {
        return kpiIds;
    }

    public void setKpiIds(List<String> kpiIds) {
        this.kpiIds = kpiIds;
    }

    public List<String> getParameterIds() {
        return parameterIds;
    }

    public void setParameterIds(List<String> parameterIds) {
        this.parameterIds = parameterIds;
    }

    /**
     * Method that serializes the object.
     * The returned string has the following format:
 
 productId
 recipeId
 list of kpi ids
 list of parameter ids
 registered
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(productId);
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
//        builder.append(recipeId);
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
//        builder.append(ListsToString.writeKPIIds(kpiIds));
//
//        builder.append(SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
//        builder.append(ListsToString.writeParameterIds(parameterIds));
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        builder.append(SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
//        builder.append(stringRegisteredTimestamp);
////        builder.append(SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
////        builder.append(registered);
//        
//        return builder.toString();
//        
//    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
 
 productId
 recipeId
 list of kpi ids
 list of parameter ids
 registered
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
     */
//    public static RecipeExecutionData fromString(String object) throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
//
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("RecipeExecutionData - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        return new RecipeExecutionData(
//                tokenizer.nextToken(),                                          // product id
//                tokenizer.nextToken(),                                          // recipe id
//                StringToLists.readKPIIds(tokenizer.nextToken()),                // list of kpi ids
//                StringToLists.readParameterIds(tokenizer.nextToken()),          // list of parameter ids
//                sdf.parse(tokenizer.nextToken())                                // registered
////                tokenizer.nextToken()                                // registered                
//        );
//    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 productId
 recipeId
 list of kpi ids
 list of parameter ids
 registered
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();
//        Document doc = new Document();
//        
//        doc.append("productId", productId);
//        doc.append("recipeId", recipeId);
//        doc.append("kpiIds", kpiIds);
//        doc.append("parameterIds", parameterIds);
//        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
//        
//        return doc;        
    }
    
//    public Document toBSON2() {
//        Document doc2 = Document.parse(toJSON());
//        
//        return doc2;        
//    }
//    public String toJSON() {
//        String jsonInString = null;
//        String jsonInFormattedString;
//        
//		ObjectMapper mapper = new ObjectMapper();
//
//		try {
//			// Convert object to JSON string
//			jsonInString = mapper.writeValueAsString(this);
//			// System.out.println(jsonInString);
//
//			// Convert object to JSON string and pretty print
//			jsonInFormattedString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
//			// System.out.println(jsonInFormattedString);
//                        
//                        
//		} catch (JsonGenerationException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//                return jsonInString;
//	}    
    
//    public static RecipeExecutionData fromJSON(String jsonString) {
//        RecipeExecutionData red = null;
//		ObjectMapper mapper = new ObjectMapper();
//
//		try {
//			 Convert JSON string to Object
//			red = mapper.readValue(jsonString, RecipeExecutionData.class);
//			 System.out.println(red);
//		} catch (JsonGenerationException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//                
//                return red;
//	}    
    
//    public static void main(String[] args)
//    {
//        RecipeExecutionData red = RecipeExecutionDataTest.getTestObject();
//        System.out.println("red = " + red);
//        
//        String json2 = red.toJSON2();
//        System.out.println("json2 automatic jackson = " + json2);
//        
//        String json1 = red.toBSON().toJson();
//        System.out.println("json2 manual mongodb = " + json1);
//
//        Document bsonDocument1 = red.toBSON();
//        String bson1 = bsonDocument1.toString();
//        System.out.println("bson1 manual = " + bson1);
//        Document bsonDocument2 = red.toBSON2();
//        String bson2 = bsonDocument2.toString();
//        System.out.println("bson2 automatic mongodb = " + bson2);
//        
//        RecipeExecutionData target2 = (RecipeExecutionData)fromJSON2(json2, RecipeExecutionData.class);
//        System.out.println("target2 from automatic json = " + target2);
//        
//        // target1 va in errore perchè il json2 di partenza deriva dal bson fatto a mano
//        // e il campo non si chiama registered come dovrebbe, ma solo registered        
//        RecipeExecutionData target1 = (RecipeExecutionData)fromJSON2(json1, RecipeExecutionData.class);
//        System.out.println("target1 from manual bson document = " + target1);
//
//        RecipeExecutionData target22 = (RecipeExecutionData)fromBSON2(bsonDocument2, RecipeExecutionData.class);
//        System.out.println("target22 from automatic bson = " + target22);
//        RecipeExecutionData target21 = (RecipeExecutionData)fromBSON2(bsonDocument1, RecipeExecutionData.class);
//        System.out.println("target21 from manual bson = " + target21);
//    }
}
