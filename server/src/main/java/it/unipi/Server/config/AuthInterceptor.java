package it.unipi.Server.config;

import it.unipi.Server.model.User;
import it.unipi.Server.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Controlla le richieste prima che arrivino ai controller specifici, tranne
 * per i path espressamente scartati nel file WebConfig.java. 
 * @author lucamaffioli
 */
@Component
public class AuthInterceptor implements HandlerInterceptor{
    
    @Autowired
    private AuthService authService;
    
    /**
     * In esecuzione prima dei controller per verifica token.
     * @param request richiesta Http
     * @param response risposta Http
     * @return true se token valido, false altrimenti.
     * @throws Exception 
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // Salvataggio token prelevato dal campo Authorization. 
        String token = request.getHeader("Authorization");
        
        // Gestione token mancante o campo vuoto.
        if (token == null || token.isEmpty()) {
            // Risposta con codice UNAUTHORIZED e messaggio di errore.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "MISSING_TOKEN");
            return false;
        }
        
        // Utente collegato al token.
        User user = authService.getUserByToken(token);
        if (user == null) {
            // Risposta con codice UNAUTHORIZED e messaggio di errore.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "TOKEN_EXPIRED_OR_NOT_VALID");
            return false;
        }
        
        /**
         * Visto che l'utente legato al token è stato individuato viene associato
         * come attributo alla richiesta in modo da essere facilmente recuperato
         * dal controller che elaborerà la richiesta.
         */
        request.setAttribute("loggedUser", user);
        
        /**
         * Rinnovo scadenza token.
         */
        authService.renewExpiration(token);
        
        return true;
    }
}
