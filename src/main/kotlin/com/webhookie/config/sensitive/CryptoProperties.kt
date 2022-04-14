package com.webhookie.config.sensitive

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 14/4/2022 14:52
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "webhookie.crypto")
data class CryptoProperties(
  val key: String
)
