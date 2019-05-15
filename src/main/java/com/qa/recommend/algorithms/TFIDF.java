package com.qa.recommend.algorithms;


import com.qa.recommend.reWrite.KeyWordComputer;
import com.qa.recommend.reWrite.Keyword;
import org.ansj.library.DicLibrary;
import org.ansj.library.StopLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

/**
 * Created by 3tu on 2019/5/9.
 */
public class TFIDF {

    /**
     *
     * @param content 文本内容
     * @param keyNums 返回的关键词数目
     * @return
     */
    public  List<Keyword> getTFIDF(String content, int keyNums) {

        // 使用停顿词表，重写方法，在里面调用
        StopLibrary.insertStopWords(StopLibrary.DEFAULT);
        DicLibrary.insert(DicLibrary.DEFAULT,"nio");

        ToAnalysis to = new ToAnalysis();
        KeyWordComputer kwc = new KeyWordComputer(keyNums,to);
        return kwc.computeArticleTfidf(content);
    }
}
