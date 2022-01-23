package com.webrtc.webrtcsignalingserver;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service(value = "socketIOService")
public class SocketIOServiceImpl implements ISocketIOService {

    /**
     * Store connected clients
     */
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    /**
     * Custom Event`push_data_event` for service side to client communication
     */
    private static final String PUSH_DATA_EVENT = "create";

    @Autowired
    private SocketIOServer socketIOServer;

    /**
     * Spring IoC After the container is created, start after loading the SocketIOServiceImpl Bean
     */
    @PostConstruct
    private void autoStartup() {
        start();
    }

    /**
     * Spring IoC Container closes before destroying SocketIOServiceImpl Bean to avoid restarting project service port occupancy
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    @Override
    public void start() {
        // Listen for client connections
        socketIOServer.addConnectListener(client -> {
            log.debug("************ Client: " + getIpByClient(client) + " Connected ************");
            // Custom Events `connected` -> communicate with clients (built-in events such as Socket.EVENT_CONNECT can also be used)
            client.sendEvent("connected", "You're connected successfully...");
            String userId = getParamsByClient(client);
            if (userId != null) {
                clientMap.put(userId, client);
            }
        });

        // Listening Client Disconnect
        socketIOServer.addDisconnectListener(client -> {
            String clientIp = getIpByClient(client);
            log.debug(clientIp + " *********************** " + "Client disconnected");
            String userId = getParamsByClient(client);
            if (userId != null) {
                clientMap.remove(userId);
                client.disconnect();
            }
        });

        // Custom Event`client_info_event` ->Listen for client messages
        socketIOServer.addEventListener(PUSH_DATA_EVENT, String.class, (client, data, ackSender) -> {
            // When a client pushes a `client_info_event` event, onData accepts data, which is json data of type string here and can be Byte[], other types of object
            String clientIp = getIpByClient(client);
            log.debug(clientIp + " ************ Client:" + data);
        });

        // Start Services
        socketIOServer.start();

        // Broadcast: The default is to broadcast to all socket connections, but not to the sender himself, who needs to be sent separately if he intends to receive the message himself.
        new Thread(() -> {
            int i = 0;
            while (true) {
                try {
                    // Send broadcast message every 3 seconds
                    Thread.sleep(3000);
             //       socketIOServer.getBroadcastOperations().sendEvent("myBroadcast", "Broadcast message " );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    @Override
    public void pushMessageToUser(String userId, String msgContent) {
        SocketIOClient client = clientMap.get(userId);
        if (client != null) {
            client.sendEvent(PUSH_DATA_EVENT, msgContent);
        }
    }

    /**
     * Get the userId parameter in the client url (modified here to suit individual needs and client side)
     *
     * @param client: Client
     * @return: java.lang.String
     */
    private String getParamsByClient(SocketIOClient client) {
        // Get the client url parameter (where userId is the unique identity)
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> userIdList = params.get("EIO");
        if (!CollectionUtils.isEmpty(userIdList)) {
            return userIdList.get(0);
        }
        return null;
    }

    /**
     * Get the connected client ip address
     *
     * @param client: Client
     * @return: java.lang.String
     */
    private String getIpByClient(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));
        return clientIp;
    }

}
