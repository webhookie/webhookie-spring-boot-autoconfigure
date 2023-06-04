package com.webhookie.config.web.api

import com.webhookie.config.web.ApplicationProperties
import com.webhookie.config.web.WebfluxConfig.Companion.PROPAGATE_TOKEN_WEB_CLIENT_BEAN_NAME
import com.webhookie.config.web.api.annotation.ApiClientWithAuthPropagation
import kotlin.reflect.KClass

class PropagateTokenExternalApiClientBeanRegistrar(
  props: ApplicationProperties
) : ExternalApiClientBeanRegistrar<ApiClientWithAuthPropagation>(props) {
  override fun initWebClientBuilder(clazz: Class<*>): String {
    return PROPAGATE_TOKEN_WEB_CLIENT_BEAN_NAME
  }

  override fun findPropertiesClass(clazz: Class<*>): KClass<out ExternalApiProperties> {
    return clazz.getAnnotation(aClass).propertiesClass
  }

  override val aClass: Class<ApiClientWithAuthPropagation>
    get() = ApiClientWithAuthPropagation::class.java
}
