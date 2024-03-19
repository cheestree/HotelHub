package org.cheese.hotelhubserver.repository.jdbi

import org.cheese.hotelhubserver.repository.jdbi.mappers.InstantMapper
import org.cheese.hotelhubserver.repository.jdbi.mappers.PasswordValidationInfoMapper
import org.cheese.hotelhubserver.repository.jdbi.mappers.TokenValidationInfoMapper
import org.cheese.hotelhubserver.repository.jdbi.mappers.CritiqueMapper
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin

fun Jdbi.configureWithAppRequirements(): Jdbi {
    installPlugin(KotlinPlugin())
    installPlugin(PostgresPlugin())

    registerColumnMapper(PasswordValidationInfoMapper())
    registerColumnMapper(TokenValidationInfoMapper())
    registerColumnMapper(InstantMapper())
    registerColumnMapper(CritiqueMapper())

    return this
}
