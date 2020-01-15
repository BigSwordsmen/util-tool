/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.json;

/**
 *
 * @author zhaoj
 * @version JsonTest.java, v 0.1 2019-04-17 11:30
 */
public class JsonTest {
    /**
     * 选择一个合适的JSON库要从多个方面进行考虑：
     字符串解析成JSON性能
     字符串解析成JavaBean性能
     JavaBean构造JSON性能
     集合构造JSON性能
     易用性
     */

    /**
     * Gson

     项目地址：https://github.com/google/gson
     Gson是目前功能最全的Json解析神器，Gson当初是为因应Google公司内部需求而由Google自行研发而来，但自从在2008年五月公开发布第一版后已被许多公司或用户应用。 Gson的应用主要为toJson与fromJson两个转换函数，无依赖，不需要例外额外的jar，能够直接跑在JDK上。 在使用这种对象转换之前，需先创建好对象的类型以及其成员才能成功的将JSON字符串成功转换成相对应的对象。 类里面只要有get和set方法，Gson完全可以实现复杂类型的json到bean或bean到json的转换，是JSON解析的神器。

     FastJson

     项目地址：https://github.com/alibaba/fastjson
     Fastjson是一个Java语言编写的高性能的JSON处理器,由阿里巴巴公司开发。无依赖，不需要例外额外的jar，能够直接跑在JDK上。 FastJson在复杂类型的Bean转换Json上会出现一些问题，可能会出现引用的类型，导致Json转换出错，需要制定引用。 FastJson采用独创的算法，将parse的速度提升到极致，超过所有json库。

     Jackson

     项目地址：https://github.com/FasterXML/jackson
     Jackson是当前用的比较广泛的，用来序列化和反序列化json的Java开源框架。Jackson社区相对比较活跃，更新速度也比较快， 从Github中的统计来看，Jackson是最流行的json解析器之一，Spring MVC的默认json解析器便是Jackson。

     Jackson优点很多：

     Jackson 所依赖的jar包较少，简单易用。
     与其他 Java 的 json 的框架 Gson 等相比，Jackson 解析大的 json 文件速度比较快。
     Jackson 运行时占用内存比较低，性能比较好
     Jackson 有灵活的 API，可以很容易进行扩展和定制。
     目前最新版本是2.9.4，Jackson 的核心模块由三部分组成：

     jackson-core 核心包，提供基于”流模式”解析的相关 API，它包括 JsonPaser 和 JsonGenerator。Jackson 内部实现正是通过高性能的流模式 API 的 JsonGenerator 和 JsonParser 来生成和解析 json。
     jackson-annotations 注解包，提供标准注解功能；
     jackson-databind 数据绑定包，提供基于”对象绑定” 解析的相关 API（ ObjectMapper ）和”树模型” 解析的相关 API（JsonNode）；基于”对象绑定” 解析的 API 和”树模型”解析的 API 依赖基于”流模式”解析的 API。
     为什么Jackson的介绍这么长啊？因为它也是本人的最爱。

     Json-lib

     项目地址：http://json-lib.sourceforge.net/index.html
     json-lib最开始的也是应用最广泛的json解析工具，json-lib 不好的地方确实是依赖于很多第三方包，对于复杂类型的转换，json-lib对于json转换成bean还有缺陷， 比如一个类里面会出现另一个类的list或者map集合，json-lib从json到bean的转换就会出现问题。json-lib在功能和性能上面都不能满足现在互联网化的需求。
     * @param args
     */
    public static void main(String[] args) {

    }
}
