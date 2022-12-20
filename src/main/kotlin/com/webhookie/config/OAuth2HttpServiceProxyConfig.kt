package com.webhookie.config

import com.webhookie.common.Constants
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.service.invoker.HttpServiceProxyFactory

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 20/12/2022 16:36
 */
@Configuration
class OAuth2HttpServiceProxyConfig(
  private val oauth2WebClientBuilder: WebClient.Builder
) {
  @Bean
  fun adminServiceProxyFactory(): HttpServiceProxyFactory {
    return createHttpServiceProxyFactoryFor(Constants.Services.ADMIN_SERVICE)
  }

  @Bean
  fun apiIngressProxyFactory(): HttpServiceProxyFactory {
    return createHttpServiceProxyFactoryFor(Constants.Services.API_INGRESS)
  }

  @Bean
  fun httpPublisherProxyFactory(): HttpServiceProxyFactory {
    return createHttpServiceProxyFactoryFor(Constants.Services.HTTP_PUBLISHER)
  }

  @Bean
  fun profileServiceProxyFactory(): HttpServiceProxyFactory {
    return createHttpServiceProxyFactoryFor(Constants.Services.PROFILE_SERVICE)
  }

  @Bean
  fun subscriptionServiceProxyFactory(): HttpServiceProxyFactory {
    return createHttpServiceProxyFactoryFor(Constants.Services.SUBSCRIPTION_SERVICE)
  }

  @Bean
  fun trafficServiceProxyFactory(): HttpServiceProxyFactory {
    return createHttpServiceProxyFactoryFor(Constants.Services.TRAFFIC_SERVICE)
  }

  @Bean
  fun transformationServiceProxyFactory(): HttpServiceProxyFactory {
    return createHttpServiceProxyFactoryFor(Constants.Services.TRANSFORMATION_SERVICE)
  }

  @Bean
  fun webhookApiRepoProxyFactory(): HttpServiceProxyFactory {
    return createHttpServiceProxyFactoryFor(Constants.Services.WEBHOOK_REPO)
  }

  private fun createHttpServiceProxyFactoryFor(baseUrl: String): HttpServiceProxyFactory {
    return WebClientConfig.createHttpServiceProxyFactoryFor(
      oauth2WebClientBuilder,
      baseUrl
    )
  }
}
