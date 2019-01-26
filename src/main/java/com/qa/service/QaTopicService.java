package com.qa.service;


import com.qa.entity.QaTopic;

import java.util.List;

public interface QaTopicService {
    public List<QaTopic> findAllTopic();
    public boolean DeleteTopic(int i);
    public void UpdateTopic(QaTopic qaTopic);
    public void AddTopic(QaTopic qaTopic);
}
