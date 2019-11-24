package com.example.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.example.entities.AccountEntity;
import com.example.entities.TransactionEntity;
import com.example.exceptions.WalletException;

/**
 * Transaction JPA repository
 *  <p> Generates SQL queries to access the database to manage Transaction entities</p>
 */
@Component
@Transactional(rollbackOn = WalletException.class)
public class TransactionDAO {
    
    @PersistenceContext
    private EntityManager entityManager;

	public void save(TransactionEntity transaction) {
		entityManager.unwrap(Session.class).saveOrUpdate(transaction);
	}

	public List findByAccountEntity(AccountEntity accountEntity) {
		Criteria cr = entityManager.unwrap(Session.class).createCriteria(TransactionEntity.class);
		cr.add(Restrictions.eq("accountEntity", accountEntity));
		List<TransactionEntity> list = cr.list();
		Collections.sort(list, new Comparator<TransactionEntity>() {
			@Override
			public int compare(TransactionEntity o1, TransactionEntity o2) {
				return o1.getAccountEntity().getId().compareTo(o2.getAccountEntity().getId());
			}
		});
		return cr.list();
		
	}

}
