package com.webhookie.config.web

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 28/2/2023 23:31
 */
@ConfigurationProperties(prefix = "webhookie.service")
data class ApplicationProperties(
  val basePackage: String = "com.webhookie"
)
