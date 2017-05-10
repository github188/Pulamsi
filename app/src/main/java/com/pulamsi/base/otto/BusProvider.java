package com.pulamsi.base.otto;


import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * otto evenbus单例提供类
 *
 * Created by WilliamChik on 15/7/15.
 */
public class BusProvider {

  /** 事件调用默认在主线程中执行 */
  private static final Bus bus = new Bus(ThreadEnforcer.MAIN);

  private BusProvider() {

  }

  public static Bus getInstance() {
    return bus;
  }
}
