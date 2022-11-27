package com.webhookie.config.sensitive

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 14/4/2022 14:52
 */
@ConfigurationProperties(prefix = "webhookie.crypto")
data class CryptoProperties(
  val key: String
)
