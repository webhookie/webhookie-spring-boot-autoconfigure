package com.webhookie.config.web.api.basic

import com.webhookie.config.web.ApplicationProperties
import com.webhookie.config.web.api.ExternalApiClientBeanRegistrar
import com.webhookie.config.web.api.ExternalApiConfig.Companion.BASIC_AUTH_WEB_CLIENT_FACTORY_BEAN_NAME
import com.webhookie.config.web.api.ExternalApiConfig.Companion.BASIC_AUTH_WEB_CLIENT_FACTORY_METHOD
import com.webhookie.config.web.api.ExternalApiProperties
import com.webhookie.config.web.api.oauth.OAuth2AuthorizeWebClientBuilderFactory
import com.webhookie.config.web.api.annotation.ApiClientWithBasicAuth
import java.lang.StringBuilder
import kotlin.reflect.KClass
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionBuilder

class BasicAuthExternalApiClientBeanRegistrar(
  props: ApplicationProperties
) : ExternalApiClientBeanRegistrar<ApiClientWithBasicAuth>(props) {
  override val aClass: Class<ApiClientWithBasicAuth>
    get() = ApiClientWithBasicAuth::class.java

  override fun initWebClientBuilder(clazz: Class<*>): String {
    val annotation = clazz.getAnnotation(aClass)
    val authDetailsClass = annotation.authDetailsClass.java
    val detailsBeanName = getSingleBeanNameForType(authDetailsClass)
    val webClientBuilderBeanName = generateWebClientBuilderBeanName(annotation, clazz)

    registerWebClientBuilder(detailsBeanName, webClientBuilderBeanName)

    return webClientBuilderBeanName
  }

  override fun findPropertiesClass(clazz: Class<*>): KClass<out ExternalApiProperties> {
    return clazz.getAnnotation(aClass).propertiesClass
  }

  private fun registerWebClientBuilder(detailsBeanName: String, webClientBuilderBeanName: String) {
    val wcb: BeanDefinition = BeanDefinitionBuilder
      .rootBeanDefinition(OAuth2AuthorizeWebClientBuilderFactory::class.java)
      .setFactoryMethodOnBean(BASIC_AUTH_WEB_CLIENT_FACTORY_METHOD, BASIC_AUTH_WEB_CLIENT_FACTORY_BEAN_NAME)
      .addDependsOn(detailsBeanName)
      .addConstructorArgReference(detailsBeanName)
      .setScope(BeanDefinition.SCOPE_PROTOTYPE)
      .beanDefinition

    register(webClientBuilderBeanName, wcb)
  }

  private fun generateWebClientBuilderBeanName(
    annotation: ApiClientWithBasicAuth,
    clazz: Class<*>
  ): String {
    return StringBuilder(annotation.authDetailsClass.simpleName)
      .append("-${clazz.name}")
      .append("-$WEB_CLIENT_BUILDER_BEAN_SUFFIX")
      .toString()
  }
}
