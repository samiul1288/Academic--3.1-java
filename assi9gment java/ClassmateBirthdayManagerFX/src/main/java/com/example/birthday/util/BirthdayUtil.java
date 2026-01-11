package com.example.birthday.util;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;

public final class BirthdayUtil {
    private BirthdayUtil() {}

    // আজ থেকে সামনে যে জন্মদিনটা আসবে তার তারিখ (এই বছর বা পরের বছর)
    public static LocalDate nextOccurrence(LocalDate dob, LocalDate today) {
        MonthDay md = MonthDay.from(dob);
        LocalDate thisYear = md.atYear(Year.from(today).getValue());
        if (!thisYear.isBefore(today)) return thisYear;
        return md.atYear(Year.from(today).getValue() + 1);
    }
}
