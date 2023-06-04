package com.webhookie.config.web.api.oauth

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 17/5/2023 12:04
 */
class AuthorizeRequestFactory(
  private val repository: MutableReactiveClientRegistrationRepository,
) {
  fun create(
    details: OAuthTokenEndpointDetails,
    requestTarget: String
  ): OAuth2AuthorizeRequest {
    val registration: ClientRegistration =
      ClientRegistration.withRegistrationId(requestTarget + " -" + details.clientId)
        .tokenUri(details.tokenEndpoint)
        .clientId(details.clientId)
        .clientSecret(details.secret)
        .scope(details.scope)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .build()
    repository.registerClient(registration)
    return OAuth2AuthorizeRequest.withClientRegistrationId(registration.registrationId)
      .principal("ces-internal-client")
      .build()
  }
}

