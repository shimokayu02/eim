package sample.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.mutable.MutableInt;

import sample.SkipException;

/**
 * <p>
 * <b>カタカナをローマ字に変換するutilの作成　[Java]</b></p>
 * <ul>
 * <li>全角カナのみを考慮し、半角カナが連携される事を想定しない。</li>
 * <li>例外無しにカタカナへ変換。変換できない時は`null`</li>
 * <li>訓令式でなくヘボン式を優先して採用。</li>
 * <li>「ッ」は、次の音の子音字を重ねる。<br>
 * 　ただし、`cch`というように`ch`が続く場合は`c`でなく`t`を用いて`tch`とする。<br>
 * 　ex) 「ビッチュウマツヤマジョウ」bi<font color="Red">tch</font>umatsuyamajo</li>
 * <li>「ン」は`n`で統一。<br>
 * 　ただし、`b`，`p`，`m`の前は`n`でなく`m`を使う。<br>
 * 　ex) 「シンバシ」shi<font color="Red">mb</font>ashi，「テンプラ」te<font color="Red">mp</font>ura，「テンマングウ」te<font color="Red">mm</font>angu</li>
 * <li>ウ段とオ段の長音に対応する。<br>
 * 　ただし、オ段は`ou`のみの対応とし、`oo`の場合、そのまま表記する。また、`h`は付与しない。<br>
 * 　ex) 「オオヤ」<font color="Red">oo</font>ya</li>
 * </ul>
 *
 * <p>
 * 参考リンク
 * <ul>
 * <li>「<a href="https://ja.wikipedia.org/wiki/%E3%83%AD%E3%83%BC%E3%83%9E%E5%AD%97">ローマ字 - Wikipedia</a>」
 * <li>「<a href="https://lazesoftware.com/tool/hepburn/">ヘボン式ローマ字変換ツール - LAZE SOFTWARE</a>」
 * <li>「<a href="https://javascript.programmer-reference.com/js-regexp-sample/">[JavaScript] 正規表現パターンサンプル集 | コピペで使える JavaScript逆引きリファレンス</a>」
 * <li>「<a href="https://www.tactsystem.co.jp/blog/post-94/">正規表現でカタカナを検索 | CreatorsBlog｜タクトシステム株式会社</a>」
 * <li>「<a href="https://teratail.com/questions/143973">Java - 平仮名からローマ字への変換｜teratail</a>」
 * </ul>
 *
 * @see sample.model.constraints.Kana
 */
public abstract class KanaToLatin {

	public static void main(String[] args) {

		System.out.println(String.format("ショウボウシャ:%s", convert("ショウボウシャ"))); // shobosha，消防車
		System.out.println(String.format("ワイングラス:%s", convert("ワイングラス"))); // waingurasu

		System.out.println(String.format("ビッチュウマツヤマジョウ:%s", convert("ビッチュウマツヤマジョウ"))); // bitchumatsuyamajo，備中松山城

		System.out.println(String.format("シンバシ:%s", convert("シンバシ"))); // shimbashi，新橋
		System.out.println(String.format("テンプラ:%s", convert("テンプラ"))); // tempura
		System.out.println(String.format("テンマングウ:%s", convert("テンマングウ"))); // temmangu，天満宮

		System.out.println(String.format("オオヤ:%s", convert("オオヤ"))); // ooya，大家
	}

	/** 全角カタカナをローマ字に変換します。 */
	public static String convert(String kana) {
		try {
			if (!kana.matches("^[ァ-イウ-ロワヲンヴ・ー]+$")) {
				throw new SkipException(String.format("Could not convert \"%s\" to Romaji.", kana));
			}
			if (kanaExToLatinMap().containsKey(kana)) {
				return kanaExToLatinMap().get(kana);
			}
			List<String> strList = new ArrayList<>();
			Map<Integer, String> tmpLowerMap = new HashMap<>();
			MutableInt index = new MutableInt(0);
			splitToList(new StringBuilder(kana).reverse().toString()).stream().forEach(x -> {
				if (LOWER.contains(x)) {
					tmpLowerMap.put(index.getAndDecrement(), x);
				} else if (x.equals("ッ")) {
					strList.add(tmpY(index.getValue(), strList));
				} else if (x.equals("ー")) {
					index.decrement();
				} else if (x.equals("・")) {
					strList.add(" ");
				} else {
					strList.add(kanaToLatinMap().get(tmpX(index.getValue(), x, tmpLowerMap)));
				}
				index.increment();
			});
			return strList.stream().reduce((a, b) -> b.concat(a)).get()
					.replaceAll("ou", "o")
					.replaceAll("u+", "u")
					.replaceAll("nb", "mb")
					.replaceAll("np", "mp")
					.replaceAll("nm", "mm")
					.replaceAll("cch", "tch");
		} catch (Exception e) {
			// 変換できない時はnull
			return null;
		}
	}

