package com.webhookie.config.sensitive

import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
import com.webhookie.common.annotation.SensitiveData
import com.webhookie.common.crypto.CryptoDriver
import com.webhookie.common.extension.log

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 01:16
 */
class SensitiveDataAnnotationIntrospector(
  private val driver: CryptoDriver,
  private val cryptoProperties: CryptoProperties
): NopAnnotationIntrospector() {
  override fun findSerializer(am: Annotated): Any? {
    val dataAnnotation: SensitiveData? = am.getAnnotation(SensitiveData::class.java)
    return if (dataAnnotation != null) {
      log.info("Registering SensitiveDataSerializer instance for '{}'", am.rawType)
      SensitiveDataSerializer(cryptoProperties, driver)
    } else null
  }

  override fun findDeserializer(am: Annotated): Any? {
    val dataAnnotation: SensitiveData? = am.getAnnotation(SensitiveData::class.java)
    return if (dataAnnotation != null) {
      log.info("Registering SensitiveDataDeserializer instance for '{}'", am.rawType)
      SensitiveDataDeserializer(cryptoProperties, driver, am.rawType)
    } else if(isContainer(am)) {
      log.info("Registering SensitiveContainerDeserializer instance for '{}'", am.rawType)
      SensitiveContainerDeserializer(cryptoProperties, driver, am.rawType)
    } else {
      null
    }
  }

  private fun isContainer(am: Annotated): Boolean {
    val predicate: (Annotation) -> Boolean = { a -> a.annotationClass == SensitiveData::class }

    return am.rawType.declaredFields
      .any { f -> f.annotations.any(predicate) || f.type.annotations.any(predicate) }
  }

  companion object {
    private const val serialVersionUID = 1L
  }
}

