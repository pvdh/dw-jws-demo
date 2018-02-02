package de.vonderhagen.dw;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class OauthJwtConfiguration extends Configuration {
      @NotEmpty
    private String publicResponse;

    @NotEmpty
    private String protectedResponse;
    private String corsOrigins;
    private String jwsCertUrl;
    private String jwsExpectedIssuer;
    private String jwsExpectedAudience;

    @JsonProperty
    public String getPublicResponse() {
        return publicResponse;
    }

    @JsonProperty
    public void setPublicResponse(String publicRespone) {
        this.publicResponse = publicRespone;
    }

    @JsonProperty
    public String getProtectedResponse() {
        return protectedResponse;
    }

    @JsonProperty
    public void setProtectedResponse(String protectedResponse) {
        this.protectedResponse = protectedResponse;
    }

    /**
     * @return the corsOrigins
     */
    public String getCorsOrigins() {
        return corsOrigins;
    }

    /**
     * @param corsOrigins the corsOrigins to set
     */
    public void setCorsOrigins(String corsOrigins) {
        this.corsOrigins = corsOrigins;
    }

    /**
     * @return the jwsCertUrl
     */
    public String getJwsCertUrl() {
        return jwsCertUrl;
    }

    /**
     * @param jwsCertUrl the jwsCertUrl to set
     */
    public void setJwsCertUrl(String jwsCertUrl) {
        this.jwsCertUrl = jwsCertUrl;
    }

    /**
     * @return the jwsExpectedIssuer
     */
    public String getJwsExpectedIssuer() {
        return jwsExpectedIssuer;
    }

    /**
     * @param jwsExpectedIssuer the jwsExpectedIssuer to set
     */
    public void setJwsExpectedIssuer(String jwsExpectedIssuer) {
        this.jwsExpectedIssuer = jwsExpectedIssuer;
    }

    /**
     * @return the jwsExpectedAudience
     */
    public String getJwsExpectedAudience() {
        return jwsExpectedAudience;
    }

    /**
     * @param jwsExpectedAudience the jwsExpectedAudience to set
     */
    public void setJwsExpectedAudience(String jwsExpectedAudience) {
        this.jwsExpectedAudience = jwsExpectedAudience;
    }
}
