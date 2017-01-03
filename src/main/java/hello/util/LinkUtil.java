package hello.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class LinkUtil {

    private final ObjectMapper objectMapper;
    
    private final Pattern idPtn = Pattern.compile(".*/(\\d+)[^/]*$");
    
    @Autowired
    public LinkUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    public long extractIdFromHref(JsonNode jn, String rel) {
        LinkDiscoverer discoverer = new HalLinkDiscoverer();
        Link link = discoverer.findLinkWithRel(rel, jn.toString());
        if (link == null) {
            return -1L;
        }
        String href = link.getHref();
        Matcher m = idPtn.matcher(href);
        if (m.matches()) {
            return Long.valueOf(m.group(1));
        }
        return -1L;
    }
}
