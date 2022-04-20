package com.itcast.protostuff;

import cn.hutool.json.JSONUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ProtostuffUtils {

    private ProtostuffUtils() {}

    /**
     * 缓存Schema
     */
    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();


    /**
     * 序列化（对象 -> 字节数组）
     */
    @SuppressWarnings({"unchecked"})
    public static <T> byte[] serialize(T t){
        Class<T> clazz = (Class<T>) t.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(t, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getCause().getMessage(), e);
        } finally {
            buffer.clear();
        }
    }


    /**
     * 反序列化（字节数组 -> 对象）
     */
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            Schema<T> schema = getSchema(cls);
            T t = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(data, t, schema);
            return t;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    @SuppressWarnings({"unchecked"})
    private static <T> Schema<T> getSchema(Class<T> clazz){
        Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
        if (Objects.isNull(schema)) {
            schema = RuntimeSchema.getSchema(clazz);
            if (Objects.nonNull(schema)) schemaCache.put(clazz, schema);
        }
        return schema;
    }

    public static void main(String[] args) {
        User user = new User("1", "张三", 10, "描述");
        byte[] bytes = ProtostuffUtils.serialize(user);
        User deserialize = ProtostuffUtils.deserialize(bytes, User.class);
        System.out.println(JSONUtil.toJsonStr(deserialize));

        Group group = new Group();
        group.setId("1");
        group.setName("一组");
        group.setUsers(Collections.singletonList(user));

        byte[] bs = ProtostuffUtils.serialize(group);
        System.out.println(JSONUtil.toJsonStr(ProtostuffUtils.deserialize(bs, Group.class)));
    }
}
