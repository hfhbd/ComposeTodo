package app.softwork.composetodo

import app.softwork.composetodo.controller.*
import app.softwork.composetodo.dao.*
import app.softwork.composetodo.dao.User
import app.softwork.composetodo.dto.*
import com.auth0.jwt.*
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.*
import com.auth0.jwt.impl.*
import com.auth0.jwt.interfaces.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import kotlinx.datetime.*
import kotlinx.datetime.Clock
import kotlinx.uuid.*
import kotlin.time.*

@ExperimentalTime
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

    suspend fun validate(credential: JWTCredential): User? =
        if (audience in credential.payload.audience) {
            credential.payload.subject.toUUIDOrNull()?.let { userID ->
                UserController.find(userID)
            }
        } else null

    fun token(user: User): Token {
        val now = Clock.System.now()
        return Token(Token.Payload(
            issuer = issuer,
            subject = user.id.value,
            expiredAt = now + expireDuration,
            notBefore = now,
            issuedAt = now,
            audience = audience
        ).build(algorithm))
    }


    fun Token.Payload.build(algorithm: Algorithm): String = JWT.create()
        .withIssuer(issuer)
        .withSubject(subject.toString())
        .withExpiresAt(expiredAt)
        .withNotBefore(notBefore)
        .withIssuedAt(issuedAt)
        .withAudience(audience)
        .sign(algorithm)

    fun JWTCreator.Builder.withIssuedAt(createdAt: Instant) =
        withClaim(PublicClaims.ISSUED_AT, createdAt.epochSeconds)

    fun JWTCreator.Builder.withExpiresAt(expireAt: Instant) =
        withClaim(PublicClaims.EXPIRES_AT, expireAt.epochSeconds)

    fun JWTCreator.Builder.withNotBefore(notBefore: Instant) =
        withClaim(PublicClaims.NOT_BEFORE, notBefore.epochSeconds)
}
