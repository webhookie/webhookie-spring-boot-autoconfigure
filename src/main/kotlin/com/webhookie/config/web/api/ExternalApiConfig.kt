package com.webhookie.config.web.api

import com.webhookie.config.web.api.basic.BasicAuthAuthorizeWebClientBuilderFactory
import com.webhookie.config.web.api.oauth.OAuth2AuthorizeWebClientBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 25/5/2023 16:08
 */
@Configuration
class ExternalApiConfig {
  @Bean(OAUTH2_WEB_CLIENT_FACTORY_BEAN_NAME)
  fun oAuth2AuthorizeWebClientBuilderFactory(): OAuth2AuthorizeWebClientBuilderFactory {
    return OAuth2AuthorizeWebClientBuilderFactory()
  }

  @Bean(BASIC_AUTH_WEB_CLIENT_FACTORY_BEAN_NAME)
  fun basicAuthAuthorizeWebClientBuilderFactory(): BasicAuthAuthorizeWebClientBuilderFactory {
    return BasicAuthAuthorizeWebClientBuilderFactory()
  }

  @Bean(EXTERNAL_API_FACTORY_BEAN_NAME)
  fun externalApiFactory(): ExternalApiFactory {
    return ExternalApiFactory()
  }

  companion object {
    const val OAUTH2_WEB_CLIENT_FACTORY_BEAN_NAME = "oAuth2AuthorizeWebClientBuilderFactory"
    const val BASIC_AUTH_WEB_CLIENT_FACTORY_BEAN_NAME = "basicAuthAuthorizeWebClientBuilderFactory"
    const val EXTERNAL_API_FACTORY_BEAN_NAME = "externalApiFactory"
    const val OAUTH2_WEB_CLIENT_FACTORY_METHOD = "create"
    const val BASIC_AUTH_WEB_CLIENT_FACTORY_METHOD = "create"
    const val EXTERNAL_API_FACTORY_METHOD = "create"
  }
}
