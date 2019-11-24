package com.example.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.example.entities.AccountEntity;
import com.example.exceptions.WalletException;

@Component
@Transactional(rollbackOn = WalletException.class)
public class AccountDAO {
    
    @PersistenceContext
    private EntityManager entityManager;

    public java.util.List<AccountEntity> getAllAccounts() {
        return entityManager.unwrap(Session.class).createCriteria(AccountEntity.class).list();
    }

	public List findByEmailId(String emailId) {
		
		Criteria cr = entityManager.unwrap(Session.class).createCriteria(AccountEntity.class);
		cr.add(Restrictions.eq("emailId", emailId));
		return cr.list();
	}

	public Object save(AccountEntity accountEntity) {
		return entityManager.unwrap(Session.class).save(accountEntity);
	}
	
	public void update(AccountEntity accountEntity) {
		entityManager.unwrap(Session.class).merge(accountEntity);
	}
	
	public List findAllTransactions(String emailId) {
		Calendar c=new GregorianCalendar();
		c.add(Calendar.DATE, -30);
		Date d = c.getTime();
		
		Criteria cr = entityManager.unwrap(Session.class).createCriteria(AccountEntity.class);
		cr.add(Restrictions.eq("emailId", emailId));
		cr.add(Restrictions.gt("lastUpdated", d));
		return cr.list();
	}

	public List<AccountEntity> findAllTransactions(String emailId, String fromDateString, String toDateString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date fromDate = formatter.parse(fromDateString);
		Date toDate = formatter.parse(toDateString);
		Criteria cr = entityManager.unwrap(Session.class).createCriteria(AccountEntity.class);
		cr.add(Restrictions.eq("emailId", emailId));
		cr.add(Restrictions.lt("lastUpdated", fromDate));
		cr.add(Restrictions.gt("lastUpdated", toDate));
		return cr.list();
	}
}