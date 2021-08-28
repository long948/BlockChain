package com.block.xjfkchain.data;

import com.chad.library.adapter.base.entity.SectionEntity;

public class ExamCourseSection extends SectionEntity<InComeEntity> {
    public ExamCourseSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ExamCourseSection(InComeEntity s) {
        super(s);
    }
}
