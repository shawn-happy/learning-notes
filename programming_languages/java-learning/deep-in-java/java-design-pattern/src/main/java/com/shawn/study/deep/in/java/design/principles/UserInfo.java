package com.shawn.study.deep.in.java.design.principles;

import java.util.Date;

/**
 * @author Shawn
 * @description
 * @since 2020/7/5
 */
public class UserInfo {

  private int id;
  private String username;
  private String realname;
  private String password;
  private String email;
  private String phone;
  private Date createTime;
  private Date updateTime;
  /** 省 */
  private String provinceOfAddress;

  /** 市 */
  private String cityOfAddress;

  /** 区 */
  private String regionOfAddress;

  /** 详细地址 */
  private String detailOfAddress;
}
