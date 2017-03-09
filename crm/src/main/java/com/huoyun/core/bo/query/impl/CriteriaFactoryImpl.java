package com.huoyun.core.bo.query.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.CriteriaFactory;
import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.core.bo.query.Token;
import com.huoyun.core.bo.query.ValueConverter;
import com.huoyun.core.bo.query.ValueConverterFactory;
import com.huoyun.core.bo.query.criteria.And;
import com.huoyun.core.bo.query.criteria.ComparableCriteria;
import com.huoyun.core.bo.query.criteria.Criteria;
import com.huoyun.core.bo.query.criteria.Equal;
import com.huoyun.core.bo.query.criteria.LogicalCriteria;
import com.huoyun.core.bo.query.criteria.NotEqual;
import com.huoyun.core.bo.query.criteria.Or;
import com.huoyun.core.bo.query.criteria.Like;
import com.huoyun.exception.BusinessException;

public class CriteriaFactoryImpl implements CriteriaFactory {

	private final static String Equal = "eq";
	private final static String NotEqual = "ne";
	private final static String And = "and";
	private final static String Or = "or";
	private final static String Like = "like";

	@Override
	public List<Criteria> parse(BoMeta boMeta, String query) throws BusinessException {
		TokenParser filter = new TokenParser(query);
		List<Token> tokens = filter.getTokens();

		return this.parseToCriteriaList(boMeta, tokens);
	}

	@SuppressWarnings("unchecked")
	private <T extends Criteria> Class<T> getCriteriaClass(String op) throws BusinessException {
		switch (op.toLowerCase()) {
		case Equal:
			return (Class<T>) Equal.class;
		case NotEqual:
			return (Class<T>) NotEqual.class;
		case Like:
			return (Class<T>) Like.class;
		case And:
			return (Class<T>) And.class;
		case Or:
			return (Class<T>) Or.class;
		}

		throw new BusinessException(ErrorCode.Not_Sopport_Criteria_Query);
	}

	private List<Criteria> parseToCriteriaList(BoMeta boMeta, List<Token> tokens) throws BusinessException {

		if (tokens == null || tokens.size() == 0) {
			return null;
		}

		if (tokens.size() == 1) {
			if (tokens.get(0) instanceof ListToken) {
				return this.parseToCriteriaList(boMeta, ((ListToken) tokens.get(0)).getTokens());
			}

			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

		if (tokens.size() == 2) {
			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

		List<Criteria> criterias = new ArrayList<Criteria>();
		Cursor cursor = new Cursor();
		while (cursor.getValue() < tokens.size()) {
			Token opToken = tokens.get(cursor.getValue() + 1);
			Class<Criteria> criteriaClass = this.getCriteriaClass(opToken.getExpr());
			Token leftToken = tokens.get(cursor.getValue());
			Token rightToken = tokens.get(cursor.getValue() + 2);
			if (ComparableCriteria.class.isAssignableFrom(criteriaClass)) {
				Criteria criteria = this.newComparableCriteria(boMeta, criteriaClass, leftToken, rightToken);
				criterias.add(criteria);
			} else if (LogicalCriteria.class.isAssignableFrom(criteriaClass)) {
				Criteria criteria = this.newLogicalCriteria(boMeta, criteriaClass, leftToken, rightToken);
				criterias.add(criteria);
			} else {
				throw new BusinessException(ErrorCode.Not_Sopport_Criteria_Query);
			}

			cursor.move(3);
		}

		return criterias;
	}

	private Criteria newComparableCriteria(BoMeta boMeta, Class<Criteria> criteriaClass, Token propNameToken,
			Token propValueToken) throws BusinessException {
		PropertyMeta propMeta = this.getPropMeta(boMeta, propNameToken);
		Object propValue = this.getValue(propMeta, propValueToken);
		try {
			Constructor<Criteria> constructor = criteriaClass.getConstructor(PropertyMeta.class, Object.class);
			return constructor.newInstance(propMeta, propValue);
		} catch (Exception e) {
			throw new BusinessException(BoErrorCode.Bo_Query_Express_Parse_Failed);
		}
	}

	private Criteria newLogicalCriteria(BoMeta boMeta, Class<Criteria> criteriaClass, Token leftToken, Token rightToken)
			throws BusinessException {
		List<Criteria> leftCriterias = this.parseToCriteriaList(boMeta, this.toListTokens(leftToken));
		List<Criteria> rightCriterias = this.parseToCriteriaList(boMeta, this.toListTokens(rightToken));

		try {
			Constructor<Criteria> constructor = criteriaClass.getConstructor(List.class, List.class);

			return constructor.newInstance(leftCriterias, rightCriterias);
		} catch (Exception e) {
			throw new BusinessException(BoErrorCode.Bo_Query_Express_Parse_Failed);
		}
	}

	private List<Token> toListTokens(Token token) {

		if (token instanceof StringToken) {
			List<Token> tokens = new ArrayList<Token>();
			tokens.add(token);
			return tokens;
		}
		return ((ListToken) token).getTokens();

	}

	private PropertyMeta getPropMeta(BoMeta boMeta, Token propToken) throws BusinessException {
		if (propToken instanceof StringToken) {
			String propName = propToken.getExpr();
			PropertyMeta propMeta = boMeta.getPropertyMeta(propName);
			if (propMeta != null) {
				return propMeta;
			}
		}

		throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
	}

	private Object getValue(PropertyMeta propMeta, Token valueToken) throws BusinessException {
		ValueConverter valueConverter = ValueConverterFactory.getValueConverter(propMeta);
		if (valueConverter != null && valueToken instanceof StringToken) {
			return valueConverter.converter(valueToken.getExpr());
		}

		throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
	}
}
