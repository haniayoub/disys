package diSys.Common;

/**
 * Executer interface each executer in the system should implement this
 * @author saeed
 *
 * @param <T>
 * @param <R>
 */
public interface IExecutor<T extends Item,R extends Item> {
	 public R run(T task) throws Exception;
	 @SuppressWarnings("unchecked")
	public T GetTaskClass();
}
