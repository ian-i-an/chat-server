package com.example.chatserver.global.utils;

import java.util.concurrent.ThreadLocalRandom;

public final class NicknameGenerator {
    private NicknameGenerator() {
        throw new AssertionError("이 클래스는 인스턴스화할 수 없습니다.");
    }

    private static final String[] ADJECTIVES = {
            "귀여운", "화려한", "배고픈", "졸린", "신나는",
            "용감한", "행복한", "춤추는", "멋진", "게으른",
            "재빠른", "조용한", "똑똑한", "엉뚱한", "소심한", "빠른",
            "포근한", "말랑한", "바쁜", "여유로운", "반짝이는",
            "투덜대는", "친절한", "다정한", "시크한", "쾌활한",
            "늠름한", "수줍은", "엉성한", "새침한", "느긋한",
            "따뜻한", "시원한", "몽글몽글한", "씩씩한", "아담한",
            "둥근", "네모난", "길쭉한", "납작한", "뾰족한","동그란",
            "매끄러운", "거친", "단단한", "푹신한", "투명한",
            "반짝이는", "빛나는", "새콤한", "달콤한", "고소한",
            "매운","울적한","웃긴", "까만", "새하얀", "푸른색의", "붉은",
            "짭짤한", "향기로운", "차가운", "뜨거운", "무거운","나쁜",
            "가벼운", "커다란", "조그만", "낡은", "새로운","오동통통한", "움직이는", "뛰는", "기어가는", "매달린"
    };

    private static final String[] NOUNS = {
            "나무늘보", "알파카", "고양이", "강아지", "토끼",
            "다람쥐", "펭귄", "햄스터", "거북이", "병아리","자라",
            "판다", "곰돌이", "수달", "쿼카", "미어캣",
            "사자", "호랑이", "여우", "늑대", "코끼리",
            "기린", "원숭이", "하마", "악어", "참새",
            "까마귀", "비둘기", "오리", "백조", "독수리",
            "부엉이", "고슴도치", "두더지", "카멜레온", "코알라",
            "고래", "돌고래", "물고기", "펠리컨", "사막여우", "북극여우","포도", "딸기","사과",
            "청포도", "수박", "참외", "복숭아", "자두", "귤",
            "오렌지", "바나나", "파인애플", "망고", "체리",
            "레몬", "모과", "감자", "고구마", "양파",
            "당근", "토마토", "오이", "가지", "호박"
    };

    public static String generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];


        int number = random.nextInt(1, 99);

        return adjective + " " + noun + number;
    }
}