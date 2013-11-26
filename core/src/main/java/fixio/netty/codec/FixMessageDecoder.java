/*
 * Copyright 2013 The FIX.io Project
 *
 * The FIX.io Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package fixio.netty.codec;

import fixio.fixprotocol.SimpleFixMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class FixMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!in.isReadable()) {
            return;
        }
        int num = -1;
        SimpleFixMessage result = new SimpleFixMessage();

        final StringBuilder buffer = new StringBuilder();
        while (in.isReadable()) {
            byte b = in.readByte();
            if (b == 1) {
                result.add(num, buffer.toString());
                buffer.setLength(0);
            } else if (b == '=') {
                num = Integer.parseInt(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }

        }
        out.add(result);
    }


}
