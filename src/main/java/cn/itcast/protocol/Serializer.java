package cn.itcast.protocol;


import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/6 21:39
 * @Modified By:
 */
public interface Serializer {

    /**
     * 反序列化方法
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    /**
     * 序列化方法
     * @param t
     * @param <T>
     * @return
     */
    <T> byte[] serializer(T t);


    enum Algorithm implements Serializer {

        JAVA {
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (T) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("java反序列化失败", e);
                }
            }

            @Override
            public <T> byte[] serializer(T t) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(t);
                    return bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("java序列化失败", e);
                }
            }
        },

        GSON {

            private final Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new Serializer.ClassCodec()).create();

            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                String json = new String(bytes, StandardCharsets.UTF_8);
                return gson.fromJson(json, clazz);
            }

            @Override
            public <T> byte[] serializer(T t) {
                String json = gson.toJson(t);
                return json.getBytes(StandardCharsets.UTF_8);
            }
        }

        ;

        public static Algorithm findAlgorithm(int ordinal) {
            Algorithm[] values = Algorithm.values();
            for (Algorithm algorithm : values) {
                if (algorithm.ordinal() == ordinal) return algorithm;
            }
            return null;
        }

    }

    class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String str = json.getAsString();
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            // class -> json
            return new JsonPrimitive(src.getName());
        }
    }

}
