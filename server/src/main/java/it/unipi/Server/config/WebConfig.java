package it.unipi.Server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe di configurazione.
 * @author lucamaffioli
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired 
    private AuthInterceptor authInterceptor;
    
    /**
     * Funzione per specificare l'interceptor e le regole seguite da questo.
     * @param registry 
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * Specificati i path delle richieste che authInterceptor deve ignorare 
         * in quanto queste non avranno come token un valore significativo.
         */
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(           
                        "/api/auth/**",  
                        "/api/setup/inizializza"
                );
    }
}
