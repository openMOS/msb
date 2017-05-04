/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.agentcloud.utilities.Constants;
import java.util.StringTokenizer;

/**
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public class PhysicalLocation extends Location {
    private String referenceFrameName;
    private long x;
    private long y;
    private long z;
    private long alpha;
    private long beta;
    private long gamma;

    public PhysicalLocation() {}
    
    public PhysicalLocation(String referenceFrameName, long x, long y, long z, long alpha, long beta, long gamma) {
        this.referenceFrameName = referenceFrameName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
    }

    public String getReferenceFrameName() {
        return referenceFrameName;
    }

    public void setReferenceFrameName(String referenceFrameName) {
        this.referenceFrameName = referenceFrameName;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }

    public long getAlpha() {
        return alpha;
    }

    public void setAlpha(long alpha) {
        this.alpha = alpha;
    }

    public long getBeta() {
        return beta;
    }

    public void setBeta(long beta) {
        this.beta = beta;
    }

    public long getGamma() {
        return gamma;
    }

    public void setGamma(long gamma) {
        this.gamma = gamma;
    }

    /**
     * Method that serializes the object.
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        return referenceFrameName + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PHYSICAL_LOCATION + x + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PHYSICAL_LOCATION + y + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PHYSICAL_LOCATION + z + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PHYSICAL_LOCATION + alpha + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PHYSICAL_LOCATION + beta + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PHYSICAL_LOCATION + gamma;
    }
    
    /**
    * Method that deserializes a String object.
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
    public static PhysicalLocation fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PHYSICAL_LOCATION);
        return new PhysicalLocation(tokenizer.nextToken(), Long.parseLong(tokenizer.nextToken()), Long.parseLong(tokenizer.nextToken()), Long.parseLong(tokenizer.nextToken()), Long.parseLong(tokenizer.nextToken()), Long.parseLong(tokenizer.nextToken()), Long.parseLong(tokenizer.nextToken()));
    }
}
