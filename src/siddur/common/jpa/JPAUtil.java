package siddur.common.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import siddur.common.miscellaneous.Paging;
import siddur.common.security.PermissionManager;
import siddur.common.security.RoleInfo;
import siddur.common.security.UserInfo;



public class JPAUtil {
	private static EntityManagerFactory entityMgrF;
	
	public static void init(){
		EntityManager entityMgr = newEntityMgr();
		UserInfo u = entityMgr.find(UserInfo.class, 1);
		if(u == null){
			EntityTransaction t = entityMgr.getTransaction();
			t.begin();
			
			initRoleAndUser(entityMgr);
			
			t.commit();
		}
		entityMgr.close();
	}
	
	
	private static void initRoleAndUser(EntityManager entityMgr){
		//admin role
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setRolename("admin");
		roleInfo.setPermission(PermissionManager.admin().toInteger());
		entityMgr.persist(roleInfo);
		
		//admin user
		UserInfo user = new UserInfo();
		user.setUsername("admin");
		user.setPassword("password");
		user.setRole(roleInfo);
		entityMgr.persist(user);
		
		//client role
		roleInfo = new RoleInfo();
		roleInfo.setRolename("client");
		roleInfo.setPermission(PermissionManager.client().toInteger());
		entityMgr.persist(roleInfo);
		
		//client user
		user = new UserInfo();
		user.setUsername("client");
		user.setPassword("password");
		user.setRole(roleInfo);
		entityMgr.persist(user);
		
		//editor role
		roleInfo = new RoleInfo();
		roleInfo.setRolename("editor");
		roleInfo.setPermission(PermissionManager.editor().toInteger());
		entityMgr.persist(roleInfo);
		
		//editor user
		user = new UserInfo();
		user.setUsername("editor");
		user.setPassword("password");
		user.setRole(roleInfo);
		entityMgr.persist(user);
	}
	
	public static EntityManager newEntityMgr(){
		if(entityMgrF == null){
			entityMgrF = Persistence.createEntityManagerFactory("toolcloud");
//			Configuration cfg = new Ejb3Configuration().configure("toolcloud", null ).getHibernateConfiguration();
//			SchemaExport se = new SchemaExport(cfg);
//			se.create(true, false);
		}
		return entityMgrF.createEntityManager();
	}
	
	
	public static UserInfo getUser(String username){
		EntityManager em = newEntityMgr();
		UserInfo u = null;
		try {
			u = em.createQuery("from UserInfo u where u.username='" + username + "'", UserInfo.class)
				.getSingleResult();
		} catch (Exception e) {
		}
		em.close();
		return u;
	}
	
	public static <E> Paging<E> getPageData(HttpServletRequest req, TypedQuery<E> query, TypedQuery<Long> totalQuery){
		String pageIndex = req.getParameter("pageIndex");
		String pageSize = req.getParameter("pageSize");
		
		
		int index = 1, size = 20;
		try {
			if(pageIndex != null)
				index = Integer.parseInt(pageIndex);
		} catch (NumberFormatException e) {
		}
		try {
			if(pageSize != null)
				size = Integer.parseInt(pageSize);
		} catch (NumberFormatException e) {
		}
			
		if(index >= 0 && size > 0){
			int start = (index - 1) * size;
			query.setFirstResult(start);
			query.setMaxResults(size);
		}
		
		Paging<E> p = new Paging<E>();
		p.setData(query.getResultList());
		p.setPageIndex(index);
		p.setPageSize(size);
		
		p.setTotal(totalQuery.getSingleResult().intValue());
		
		return p;
	}
	
	
	public static <E> List<E> findAll(Class<E> claz, EntityManager em) {
		List<E> list = em.createQuery("from " + claz.getSimpleName(), claz)
				.getResultList();
		return list;
	}

	public static <E> List<E> findAll(Class<E> claz) {
		EntityManager em = newEntityMgr();
		List<E> list = em.createQuery("from " + claz.getSimpleName(), claz)
				.getResultList();
		em.close();
		return list;
	}
}
