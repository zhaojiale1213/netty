package cn.itcast.client.handler;

import cn.hutool.json.JSONUtil;
import cn.itcast.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/4/16 17:15
 * @Modified By:
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    /** Promise<?> ? 通配符 存值的时候会报错，取值无影响 */
    public static final Map<Integer, Promise<Object>> promiseMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        log.debug("rpc return {}", JSONUtil.toJsonStr(msg));
        Promise<Object> promise = promiseMap.remove(msg.getSequenceId());
        if (Objects.isNull(promise)) throw new RuntimeException("promise is null");
        Exception exceptionValue = msg.getExceptionValue();
        if (Objects.isNull(exceptionValue)) {
            promise.setSuccess(msg.getReturnValue());
        } else {
            promise.setFailure(exceptionValue);
        }
    }
}
