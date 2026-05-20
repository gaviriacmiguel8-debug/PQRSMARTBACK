package Proyecto.PQRSMART.Config;

import Proyecto.PQRSMART.Domain.Service.UserTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserToolsConfig {
    @Bean
    public ToolCallbackProvider usuarioToolCallbackProvider(UserTools userTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(userTools)
                .build();
    }

}
