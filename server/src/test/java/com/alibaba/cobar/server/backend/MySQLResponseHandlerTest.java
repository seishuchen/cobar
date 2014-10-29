/*
 * Copyright 1999-2014 Alibaba Group.
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
package com.alibaba.cobar.server.backend;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.cobar.server.defs.ErrorCode;
import com.alibaba.cobar.server.net.packet.AbstractPacket;
import com.alibaba.cobar.server.net.packet.CommandPacket;

/**
 * @author xianmao.hexm
 */
public class MySQLResponseHandlerTest implements MySQLResponseHandler {

    private static final Logger LOGGER = Logger.getLogger(MySQLResponseHandlerTest.class);

    private MySQLConnection connection;
    private CommandPacket packet;

    public MySQLResponseHandlerTest() {
        CommandPacket packet = new CommandPacket();
        packet.packetId = 0;
        packet.command = AbstractPacket.COM_QUERY;
        packet.arg = "select 1".getBytes();
        this.packet = packet;
    }

    @Override
    public void setConnection(MySQLConnection c) {
        this.connection = c;
    }

    @Override
    public void error(int code, Throwable t) {
        LOGGER.warn(connection.toString(), t);
        switch (code) {
        case ErrorCode.ERR_HANDLE_DATA:
        case ErrorCode.ERR_PUT_WRITE_QUEUE:
        default:
            connection.close();
        }
    }

    @Override
    public void connectionAquired() {
        packet.write(connection);
    }

    @Override
    public void okPacket(byte[] data) {

    }

    @Override
    public void errorPacket(byte[] data) {

    }

    @Override
    public void fieldEofPacket(byte[] header, List<byte[]> fields, byte[] data) {

    }

    @Override
    public void rowDataPacket(byte[] data) {

    }

    @Override
    public void rowEofPacket(byte[] data) {
        packet.write(connection);
    }

}
