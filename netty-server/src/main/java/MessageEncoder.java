import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteOrder;

/**
 * Created by d.cao on 2016/12/23.
 *
 * +------------+--------------+
 * |   length   |     body     |
 * | 0x0000012C | (300 bytes)  |
 * +-----------+------++-------+
 *
 * length is big-endian, occupied 4 bytes
 *
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        byte[] data = msg.toString().getBytes();
        out.order(ByteOrder.BIG_ENDIAN).writeInt(data.length);
        out.writeBytes(data, 0, data.length);
    }

}
