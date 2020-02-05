package generate.business.domain.businessrules.ruleattributes;

import generate.business.domain.businessrules.BusinessRule;

import java.util.ArrayList;

public class Trigger {
    private String triggercode;
    private String triggerevent;
    private ArrayList<BusinessRule> businessRules = new ArrayList<>();

    public Trigger(String triggercode) {
        this.triggercode = triggercode;
        this.triggerevent = "before delete or insert or update";
    }

    public String getTriggercode() {
        return triggercode;
    }

    public String getTriggerevent() {
        return triggerevent;
    }

    public void setTriggerevent(String event) {
        this.triggerevent = event;
    }



    public void addBusinessRule(BusinessRule businessRule) {
        businessRules.add(businessRule);
    }

    public ArrayList<BusinessRule> getBusinessRules() {
        return businessRules;
    }
}
