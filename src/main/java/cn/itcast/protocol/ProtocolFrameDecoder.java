package cn.itcast.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Description: 解决粘包半包问题的handler, 第一个使用
 * @Author: zjl
 * @Date:Created in 2022/3/24 22:48
 * @Modified By:
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {


    public ProtocolFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

}
