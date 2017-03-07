package com.huoyun.core.bo.ext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.validator.RuleType;

@BoEntity(allowCustomized = false)
@Entity
@Table
public class ValidationRule extends LiteBusinessObject {

	public ValidationRule() {

	}

	public ValidationRule(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	//@SequenceGenerator(name = "ValidationRule_SEQ", sequenceName = "VALIDATION_RULE_SEQ")
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ValidationRule_SEQ")
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id)
	private Long id;
	
	private RuleType ruleType;
	
	private String expr;
	
	private String errorMessage;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}

}
