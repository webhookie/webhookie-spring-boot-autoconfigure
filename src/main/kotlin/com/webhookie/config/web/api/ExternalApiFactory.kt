package com.webhookie.config.web.api

import java.util.function.Supplier
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 25/5/2023 16:10
 */
class ExternalApiFactory {
  @Suppress("unused")
  fun <T> create(
    apiPropertiesSupplier: Supplier<out ExternalApiProperties>,
    webClientBuilderSupplier: Supplier<WebClient.Builder>,
    clazz: Class<T>
  ): T {
    val apiProperties = apiPropertiesSupplier.get()

    val client = webClientBuilderSupplier.get()
      .baseUrl(apiProperties.baseUrl)
      .build()
    val factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build()
    return factory.createClient(clazz)
  }
}
