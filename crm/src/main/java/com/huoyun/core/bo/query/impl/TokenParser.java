package com.huoyun.core.bo.query.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.core.bo.query.Token;
import com.huoyun.exception.BusinessException;

public class TokenParser extends AbstractParser {
	private static final char LeftBracket = '(';
	private static final char RightBracket = ')';
	private static final char SingleQuotes = '\'';
	private static final char WhiteSpace = ' ';
	private final static List<String> FunctionList = new ArrayList<>();
	private final static List<String> CollectionFunctionList = new ArrayList<>();

	private List<Token> tokens = new ArrayList<>();
	private boolean isParsed = false;

	static {
		FunctionList.add("now");
		FunctionList.add("yesterday");
		FunctionList.add("tomorrow");

		CollectionFunctionList.add("in");
		CollectionFunctionList.add("between");
	}

	public TokenParser(String expr) {
		super(expr);
	}

	@Override
	public List<Token> getTokens() throws BusinessException {
		if (this.isParsed) {
			return this.tokens;
		}

		this.parse(this.getExpr(), new Cursor(), this.tokens);
		this.isParsed = true;
		return this.tokens;
	}

	private void pushStringToken(String expr, Cursor cursor, List<Token> tokens) {
		String subExpr = StringUtils.substring(expr, cursor.getStart(),
				cursor.getValue());
		tokens.add(new StringToken(subExpr));
		cursor.resetStart();
	}

	private void parse(String expr, Cursor cursor, List<Token> tokens)
			throws BusinessException {
		int quotesCounter = 0;

		while (cursor.getValue() < expr.length()) {
			char currentChar = expr.charAt(cursor.getValue());
			if (currentChar == SingleQuotes) {
				quotesCounter++;
			} else if (currentChar == LeftBracket) {
				if (quotesCounter % 2 == 0) {
					if (cursor.getStart() < cursor.getValue()) {
						this.pushStringToken(expr, cursor, tokens);
					}

					parseBracket(expr, cursor, tokens);
					continue;
				}
			} else if (currentChar == WhiteSpace) {
				if (quotesCounter % 2 == 0) {
					if (cursor.getStart() < cursor.getValue()) {
						this.pushStringToken(expr, cursor, tokens);
					}

					if (expr.charAt(cursor.getValue()) == WhiteSpace) {
						this.moveToUnWhitespaceChar(expr, cursor);
					}

					parse(expr, cursor, tokens);
				}
			}

			cursor.move(1);
		}

		if (cursor.getStart() < cursor.getValue()
				&& cursor.getValue() <= expr.length()) {
			this.pushStringToken(expr, cursor, tokens);
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
			String bracketContent = StringUtils.substring(subExpr, 1,
					subExpr.length() - 1);
			int startIndex = cursor.getStart();
			cursor.resetStart();
			if (tokens.size() > 0) {
				char lastChar = expr.charAt(startIndex - 1);
				Token lastToken = tokens.get(tokens.size() - 1);
				if (lastToken instanceof StringToken) {
					if (this.isCollection(lastToken.getExpr())) {
						this.putCollectionStringToken(lastChar, tokens,
								bracketContent);
						return;
					}

					if (this.isFunction(lastChar, lastToken.getExpr())) {
						this.putFunctionStringToken(lastToken, subExpr);
						return;
					}

				} else {
					throw new BusinessException(
							ErrorCode.Query_Expression_Parse_Failed);
				}
			}

			ListToken listToken = new ListToken(subExpr);
			tokens.add(listToken);
			this.parse(bracketContent, new Cursor(), listToken.getTokens());
			return;
		}

		throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);

	}

	private void putCollectionStringToken(char lastChar, List<Token> tokens,
			String expr) throws BusinessException {
		if (lastChar != WhiteSpace) {
			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

		tokens.add(new StringToken(expr));
	}

	private void putFunctionStringToken(Token lastToken, String expr)
			throws BusinessException {
		if (expr.length() != 2) {
			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

		lastToken.setExpr(lastToken.getExpr() + expr);
		return;

	}

	private boolean isFunction(char lastChar, String expr) {
		if (lastChar == WhiteSpace) {
			return false;
		}

		for (String func : FunctionList) {
			if (StringUtils.equalsIgnoreCase(func, expr)) {
				return true;
			}
		}

		return false;
	}

	private boolean isCollection(String expr) {
		for (String item : CollectionFunctionList) {
			if (StringUtils.equalsIgnoreCase(item, expr)) {
				return true;
			}
		}

		return false;
	}

}
