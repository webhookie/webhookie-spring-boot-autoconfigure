package com.webhookie.config.web.api.annotation

import com.webhookie.config.web.api.ExternalApiProperties
import kotlin.reflect.KClass

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 25/5/2023 16:24
 */
@Suppress("unused")
@Target(AnnotationTarget.CLASS)
@Retention
annotation class ApiClientWithAuthPropagation(
  val propertiesClass: KClass<out ExternalApiProperties>
)
