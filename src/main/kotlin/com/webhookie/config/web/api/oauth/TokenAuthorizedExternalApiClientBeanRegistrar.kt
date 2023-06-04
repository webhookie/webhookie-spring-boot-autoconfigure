package com.webhookie.config.web.api.oauth

import com.webhookie.config.web.ApplicationProperties
import com.webhookie.config.web.api.ExternalApiClientBeanRegistrar
import com.webhookie.config.web.api.ExternalApiConfig.Companion.OAUTH2_WEB_CLIENT_FACTORY_BEAN_NAME
import com.webhookie.config.web.api.ExternalApiConfig.Companion.OAUTH2_WEB_CLIENT_FACTORY_METHOD
import com.webhookie.config.web.api.ExternalApiProperties
import com.webhookie.config.web.api.annotation.ApiClientWithAuthorization
import java.lang.StringBuilder
import kotlin.reflect.KClass
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionBuilder

class TokenAuthorizedExternalApiClientBeanRegistrar(
  props: ApplicationProperties
) : ExternalApiClientBeanRegistrar<ApiClientWithAuthorization>(props) {
  override val aClass: Class<ApiClientWithAuthorization>
    get() = ApiClientWithAuthorization::class.java

  override fun initWebClientBuilder(clazz: Class<*>): String {
    val annotation = clazz.getAnnotation(aClass)
    val tokenEndpointDetailsClass = annotation.tokenEndpointDetailsClass.java
    val detailsBeanName = getSingleBeanNameForType(tokenEndpointDetailsClass)
    val webClientBuilderBeanName = generateWebClientBuilderBeanName(annotation, clazz)

    registerWebClientBuilder(detailsBeanName, webClientBuilderBeanName)

    return webClientBuilderBeanName
  }

  override fun findPropertiesClass(clazz: Class<*>): KClass<out ExternalApiProperties> {
    return clazz.getAnnotation(aClass).propertiesClass
  }

  private fun registerWebClientBuilder(detailsBeanName: String, webClientBuilderBeanName: String) {
    val authorizerSupplier = oAuth2TokenAuthorizerBeanSupplier()

    val wcb: BeanDefinition = BeanDefinitionBuilder
      .rootBeanDefinition(OAuth2AuthorizeWebClientBuilderFactory::class.java)
      .setFactoryMethodOnBean(OAUTH2_WEB_CLIENT_FACTORY_METHOD, OAUTH2_WEB_CLIENT_FACTORY_BEAN_NAME)
      .addDependsOn(detailsBeanName)
      .addConstructorArgValue(authorizerSupplier)
      .addConstructorArgReference(detailsBeanName)
      .setScope(BeanDefinition.SCOPE_PROTOTYPE)
      .beanDefinition

    register(webClientBuilderBeanName, wcb)
  }

  private fun generateWebClientBuilderBeanName(
    annotation: ApiClientWithAuthorization,
    clazz: Class<*>
  ): String {
    return StringBuilder(annotation.tokenEndpointDetailsClass.simpleName)
      .append("-${clazz.name}")
      .append("-$WEB_CLIENT_BUILDER_BEAN_SUFFIX")
      .toString()
  }
}
