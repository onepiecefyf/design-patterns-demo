package com.feng.snail.common.study.three;

/**
 * Redis开发与运维 第三章
 * 3.5 BitMaps
 */
public class BitMapsStudy {

  /**
   *
   * 1、在第一次初始化Bitmaps时，假如偏移 量非常大，那么整个初始化过程执行会比较慢，可能会造成Redis的阻塞。
   * 2、获取值 gitbit key offset 结果不存在直接返回0; 获取id=8的用户是否在2016-08-09这天访问过  gitbit unique:users:2016-08-09 8
   * 3、bitop op destkey key[key....]
   *    bitop是一个复合操作，它可以做多个Bitmaps的and（交集）、or（并集）、not（非）、xor（异或）操作并将结果保存在destkey中
   *    求交集
   *       bitop and unique:users:and:2016-04-04_03 unique: users:2016-04-03 unique:users:2016-04-03
   *    求并集
   *       bitop or unique:users:or:2016-04-04_03 unique: users:2016-04-03 unique:users:2016-04-03
   *    求总数
   *       bitcount unique:users:and:2016-04-04_03
   * 4、setbit unique:users:2016-04-05 0 1 保存用户信息到bitmaps 每个用户只占用1位
   *    使用hash类型每个占用64位 节省64倍的空间
   *
   *
   *
   */

}
