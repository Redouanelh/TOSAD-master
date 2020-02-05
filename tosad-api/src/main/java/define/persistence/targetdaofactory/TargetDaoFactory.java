package define.persistence.targetdaofactory;

import define.persistence.dao.TargetDao;

public interface TargetDaoFactory {

    public TargetDao getTargetDao();
}
