package com.javablog.smsplatform.search.util;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class IKAnalyzerUtil {
    public static List segment(String text) throws IOException {
        List<String> list = new ArrayList<>();
        StringReader re = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(re, true);
        Lexeme lex;
        while ((lex = ik.next()) != null) {
            list.add(lex.getLexemeText());
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        String str = "中车青岛四方车辆研究所有限公司副总经理陈凯表示，“青岛地铁6号线的智能化程度是目前国内最好的，甚至可以" +
                "说是世界一流水平，加上系统全自主化研制，未来将成为新轨道交通样板，成为新的‘中国名片’也当之无愧。";
        System.out.println(segment(str));
    }
}
