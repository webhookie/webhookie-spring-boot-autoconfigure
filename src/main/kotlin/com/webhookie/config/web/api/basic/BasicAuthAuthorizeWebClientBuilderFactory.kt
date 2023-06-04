package com.webhookie.config.web.api.basic

import com.webhookie.config.web.api.basic.BasicAuthEndpointDetails
import com.webhookie.config.web.api.oauth.OAuthExchangeFilterFunctions.Companion.oAuth2Authentication
import com.webhookie.config.web.api.oauth.OAuth2TokenAuthorizer
import com.webhookie.config.web.api.oauth.OAuthTokenEndpointDetails
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions
import java.util.function.Supplier
import org.springframework.web.reactive.function.client.WebClient

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 25/5/2023 15:59
 */
class BasicAuthAuthorizeWebClientBuilderFactory {
  @Suppress("unused")
  fun create(details: BasicAuthEndpointDetails): WebClient.Builder {
    return WebClient.builder()
      .filter(ExchangeFilterFunctions.basicAuthentication(details.username, details.password))
  }
}

