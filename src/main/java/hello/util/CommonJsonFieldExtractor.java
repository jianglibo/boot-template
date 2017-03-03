/**
 * Copyright 2015 jianglibo@gmail.com
 *
 */
package hello.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author jianglibo@gmail.com
 *
 */
@Component
public class CommonJsonFieldExtractor {
    
    private static final String PATCH_FIELD = "_patch";
    
    public CommonJsonFieldExtractor() {}

    public Set<String> patchFields(JsonNode model) {
        JsonNode jn = model.path(PATCH_FIELD);
        Set<String> ss = new HashSet<>();
        if (!jn.isMissingNode()) {
            if (jn.isArray()) {
            	List<JsonNode> jns = new ArrayList<>();
            	ArrayNode an = (ArrayNode) jn;
            	an.forEach(cjn -> {
            		jns.add(cjn);
            	});
            	
                ss = jns.stream()//
                        .filter(it -> it.isTextual()) //
                        .map(it -> it.asText())//
                        .collect(Collectors.toSet());
            } else if (jn.isTextual()) {
                ss.add(jn.asText());
            }
        }
        return ss;
    }
}
