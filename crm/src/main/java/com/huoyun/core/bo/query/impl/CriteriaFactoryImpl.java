package com.huoyun.core.bo.query.impl;

import java.lang.reflect.Constructor;
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
import com.huoyun.core.bo.query.criteria.CollectionCriteria;
import com.huoyun.core.bo.query.criteria.ComparableCriteria;
import com.huoyun.core.bo.query.criteria.Criteria;
import com.huoyun.core.bo.query.criteria.Equal;
import com.huoyun.core.bo.query.criteria.LogicalCriteria;
import com.huoyun.core.bo.query.criteria.NotEqual;
import com.huoyun.core.bo.query.criteria.Or;
import com.huoyun.core.bo.query.criteria.Like;
import com.huoyun.core.bo.query.criteria.In;
import com.huoyun.exception.BusinessException;

public class CriteriaFactoryImpl implements CriteriaFactory {

	private final static String Equal = "eq";
	private final static String NotEqual = "ne";
	private final static String And = "and";
	private final static String Or = "or";
	private final static String Like = "like";
	private final static String In = "in";

	@Override
	public Criteria parse(BoMeta boMeta, String query) throws BusinessException {
		TokenParser filter = new TokenParser(query);
		List<Token> tokens = filter.getTokens();

		return parse(boMeta, tokens, new Cursor());
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
		case In:
			return (Class<T>) In.class;
		}

		throw new BusinessException(ErrorCode.Not_Sopport_Criteria_Query);
	}

	private Criteria parse(BoMeta boMeta, List<Token> tokens, Cursor cursor) throws BusinessException {

		if (tokens.size() - cursor.getValue() <= 0) {
			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

		if (tokens.size() - cursor.getValue() == 1) {
			if (tokens.get(cursor.getValue()) instanceof ListToken) {
				return this.parse(boMeta, ((ListToken) tokens.get(cursor.getValue())).getTokens(), new Cursor());
			}

			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

		if (tokens.size() - cursor.getValue() == 2) {
			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

		Token opToken = tokens.get(cursor.getValue() + 1);
		Class<Criteria> criteriaClass = this.getCriteriaClass(opToken.getExpr());
		Token leftToken = tokens.get(cursor.getValue());

		if (ComparableCriteria.class.isAssignableFrom(criteriaClass)) {
			Token rightToken = tokens.get(cursor.getValue() + 2);
			Criteria criteria = this.newComparableCriteria(boMeta, criteriaClass, leftToken, rightToken);
			cursor.move(3);
			if (cursor.getValue() >= tokens.size()) {
				return criteria;
			}

			Token nextToken = tokens.get(cursor.getValue());
			Class<Criteria> nextCriteriaClass = this.getCriteriaClass(nextToken.getExpr());
			if (!LogicalCriteria.class.isAssignableFrom(nextCriteriaClass)) {
				throw new BusinessException(ErrorCode.Not_Sopport_Criteria_Query);
			}

			cursor.move(1);
			Criteria rightCriteria = this.parse(boMeta, tokens, cursor);
			Criteria nextCriteria = this.newLogicalCriteria(boMeta, nextCriteriaClass, criteria, rightCriteria);
			return nextCriteria;

		}

		if (LogicalCriteria.class.isAssignableFrom(criteriaClass)) {
			if (!(leftToken instanceof ListToken)) {
				throw new BusinessException(ErrorCode.Not_Sopport_Criteria_Query);
			}

			Criteria leftCriteria = this.parse(boMeta, ((ListToken) leftToken).getTokens(), new Cursor());
			cursor.move(2);

			Criteria rightCriteria = this.parse(boMeta, tokens, cursor);
			Criteria nextCriteria = this.newLogicalCriteria(boMeta, criteriaClass, leftCriteria, rightCriteria);
			return nextCriteria;
		}

		throw new BusinessException(ErrorCode.Not_Sopport_Criteria_Query);

	}

	private Criteria newComparableCriteria(BoMeta boMeta, Class<Criteria> criteriaClass, Token propNameToken,
			Token propValueToken) throws BusinessException {
		PropertyMeta propMeta = this.getPropMeta(boMeta, propNameToken);
		Object propValue = null;
		if (!CollectionCriteria.class.isAssignableFrom(criteriaClass)) {
			propValue = this.getValue(propMeta, propValueToken);
		} else {
			propValue = this.getMultiValues(propMeta, propValueToken);
		}

		try {
			Constructor<Criteria> constructor = criteriaClass.getConstructor(PropertyMeta.class, Object.class);
			return constructor.newInstance(propMeta, propValue);
		} catch (Exception e) {
			throw new BusinessException(BoErrorCode.Bo_Query_Express_Parse_Failed);
		}
	}

	private Criteria newLogicalCriteria(BoMeta boMeta, Class<Criteria> criteriaClass, Criteria leftCriteria,
			Criteria rightCriteria) throws BusinessException {

		try {
			Constructor<Criteria> constructor = criteriaClass.getConstructor(Criteria.class, Criteria.class);
			return constructor.newInstance(leftCriteria, rightCriteria);
		} catch (Exception e) {
			throw new BusinessException(BoErrorCode.Bo_Query_Express_Parse_Failed);
		}
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

	private List<Object> getMultiValues(PropertyMeta propMeta, Token valueToken) throws BusinessException {
		ValueConverter valueConverter = ValueConverterFactory.getValueConverter(propMeta);
		if (valueConverter != null && valueToken instanceof StringToken) {
			return valueConverter.converterToList(valueToken.getExpr());
		}

		throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
	}
}
