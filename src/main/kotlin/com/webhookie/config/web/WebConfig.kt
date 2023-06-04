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

package com.webhookie.config.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.webhookie.common.service.TimeMachine
import com.webhookie.common.extension.log
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import reactor.core.publisher.Mono
import java.time.Instant


/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 17/1/21 13:58
 */
@Configuration
@EnableWebFlux
@ConditionalOnWebApplication
@ConditionalOnBean(ObjectMapper::class)
@EnableConfigurationProperties(ApplicationProperties::class)
class WebConfig(
  private val objectMapper: ObjectMapper
): WebFluxConfigurer {
  override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
    val cc = configurer
      .customCodecs()

    cc.register(Jackson2JsonDecoder(objectMapper))
    cc.register(Jackson2JsonEncoder(objectMapper))
  }

  @Bean
  @Order(-1)
  @Profile(value = ["!prod"])
  fun logGatewayRequest(
    timeMachine: TimeMachine
  ): WebFilter {
    return WebFilter { exchange, chain ->
      val filterMono = chain.filter(exchange)
      return@WebFilter if (log.isDebugEnabled) {
        filterMono
          .then(debugRequestResponse(exchange, timeMachine.now(), timeMachine))
      } else {
        filterMono
      }
    }
  }

  private fun debugRequestResponse(
    exchange: ServerWebExchange,
    start: Instant,
    timeMachine: TimeMachine,
  ): Mono<Void> = Mono.fromRunnable {
    val finish = timeMachine.now()
    val request = exchange.request
    val response = exchange.response
    if (log.isDebugEnabled) {
      log.debug(
        "'{} {} {}' {} {} ms",
        request.method.name(),
        request.uri,
        request.id,
        response.statusCode?.value(),
        finish.toEpochMilli() - start.toEpochMilli()
      )
    }
    if (log.isTraceEnabled) {
      val requestHeaders = request.headers
        .mapValues {
          val value = if (!it.key.contentEquals(HttpHeaders.AUTHORIZATION, ignoreCase = true)) {
            it.value.toString()
          } else {
            "*****"
          }

          value
        }
      log.trace("Request headers: {}", requestHeaders)
      log.trace("Response headers: {}", response.headers)
    }
  }
}
