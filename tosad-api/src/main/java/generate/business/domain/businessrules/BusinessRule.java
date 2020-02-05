package generate.business.domain.businessrules;

import generate.business.domain.businessrules.ruleattributes.LiteralValue;
import generate.business.domain.businessrules.ruleattributes.Table;

public interface BusinessRule {
    String generateDynamicPart();
    void addValue(LiteralValue value);
    String getName();
    Table getTable();
    String generateDeclare();
}
