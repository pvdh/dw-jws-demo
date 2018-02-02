package de.vonderhagen.dw.resources;

import de.vonderhagen.dw.api.HelloWorld;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.caching.CacheControl;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author of7085
 */
@Path("/publicApi")
@Produces(MediaType.APPLICATION_JSON)
@CacheControl(noCache = true)
public class PublicResource {
    String a;
    
    public PublicResource(String a){
        this.a = a;
    }
    
    @GET
    @Timed
    public HelloWorld sayHello() {
           Logger logger = LoggerFactory.getLogger(PublicResource.class);
           logger.warn("GET /publicApi");
        return new HelloWorld(1,a);
    }
    
}
