package com.xcodemap;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTools {

    @Tool("获取当前日期，返回格式为 yyyy-MM-dd")
    public String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Tool("计算两个日期之间的天数差。参数 startDate 和 endDate 的格式为 yyyy-MM-dd")
    public long daysBetweenDates(@P(name = "startDate", value = "start date") String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ChronoUnit.DAYS.between(start, end);
    }
}

