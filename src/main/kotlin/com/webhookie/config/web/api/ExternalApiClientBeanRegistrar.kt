package com.webhookie.config.web.api

import com.webhookie.config.web.ApplicationProperties
import com.webhookie.config.web.WebfluxConfig.Companion.OAUTH2_CALLBACK_AUTHORIZER_BEAN_NAME
import com.webhookie.config.web.api.ExternalApiConfig.Companion.EXTERNAL_API_FACTORY_BEAN_NAME
import com.webhookie.config.web.api.ExternalApiConfig.Companion.EXTERNAL_API_FACTORY_METHOD
import com.webhookie.config.web.api.oauth.OAuth2TokenAuthorizer
import java.util.function.Supplier
import kotlin.reflect.KClass
import org.reflections.Reflections
import org.springframework.beans.factory.BeanCreationException
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.web.reactive.function.client.WebClient

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 25/5/2023 16:21
 */
abstract class ExternalApiClientBeanRegistrar<A: Annotation>(props: ApplicationProperties): BeanFactoryPostProcessor {
  private val reflections = Reflections(props.basePackage)
  private lateinit var beanFactory: ConfigurableListableBeanFactory
  private lateinit var registry: DefaultListableBeanFactory

  override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
    this.beanFactory = beanFactory
    this.registry = beanFactory as DefaultListableBeanFactory

    reflections.getTypesAnnotatedWith(aClass)
      .forEach {
        createApiBeanDefinition(it)
      }
  }

  private fun createApiBeanDefinition(clazz: Class<*>) {
    val propertiesClass = findPropertiesClass(clazz)
    val webClientBuilderBeanName = initWebClientBuilder(clazz)

    val apiPropertiesBeanName = getSingleBeanNameForType(propertiesClass.java)
    val apiPropertiesSupplier = beanSupplier(apiPropertiesBeanName, propertiesClass.java)
    val webClientSupplier = beanSupplier(webClientBuilderBeanName, WebClient.Builder::class.java)

    val beanDefinition = BeanDefinitionBuilder
      .rootBeanDefinition(ExternalApiFactory::class.java)
      .setFactoryMethodOnBean(EXTERNAL_API_FACTORY_METHOD, EXTERNAL_API_FACTORY_BEAN_NAME)
      .addDependsOn(webClientBuilderBeanName)
      .addDependsOn(apiPropertiesBeanName)
      .addConstructorArgValue(apiPropertiesSupplier)
      .addConstructorArgValue(webClientSupplier)
      .addConstructorArgValue(clazz)
      .setScope(BeanDefinition.SCOPE_PROTOTYPE)
      .beanDefinition

    register("${clazz.name}-$EXTERNAL_API_BEAN_SUFFIX", beanDefinition)
  }

  private fun <T> beanSupplier(name: String, clazz: Class<T>): Supplier<T> {
    return Supplier {
      beanFactory.getBean(name, clazz)
    }
  }

  protected fun getSingleBeanNameForType(clazz: Class<*>): String {
    return beanFactory.getBeanNamesForType(clazz)
      .firstOrNull()
      ?: throw BeanCreationException("Unable to find bean of type: $clazz")
  }

  protected fun oAuth2TokenAuthorizerBeanSupplier(): Supplier<OAuth2TokenAuthorizer> {
    return beanSupplier(OAUTH2_CALLBACK_AUTHORIZER_BEAN_NAME, OAuth2TokenAuthorizer::class.java)
  }
  
  protected fun register(name: String, beanDefinition: BeanDefinition) {
    registry.registerBeanDefinition(name, beanDefinition)
  }

  abstract fun initWebClientBuilder(clazz: Class<*>): String
  abstract fun findPropertiesClass(clazz: Class<*>): KClass<out ExternalApiProperties>
  abstract val aClass: Class<A>

  companion object {
    const val WEB_CLIENT_BUILDER_BEAN_SUFFIX = "tokenAwareWebClientBuilder"
    const val EXTERNAL_API_BEAN_SUFFIX = "api"
  }
}

