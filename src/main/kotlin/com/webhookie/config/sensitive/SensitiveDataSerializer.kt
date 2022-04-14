package com.webhookie.config.sensitive

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.webhookie.common.crypto.CryptoDriver

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 01:14
 */
class SensitiveDataSerializer(
  private val cryptoProperties: CryptoProperties,
  private val driver: CryptoDriver
) : StdSerializer<Any>(Any::class.java) {
  override fun serialize(value: Any, gen: JsonGenerator, provider: SerializerProvider) {
    val string = driver.ecbEncryptToString(cryptoProperties.key, value)
    gen.writeString(string)
  }
}

