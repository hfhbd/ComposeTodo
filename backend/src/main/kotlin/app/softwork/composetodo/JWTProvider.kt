package app.softwork.composetodo

import app.softwork.composetodo.dao.User
import app.softwork.composetodo.dto.*
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import com.auth0.jwt.impl.*
import io.ktor.server.auth.jwt.*
import kotlinx.datetime.*
import kotlin.time.*

data class JWTProvider(
    val algorithm: Algorithm,
    val issuer: String,
    val audience: String,
    val expireDuration: Duration
) {
    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    suspend fun validate(credential: JWTCredential, find: suspend (String) -> User?): User? =
        if (audience in credential.payload.audience) {
            credential.payload.subject?.let { username ->
                find(username)
            }
        } else {
            null
        }

    fun token(refreshToken: RefreshToken): Token {
        val now = Clock.System.now()
        return Token(
            Token.Payload(
                issuer = issuer,
                subject = refreshToken.value,
                expiredAt = now + expireDuration,
                notBefore = now,
                issuedAt = now,
                audience = audience
            ).build(algorithm)
        )
    }

    private fun Token.Payload.build(algorithm: Algorithm): String = JWT.create()
        .withIssuer(issuer)
        .withSubject(subject)
        .withExpiresAt(expiredAt)
        .withNotBefore(notBefore)
        .withIssuedAt(issuedAt)
        .withAudience(audience)
        .sign(algorithm)

    private fun JWTCreator.Builder.withIssuedAt(createdAt: Instant): JWTCreator.Builder =
        withClaim(PublicClaims.ISSUED_AT, createdAt.epochSeconds)

    private fun JWTCreator.Builder.withExpiresAt(expireAt: Instant): JWTCreator.Builder =
        withClaim(PublicClaims.EXPIRES_AT, expireAt.epochSeconds)

    private fun JWTCreator.Builder.withNotBefore(notBefore: Instant): JWTCreator.Builder =
        withClaim(PublicClaims.NOT_BEFORE, notBefore.epochSeconds)
}
