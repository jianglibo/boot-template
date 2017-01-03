/**
 * Copyright 2015 jianglibo@gmail.com
 *
 */
package hello.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author jianglibo@gmail.com
 *         2015å¹?12æœ?17æ—?
 *
 */
@Component
public class CommonJsonFieldExtractor {
    
    private static final String PATCH_FIELD = "_patch";
    
    public CommonJsonFieldExtractor() {}

    public Set<String> patchFields(JsonNode model) {
        JsonNode jn = model.path(PATCH_FIELD);
        Set<String> ss = Sets.newHashSet();
        if (!jn.isMissingNode()) {
            if (jn.isArray()) {
                ss = Lists.newArrayList((ArrayNode) jn).stream()//
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
