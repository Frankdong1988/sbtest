package com.frank.sbtest.common.utils;

/**
 * @author tongyi
 * @date Sep 28, 2013
 */
public class StringUtils {
	
	public static String formatColumName(String name) {
		if(null == name) {
			return null;
		}
		if("".equals(name.trim())) {
			return "";
		}
		if(name.contains("_")) {
			String[] nameArray = name.split("_");
			name = "";
			for(int i=0; i<nameArray.length; i++) {
				name += charToUpperCase(nameArray[i], 0);
			}
			return charToLowerCase(name, 0);
		}
		return charToLowerCase(name, 0);
	}
	public static void main(String[] args) {
		System.out.println(formatColumName("a_ahsn"));
	}
	
	/**
	 * 指定字符大写
	 * @param name
	 * @param i 指定字符的下标，从0开始
	 * @return 如果指定位置的字母本身为大写，将直接返回原字符串
	 */
	public static String charToUpperCase(String name,int i){
		char[] cs=name.toCharArray();
		if(i>=cs.length){
			throw new RuntimeException("下标越界");
		}
		if(!String.valueOf(cs[i]).matches("^[a-zA-Z]$")){
			throw new RuntimeException("指定字符不属于英文字母范围");
		}
		if(((int)cs[i]) > 64 && ((int)cs[i]) < 91){
			return name;
		}
        cs[i]-=32;
        return String.valueOf(cs);
	}
	/**
	 * 指定字符小写
	 * @param name
	 * @param i 指定字符的下标，从0开始
	 * @return 如果指定位置的字母本身为小写，将直接返回原字符串
	 */
	public static String charToLowerCase(String name,int i){
		char[] cs=name.toCharArray();
		if(i>=cs.length){
			throw new RuntimeException("下标越界");
		}
		if(!String.valueOf(cs[i]).matches("^[a-zA-Z]$")){
			throw new RuntimeException("指定字符不属于英文字母范围");
		}
		if(((int)cs[i]) > 96 && ((int)cs[i]) < 123){
			return name;
		}
		cs[i]+=32;
		return String.valueOf(cs);
	}
	
	public static String toUpperCaseFirst(String value) {
		return Character.toUpperCase(value.charAt(0)) + value.substring(1);
	}
	public static String toLowCaseFirst(String value) {
		return Character.toLowerCase(value.charAt(0)) + value.substring(1);
	}
	public static boolean javaBoolean(String value) {
		return value.startsWith("tinyint(1)");
	}

