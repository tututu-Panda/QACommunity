package com.qa.recommend.reWrite;

/**
 * Created by 3tu on 2019/5/13.
 *     由于无法识别停顿词，因此重写该文件
 *     具体在识别方法后面加上停顿词表
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.StopLibrary;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.util.StringUtil;

public class KeyWordComputer<T extends Analysis> {
    private static final Map<String, Double> POS_SCORE = new HashMap();
    private T analysisType;
    private int nKeyword = 5;

    public KeyWordComputer() {
    }

    public void setAnalysisType(T analysisType) {
        this.analysisType = analysisType;
    }

    public KeyWordComputer(int nKeyword) {
        this.nKeyword = nKeyword;
        this.analysisType = (T) new NlpAnalysis();
    }

    public KeyWordComputer(int nKeyword, T analysisType) {
        this.nKeyword = nKeyword;
        this.analysisType = analysisType;
    }

    private List<Keyword> computeArticleTfidf(String content, int titleLength) {
        Map<String, Keyword> tm = new HashMap();

//        List<Term> parse = this.analysisType.parseStr(content).getTerms();
//        Result terms = this.analysisType.parseStr(content);

        // 使用自定义字典
        Result terms = DicAnalysis.parse(content);
        // 加入停顿词识别
        List<Term> parse = terms.recognition(StopLibrary.get()).getTerms();

        Iterator var5 = parse.iterator();

        while(var5.hasNext()) {
            Term term = (Term)var5.next();
            double weight = this.getWeight(term, content.length(), titleLength);
            if(weight != 0.0D) {
                Keyword keyword = (Keyword)tm.get(term.getName());
                if(keyword == null) {
                    keyword = new Keyword(term.getName(), term.termNatures().allFreq, weight);
                    tm.put(term.getName(), keyword);
                } else {
                    keyword.updateWeight(1);
                }
            }
        }

        TreeSet<Keyword> treeSet = new TreeSet(tm.values());
        ArrayList<Keyword> arrayList = new ArrayList(treeSet);
        if(treeSet.size() <= this.nKeyword) {
            return arrayList;
        } else {
            return arrayList.subList(0, this.nKeyword);
        }
    }

    public List<Keyword> computeArticleTfidf(String title, String content) {
        if(StringUtil.isBlank(title)) {
            title = "";
        }

        if(StringUtil.isBlank(content)) {
            content = "";
        }

        return this.computeArticleTfidf(title + "\t" + content, title.length());
    }

    public List<Keyword> computeArticleTfidf(String content) {
        return this.computeArticleTfidf(content, 0);
    }

    private double getWeight(Term term, int length, int titleLength) {
        if(term.getName().trim().length() < 2) {
            return 0.0D;
        } else {
            String pos = term.natrue().natureStr;
            Double posScore = (Double)POS_SCORE.get(pos);
            if(posScore == null) {
                posScore = Double.valueOf(1.0D);
            } else if(posScore.doubleValue() == 0.0D) {
                return 0.0D;
            }

            return titleLength > term.getOffe()?5.0D * posScore.doubleValue():(double)(length - term.getOffe()) * posScore.doubleValue() / (double)length;
        }
    }

    static {
        POS_SCORE.put("null", Double.valueOf(0.0D));
        POS_SCORE.put("w", Double.valueOf(0.0D));
        POS_SCORE.put("en", Double.valueOf(0.0D));
        POS_SCORE.put("m", Double.valueOf(0.0D));
        POS_SCORE.put("num", Double.valueOf(0.0D));
        POS_SCORE.put("nr", Double.valueOf(3.0D));
        POS_SCORE.put("nrf", Double.valueOf(3.0D));
        POS_SCORE.put("nw", Double.valueOf(3.0D));
        POS_SCORE.put("nt", Double.valueOf(3.0D));
        POS_SCORE.put("l", Double.valueOf(0.2D));
        POS_SCORE.put("a", Double.valueOf(0.2D));
        POS_SCORE.put("nz", Double.valueOf(3.0D));
        POS_SCORE.put("v", Double.valueOf(0.2D));
        POS_SCORE.put("kw", Double.valueOf(6.0D));

        // 加入词性区权重
        POS_SCORE.put("b", Double.valueOf(0.3D));
    }
}
