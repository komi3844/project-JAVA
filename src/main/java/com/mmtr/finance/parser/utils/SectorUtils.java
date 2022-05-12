package com.mmtr.finance.parser.utils;

import java.util.Map;

public interface SectorUtils {
    static String getTranslate(String english) {
        Map<String, String> mapSectors = MapFromArrays.convert(
                new String[]{"Basic Materials", "Communication Services", "Consumer Cyclical",
                        "Consumer Defensive", "Energy", "Financial",
                        "Healthcare", "Industrials", "Real Estate",
                        "Technology", "Utilities"},
                new String[]{"Основные материалы", "Коммуникационные услуги", "Потребительский (цикличный)",
                        "Потребительский (защитный)", "Энергетика", "Финансы",
                        "Здравоохранение", "Промышленность", "Недвижимость",
                        "Технологии", "Коммунальные услуги"});
        return mapSectors.get(english);
    }
}
