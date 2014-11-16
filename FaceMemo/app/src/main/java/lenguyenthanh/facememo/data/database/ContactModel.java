package lenguyenthanh.facememo.data.database;

import java.util.List;

import de.greenrobot.dao.query.LazyList;
import lenguyenthanh.facememo.data.dao.ContactDao;
import lenguyenthanh.facememo.data.entities.Contact;

/**
 * Created by lenguyenthanh on 7/15/14.
 */
public class ContactModel extends BaseEntityDao<ContactDao, Contact, Long> {

    private static ContactModel instance;


    public synchronized static ContactModel getInstance() {
        if (instance == null) {
            instance = new ContactModel();
        }
        return instance;
    }

    @Override
    protected ContactDao initEntityDao() {
        return DBHelper.getInstance().getDaoSession().getContactDao();
    }

    public LazyList<Contact> lazyLoadContacts() {
        return dao.queryBuilder().orderAsc(ContactDao.Properties.FirstName).listLazy();
    }

    public List<Contact> listContacts() {
        return dao.queryBuilder().orderAsc(ContactDao.Properties.FirstName).list();
    }

    public LazyList<Contact> recentContact(int number){
        return dao.queryBuilder().orderDesc(ContactDao.Properties.Id).limit(number).listLazy();
    }

    public Contact load(Long key){
        return dao.load(key);
    }
}
