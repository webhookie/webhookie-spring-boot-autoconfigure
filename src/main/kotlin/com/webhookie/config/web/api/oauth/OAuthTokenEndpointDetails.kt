package com.webhookie.config.web.api.oauth

/**
 *
 * @author Arthur Kazemi<arthur.kazemi@coexservices.com.au>
 * @since 17/5/2023 12:04
 */
interface OAuthTokenEndpointDetails {
  val tokenEndpoint: String
  val clientId: String
  val secret: String
  val scope: String
}
