package com.webhookie.config.sensitive

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.webhookie.common.crypto.CryptoDriver
import com.webhookie.common.crypto.utils.CryptoUtils
import java.util.*

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 01:15
 */
class SensitiveDataDeserializer(
  private val cryptoProperties: CryptoProperties,
  private val driver: CryptoDriver,
  private val clazz: Class<*>
) : StdDeserializer<Any>(clazz) {
  override fun deserialize(jp: JsonParser, p1: DeserializationContext): Any {
    val objectMapper = ObjectMapper()
    val key = CryptoUtils.convertStringToSecretKey(cryptoProperties.key)
    val node: JsonNode = jp.codec.readTree(jp)


    val ciphertext: ByteArray = Base64.getDecoder().decode(node.textValue())
    val decryptedtext: ByteArray = driver.ecbDecrypt(key, ciphertext)
    val value = String(decryptedtext)

    return try {
      objectMapper.readValue(value, clazz)
    } catch (_: Exception) {
      value
    }
  }
}
