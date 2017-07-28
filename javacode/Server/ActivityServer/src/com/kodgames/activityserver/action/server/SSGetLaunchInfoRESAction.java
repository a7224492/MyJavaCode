package com.kodgames.activityserver.action.server;

import com.kodgames.activityserver.service.server.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

/**
 * Created by lz on 2016/8/22.
 */
@ActionAnnotation(messageClass = SSGetLaunchInfoRES.class, actionClass = SSGetLaunchInfoRESAction.class, serviceClass = ServerService.class)
public class SSGetLaunchInfoRESAction extends ProtobufMessageHandler<ServerService, SSGetLaunchInfoRES> {
    final static Logger logger = LoggerFactory.getLogger(SSGetLaunchInfoRESAction.class);

    @Override
    public void handleMessage(Connection connection, ServerService service, SSGetLaunchInfoRES message,
                              int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        if (message.getResult() != PlatformProtocolsConfig.SS_GET_LAUNCH_INFO_SUCCESS) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!getlaunchInfo failed!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        ConnectionManager.getInstance().setLocalPeerID(message.getServer().getId());

        // 连接Manager并获得启动信息后, 开始启动本地服务
        service.startSelf(ServerConfig.fromProto(message.getServer()));
    }

}