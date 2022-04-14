package com.webhookie.config

import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.webhookie.common.crypto.CryptoDriver
import com.webhookie.config.sensitive.CryptoProperties
import com.webhookie.config.sensitive.SensitiveDataAnnotationIntrospector
import org.slf4j.Logger
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@ConditionalOnProperty(prefix = "webhookie.crypto", value = ["key"], matchIfMissing = false)
@Configuration
@AutoConfigureBefore(JacksonAutoConfiguration::class)
@EnableConfigurationProperties(CryptoProperties::class)
class CryptoConfig {
  @Bean
  fun cryptoDriver() : CryptoDriver = CryptoDriver()

  @Bean
  fun sensitiveDataAnnotationIntrospector(
    log: Logger,
    cryptoProperties: CryptoProperties,
    driver: CryptoDriver,
  ): SensitiveDataAnnotationIntrospector {
    return SensitiveDataAnnotationIntrospector(log, driver, cryptoProperties)
  }


  @Bean
  @Primary
  fun objectMapper(
    builder: Jackson2ObjectMapperBuilder,
    introspector: SensitiveDataAnnotationIntrospector
  ): ObjectMapper {
    val objectMapper = builder
      .build<ObjectMapper>()

    val is1 = AnnotationIntrospector.pair(
      objectMapper.serializationConfig.annotationIntrospector, introspector
    )
    val is2 = AnnotationIntrospector.pair(
      objectMapper.deserializationConfig.annotationIntrospector, introspector
    )
    objectMapper.setAnnotationIntrospectors(is1, is2)

    return objectMapper.registerKotlinModule()
  }
}
