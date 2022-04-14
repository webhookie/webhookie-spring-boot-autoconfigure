package com.webhookie.config.sensitive

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.webhookie.common.annotation.SensitiveData
import com.webhookie.common.crypto.CryptoDriver
import java.lang.reflect.Field

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 01:15
 */
class SensitiveContainerDeserializer(
  private val cryptoProperties: CryptoProperties,
  private val driver: CryptoDriver,
  private val clazz: Class<*>
) : StdDeserializer<Any>(clazz) {
  private val objectMapper = ObjectMapper().registerKotlinModule()

  override fun deserialize(jp: JsonParser, p1: DeserializationContext): Any {
    val node: JsonNode = jp.codec.readTree(jp)
    val predicate: (Annotation) -> Boolean = { a -> a.annotationClass == SensitiveData::class }
    val typeRef: TypeReference<MutableMap<String, Any>> = object : TypeReference<MutableMap<String, Any>>() {}

    val map: MutableMap<String, Any> = objectMapper.convertValue(node, typeRef)

    clazz.declaredFields
      .filter { f -> f.annotations.any(predicate) || f.type.annotations.any(predicate) }
      .forEach { map[it.name] = readField(node, it) }

    val v = objectMapper.writeValueAsString(map)
    return objectMapper.readValue(v, clazz)
  }

  private fun <T> readField(node: JsonNode, field: Field): T {
    val codedValue = node.get(field.name).textValue()
    @Suppress("UNCHECKED_CAST")
    val type = field.type as Class<T>
    return driver.ecbDecrypt(cryptoProperties.key, codedValue, type)
  }
}
