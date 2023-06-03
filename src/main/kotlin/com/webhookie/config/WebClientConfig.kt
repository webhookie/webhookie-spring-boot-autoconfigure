/*
 * webhookie - webhook infrastructure that can be incorporated into any microservice or integration architecture.
 * Copyright (C) 2021 Hookie Solutions AB, info@hookiesolutions.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * If your software can interact with users remotely through a computer network, you should also make sure that it provides a way for users to get its source. For example, if your program is a web application, its interface could display a "Source" link that leads users to an archive of the code. There are many ways you could offer source, and different solutions will be better for different programs; see section 13 for the specific requirements.
 *
 * You should also get your employer (if you work as a programmer) or school, if any, to sign a "copyright disclaimer" for the program, if necessary. For more information on this, and how to apply and follow the GNU AGPL, see <https://www.gnu.org/licenses/>.
 */

package com.webhookie.config

import com.webhookie.common.Constants
import com.webhookie.common.properties.WebhookieSecurityProperties
import com.webhookie.security.SecurityConfig
import com.webhookie.common.extension.log
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.server.WebFilter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import reactor.core.publisher.Mono


/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 3/8/21 15:56
 */
@Configuration
@AutoConfigureAfter(SecurityConfig::class)
class WebClientConfig {
  @Bean
  @Scope("prototype")
  @ConditionalOnMissingBean(WebClient.Builder::class)
  fun webClientBuilder(
    customizerProvider: ObjectProvider<WebClientCustomizer>,
/*
    reactorDeferringLoadBalancerExchangeFilterFunction: DeferringLoadBalancerExchangeFilterFunction<LoadBalancedExchangeFilterFunction>
*/
  ): WebClient.Builder {
    val builder = WebClient.builder()
    customizerProvider.orderedStream().forEach { customizer: WebClientCustomizer ->
      customizer.customize(
        builder
      )
    }
    return builder
//      .filter(reactorDeferringLoadBalancerExchangeFilterFunction)
  }

  @Bean
  @Scope("prototype")
  @ConditionalOnBean(WebhookieSecurityProperties::class)
  fun basicAuthWebClientBuilder(
    reactorDeferringLoadBalancerExchangeFilterFunction: DeferringLoadBalancerExchangeFilterFunction<LoadBalancedExchangeFilterFunction>,
    webClientBuilder: WebClient.Builder,
    securityProperties: WebhookieSecurityProperties
  ): WebClient.Builder {
    val user = securityProperties.basic.user
    return webClientBuilder
      .filter(ExchangeFilterFunctions.basicAuthentication(user.username, user.password))
      .filter(reactorDeferringLoadBalancerExchangeFilterFunction)
  }

  @Bean
  @Scope("prototype")
  fun oauth2WebClientBuilder(
    reactorDeferringLoadBalancerExchangeFilterFunction: DeferringLoadBalancerExchangeFilterFunction<LoadBalancedExchangeFilterFunction>,
    webClientBuilder: WebClient.Builder,
  ): WebClient.Builder {
    return webClientBuilder
      .filter(authTokenExchangeFilterFunction())
      .filter(reactorDeferringLoadBalancerExchangeFilterFunction)
  }

  fun authTokenExchangeFilterFunction() : ExchangeFilterFunction {
    return ExchangeFilterFunction { request, next ->
      Mono.deferContextual {
        Mono.just(it)
      }
        .flatMap{ ctx ->
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
  fun authTokenWebFilter() : WebFilter {
    return WebFilter { exchange, chain ->
      chain.filter(exchange)
        .contextWrite { context ->
          val token = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
          if(token != null) {
            @Suppress("UnnecessaryVariable")
            val ctx = context.put(TOKEN_HEADER_KEY, token)

            return@contextWrite ctx
          }

          return@contextWrite context
        }
    }
  }

  class Intercom {
    companion object {
      fun hmacSigner(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createIntercomHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.HMAC_SIGNER
        )
      }

      fun oauth2Signer(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createIntercomHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.OAUTH2_SIGNER
        )
      }

      fun trafficService(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createIntercomHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.TRAFFIC_SERVICE
        )
      }

      fun adminService(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createIntercomHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.ADMIN_SERVICE
        )
      }
    }
  }

  class OAuth {
    companion object {
      fun hmacSigner(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.HMAC_SIGNER
        )
      }

      fun oauth2Signer(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.OAUTH2_SIGNER
        )
      }

      fun trafficService(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.TRAFFIC_SERVICE
        )
      }

      fun transformationService(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.TRANSFORMATION_SERVICE
        )
      }

      fun webhookApiRepo(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.WEBHOOK_REPO
        )
      }

      fun adminService(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.ADMIN_SERVICE
        )
      }

      fun profileService(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.PROFILE_SERVICE
        )
      }

      fun subscriptionService(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.SUBSCRIPTION_SERVICE
        )
      }

      fun apiIngress(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.API_INGRESS
        )
      }

      fun httpPublisher(webClientBuilder: WebClient.Builder): HttpServiceProxyFactory {
        return createHttpServiceProxyFactoryFor(
          webClientBuilder,
          Constants.Services.HTTP_PUBLISHER
        )
      }
    }
  }

  companion object {
    const val TOKEN_HEADER_KEY = "AUTHORIZATION_TOKEN_HEADER_KEY"

    fun createHttpServiceProxyFactoryFor(
      webClientBuilder: WebClient.Builder,
      baseUrl: String,
    ): HttpServiceProxyFactory {
      val webClient = webClientBuilder.baseUrl(baseUrl).build()
      val webClientAdapter = WebClientAdapter.forClient(webClient)

      return HttpServiceProxyFactory
        .builder()
        .clientAdapter(webClientAdapter)
        .build()
    }

    fun createIntercomHttpServiceProxyFactoryFor(
      webClientBuilder: WebClient.Builder,
      baseUrl: String,
    ): HttpServiceProxyFactory {
      return createHttpServiceProxyFactoryFor(webClientBuilder, "${baseUrl}${Constants.Intercom.INTERCOM_PATH}")
    }
  }
}