	private static final List<String> LOWER = Arrays.asList("ァ", "ィ", "ェ", "ォ", "ャ", "ュ", "ョ");

	private static String tmpX(int indexKey, String x, Map<Integer, String> tmpLowerMap) {
		return x + tmpLowerMap.getOrDefault(indexKey, "");
	}

	private static String tmpY(int indexKey, List<String> strList) {
		return strList.get(indexKey - 1).substring(0, 1);
	}

	private static List<String> splitToList(String str) {
		return Stream.of(str.split("")).collect(Collectors.toList());
	}

	private static Map<String, String> kanaToLatinMap() {

		Map<String, String> kanaToLatinMap = new HashMap<>();

		kanaToLatinMap.put("ア", "a");
		kanaToLatinMap.put("イ", "i");
		kanaToLatinMap.put("ウ", "u");
		kanaToLatinMap.put("エ", "e");
		kanaToLatinMap.put("オ", "o");

		kanaToLatinMap.put("カ", "ka");		kanaToLatinMap.put("ガ", "ga");
		kanaToLatinMap.put("キ", "ki");		kanaToLatinMap.put("ギ", "gi");
		kanaToLatinMap.put("ク", "ku");		kanaToLatinMap.put("グ", "gu");		kanaToLatinMap.put("クァ", "kwa");	kanaToLatinMap.put("グァ", "gwa");
		kanaToLatinMap.put("ケ", "ke");		kanaToLatinMap.put("ゲ", "ge");
		kanaToLatinMap.put("コ", "ko");		kanaToLatinMap.put("ゴ", "go");
		kanaToLatinMap.put("キャ", "kya");	kanaToLatinMap.put("ギャ", "gya");
		kanaToLatinMap.put("キュ", "kyu");	kanaToLatinMap.put("ギュ", "gyu");
		kanaToLatinMap.put("キョ", "kyo");	kanaToLatinMap.put("ギョ", "gyo");

		kanaToLatinMap.put("サ", "sa");		kanaToLatinMap.put("ザ", "za");
		kanaToLatinMap.put("シ", "shi");	kanaToLatinMap.put("ジ", "ji");
		kanaToLatinMap.put("ス", "su");		kanaToLatinMap.put("ズ", "zu");
		kanaToLatinMap.put("セ", "se");		kanaToLatinMap.put("ゼ", "ze");
		kanaToLatinMap.put("ソ", "so");		kanaToLatinMap.put("ゾ", "zo");
		kanaToLatinMap.put("シャ", "sha");	kanaToLatinMap.put("ジャ", "ja");
		kanaToLatinMap.put("シュ", "shu");	kanaToLatinMap.put("ジュ", "ju");
		kanaToLatinMap.put("ショ", "sho");	kanaToLatinMap.put("ジョ", "jo");

		kanaToLatinMap.put("タ", "ta");		kanaToLatinMap.put("ダ", "da");
		kanaToLatinMap.put("チ", "chi");	kanaToLatinMap.put("ヂ", "di");
		kanaToLatinMap.put("ツ", "tsu");	kanaToLatinMap.put("ヅ", "du");
		kanaToLatinMap.put("テ", "te");		kanaToLatinMap.put("デ", "de");
		kanaToLatinMap.put("ト", "to");		kanaToLatinMap.put("ド", "do");
		kanaToLatinMap.put("チャ", "cha");	kanaToLatinMap.put("ヂャ", "dya");
		kanaToLatinMap.put("チュ", "chu");	kanaToLatinMap.put("ヂュ", "dyu");
		kanaToLatinMap.put("チョ", "cho");	kanaToLatinMap.put("ヂョ", "dyo");

		kanaToLatinMap.put("ナ", "na");
		kanaToLatinMap.put("ニ", "ni");
		kanaToLatinMap.put("ヌ", "nu");
		kanaToLatinMap.put("ネ", "ne");
		kanaToLatinMap.put("ノ", "no");
		kanaToLatinMap.put("ニャ", "nya");
		kanaToLatinMap.put("ニュ", "nyu");
		kanaToLatinMap.put("ニョ", "nyo");

		kanaToLatinMap.put("ハ", "ha");		kanaToLatinMap.put("バ", "ba");		kanaToLatinMap.put("パ", "pa");
		kanaToLatinMap.put("ヒ", "hi");		kanaToLatinMap.put("ビ", "bi");		kanaToLatinMap.put("ピ", "pi");
		kanaToLatinMap.put("フ", "fu");		kanaToLatinMap.put("ブ", "bu");		kanaToLatinMap.put("プ", "pu");
		kanaToLatinMap.put("ヘ", "he");		kanaToLatinMap.put("ベ", "be");		kanaToLatinMap.put("ペ", "pe");
		kanaToLatinMap.put("ホ", "ho");		kanaToLatinMap.put("ボ", "bo");		kanaToLatinMap.put("ポ", "po");
		kanaToLatinMap.put("ヒャ", "hya");	kanaToLatinMap.put("ビャ", "bya");	kanaToLatinMap.put("ピャ", "pya");
		kanaToLatinMap.put("ヒュ", "hyu");	kanaToLatinMap.put("ビュ", "byu");	kanaToLatinMap.put("ピュ", "pyu");
		kanaToLatinMap.put("ヒョ", "hyo");	kanaToLatinMap.put("ビョ", "byo");	kanaToLatinMap.put("ピョ", "pyo");

		kanaToLatinMap.put("マ", "ma");
		kanaToLatinMap.put("ミ", "mi");
		kanaToLatinMap.put("ム", "mu");
		kanaToLatinMap.put("メ", "me");
		kanaToLatinMap.put("モ", "mo");
		kanaToLatinMap.put("ミャ", "mya");
		kanaToLatinMap.put("ミュ", "myu");
		kanaToLatinMap.put("ミョ", "myo");

		kanaToLatinMap.put("ヤ", "ya");
		kanaToLatinMap.put("ユ", "yu");
		kanaToLatinMap.put("ヨ", "yo");

		kanaToLatinMap.put("ラ", "ra");
		kanaToLatinMap.put("リ", "ri");
		kanaToLatinMap.put("ル", "ru");
		kanaToLatinMap.put("レ", "re");
		kanaToLatinMap.put("ロ", "ro");
		kanaToLatinMap.put("リャ", "rya");
		kanaToLatinMap.put("リュ", "ryu");
		kanaToLatinMap.put("リョ", "ryo");

		kanaToLatinMap.put("ワ", "wa");
		kanaToLatinMap.put("ヲ", "wo");
		kanaToLatinMap.put("ン", "n");

		kanaToLatinMap.put("ヴァ", "va");	kanaToLatinMap.put("ヴィ", "vi");	kanaToLatinMap.put("ヴ", "vu");
		kanaToLatinMap.put("ヴェ", "ve");	kanaToLatinMap.put("ヴォ", "vo");

		kanaToLatinMap.put("ファ", "fa");	kanaToLatinMap.put("フィ", "fi");	kanaToLatinMap.put("フェ", "fe");	kanaToLatinMap.put("フォ", "fo");

		return kanaToLatinMap;
	}

	private static Map<String, String> kanaExToLatinMap() {
		Map<String, String> kanaExToLatinMap = new HashMap<>();
		kanaExToLatinMap.put("ヘルメス", "hermes");
		kanaExToLatinMap.put("オオノ", "ohno");
		kanaExToLatinMap.put("イノウエ", "inoue");
		return kanaExToLatinMap;
	}

}
