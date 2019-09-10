package org.example.netty.example06;

/**
 * Protocoal Buffers实验
 *
 * 两种方式生成源代码
 *   1. 下载安装protobuf编译器： https://github.com/protocolbuffers/protobuf/releases
 *      运行protoc命令，直接生成源码到项目源码路径
 *        protoc --java_out=src\main\java src\main\proto\netty-06.proto
 *   2. 配置构建插件：protobuf-maven-plugin，在项目构建目录下生成源码，IDE可直接引用
 *        mvn protobuf:compile
 *
 *   See https://www.xolstice.org/protobuf-maven-plugin/
 *   See https://github.com/grpc/grpc-java
 */
public class ProtoBufTest {

    public static void main(String[] args) throws Exception {
        DataInfo.Student student = DataInfo.Student.newBuilder().
                setName("张三").setAge(20).setAddress("北京").build();

        byte[] student2ByteArray = student.toByteArray();

        DataInfo.Student student2 = DataInfo.Student.parseFrom(student2ByteArray);

        System.out.println(student2.getName());
        System.out.println(student2.getAge());
        System.out.println(student2.getAddress());
    }

}
