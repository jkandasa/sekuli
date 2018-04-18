package com.redhat.qe.rest.core.typeresolvers;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.qe.rest.core.RestObjectMapper;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */
public class SimpleJavaTypeResolver {

    private final ObjectMapper objectMapper;

    public SimpleJavaTypeResolver() {
        this.objectMapper = new RestObjectMapper();
    }

    /**
     * Simple Class, i.e.: Double
     */
    public JavaType get(Class<?> clazz) {
        return objectMapper.getTypeFactory().constructType(clazz);
    }

    /**
     * Simple Class with Generic, i.e.: Metric<Double>
     */
    public JavaType get(Class<?> clazz, Class<?> parametrizedClazz) {
        JavaType parametrizedClazzType = objectMapper.getTypeFactory().constructType(parametrizedClazz);

        return objectMapper.getTypeFactory().constructParametricType(clazz, parametrizedClazzType);
    }

    /**
     * Simple Class with Map with Generic Key and Value, i.e.: Metric<Map<<String, Object>>
     */
    public JavaType get(Class<?> clazz, @SuppressWarnings("rawtypes") Class<? extends Map> mapClazz,
            Class<?> mapClazzKey,
            Class<?> mapClazzValue) {
        JavaType mapClazzKeyType = objectMapper.getTypeFactory().constructType(mapClazzKey);
        JavaType mapClazzValuType = objectMapper.getTypeFactory().constructType(mapClazzValue);
        JavaType mapClazzType = objectMapper.getTypeFactory().constructMapType(mapClazz, mapClazzKeyType,
                mapClazzValuType);

        return objectMapper.getTypeFactory().constructParametricType(clazz, mapClazzType);
    }

    /**
     * Simple Class with List with Generic, i.e.: List<Double>
     */
    public JavaType get(Class<?> baseClazz, @SuppressWarnings("rawtypes") Class<? extends List> collectionClazz,
            Class<?> clazz) {
        JavaType clazzType = objectMapper.getTypeFactory().constructType(clazz);
        JavaType collClazz = objectMapper.getTypeFactory().constructCollectionType(collectionClazz, clazzType);
        return objectMapper.getTypeFactory().constructParametricType(baseClazz, collClazz);

    }
}