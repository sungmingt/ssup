package com.ssup.backend.domain.language;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LanguageLevel {

    NATIVE("Native(원어민 수준)", "원어민처럼 자연스럽게 언어를 구사해요. 복잡한 표현, 은유, 속어, 전문 분야 언어까지 능숙하며, 문화적 뉘앙스까지 이해하고 상황에 맞춰 표현 가능해요."),
    ADVANCED(" Advanced(고급)", "전문적인 내용도 무리 없이 말하고 이해할 수 있어요. 다양한 문체·어조를 사용할 수 있고, 가끔 문법 실수는 있으나 의사소통에 전혀 지장이 없어요."),
    INTERMEDIATE("Intermediate(중급)", "일상 대화와 업무 기본 커뮤니케이션이 가능해요. 기본 문법과 표현은 정확하지만 복잡한 상황에서 말이 막힐 수 있어요."),
    ELEMENTARY("Elementary(초중급)", "기본적인 문장 구성이 가능해요. 단순한 일상 상황(소개, 구매, 질문 등)에서 의사소통 가능이 가능하지만, 구사하는 단어가 제한적이며 문법 오류가 잦아요."),
    BEGINNER("Beginner(초보)", "매우 기초적인 단어와 표현만 이해할 수 있어요. 자기소개, 숫자, 시간 등 한정된 주제에 대해서만 말할 수 있어요.");

    private final String name;
    private final String description;
}
