package com.webhookie.config.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 31/7/2022 23:50
 */
@Configuration
class LivenessConfig {
  @Bean
  fun aliveRouter() = router {
    this.GET("/alive") {
      ServerResponse.ok().bodyValue("I am alive!")
    }
  }
}
