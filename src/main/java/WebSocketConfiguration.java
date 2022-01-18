import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler(), "/socket")
                .setAllowedOrigins("*");
    }

}
