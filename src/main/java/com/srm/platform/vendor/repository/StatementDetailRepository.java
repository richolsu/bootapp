package com.srm.platform.vendor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.srm.platform.vendor.model.StatementDetail;
import com.srm.platform.vendor.utility.StatementDetailItem;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface StatementDetailRepository extends JpaRepository<StatementDetail, Long> {

	StatementDetail findOneById(Long id);

	@Query(value = "select a.*, c.code purchase_in_code, c.rowno, c.quantity, c.price, c.cost, c.cmassunitname, c.assitantunitname, c.irate, c.number, d.date purchase_in_date, e.code inventorycode, e.name inventoryname, e.specs from statement_detail a left join statement_main b on a.code=b.code left join purchase_in_detail c on a.purchase_in_detail_id=c.id left join purchase_in_main d on c.code=d.code left join inventory e on c.inventorycode=e.code where a.code=?1", nativeQuery = true)
	List<StatementDetailItem> findDetailsByCode(String code);

}