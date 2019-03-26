package com.srm.platform.vendor.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.srm.platform.vendor.model.Account;
import com.srm.platform.vendor.searchitem.AccountSearchItem;
import com.srm.platform.vendor.utility.Constants;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findOneByUsername(String username);

	Account findOneByEmail(String email);

	Account findOneByMobile(String mobile);

	Account findOneByWeixin(String weixin);

	Account findOneById(Long id);

	@Query(value = "SELECT t.*, u.name unitname FROM account t left join unit u on t.unit_id=u.id left join vendor v on t.vendor_code=v.code WHERE "
			+ "u.name LIKE %?1% or t.username LIKE %?1% or t.realname LIKE %?1% or t.duty LIKE %?1% or t.email LIKE %?1%", countQuery = "SELECT count(t.id) FROM account t left join unit u on t.unit_id=u.id left join vendor v on t.vendor_code=v.code WHERE "
					+ "u.name LIKE %?1% or t.username LIKE %?1% or t.realname LIKE %?1% or t.duty LIKE %?1% or t.email LIKE %?1%", nativeQuery = true)
	Page<Account> findBySearchTerm(String search, Pageable pageable);

	@Query(value = "SELECT id, username, realname FROM account t WHERE "
			+ "t.role='ROLE_BUYER' and t.username LIKE %?1% or t.realname LIKE %?1%", nativeQuery = true)
	Page<AccountSearchItem> findForAutoComplete(String search, Pageable pageable);

	@Query(value = "SELECT id, username, realname FROM account t WHERE " + "t.username in ?1", nativeQuery = true)
	List<AccountSearchItem> findAccountsByUsernames(List<String> usernames);

	@Query(value = "SELECT * FROM account where vendor_code=?1", nativeQuery = true)
	List<Account> findAccountsByVendor(String vendorCode);

	@Query(value = "SELECT * FROM account where unit_id in ?1", nativeQuery = true)
	List<Account> findAccountsByUnitIdList(List<String> unitIdList);

	@Query(value = "SELECT * FROM account where role='ROLE_VENDOR' and unit_id in ?1", nativeQuery = true)
	List<Account> findAllVendorsByUnitIdList(List<String> unitIdList);

	@Query(value = "SELECT * FROM account where role='ROLE_BUYER' and unit_id in (select unit_id from unit_provide where provide_id in (select provide_id from vendor_provide where vendor_code=?1))", nativeQuery = true)
	List<Account> findAllBuyersByVendorCode(String vendorCode);
	
	@Query(value = "SELECT * FROM account where vendor_code in ?1", nativeQuery = true)
	List<Account> findAccountsByVendorCodeList(List<String> vendorCodeList);

	@Query(value = "SELECT * FROM account where role<>'ROLE_VENDOR'", nativeQuery = true)
	List<Account> findAllExceptVendor();

	@Query(value = "SELECT * FROM account where id in ?1", nativeQuery = true)
	List<Account> findAllByIdList(List<Long> idList);
	
	@Query(value = "select t.*, case t.role when 'ROLE_VENDOR' then p.name else u.name end as unitname from account t left join unit u on t.unit_id=u.id left join vendor v on t.vendor_code=v.code left join (select group_concat(a.name) name, c.vendor_code from unit a left join unit_provide b on a.id=b.unit_id left join vendor_provide c on b.provide_id=c.provide_id where c.vendor_code is not null GROUP BY c.vendor_code) p on t.vendor_code=p.vendor_code where t.id=?1 limit 1", nativeQuery = true)
	AccountSearchItem findOneVendorById(Long id);
	
	@Query(value = "SELECT * FROM account a left join permission_user_scope b on on b.target_id=a.id where role='ROLE_VENDOR' and account_id=?1 and dimension_id=" + Constants.PERMISSION_DIMENSION_ACCOUNT, nativeQuery = true)
	List<Account> findPermissionScopeAccountsOf(Long accountId);
	
}