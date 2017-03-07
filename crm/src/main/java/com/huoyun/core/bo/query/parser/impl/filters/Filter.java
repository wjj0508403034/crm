package com.huoyun.core.bo.query.parser.impl.filters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.query.criteria.And;
import com.huoyun.core.bo.query.criteria.CriteriaExpr;
import com.huoyun.core.bo.query.parser.ErrorCode;
import com.huoyun.core.bo.query.parser.Token;
import com.huoyun.core.bo.query.parser.impl.AbstractParser;
import com.huoyun.core.bo.query.parser.impl.ListToken;
import com.huoyun.core.bo.query.parser.impl.StringToken;
import com.huoyun.exception.BusinessException;

public class Filter extends AbstractParser {

	public static final String Name = "$filter";
	private static final char LeftBracket = '(';
	private static final char RightBracket = ')';
	private static final char SingleQuotes = '\'';
	private static final char WhiteSpace = ' ';

	private List<Token> tokens = new ArrayList<>();

	public Filter(String expr) {
		super(expr);
	}

	@Override
	public void parser() throws BusinessException {
		this.parseExpr(this.getExpr(), new Cursor(), this.tokens);
		if (this.tokens.size() % 3 != 0) {
			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

		List<CriteriaExpr> criterias = new ArrayList<>();
		Cursor cursor = new Cursor();
		while (cursor.getValue() < this.tokens.size()) {
			Token token = this.tokens.get(cursor.getValue() + 1);
			if (token instanceof StringToken) {
				String op = token.getExpr().toLowerCase();
				switch (op) {
				case "and":
				case "or":
					List<CriteriaExpr> left = this.parse(this.tokens.get(0));
					List<CriteriaExpr> right = this.parse(this.tokens
							.get(cursor.getValue() + 2));
					if (StringUtils.equals(op, "and")) {
						criterias.add(new And(left, right));
					}else{
						
					}
				case "eq":
				case "ne":
				case "le":
				case "ge":
				case "like":

				}

			} else {
				throw new BusinessException(
						ErrorCode.Query_Expression_Parse_Failed);
			}
			cursor.move(3);
		}
	}

	private List<CriteriaExpr> parse(Token token) {
		return null;
	}

	private void parseExpr(String expr, Cursor cursor, List<Token> tokens)
			throws BusinessException {
		int quotesCounter = 0;

		while (cursor.getValue() < expr.length()) {
			char currentChar = expr.charAt(cursor.getValue());
			if (currentChar == SingleQuotes) {
				quotesCounter++;
			} else if (currentChar == LeftBracket) {
				if (quotesCounter % 2 == 0) {
					parseBracket(expr, cursor, tokens);
					continue;
				}
			} else if (currentChar == WhiteSpace) {
				if (quotesCounter % 2 == 0) {
					if (cursor.getStart() < cursor.getValue()) {
						String subExpr = StringUtils.substring(expr,
								cursor.getStart(), cursor.getValue());
						tokens.add(new StringToken(subExpr));
						cursor.resetStart();
					}

					if (expr.charAt(cursor.getValue()) == WhiteSpace) {
						this.moveToUnWhitespaceChar(expr, cursor);
					}

					parseExpr(expr, cursor, tokens);
				}
			}

			cursor.move(1);
		}

		if (cursor.getStart() < cursor.getValue()
				&& cursor.getValue() <= expr.length()) {
			String subExpr = StringUtils.substring(expr, cursor.getStart(),
					cursor.getValue());
			tokens.add(new StringToken(subExpr));
			cursor.resetStart();
		}
	}

	private void moveToUnWhitespaceChar(String expr, Cursor cursor) {
		while (cursor.getValue() < expr.length()) {
			cursor.move(1);
			if (expr.charAt(cursor.getValue()) != WhiteSpace) {
				break;
			}
		}

		cursor.resetStart();
	}

	private void parseBracket(String expr, Cursor cursor, List<Token> tokens)
			throws BusinessException {
		int start = cursor.getValue();
		int end = -1;
		int quotesCounter = 0;
		int leftBracketCounter = 0;
		int rightBracketCounter = 0;
		while (cursor.getValue() < expr.length()) {
			char currentChar = expr.charAt(cursor.getValue());
			cursor.move(1);
			if (currentChar == SingleQuotes) {
				quotesCounter++;
			} else if (currentChar == RightBracket) {
				if (quotesCounter % 2 == 0) {
					rightBracketCounter++;
					if (rightBracketCounter == leftBracketCounter) {
						end = cursor.getValue();
						break;
					}
				}
			} else if (currentChar == LeftBracket) {
				if (quotesCounter % 2 == 0) {
					leftBracketCounter++;
				}
			}
		}

		if (end != -1 && cursor.getValue() > start) {
			String subExpr = StringUtils.substring(expr, start,
					cursor.getValue());
			cursor.resetStart();
			ListToken listToken = new ListToken(subExpr);
			tokens.add(listToken);
			this.parseExpr(
					StringUtils.substring(subExpr, 1, subExpr.length() - 1),
					new Cursor(), listToken.getTokens());
			return;
		}

		throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);

	}

}
