package com.webhookie.config.web.api.oauth

import java.util.concurrent.ConcurrentHashMap
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import reactor.core.publisher.Mono

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 17/5/2023 12:04
 */
class MutableReactiveClientRegistrationRepository : ReactiveClientRegistrationRepository {
  private val clientIdToClientRegistration: MutableMap<String, ClientRegistration> =
    ConcurrentHashMap()

  override fun findByRegistrationId(registrationId: String): Mono<ClientRegistration> {
    return Mono.justOrEmpty(clientIdToClientRegistration[registrationId])
  }

  fun registerClient(registration: ClientRegistration) {
    this.clientIdToClientRegistration[registration.registrationId] = registration
  }
}
