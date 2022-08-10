package com.shawn.study.deep.in.java.design.jdbc.v1;

import static com.shawn.study.deep.in.java.design.jdbc.v1.JdbcOperator.delete;
import static com.shawn.study.deep.in.java.design.jdbc.v1.JdbcOperator.printMsg;
import static com.shawn.study.deep.in.java.design.jdbc.v1.JdbcOperator.queryAllCoffees;
import static com.shawn.study.deep.in.java.design.jdbc.v1.JdbcOperator.queryCoffeeById;
import static com.shawn.study.deep.in.java.design.jdbc.v1.JdbcOperator.save;
import static com.shawn.study.deep.in.java.design.jdbc.v1.JdbcOperator.update;

import com.shawn.study.deep.in.java.design.jdbc.entity.Coffee;
import java.util.Date;

/**
 * v1 test class
 *
 * @author shawn
 * @since 2020/8/16
 */
public class JdbcOperatorTestMain {

  public static void main(String[] args) {
    printMsg("test select all");
    queryAllCoffees();
    printMsg("test select by id = 10");
    queryCoffeeById(10);
    printMsg("test delete by id = 10");
    delete(10);
    printMsg("test select by id = 10");
    queryCoffeeById(10);
    Date date = new Date();
    Coffee coffee = new Coffee(10, date, date, "IceCoffee", 20);
    printMsg("test insert");
    save(coffee);
    printMsg("test select by id = 2");
    Coffee coffee1 = queryCoffeeById(2);
    coffee1.setUpdateTime(new Date());
    coffee1.setPrice(25);
    printMsg("test update");
    update(coffee1);
    printMsg("test select by id = 2");
    queryCoffeeById(2);
  }
}
