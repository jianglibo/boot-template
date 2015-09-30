/**
 * Copyright 2015 Hangzhou NetFrog Inc.
 *
 */
package hello.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * @author jianglibo@gmail.com
 *         2015年9月30日
 *
 */
public class Pmodule extends SimpleModule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Pmodule() {
        super(new Version(1, 0, 0, null, "hello", "jackson-module"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fasterxml.jackson.databind.module.SimpleModule#setupModule(com.fasterxml.jackson.databind.Module.SetupContext)
     */
    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        addSerializer(new ProjectionSerializer());
    }

    private static class ProjectionSerializer extends StdSerializer<ProjectionResourceContent> {
        
        private boolean unwrapping;

        /**
         * @param t
         */
        protected ProjectionSerializer() {
            super(ProjectionResourceContent.class);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator,
         * com.fasterxml.jackson.databind.SerializerProvider)
         */
        @Override
        public void serialize(ProjectionResourceContent value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            provider.//
                    findValueSerializer(value.getProjectionInterface(), null).//
                    unwrappingSerializer(null).//
                    serialize(value.getProjection(), jgen, provider);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.fasterxml.jackson.databind.JsonSerializer#isUnwrappingSerializer()
         */
        @Override
        public boolean isUnwrappingSerializer() {
            return unwrapping;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.fasterxml.jackson.databind.JsonSerializer#unwrappingSerializer(com.fasterxml.jackson.databind.util.NameTransformer)
         */
        @Override
        public JsonSerializer<ProjectionResourceContent> unwrappingSerializer(NameTransformer unwrapper) {

            this.unwrapping = true;
            return this;
        }

    }

    static class ProjectionResourceContent {

        private final Object projection;
        private final Class<?> projectionInterface;

        /**
         * @param projection
         * @param projectionInterface
         */
        public ProjectionResourceContent(Object projection, Class<?> projectionInterface) {
            this.projection = projection;
            this.projectionInterface = projectionInterface;
        }

        public Object getProjection() {
            return projection;
        }

        public Class<?> getProjectionInterface() {
            return projectionInterface;
        }
    }

}
