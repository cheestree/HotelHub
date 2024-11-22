package com.cheese.hotelhub.domain.annotation

import com.cheese.hotelhub.domain.enums.Role

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RoleRoute(val role: Role)
