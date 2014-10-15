/*
 * Copyright 1999-2012 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cobar.server.core.net.packet;

import java.nio.ByteBuffer;

import com.alibaba.cobar.server.core.net.FrontendConnection;
import com.alibaba.cobar.server.core.net.protocol.MySQLMessage;
import com.alibaba.cobar.server.util.ByteBufferUtil;

/**
 * From Server To Client, at the end of a series of Field Packets, and at the
 * end of a series of Data Packets.With prepared statements, EOF Packet can also
 * end parameter information, which we'll describe later.
 * 
 * <pre>
 * Bytes                 Name
 * -----                 ----
 * 1                     field_count, always = 0xfe
 * 2                     warning_count
 * 2                     Status Flags
 * 
 * @see http://forge.mysql.com/wiki/MySQL_Internals_ClientServer_Protocol#EOF_Packet
 * </pre>
 * 
 * @author xianmao.hexm 2010-7-16 上午10:55:53
 */
public class EOFPacket extends AbstractPacket {

    public static final byte FIELD_COUNT = (byte) 0xfe;

    public byte fieldCount = FIELD_COUNT;
    public int warningCount;
    public int status = 2;

    public void read(byte[] data) {
        MySQLMessage mm = new MySQLMessage(data);
        packetLength = mm.readUB3();
        packetId = mm.read();
        fieldCount = mm.read();
        warningCount = mm.readUB2();
        status = mm.readUB2();
    }

    @Override
    public ByteBuffer write(ByteBuffer buffer, FrontendConnection c) {
        packetLength = calcPacketLength();
        int headerSize = c.getProtocol().getPacketHeaderSize();
        buffer = ByteBufferUtil.check(buffer, headerSize + packetLength, c);
        ByteBufferUtil.writeUB3(buffer, packetLength);
        buffer.put(packetId);
        buffer.put(fieldCount);
        ByteBufferUtil.writeUB2(buffer, warningCount);
        ByteBufferUtil.writeUB2(buffer, status);
        return buffer;
    }

    @Override
    public int calcPacketLength() {
        return 5;// 1+2+2;
    }

    @Override
    protected String getPacketInfo() {
        return "EOF Packet";
    }

}