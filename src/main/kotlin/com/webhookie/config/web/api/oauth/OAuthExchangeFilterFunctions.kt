package com.webhookie.config.web.api.oauth

import com.webhookie.config.web.api.oauth.OAuth2TokenAuthorizer
import com.webhookie.config.web.api.oauth.OAuthTokenEndpointDetails
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 17/5/2023 12:28
 */
class OAuthExchangeFilterFunctions {
  companion object {
    fun oAuth2Authentication(
      authorizer: OAuth2TokenAuthorizer,
      details: OAuthTokenEndpointDetails
    ): ExchangeFilterFunction {
      return ExchangeFilterFunction { request, next ->
        authorizer.authorize(details)
          .flatMap { token ->
            val newRequest = ClientRequest
              .from(request)
              .header(HttpHeaders.AUTHORIZATION, token)
              .build()
            next.exchange(newRequest)
          }
          .onErrorResume { next.exchange(request) }
      }
    }
  }
}
