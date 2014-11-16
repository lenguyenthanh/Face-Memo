package lenguyenthanh.facememo.data.database;

import java.util.Collection;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.LazyList;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * @author lenguyenthanh on 8/1/14.
 */

public abstract class BaseEntityDao<DAO extends AbstractDao<E, K>, E, K> {
    protected DAO dao;

    public BaseEntityDao() {
        dao = initEntityDao();
    }

    protected abstract DAO initEntityDao();

    public void insertEntity(E entity) {
        dao.insert(entity);
    }

    public void deleteEntity(E entity) {
        dao.delete(entity);
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public void deleteEntityByKey(K key) {
        dao.deleteByKey(key);
    }

    public void insertOrReplace(E item) {
        dao.insertOrReplace(item);
    }

    public void update(E item){
        dao.update(item);
    }

    public void insertOrReplaceAll(final Collection<E> items) {
        dao.insertOrReplaceInTx(items);
    }

    protected QueryBuilder<E> getQuery(final Property property, final boolean asc) {
        if (asc) return dao.queryBuilder().orderAsc(property);
        else return dao.queryBuilder().orderDesc(property);
    }

    protected QueryBuilder<E> getQuery(final Property property, final int offset, final int limit, final boolean asc) {
        if (asc) return dao.queryBuilder().offset(offset).limit(limit).orderAsc(property);
        else return dao.queryBuilder().offset(offset).limit(limit).orderDesc(property);
    }

    public List<E> listEntities(final Property property) {
        return getQuery(property, false).list();
    }


    public List<E> listEntities(final Property property, final int offset, final int limit, final boolean asc) {
        return getQuery(property, offset, limit, asc)
                .list();
    }

    public LazyList<E> lazyListEntities(final Property property, final boolean asc) {
        return getQuery(property, asc)
                .listLazy();
    }

    public LazyList<E> lazyListEntities(final Property property, final int offset, final int limit, final boolean asc) {
        return getQuery(property, offset, limit, asc)
                .listLazy();
    }

    public void deleteOldItemBySize(final int size, final Property property) {
        int count = (int) dao.queryBuilder().count();
        LazyList<E> items = lazyListEntities(property, size, count, true);
        dao.deleteInTx(items);
    }


    public List<E> queryEntitiesByKey(K key, boolean asc) {
        QueryBuilder<E> qb = dao.queryBuilder().where(dao.getPkProperty().eq(key), (WhereCondition) null);
        if (asc) {
            qb.orderAsc(dao.getPkProperty());
        } else {
            qb.orderDesc(dao.getPkProperty());
        }
        return qb.list();
    }

    public E queryEntityByKey(K key) {
        QueryBuilder<E> qb = dao.queryBuilder().where(dao.getPkProperty().eq(key), (WhereCondition) null);
        return qb.unique();
    }

    public List<E> queryEntitiesByArgs(WhereCondition... whereCondition) {
        if (whereCondition == null || whereCondition.length == 0) {
            return dao.queryBuilder().list();
        }
        return dao.queryBuilder().where(null, whereCondition).list();
    }

    public List<E> queryEntities(String where, String... selectionArg) {
        return dao.queryRaw(where, selectionArg);
    }

}
