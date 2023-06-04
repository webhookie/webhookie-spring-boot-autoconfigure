package com.webhookie.config.web

import com.webhookie.common.extension.log
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.WebFilter
import reactor.core.publisher.Mono

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 17/5/2023 11:58
 */
@Configuration
class WebfluxConfig {
  @Bean
  @Scope(SCOPE_PROTOTYPE)
  fun webClientBuilder(
    customizerProvider: ObjectProvider<WebClientCustomizer>,
    logRequest: ExchangeFilterFunction,
    logResponse: ExchangeFilterFunction,
  ): WebClient.Builder {
    val builder = WebClient.builder()
    customizerProvider.orderedStream().forEach { customizer: WebClientCustomizer ->
      customizer.customize(builder)
    }

    if (log.isDebugEnabled) {
      builder.filter(logRequest)
      builder.filter(logResponse)
    }
    return builder
  }

  @Bean(PROPAGATE_TOKEN_WEB_CLIENT_BEAN_NAME)
  @Scope(SCOPE_PROTOTYPE)
  fun propagateAuthTokenWebClientBuilder(
    webClientBuilder: WebClient.Builder,
    propagateAuthTokenExchangeFilterFunction: ExchangeFilterFunction
  ): WebClient.Builder {
    return webClientBuilder
      .filter(propagateAuthTokenExchangeFilterFunction)
  }

  @Bean
  fun propagateAuthTokenExchangeFilterFunction(): ExchangeFilterFunction {
    return ExchangeFilterFunction { request, next ->
      Mono.deferContextual { Mono.just(it) }
        .flatMap { ctx ->
          if (ctx.hasKey(TOKEN_HEADER_KEY)) {
            log.debug("adding AUTHORIZATION header to the downstream request....")
            val newRequest = ClientRequest
              .from(request)
              .header(HttpHeaders.AUTHORIZATION, ctx[TOKEN_HEADER_KEY])
              .build()
            next.exchange(newRequest)
          } else {
            next.exchange(request)
          }
        }
    }
  }

  @Bean
  fun authTokenWebFilter(): WebFilter {
    return WebFilter { exchange, chain ->
      chain.filter(exchange)
        .contextWrite { context ->
          val token = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
          if (token != null) {
            @Suppress("UnnecessaryVariable")
            val ctx = context.put(TOKEN_HEADER_KEY, token)

            return@contextWrite ctx
          }

          return@contextWrite context
        }
    }
  }

  @Bean
  fun logRequest(): ExchangeFilterFunction {
    return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
      log.info("Request: {} {}", clientRequest.method(), clientRequest.url())
      clientRequest.headers()
        .filterKeys { it != null }
        .filterValues { it != null && it.isNotEmpty() }
        .filter { it.key != HttpHeaders.AUTHORIZATION }
        .forEach { ( name: String, values: List<String>) ->
          log.info("Header: {}={}", name, values)
        }
      Mono.just(clientRequest)
    }
  }

  @Bean
  fun logResponse(): ExchangeFilterFunction {
    return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
      log.info("Response: {}", clientResponse.headers().asHttpHeaders()["property-header"])
      Mono.just(clientResponse)
    }
  }


  companion object {
    const val TOKEN_HEADER_KEY = "CES_AUTHORIZATION_TOKEN_HEADER_KEY"
    const val PROPAGATE_TOKEN_WEB_CLIENT_BEAN_NAME = "propagateAuthTokenWebClientBuilder"
    const val OAUTH2_CALLBACK_AUTHORIZER_BEAN_NAME = "oAuth2CallbackAuthorizer"
  }
}
