package com.sleepyhead.memo.repository

import com.sleepyhead.memo.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, Long> {

}