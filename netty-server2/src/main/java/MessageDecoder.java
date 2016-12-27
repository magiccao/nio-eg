import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by d.cao on 2016/12/23.
 *
 * +-------+------------+------+-------------+
 * | type |   length   |  op  |     body     |
 * | 0x01 | 0x0000AC02 | 0x00 | (300 bytes)  |
 * +------+------------+------+--------------+
 *
 * length is big-endian, occupied 4 bytes
 *
 */
public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int type = in.readByte();
        ByteBuf buf = in.order(ByteOrder.BIG_ENDIAN);
        int length = buf.readInt();
        int op = in.readByte();
        byte[] body = in.readBytes(length).array();

        // gen msg
        Message msg = new Message(type, length, op, body);
        out.add(msg);
    }

}
