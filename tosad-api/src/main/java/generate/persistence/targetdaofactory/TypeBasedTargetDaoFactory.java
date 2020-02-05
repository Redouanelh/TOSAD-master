package generate.persistence.targetdaofactory;

import generate.persistence.dao.BaseDao;
import generate.persistence.dao.OracleBaseDao;
import generate.persistence.dao.TargetDao;
import generate.persistence.dao.TargetOracleDao;

public class TypeBasedTargetDaoFactory implements TargetDaoFactory {

	private String type;
	private String url;
	private String user;
	private String pass;

	public TypeBasedTargetDaoFactory(String type, String url, String user, String pass) {
		this.type = type;
		this.url = url;
		this.user = user;
		this.pass = pass;
	}

	public TargetDao getTargetDao() {
		if(this.type.equals("Oracle")) {
			BaseDao oraclebasedao = new OracleBaseDao(this.url, this.user, this.pass);
			return new TargetOracleDao(oraclebasedao);
		}
		return null;
	}


}
