package cn.huangxulin.learning.threadpool;

/**
 * 子类重写父类的方法，子类方法的访问权限要大于等于父类方法的权限
 *
 * <pre>
 *     public     ->  public     √
 *     protected  ->  public     √
 *     public     ->  protected  ×
 *     protected  ->  private    ×
 * </pre>
 *
 * @author hxl
 */
public class Animal {

    protected void breathing() {
        System.out.println("动物在呼吸...");
    }
}
