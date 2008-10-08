package Common;


public interface IExecutor<T extends Item,R extends Item> {
	 public R run(T task);
}
