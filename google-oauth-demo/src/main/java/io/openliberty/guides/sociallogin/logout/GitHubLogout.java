// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.sociallogin.logout;

import com.ibm.websphere.security.social.UserProfileManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class GitHubLogout implements ILogout {

    // @Inject
    // @ConfigProperty(name="github.client.id")
    private String clientId = "06fd8e0f745e0d2112c8";

    // @Inject
    // @ConfigProperty(name="github.client.secret")
    private String clientSecret = "645a9c8f52a0b9a086776674e1805db0296f4134";

    public Response logout() {

        final String unauthorizeUrl = "https://api.github.com/applications/645a9c8f52a0b9a086776674e1805db0296f4134/grant";

        String accessToken = UserProfileManager
                .getUserProfile()
                .getAccessToken();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("access_token", accessToken);

        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuthStream = Base64
                .getEncoder()
                .encode(auth.getBytes(StandardCharsets.ISO_8859_1));
        String encodedAuth = new String(encodedAuthStream);

        return ClientBuilder
                .newClient()
                .target(unauthorizeUrl)
                .resolveTemplate("client_id", clientId)
                .request()
                .header("Authorization", "Basic " + encodedAuth)
                .method("DELETE", Entity.json(requestBody));
    }
}