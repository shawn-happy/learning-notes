package com.shawn.study.deep.in.java.bean.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.CalendarValidator;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.apache.commons.validator.routines.TimeValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.junit.Test;

/** 案例来自于：https://juejin.cn/post/6994779782240010276 */
public class CommonsValidatorTests {

  @Test
  public void test() {
    // null 或 空
    System.out.println(GenericValidator.isBlankOrNull(""));
    System.out.println(GenericValidator.isBlankOrNull(null));

    // int，其他类型一样：byte,short，float，double，long
    System.out.println(GenericValidator.isInt("1"));

    // 日期
    System.out.println(GenericValidator.isDate("20200829", "yyyyMMdd", true));

    // int 在指定范围内，其他类型一样：byte,short，float，double，long
    System.out.println(GenericValidator.isInRange(1, 0, 2));

    // int 最大最小，其他类型一样：float，double，long
    System.out.println(GenericValidator.minValue(1, 1));
    System.out.println(GenericValidator.maxValue(1, 1));

    // 字符串 最大最小长度
    System.out.println(GenericValidator.maxLength("daodaotest", 10));
    System.out.println(GenericValidator.minLength("daodaotest", 10));

    // 正则表达式
    System.out.println(GenericValidator.matchRegexp("daodaotest", "^d.*t$"));

    // 信用卡验证
    System.out.println(GenericValidator.isCreditCard("6227612145830440"));

    // url
    System.out.println(GenericValidator.isUrl("http://www.baidu.com"));

    // email
    System.out.println(GenericValidator.isEmail("dao@test.com"));
  }

  @Test
  public void testCalendarValidator() {
    CalendarValidator calendarValidator = CalendarValidator.getInstance();
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, 1);
    System.out.println(calendarValidator.compareDates(calendar, Calendar.getInstance()));
    System.out.println(calendarValidator.compareMonths(calendar, Calendar.getInstance()));
    System.out.println(calendarValidator.compareQuarters(calendar, Calendar.getInstance()));
    System.out.println(calendarValidator.compareWeeks(calendar, Calendar.getInstance()));
    System.out.println(calendarValidator.compareYears(calendar, Calendar.getInstance()));
  }

  @Test
  public void testCreditCardValidator() {
    CreditCardValidator creditCardValidator = new CreditCardValidator();
    System.out.println(creditCardValidator.isValid("6227612145830440"));
    System.out.println(creditCardValidator.validate("6227612"));
  }

  @Test
  public void testDateValidator() {
    DateValidator dateValidator = DateValidator.getInstance();
    String pattern = "yyyy-MM-dd";
    // 验证
    System.out.println(dateValidator.isValid("2021-07-22", pattern));
    // 验证/转换日期，不符合格式则返回空
    System.out.println(dateValidator.validate("2021-07-22", pattern));

    Date date =
        Date.from(
            LocalDate.now()
                .plusMonths(1)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    System.out.println(dateValidator.compareDates(new Date(), date, null));
    System.out.println(dateValidator.compareMonths(new Date(), date, null));
    System.out.println(dateValidator.compareQuarters(new Date(), date, null));
    System.out.println(dateValidator.compareWeeks(new Date(), date, null));
    System.out.println(dateValidator.compareYears(new Date(), date, null));
  }

  @Test
  public void testDomainValidator() {
    DomainValidator domainValidator = DomainValidator.getInstance();
    System.out.println(domainValidator.isValid("commons.apache.org"));
    System.out.println(domainValidator.isValid("https://commons.apache.org"));
    System.out.println(domainValidator.isValid("commons.apache.org/"));
  }

  @Test
  public void testEmailValidator() {
    EmailValidator emailValidator = EmailValidator.getInstance();
    System.out.println(emailValidator.isValid("duleilewuhen@sina.com"));
  }

  @Test
  public void testInetAddressValidator() {
    InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
    System.out.println(inetAddressValidator.isValid("192.168.1.1"));
    System.out.println(inetAddressValidator.isValid("CDCD:910A:2222:5498:8475:1111:3900:2020"));
    System.out.println(inetAddressValidator.isValidInet4Address("192.168.1.1"));
    System.out.println(inetAddressValidator.isValidInet6Address("fe80::ecea:feaf:cc50:7439%13"));
  }

  @Test
  public void testRegexValidator() {
    // 设置参数
    String regex1 = "^([A-Z]*)(?:\\-)([A-Z]*)*$";
    String regex2 = "^([A-Z]*)$";
    // 创建验证
    RegexValidator validator02 = new RegexValidator(new String[] {regex1, regex2}, false);
    // 验证返回boolean
    System.out.println("valid: " + validator02.isValid("abc-def"));
    // 验证返回字符串
    System.out.println("result: " + validator02.validate("abc-def"));
    // 验证返回数组
    System.out.println(String.join("**", validator02.match("abc-def")));
  }

  @Test
  public void testTimeValidator() {
    TimeValidator timeValidator = TimeValidator.getInstance();
    LocalDateTime localDateTime = LocalDateTime.now().minusHours(12);
    Calendar localDateTimeCalendar =
        GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    System.out.println(timeValidator.compareHours(localDateTimeCalendar, Calendar.getInstance()));
    System.out.println(timeValidator.compareMinutes(localDateTimeCalendar, Calendar.getInstance()));
    System.out.println(timeValidator.compareSeconds(localDateTimeCalendar, Calendar.getInstance()));
    System.out.println(timeValidator.compareTime(localDateTimeCalendar, Calendar.getInstance()));
  }

  @Test
  public void testUrlValidator() {
    UrlValidator urlValidator = UrlValidator.getInstance();
    System.out.println(urlValidator.isValid("https://commons.apache.org/"));
    System.out.println(urlValidator.isValid("https://commons.apache.org"));
    System.out.println(urlValidator.isValid("commons.apache.org/"));
    System.out.println(urlValidator.isValid("commons.apache.org"));
  }
}
