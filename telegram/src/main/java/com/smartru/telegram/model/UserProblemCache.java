package com.smartru.telegram.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smartru.performers.geometric.GeometricProblem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserProblemCache {

    private long id;
    private String username;
    private boolean equipped;
    private String shape;
    private String unknown;
    private ObjectNode props;
    private String lastProp;

    public UserProblemCache() {
        ObjectMapper mapper = new ObjectMapper();
        props = mapper.createObjectNode();
    }

    public String getGeometricProblemFromCache(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode problem = mapper.createObjectNode();
        problem.put(GeometricProblem.Parameters.SHAPE.name(), shape)
                .put(GeometricProblem.Parameters.UNKNOWN_PROPERTY.name(), unknown)
                .put(GeometricProblem.Parameters.PROPS.name(), props.toString());
        return problem.toString();
    }
}
