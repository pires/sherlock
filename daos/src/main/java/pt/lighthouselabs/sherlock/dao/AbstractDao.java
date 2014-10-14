/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pt.lighthouselabs.sherlock.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * Abstract Data-Access Object class to be implemented by all DAO's.
 */
public abstract class AbstractDao<T> {
	protected Class<T> entityClass;

	//@PersistenceContext(unitName = "SherlockPU",
	//        type = PersistenceContextType.TRANSACTION)
	//private EntityManager em;

  @PersistenceUnit(unitName = "SherlockPU")
  private EntityManagerFactory emf;

	public AbstractDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public EntityManager getEntityManager() {
    EntityManager em = this.emf.createEntityManager();
    em.getTransaction().begin();
		return em;
	}

	//public void setEntityManager(EntityManager em) {
//		this.em = em;
//	}

	public void create(T entity) {
    EntityManager em = getEntityManager();
		em.persist(entity);
    em.getTransaction().commit();
	}

	public T update(T entity) {
    EntityManager em = getEntityManager();
		T merged = em.merge(entity);
    em.getTransaction().commit();
    return merged;
	}

	public void remove(Object entityId) {
		T entity = find(entityId);
		if (entity != null) {
      EntityManager em = getEntityManager();
      em.remove(entity);
      em.getTransaction().commit();
    }
	}

	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}

	public List<T> findAll() {
		String cql = "select t from " + entityClass.getSimpleName() + " t";
		return getEntityManager().createQuery(cql).getResultList();
	}

	public int count() {
		List<T> results = findAll();
		return results == null ? 0 : results.size();
	}

}