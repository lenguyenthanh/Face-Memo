package lenguyenthanh.facememo.data.dao;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.data.dao.ContactDao;

public class ContactTest extends AbstractDaoTestLongPk<ContactDao, Contact> {

    public ContactTest() {
        super(ContactDao.class);
    }

    @Override
    protected Contact createEntity(Long key) {
        Contact entity = new Contact();
        entity.setId(key);
        return entity;
    }

}
