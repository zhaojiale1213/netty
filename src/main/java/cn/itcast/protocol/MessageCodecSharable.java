package cn.itcast.protocol;

import cn.itcast.config.Config;
import cn.itcast.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
 *               编解码器
 * @Author: zjl
 * @Date:Created in 2022/3/23 22:21
 * @Modified By:
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {


    /** 出站时使用, msg => ByteBuf  编码：12个字节(魔数 + 版本 、、、) + 4个字节长度 + 真实的内容长度对应的字节 */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outs) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 4字节的魔数, 1个int = 4个字节
        out.writeBytes(new byte[] {1, 2, 3, 4});
        // 1字节的版本
        out.writeByte(1);
        // 1字节的序列化方式 0-jdk序列化方式  1-json
        out.writeByte(Config.getSerializerAlgorithm().ordinal());
        // 1字节的消息-指令类型
        out.writeByte(msg.getMessageType());
        // 4个字节 序号
        out.writeInt(msg.getSequenceId());

        // f - 15 => 15 * 16^0 + 15 * 16^1 = 255
        /** 一个字节的填充位，保证为 2 的整数倍 */
        out.writeByte(0xff);

        // jdk方式获取序列化后的二进制数据
        byte[] bytes = Config.getSerializerAlgorithm().serializer(msg);

        // 写入长度 - 4个字节
        out.writeInt(bytes.length);

        // 将byte数组中的内容写入ByteBuf
        out.writeBytes(bytes);

        /** 消息向下传递 */
        outs.add(out);
        log.info("encode - 编码");
    }

    /** 进站时使用 ByteBuf => msg 解码, 参照编码逆向进行 */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte msgType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        // 将ByteBuf中的内容读入到byte数组中
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        Serializer.Algorithm algorithm = Objects.requireNonNull(Serializer.Algorithm.findAlgorithm(serializerType));
        Class<? extends Message> messageClass = Message.getMessageClass(msgType);
        Message message = algorithm.deserialize(messageClass, bytes);

        /** Message 抽象类无法实例化，反射默认调用空参构造 */
//        Message message = algorithm.deserialize(Message.class, bytes);

        log.info("解码：magicNum - {}, version - {}, serializerType - {}, msgType - {}, sequenceId - {}, length - {}, message - {}",
            magicNum, version, serializerType, msgType, sequenceId, length, message);
        /** 消息向下传递 */
        out.add(message);
    }
}
