package define.business.domain;

import define.business.domain.businessrules.BusinessRule;

import java.util.ArrayList;

public class Trigger {
    private String triggercode;
    private String triggerevent;
    private String failuremessage;
    private ArrayList<BusinessRule> businessRules = new ArrayList<>();

    public Trigger(String triggercode, String triggerevent, String failuremessage) {
        this.triggercode = triggercode;
        this.triggerevent = triggerevent;
        this.failuremessage = failuremessage;
    }

    public String getTriggercode() {
        return triggercode;
    }

    public String getTriggerevent() {
        return triggerevent;
    }

    public String getFailuremessage() {
        return failuremessage;
    }

    public void addBusinessRule(BusinessRule businessRule) {
        businessRules.add(businessRule);
    }

    public ArrayList<BusinessRule> getBusinessRules() {
        return businessRules;
    }
}
