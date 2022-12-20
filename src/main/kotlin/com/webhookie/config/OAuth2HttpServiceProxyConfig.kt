package com.webhookie.config

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
    return WebClientConfig.OAuth.adminService(oauth2WebClientBuilder)
  }

  @Bean
  fun apiIngressProxyFactory(): HttpServiceProxyFactory {
    return WebClientConfig.OAuth.apiIngress(oauth2WebClientBuilder)
  }

  @Bean
  fun httpPublisherProxyFactory(): HttpServiceProxyFactory {
    return WebClientConfig.OAuth.httpPublisher(oauth2WebClientBuilder)
  }

  @Bean
  fun profileServiceProxyFactory(): HttpServiceProxyFactory {
    return WebClientConfig.OAuth.profileService(oauth2WebClientBuilder)
  }

  @Bean
  fun subscriptionServiceProxyFactory(): HttpServiceProxyFactory {
    return WebClientConfig.OAuth.subscriptionService(oauth2WebClientBuilder)
  }

  @Bean
  fun trafficServiceProxyFactory(): HttpServiceProxyFactory {
    return WebClientConfig.OAuth.trafficService(oauth2WebClientBuilder)
  }

  @Bean
  fun transformationServiceProxyFactory(): HttpServiceProxyFactory {
    return WebClientConfig.OAuth.transformationService(oauth2WebClientBuilder)
  }

  @Bean
  fun webhookApiRepoProxyFactory(): HttpServiceProxyFactory {
    return WebClientConfig.OAuth.webhookApiRepo(oauth2WebClientBuilder)
  }

  @Bean
  fun hmacSignerRepoProxyFactory(): HttpServiceProxyFactory {
    return WebClientConfig.OAuth.hmacSigner(oauth2WebClientBuilder)
  }

  @Bean
  fun oauth2SignerRepoProxyFactory(): HttpServiceProxyFactory {
    return WebClientConfig.OAuth.oauth2Signer(oauth2WebClientBuilder)
  }
}
