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
package com.alibaba.cobar.server.heartbeat;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.alibaba.cobar.server.net.factory.BackendConnectionFactory;
import com.alibaba.cobar.server.startup.CobarContainer;

/**
 * @author xianmao.hexm
 */
public class MySQLDetectorFactory extends BackendConnectionFactory {

    public MySQLDetectorFactory() {
        this.idleTimeout = 300 * 1000L;
    }

    public MySQLDetector make(MySQLHeartbeat heartbeat) throws IOException {
        SocketChannel channel = getChannel();
        DataSourceConfig dsc = heartbeat.getSource().getConfigModel();
        DataNodeConfig dnc = heartbeat.getSource().getNode().getConfigModel();
        MySQLDetector detector = new MySQLDetector(channel);
        detector.setHost(dsc.getHost());
        detector.setPort(dsc.getPort());
        detector.setUser(dsc.getUser());
        detector.setPassword(dsc.getPassword());
        detector.setSchema(dsc.getDatabase());
        detector.setHeartbeatTimeout(dnc.getHeartbeatTimeout());
        detector.setHeartbeat(heartbeat);
        postConnect(detector, CobarContainer.getInstance().getConnector());
        return detector;
    }

}