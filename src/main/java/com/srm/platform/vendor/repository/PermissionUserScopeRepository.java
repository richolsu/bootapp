package com.srm.platform.vendor.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.srm.platform.vendor.model.PermissionUserScope;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PermissionUserScopeRepository extends JpaRepository<PermissionUserScope, Long> {
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "delete from permission_user_scope where account_id=?1", nativeQuery = true)
	void deleteByAccountId(Long accountId);
}