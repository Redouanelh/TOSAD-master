package generate.persistence.targetdaofactory;

import generate.persistence.dao.TargetDao;

public interface TargetDaoFactory {

    public TargetDao getTargetDao();
}
