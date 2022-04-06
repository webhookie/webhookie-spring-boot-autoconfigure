package com.webhookie.config.annotation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.config.EnableIntegrationManagement
import java.lang.annotation.Inherited

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 20:07
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@SpringBootApplication
@EnableCaching
@EnableIntegration
@EnableIntegrationManagement
@ConfigurationPropertiesScan
@EnableDiscoveryClient
annotation class WebhookieServiceApplication
