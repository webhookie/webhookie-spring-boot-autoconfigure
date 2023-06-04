package com.webhookie.config.web.api.annotation

import com.webhookie.config.web.api.ExternalApiProperties
import com.webhookie.config.web.api.basic.BasicAuthEndpointDetails
import kotlin.reflect.KClass

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 25/5/2023 16:24
 */
@Suppress("unused")
@Target(AnnotationTarget.CLASS)
@Retention
annotation class ApiClientWithBasicAuth(
  val authDetailsClass: KClass<out BasicAuthEndpointDetails>,
  val propertiesClass: KClass<out ExternalApiProperties>
)
