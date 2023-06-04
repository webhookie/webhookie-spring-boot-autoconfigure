package com.webhookie.config.web.api

import com.webhookie.config.web.ApplicationProperties
import com.webhookie.config.web.api.basic.BasicAuthExternalApiClientBeanRegistrar
import com.webhookie.config.web.api.oauth.TokenAuthorizedExternalApiClientBeanRegistrar
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 25/5/2023 16:36
 */
@Configuration
class BeanRegistrarConfig {
  @Bean
  fun propagateTokenExternalApiClientBeanRegistrar(props: ApplicationProperties): PropagateTokenExternalApiClientBeanRegistrar {
    return PropagateTokenExternalApiClientBeanRegistrar(props)
  }

  @Bean
  fun tokenAuthorizedExternalApiClientBeanRegistrar(props: ApplicationProperties): TokenAuthorizedExternalApiClientBeanRegistrar {
    return TokenAuthorizedExternalApiClientBeanRegistrar(props)
  }

  @Bean
  fun basicAuthExternalApiClientBeanRegistrar(props: ApplicationProperties): BasicAuthExternalApiClientBeanRegistrar {
    return BasicAuthExternalApiClientBeanRegistrar(props)
  }
}
