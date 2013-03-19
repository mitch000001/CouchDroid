package org.mitchwork.couchdroid;

/**
 * User: Michael Wagner
 * Date: 17.03.13
 * Time: 19:55
 */

import org.codehaus.jackson.annotate.*;

public class Sofa {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("_rev")
    private String revision;
    private String color;

    public String getId() {
        return id;
    }

    public void setId(String s) {
        id = s;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String s) {
        revision = s;
    }

    public void setColor(String s) {
        color = s;
    }

    public String getColor() {
        return color;
    }

}
