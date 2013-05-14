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
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * Abstract Data-Access Object class to be implemented by all DAO's.
 */
public abstract class AbstractDao<T> {
	protected Class<T> entityClass;

	@PersistenceContext(unitName = "SherlockPU",
	        type = PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public AbstractDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public EntityManager getEntityManager() {
		return this.em;
	}

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public void create(T entity) {
		getEntityManager().persist(entity);
	}

	public T update(T entity) {
		return getEntityManager().merge(entity);
	}

	public void remove(Object entityId) {
		T entity = find(entityId);
		if (entity != null)
			getEntityManager().remove(entity);
	}

	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}

	public List<T> findAll() {
		String sql = "select t from " + entityClass.getSimpleName() + " t";
		return getEntityManager().createQuery(sql, entityClass).getResultList();
	}

	public int count() {
		List<T> results = findAll();
		return results == null ? 0 : results.size();
	}

}