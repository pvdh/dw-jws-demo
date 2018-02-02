package de.vonderhagen.dw.resources;

import com.codahale.metrics.annotation.Timed;
import de.vonderhagen.dw.api.HelloWorld;
import io.dropwizard.jersey.caching.CacheControl;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/protectedApi")
@RolesAllowed("ORGMEMBER")
@Produces(MediaType.APPLICATION_JSON)
@CacheControl(noCache = true)
public class ProtectedResource {
      String a;
    
    public ProtectedResource(String a){
        this.a = a;
    }
    
    @GET
    @Timed
    public HelloWorld sayHello() {
                   Logger logger = LoggerFactory.getLogger(ProtectedResource.class);
           logger.warn("GET /publicApi");
        return new HelloWorld(1,a);
    }
     
}
