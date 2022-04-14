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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer
import com.webhookie.common.service.IdGenerator
import com.webhookie.common.service.ReactiveObjectMapper
import com.webhookie.common.service.TimeMachine
import com.webhookie.config.health.WebhookieHealthIndicator
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.Clock


/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 11/6/21 15:28
 */
@Configuration
class CommonBeans {
  @Bean
  fun jsonCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
    return Jackson2ObjectMapperBuilderCustomizer { builder: Jackson2ObjectMapperBuilder ->
      builder.serializationInclusion(JsonInclude.Include.NON_NULL)
        .serializers(InstantSerializer.INSTANCE)
        .deserializers(InstantDeserializer.INSTANT)
    }
  }

  @Bean
  @ConditionalOnMissingBean(Clock::class)
  fun clock(): Clock = Clock.systemUTC()

  @Bean
  fun timeMachine(clock: Clock): TimeMachine = TimeMachine(clock)

  @Bean
  fun idGenerator(): IdGenerator = IdGenerator()

  @Bean
  @ConditionalOnBean(ObjectMapper::class)
  @ConditionalOnMissingBean(ReactiveObjectMapper::class)
  fun reactiveObjectMapper(objectMapper: ObjectMapper): ReactiveObjectMapper
    = ReactiveObjectMapper(objectMapper)

  @Bean
  @ConditionalOnBean(BuildProperties::class)
  fun webhookieHealthIndicator(bp: BuildProperties): WebhookieHealthIndicator =
    WebhookieHealthIndicator(bp)

/*
  @Bean
  @ConditionalOnProperty(prefix = "webhookie-messaging", value = ["enabled"], havingValue = "true", matchIfMissing = false)
  fun retryableErrorSelector(): GenericPublisherMessageSelector {
    return GenericPublisherMessageSelector()
  }
*/
}

/*
interface GenericMessageSelector<T> {
  fun accept(source: T): Boolean
}

class GenericPublisherMessageSelector: GenericMessageSelector<GenericPublisherMessage> {
  override fun accept(source: GenericPublisherMessage): Boolean {
    return source is PublisherRequestErrorMessage || (
        source is PublisherResponseErrorMessage && (
            source.response.is5xxServerError() || source.response.isNotFound() || source.response.isUnauthorized()
            )
        )
  }
}
*/