	public static String toCamelCase(String str) {
		return new WordTokenizer() {
			@Override
			protected void startSentence(StringBuilder buffer, char ch) {
				buffer.append(Character.toLowerCase(ch));
			}

			@Override
			protected void startWord(StringBuilder buffer, char ch) {
				if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
					buffer.append(Character.toUpperCase(ch));
				} else {
					buffer.append(Character.toLowerCase(ch));
				}
			}

			@Override
			protected void inWord(StringBuilder buffer, char ch) {
				buffer.append(Character.toLowerCase(ch));
			}

			@Override
			protected void startDigitSentence(StringBuilder buffer, char ch) {
				buffer.append(ch);
			}

			@Override
			protected void startDigitWord(StringBuilder buffer, char ch) {
				buffer.append(ch);
			}

			@Override
			protected void inDigitWord(StringBuilder buffer, char ch) {
				buffer.append(ch);
			}

			@Override
			protected void inDelimiter(StringBuilder buffer, char ch) {
				if (ch != UNDERSCORE) {
					buffer.append(ch);
				}
			}
		}.parse(str);
	}

	/**
	 * 解析出下列语法所构成的<code>SENTENCE</code>。
	 * <p/>
	 * 
	 * <pre>
	 * SENTENCE = WORD (DELIMITER* WORD)*
	 * 
	 * WORD = UPPER_CASE_WORD | LOWER_CASE_WORD | TITLE_CASE_WORD | DIGIT_WORD
	 * 
	 * UPPER_CASE_WORD = UPPER_CASE_LETTER+
	 * LOWER_CASE_WORD = LOWER_CASE_LETTER+
	 * TITLE_CASE_WORD = UPPER_CASE_LETTER LOWER_CASE_LETTER+
	 * DIGIT_WORD = DIGIT+
	 * 
	 * UPPER_CASE_LETTER = Character.isUpperCase()
	 * LOWER_CASE_LETTER = Character.isLowerCase()
	 * DIGIT = Character.isDigit()
	 * NON_LETTER_DIGIT = !Character.isUpperCase() && !Character.isLowerCase() && !Character.isDigit()
	 * 
	 * DELIMITER = WHITESPACE | NON_LETTER_DIGIT
	 * </pre>
	 */
	private abstract static class WordTokenizer {
		protected static final char UNDERSCORE = '_';

		/** Parse sentence。 */
		public String parse(String str) {
			if (org.apache.commons.lang.StringUtils.isEmpty(str)) {
				return str;
			}

			int length = str.length();
			StringBuilder buffer = new StringBuilder(length);

			for (int index = 0; index < length; index++) {
				char ch = str.charAt(index);

				// 忽略空白。
				if (Character.isWhitespace(ch)) {
					continue;
				}

				// 大写字母开始：UpperCaseWord或是TitleCaseWord。
				if (Character.isUpperCase(ch)) {
					int wordIndex = index + 1;

					while (wordIndex < length) {
						char wordChar = str.charAt(wordIndex);

						if (Character.isUpperCase(wordChar)) {
							wordIndex++;
						} else if (Character.isLowerCase(wordChar)) {
							wordIndex--;
							break;
						} else {
							break;
						}
					}

					// 1. wordIndex == length，说明最后一个字母为大写，以upperCaseWord处理之。
					// 2. wordIndex == index，说明index处为一个titleCaseWord。
					// 3. wordIndex > index，说明index到wordIndex - 1处全部是大写，以upperCaseWord处理。
					if (wordIndex == length || wordIndex > index) {
						index = parseUpperCaseWord(buffer, str, index, wordIndex);
					} else {
						index = parseTitleCaseWord(buffer, str, index);
					}

					continue;
				}

				// 小写字母开始：LowerCaseWord。
				if (Character.isLowerCase(ch)) {
					index = parseLowerCaseWord(buffer, str, index);
					continue;
				}

				// 数字开始：DigitWord。
				if (Character.isDigit(ch)) {
					index = parseDigitWord(buffer, str, index);
					continue;
				}

				// 非字母数字开始：Delimiter。
				inDelimiter(buffer, ch);
			}

			return buffer.toString();
		}

		private int parseUpperCaseWord(StringBuilder buffer, String str, int index, int length) {
			char ch = str.charAt(index++);

			// 首字母，必然存在且为大写。
			if (buffer.length() == 0) {
				startSentence(buffer, ch);
			} else {
				startWord(buffer, ch);
			}

			// 后续字母，必为小写。
			for (; index < length; index++) {
				ch = str.charAt(index);
				inWord(buffer, ch);
			}

			return index - 1;
		}

		private int parseLowerCaseWord(StringBuilder buffer, String str, int index) {
			char ch = str.charAt(index++);

			// 首字母，必然存在且为小写。
			if (buffer.length() == 0) {
				startSentence(buffer, ch);
			} else {
				startWord(buffer, ch);
			}

			// 后续字母，必为小写。
			int length = str.length();

			for (; index < length; index++) {
				ch = str.charAt(index);

				if (Character.isLowerCase(ch)) {
					inWord(buffer, ch);
				} else {
					break;
				}
			}

			return index - 1;
		}

		private int parseTitleCaseWord(StringBuilder buffer, String str, int index) {
			char ch = str.charAt(index++);

			// 首字母，必然存在且为大写。
			if (buffer.length() == 0) {
				startSentence(buffer, ch);
			} else {
				startWord(buffer, ch);
			}

			// 后续字母，必为小写。
			int length = str.length();

			for (; index < length; index++) {
				ch = str.charAt(index);

				if (Character.isLowerCase(ch)) {
					inWord(buffer, ch);
				} else {
					break;
				}
			}

			return index - 1;
		}

		private int parseDigitWord(StringBuilder buffer, String str, int index) {
			char ch = str.charAt(index++);

			// 首字符，必然存在且为数字。
			if (buffer.length() == 0) {
				startDigitSentence(buffer, ch);
			} else {
				startDigitWord(buffer, ch);
			}

			// 后续字符，必为数字。
			int length = str.length();

			for (; index < length; index++) {
				ch = str.charAt(index);

				if (Character.isDigit(ch)) {
					inDigitWord(buffer, ch);
				} else {
					break;
				}
			}

			return index - 1;
		}

		protected boolean isDelimiter(char ch) {
			return !Character.isUpperCase(ch) && !Character.isLowerCase(ch) && !Character.isDigit(ch);
		}

		protected abstract void startSentence(StringBuilder buffer, char ch);

		protected abstract void startWord(StringBuilder buffer, char ch);

		protected abstract void inWord(StringBuilder buffer, char ch);

		protected abstract void startDigitSentence(StringBuilder buffer, char ch);

		protected abstract void startDigitWord(StringBuilder buffer, char ch);

		protected abstract void inDigitWord(StringBuilder buffer, char ch);

		protected abstract void inDelimiter(StringBuilder buffer, char ch);
	}
}
