package com.kkb.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 映射器（包含了大量的网址与方法的对应关系）
 * 一个请求 -> 一个方法进行响应
 */
public class HandlerMapping {
    /**
     * 用于存放指定请求地址uri（字符串）与配置文件中所描述的处理响应请求的类的映射（MVCMapping）
     */
    private static Map<String,MVCMapping> data = new HashMap<>();

    /**
     * 用于获取指定请求的处理方法（映射）
     * @param uri 用户请求地址
     * @return 返回对应方法映射
     */
    public static MVCMapping get(String uri) {
        return data.get(uri);
    }

    /**
     * 用于加载配置文件，将配置文件中指定请求与相应的类的映射进行捆绑，存放到data中
     * @param is 从配置文件中反射回来的输入流对象
     */
    public static void load(InputStream is) {
        // 通过Properties类，从is中获取其中一个个键值对，值就是配置文件中描述的类
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取配置文件中描述的类
        Collection<Object> values = properties.values();
        // 遍历所有描述的类，通过反射生成相应类的实例
        for (Object value : values) {
            // 获取类名，value就是类名，需要强转成字符串String
            String className = (String) value;
            try {
                // 通过类名，获取Class对象
                Class aClass = Class.forName(className);
                // 通过Class对象，创建对应类的实例（无参构造）
                Object obj = aClass.getConstructor().newInstance();
                // 通过Class对象，获取这个类定义的所有方法method
                Method[] methods = aClass.getMethods();
                // 遍历所有方法
                for (Method method : methods) {
                    // 判断方法是否有注解
                    Annotation[] annotations = method.getAnnotations();
                    if (annotations != null) {
                        // 遍历所有注解
                        for (Annotation annotation : annotations) {
                            if (annotation instanceof ResponseBody) {
                                // 此方法用于返回字符串给客户端
                                // 创建MVCMapping的对象
                                MVCMapping mvcMapping = new MVCMapping(obj, method, ResponseType.TEXT);
                                // 将 key=注解值（发送的请求地址uri） 和 value=mvcMapping（映射的类的对象、方法、方法类型） 存入data
                                MVCMapping o = data.put(((ResponseBody) annotation).value(), mvcMapping);
                                // 检查是否有重复地址的录入
                                if (o != null) {
                                    // 覆盖了重复地址
                                    throw new  RuntimeException("请求地址重复: " + ((ResponseBody) annotation).value());
                                }
                            } else if (annotation instanceof ResponseView) {
                                // 此方法用于返回页面给客户端
                                // 创建MVCMapping的对象
                                MVCMapping mvcMapping = new MVCMapping(obj, method, ResponseType.VIEW);
                                // 将 key=注解值（发送的请求地址uri） 和 value=mvcMapping（映射的类的对象、方法、方法类型） 存入data
                                MVCMapping o = data.put(((ResponseView) annotation).value(), mvcMapping);
                                // 检查是否有重复地址的录入
                                if (o != null) {
                                    // 覆盖了重复地址
                                    throw new  RuntimeException("请求地址重复: " + ((ResponseView) annotation).value());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 映射对象，每一个对象封装了一个方法，用于处理请求
     */
    public static class MVCMapping {
        private Object obj;
        private Method method;
        private ResponseType type;

        public MVCMapping() {
        }

        public MVCMapping(Object obj, Method method, ResponseType type) {
            this.obj = obj;
            this.method = method;
            this.type = type;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public ResponseType getType() {
            return type;
        }

        public void setType(ResponseType type) {
            this.type = type;
        }
    }
}
