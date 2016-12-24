import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by d.cao on 2016/12/23.
 *
 * +-------+------------+------+-------------+
 * | type |   length   |  op  |     body     |
 * | 0x01 | 0x00000132 | 0x00 | (300 bytes)  |
 * +------+------------+------+--------------+
 *
 * length is big-endian, occupied 4 bytes
 *
 */
public class MessageDecoder extends ByteToMessageDecoder {

    private final int MAX_MSG_SIZE = 1024 * 1024; // 1MB

    private int parseVarint32(byte[] buf, int offset, int length) {
        ByteBuffer b = ByteBuffer.wrap(buf, offset, length);
        return b.order(ByteOrder.BIG_ENDIAN).getInt();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final byte[] header = new byte[6];
        if (in.readableBytes() < header.length) {
            return;
        }
        in.markReaderIndex();

        in.readBytes(header);
        int length = parseVarint32(header, 1, 4);
        if (length < 0 || length > MAX_MSG_SIZE) {
            throw new CorruptedFrameException("illegal body length: " + length);
        }

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        // gen msg
        Message msg = new Message((int)header[0], length, (int)header[5], in.readBytes(length).array());
        out.add(msg);
    }
}
