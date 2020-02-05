package define.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import define.business.domain.LiteralValue;
import define.business.domain.Operator;
import define.business.domain.TableAttribute;
import define.business.domain.Trigger;
import define.business.domain.businessrules.BusinessRule;

public class DefineOracleDao implements DefineDao {
	private BaseDao dbconnection;
	
	public DefineOracleDao(BaseDao dbconnection) {
		this.dbconnection = dbconnection;
	}

	@Override
	public HashMap<String, HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>> getAvailableInput() {
        String query = "select operator.name as \"operator\", businessruletype.code as \"businessrulecode\", businessruletype.name as \"businessrulename\", businessruletype.description as \"businessruledescription\", category.name as \"categoryname\" from operator, businessruletype, category, operatorrule where operator.id = operatorrule.id and operatorrule.code = businessruletype.code and businessruletype.categoryid = category.id order by businessruletype.name asc";
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>> data = new HashMap<>();        
        
        try (Connection conn = dbconnection.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultset = statement.executeQuery();
            
            data.put("categories", new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>());
            while(resultset.next()) {
            	if(data.get("categories").containsKey(resultset.getString("categoryname"))) {
            		
            	} else {
            		data.get("categories").put(resultset.getString("categoryname"), new HashMap<String, HashMap<String, ArrayList<String>>>());
            	}
            	if(data.get("categories").get(resultset.getString("categoryname")).containsKey(resultset.getString("businessrulename"))) {
            		
            	} else {
            		data.get("categories").get(resultset.getString("categoryname")).put(resultset.getString("businessrulename"), new HashMap<String, ArrayList<String>>());
            		data.get("categories").get(resultset.getString("categoryname")).get(resultset.getString("businessrulename")).put("operators", new ArrayList<String>());
            	}
            	data.get("categories").get(resultset.getString("categoryname")).get(resultset.getString("businessrulename")).get("operators").add(resultset.getString("operator"));
            }
            resultset.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbconnection.closeConnection();
        return data;
    }
	
	// SAVES EVERYTHING 
	public String defineRule(BusinessRule rule) {		
		// check if target table/attribute is in our database. if not, it will be added.
		checkTargetData(rule);
					
		// only works if trigger doesn't exist, will be checked in this method
		int triggerId = getTriggerFromRule(rule.getTrigger());
		insertTrigger(rule.getTrigger(), triggerId); 
		triggerId = getTriggerFromRule(rule.getTrigger());
					
    	// insert businessrule
		String successful = saveBusinessRule(rule);
		
		// insert parameters/tablecolumns used as parameters (will be checked inside the method)
		insertCorrectParameter(rule);
			
		return successful;
	}
	
	private String saveBusinessRule(BusinessRule rule) {
		String result = "failed";
		
		int triggerId = getTriggerFromRule(rule.getTrigger());
		int operatorId = getOperatorFromRule(rule.getOperator());
		int attributeId = getTableAttributeFromRule(rule.getTable().getSelectedTableAttribute());
		String ruletypecode = getRuletypeFromRule(rule.getType());
		
		try (Connection conn = dbconnection.getConnection()) {
    		String businessruleinsert = "insert into businessrule (name, type, failure_message, attributeid, operatorid, triggerid) values (? || (businessrule_sequence.nextval + 1), ?, ?, ?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(businessruleinsert);
			
			statement.setString(1, rule.getName() + ruletypecode + "_");
			statement.setString(2, ruletypecode);
			statement.setString(3, rule.getTrigger().getFailuremessage());
			statement.setInt(4, attributeId);
			statement.setInt(5, operatorId);
			statement.setInt(6, triggerId);
			
			if (statement.executeUpdate() > 0) {
				result = "successful";
			
			statement.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;	
	}
	
	private void insertCorrectParameter(BusinessRule rule) {
		int ruleid = getBusinessRuleId();
		String ruletypecode = getRuletypeFromRule(rule.getType());
		
		try (Connection conn = dbconnection.getConnection()) {
        	if (ruletypecode.equals("ARNG") || ruletypecode.equals("ACMP") || ruletypecode.equals("ALIS") || ruletypecode.equals("AOTH") || ruletypecode.equals("TOTH") || ruletypecode.equals("EOTH") || ruletypecode.equals("MODI")) {
        		String parameterinsert = "insert into parameter (value) values (?)"; 
        		String parameterruleinsert = "insert into parameterrule (businessruleid, parameterid) values (?, ?)";
        		
        		for (LiteralValue l : rule.getValues() ) {
        			PreparedStatement pstatement = conn.prepareStatement(parameterinsert);
        			pstatement.setString(1, l.getValue());
                    pstatement.executeQuery();
                    
                    pstatement.close();
                
    				int parid = getParameterId();
    				  		
    				PreparedStatement parameterruleinsertstatement = conn.prepareStatement(parameterruleinsert);
    				parameterruleinsertstatement.setInt(1, ruleid);
    				parameterruleinsertstatement.setInt(2, parid);
    				parameterruleinsertstatement.executeUpdate();
        		}
        		
        	} else if (ruletypecode.equals("TCMP")) {        		
        		int columnid = checkTargetColumnValue(rule.getTable().getName(), rule.getValues().get(0).getValue());
        		String parameterruleinsert = "insert into parameterrule (businessruleid, attributeid) values (?, ?)";
        		
				PreparedStatement parameterruleinsertstatement = conn.prepareStatement(parameterruleinsert);
				parameterruleinsertstatement.setInt(1, ruleid);
				parameterruleinsertstatement.setInt(2, columnid);
				parameterruleinsertstatement.executeUpdate();
        		
        	} else if (ruletypecode.equals("ICMP")) {
        		int tableid = checkTargetTableValue(rule.getValues().get(0).getValue());
        		int columnid = checkTargetColumnValue(rule.getValues().get(0).getValue(), rule.getValues().get(1).getValue());
        		String parameterruleinsert = "insert into parameterrule (businessruleid, attributeid) values (?, ?)";

				PreparedStatement parameterruleinsertstatement = conn.prepareStatement(parameterruleinsert);
				parameterruleinsertstatement.setInt(1, ruleid);
				parameterruleinsertstatement.setInt(2, columnid);
				parameterruleinsertstatement.executeUpdate();
				
				// pk and fk inserts
				String parameterinsert = "insert into parameter (value) values (?)"; 
        		String parameterrulekeyinsertquery = "insert into parameterrule (businessruleid, parameterid) values (?, ?)";
				ArrayList<String> keys = new ArrayList<>();
				keys.add(rule.getValues().get(2).getValue());
				keys.add(rule.getValues().get(3).getValue());
				
				for (String key : keys ) {
        			PreparedStatement pstatement = conn.prepareStatement(parameterinsert);
        			pstatement.setString(1, key);
                    pstatement.executeQuery();
                    
                    pstatement.close();
                
    				int parid = getParameterId();
    				  		
    				PreparedStatement parameterrulekeyinsertstatement = conn.prepareStatement(parameterrulekeyinsertquery);
    				parameterrulekeyinsertstatement.setInt(1, ruleid);
    				parameterrulekeyinsertstatement.setInt(2, parid);
    				parameterrulekeyinsertstatement.executeUpdate();
				}
        	}
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	private int checkTargetTableValue(String tablename) {
		String checktargettable = "select * from targettable where name = ?";
		boolean tableExists = false;
		
		try (Connection conn = dbconnection.getConnection()) {
			PreparedStatement gettargettable = conn.prepareStatement(checktargettable);
			gettargettable.setString(1, tablename);
    		ResultSet targettableresult = gettargettable.executeQuery();
    		
    		int sizez = 0;
    		while (targettableresult.next()) {
    			sizez++;
    		}
    		
    		gettargettable.close();
    		targettableresult.close();
    		
    		if (sizez > 0) {
    			tableExists = true;
    		}
    		
    		if (!tableExists) {
    			String inserttable = "insert into targettable (name, databaseid) values (?, ?)";
    			PreparedStatement inserttablestmt = conn.prepareStatement(inserttable);
    			inserttablestmt.setString(1, tablename);
    			inserttablestmt.setInt(2, 1);
    			inserttablestmt.executeQuery();
    			
    			inserttablestmt.close();
    		}
    		
    		int tableid = 0;
    		String tableidquery = "select * from targettable where name = ?";
    		PreparedStatement tableidstmt = conn.prepareStatement(tableidquery);
    		tableidstmt.setString(1,  tablename);
    		ResultSet result = tableidstmt.executeQuery();
    		
    		while (result.next()) {
    			tableid = result.getInt("id");
    		}
    		
    		tableidstmt.close();
    		result.close();
    		
    		return tableid;
    		
		} catch(Exception e) {
			e.printStackTrace();
		}
			return 0;
	}
	
	// checks if target table/attribute as value exists or not
	private int checkTargetColumnValue(String tablename, String valuename) {
		String checktargettable = "select * from targettable where name = ?";
		String checktableattribute = "select * from targettableattribute where name = ? and tableid = ?";
		boolean tableattributeExists = false;
		
		try (Connection conn = dbconnection.getConnection()) {
			
			PreparedStatement gettargettable = conn.prepareStatement(checktargettable);
			gettargettable.setString(1, tablename);
    		ResultSet targettableresult = gettargettable.executeQuery();
    		
    		int tableid = 0;
			while (targettableresult.next()) {
				tableid = targettableresult.getInt("id");
			}			
			
			PreparedStatement tableattributestatement = conn.prepareStatement(checktableattribute);
    		tableattributestatement.setString(1, valuename);
    		tableattributestatement.setInt(2, tableid);
    		ResultSet tableattributeresult = tableattributestatement.executeQuery(); 
    		
    		int sizez = 0;
    		while (tableattributeresult.next()) {
    			sizez++;
    		}
    		
    		if (sizez > 0) {
    			tableattributeExists = true;
    		}
    		
    		if (!tableattributeExists) {
    			String inserttableattribute = "insert into targettableattribute (name, tableid) values (?, ?)";
    			PreparedStatement insertstatement = conn.prepareStatement(inserttableattribute);
    			insertstatement.setString(1, valuename);
    			insertstatement.setInt(2, tableid);
    			insertstatement.executeQuery();
    			
    			insertstatement.close();
    		}	
    		
    		int columnidnumber = 0;
    		String columnid = "select * from targettableattribute where name = ? and tableid = ?";
    		PreparedStatement columnidstatement = conn.prepareStatement(columnid);
    		columnidstatement.setString(1,  valuename);
    		columnidstatement.setInt(2, tableid);
    		ResultSet result = columnidstatement.executeQuery();
    		
    		while (result.next()) {
    			columnidnumber = result.getInt("id");
    		}
    		
    		columnidstatement.close();
    	
    		return columnidnumber;
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	// checks if target table/attributes from current rule already exist in our database or not
	private void checkTargetData(BusinessRule rule) {
		String checktargettable = "select * from targettable where name = ?";
		String inserttargettable = "insert into targettable (name, databaseid) values (?, 1)";
		boolean tableExists = false;
        try (Connection conn = dbconnection.getConnection()) {
        	
    		PreparedStatement statement = conn.prepareStatement(checktargettable);
    		statement.setString(1, rule.getTable().getName());
    		ResultSet result = statement.executeQuery();
    		
    		int size = 0;
    		while (result.next()) {
    			size++;
    		}
    		
    		if (size > 0) {
    			tableExists = true;
    		}
    		
    		if (!tableExists) {
        		PreparedStatement insertstatement = conn.prepareStatement(inserttargettable);
        		insertstatement.setString(1, rule.getTable().getName());
        		insertstatement.executeQuery();
        		insertstatement.close();
    		}
    		
    		statement.close();
    		result.close();
    		
    		// check the column
    		checkTargetColumn(rule);

        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	private void checkTargetColumn(BusinessRule rule) {
		String checktargettable = "select * from targettable where name = ?";
		String checktableattribute = "select * from targettableattribute where name = ?";
		boolean tableattributeExists = false;
		
		
		try (Connection conn = dbconnection.getConnection()) {
			PreparedStatement tableattributestatement = conn.prepareStatement(checktableattribute);
    		tableattributestatement.setString(1, rule.getTable().getSelectedTableAttribute().getName());
    		ResultSet tableattributeresult = tableattributestatement.executeQuery(); 
    		
    		int sizez = 0;
    		while (tableattributeresult.next()) {
    			sizez++;
    		}
    		
    		if (sizez > 0) {
    			tableattributeExists = true;
    		}
    		
    		if (!tableattributeExists) {
    			PreparedStatement gettargettable = conn.prepareStatement(checktargettable);
    			gettargettable.setString(1, rule.getTable().getName());
        		ResultSet targettableresult = gettargettable.executeQuery();
        		
        		int tableid = 0;
    			while (targettableresult.next()) {
    				tableid = targettableresult.getInt("id");
    			}
    			
    			String inserttableattribute = "insert into targettableattribute (name, tableid) values (?, ?)";
    			PreparedStatement insertstatement = conn.prepareStatement(inserttableattribute);
    			insertstatement.setString(1, rule.getTable().getSelectedTableAttribute().getName());
    			insertstatement.setInt(2, tableid);
    			insertstatement.executeQuery();
    			
    			insertstatement.close();
    		}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int getOperatorFromRule(Operator operator) {
		String query = "SELECT * from operator o where o.name = ?";
        int operatorId = 0;
                
		try (Connection conn = dbconnection.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, operator.getName());
            ResultSet resultset = statement.executeQuery();
            
            while (resultset.next()) {
            	operatorId = statement.getResultSet().getInt("id");
            }
            resultset.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbconnection.closeConnection();       
		return operatorId;
	}

	private int getTriggerFromRule(Trigger trigger) {
		String query = "SELECT * from generatedtrigger t where t.name = ?";
        int triggerid = 0;
                
		try (Connection conn = dbconnection.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, trigger.getTriggercode());
            ResultSet resultset = statement.executeQuery();
            
            while (resultset.next()) {
            	triggerid = statement.getResultSet().getInt("id");
            }
            resultset.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbconnection.closeConnection();
		return triggerid;
	}
	
	private int getTableAttributeFromRule(TableAttribute attribute) {
		String query = "SELECT * from targettableattribute t where t.name = ?";
        int attributeid = 0;
                
		try (Connection conn = dbconnection.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, attribute.getName());
            ResultSet resultset = statement.executeQuery();
            
            while (resultset.next()) {
            	attributeid = statement.getResultSet().getInt("id");
            }
            resultset.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbconnection.closeConnection();
		return attributeid;
	}
	
	private String getRuletypeFromRule(String type) {
		String query = "SELECT * from businessruletype b where b.name = ?";
        String ruletypecode = "";
                
		try (Connection conn = dbconnection.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, type);
            ResultSet resultset = statement.executeQuery();
            
            while (resultset.next()) {
            	ruletypecode = statement.getResultSet().getString("code");
            }
            resultset.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbconnection.closeConnection();
		return ruletypecode;
	}
	
	// if trigger doesn't exist yet, use this method
	private void insertTrigger(Trigger trigger, int triggerid) {
		
		try (Connection conn = dbconnection.getConnection()) {	
			boolean triggerAdded = false;
			if (triggerid == 0) {
				
				String triggerquery = "insert into generatedtrigger (name, event) values (?, ?)";
				PreparedStatement triggerstatement = conn.prepareStatement(triggerquery);
				triggerstatement.setString(1, trigger.getTriggercode());
				triggerstatement.setString(2, trigger.getTriggerevent());
				
				triggerAdded = triggerstatement.executeUpdate() > 0;	
				System.out.println("Trigger inserted: " + triggerAdded);
			} else {
				System.out.println("Trigger inserted: " + triggerAdded + ", trigger already exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int getBusinessRuleId() {
		String ruleidquery = "select max(id) as id from businessrule";
		int ruleid = 0;
		
		try (Connection conn = dbconnection.getConnection()) {
			PreparedStatement getruleidstatement = conn.prepareStatement(ruleidquery);
			ResultSet result = getruleidstatement.executeQuery();
			while (result.next()) {
				ruleid = result.getInt("id");
			}
			getruleidstatement.close();
			result.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ruleid;
	}
	
	private int getParameterId() {
		int parid = 0;
		String paridquery = "select max(id) as id from parameter";
		
		try (Connection conn = dbconnection.getConnection()) {
			PreparedStatement getparidstatement = conn.prepareStatement(paridquery);
			ResultSet parresult = getparidstatement.executeQuery();
			while (parresult.next()) {
				parid = parresult.getInt("id");
			}
			
			getparidstatement.close();
			parresult.close();
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return parid;
	}
	
	public String deleteBusinessRule(String name) {
		String dparameterrule = "delete from parameterrule where businessruleid = (select id from businessrule where name = ?)";
		String dbusinessrule  = "delete from businessrule where name = ?";
		String triggeridquery = "select * from generatedtrigger where id = (select triggerid from businessrule where name = ?)";
		boolean prdeleted = false;
		boolean brdeleted = false;
		
		try (Connection conn = dbconnection.getConnection()) {
			// get triggerid before deleting rule
			int triggerid = 0;
			PreparedStatement tid = conn.prepareStatement(triggeridquery);
			tid.setString(1, name);
			ResultSet tresult = tid.executeQuery();
			
			while (tresult.next()) {
				triggerid = tresult.getInt("id");
			}
			tid.close();
			tresult.close();
			
			// delete from parameterrule
			PreparedStatement prule = conn.prepareStatement(dparameterrule);
			prule.setString(1, name);
			prdeleted = prule.executeUpdate() > 0;
			prule.close();
			
			// delete from businessrule
			PreparedStatement brule = conn.prepareStatement(dbusinessrule);
			brule.setString(1, name);
			brdeleted = brule.executeUpdate() > 0;
			brule.close();
			
			// only works if the trigger doesn't have any business rules.
			deleteTrigger(triggerid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (prdeleted && brdeleted) {
			return "success";
		} else {
			return "failed";
		}
	}
	
	private void deleteTrigger(int id) {		
		String findrulesquery = "select * from businessrule where triggerid = ?";
		String deletetriggerquery = "delete from generatedtrigger where id = ?";
				
		try (Connection conn = dbconnection.getConnection()) {			
			// check if the trigger has any rules left
			int rules = 0;
			PreparedStatement findrules = conn.prepareStatement(findrulesquery);
			findrules.setInt(1, id);
			ResultSet rulesresult = findrules.executeQuery();
			while (rulesresult.next()) {
				rules++;
			}
			findrules.close();
			
			// delete trigger if there are no more rules
			if (rules == 0) {
				PreparedStatement deltrigger = conn.prepareStatement(deletetriggerquery);
				deltrigger.setInt(1, id);
				deltrigger.executeQuery();		
				deltrigger.close();
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
