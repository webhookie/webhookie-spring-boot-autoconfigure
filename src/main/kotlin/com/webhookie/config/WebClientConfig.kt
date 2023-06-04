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

import com.webhookie.security.SecurityConfig
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Configuration


/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 3/8/21 15:56
 */
@Configuration
@AutoConfigureAfter(SecurityConfig::class)
class WebClientConfig {
/*
  @Bean
  @Scope("prototype")
  @ConditionalOnBean(WebhookieSecurityProperties::class)
  fun basicAuthWebClientBuilder(
//    reactorDeferringLoadBalancerExchangeFilterFunction: DeferringLoadBalancerExchangeFilterFunction<LoadBalancedExchangeFilterFunction>,
    webClientBuilder: WebClient.Builder,
    securityProperties: WebhookieSecurityProperties
  ): WebClient.Builder {
    val user = securityProperties.basic.user
    return webClientBuilder
      .filter(ExchangeFilterFunctions.basicAuthentication(user.username, user.password))
//      .filter(reactorDeferringLoadBalancerExchangeFilterFunction)
  }

  @Bean
  @Scope("prototype")
  fun oauth2WebClientBuilder(
//    reactorDeferringLoadBalancerExchangeFilterFunction: DeferringLoadBalancerExchangeFilterFunction<LoadBalancedExchangeFilterFunction>,
    webClientBuilder: WebClient.Builder,
  ): WebClient.Builder {
    return webClientBuilder
      .filter(authTokenExchangeFilterFunction())
//      .filter(reactorDeferringLoadBalancerExchangeFilterFunction)
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

  companion object {
    const val TOKEN_HEADER_KEY = "AUTHORIZATION_TOKEN_HEADER_KEY"
  }
*/
}
