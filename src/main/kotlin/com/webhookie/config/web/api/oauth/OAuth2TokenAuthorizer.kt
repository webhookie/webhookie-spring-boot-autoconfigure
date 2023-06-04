package com.webhookie.config.web.api.oauth

import com.webhookie.common.extension.log
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import reactor.core.publisher.Mono

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 17/5/2023 12:10
 */
class OAuth2TokenAuthorizer(
  private val manager: ReactiveOAuth2AuthorizedClientManager,
  private val authorizeRequestFactory: AuthorizeRequestFactory
) {
  fun authorize(details: OAuthTokenEndpointDetails): Mono<String> {
    val requestTarget = details.scope
    log.info("Authorizing for '{}' against '{}'", requestTarget, details.tokenEndpoint)
    val authorizeRequest = authorizeRequestFactory.create(details, requestTarget)
    return manager
      .authorize(authorizeRequest)
      .map { "Bearer " + it.accessToken.tokenValue }
      .doOnError { log.error("OAuth2TokenAuthorizer failed to auth: '{}'", it.localizedMessage) }
  }
}

