package com.webhookie.config.web.api.annotation

import com.webhookie.config.web.api.ExternalApiProperties
import com.webhookie.config.web.api.oauth.OAuthTokenEndpointDetails
import kotlin.reflect.KClass

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 25/5/2023 16:24
 */
@Target(AnnotationTarget.CLASS)
@Retention
annotation class ApiClientWithAuthorization(
  val tokenEndpointDetailsClass: KClass<out OAuthTokenEndpointDetails>,
  val propertiesClass: KClass<out ExternalApiProperties>
)
