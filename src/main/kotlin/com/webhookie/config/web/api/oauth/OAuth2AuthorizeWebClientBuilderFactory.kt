package com.webhookie.config.web.api.oauth

import com.webhookie.config.web.api.oauth.OAuthExchangeFilterFunctions.Companion.oAuth2Authentication
import java.util.function.Supplier
import org.springframework.web.reactive.function.client.WebClient

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 25/5/2023 15:59
 */
class OAuth2AuthorizeWebClientBuilderFactory {
  @Suppress("unused")
  fun create(
    authorizerSupplier: Supplier<OAuth2TokenAuthorizer>,
    details: OAuthTokenEndpointDetails
  ): WebClient.Builder {
    return WebClient.builder()
      .filter(oAuth2Authentication(authorizerSupplier.get(), details))
  }
}

