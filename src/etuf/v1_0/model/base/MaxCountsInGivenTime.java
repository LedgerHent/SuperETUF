package etuf.v1_0.model.base;

public class MaxCountsInGivenTime {

	private int seconds = 60;
	private int counts = 1;
	private String target = "Default";

	/**
	 * 无参构造函数 默认60秒之内只允许一次请求
	 */
	public MaxCountsInGivenTime() {
	}

	/**
	 * 有参构造函数，在指定的时间内，允许请求的次数
	 * @param seconds 时间，秒
	 * @param counts 次数
	 * @param target 访问目标
	 */
	public MaxCountsInGivenTime(int seconds, int counts, String target) {
		setSeconds(seconds);
		setCounts(counts);
		setTarget(target);
	}

	/**
	 * @return 时间，秒
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * 设置时间，秒
	 * @param seconds 时间，秒
	 */
	public void setSeconds(int seconds) {
		if (seconds > 0 && seconds != this.seconds) {
			this.seconds = seconds;
		}
	}

	/**
	 * 
	 * @return 次数
	 */
	public int getCounts() {
		return counts;
	}

	/**
	 * 设置次数
	 * @param counts 
	 */
	public void setCounts(int counts) {
		if (counts > 0 && counts != this.counts) {
			this.counts = counts;
		}
	}

	/**
	 * 
	 * @return 访问目标标识（必须是唯一）
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * 设置访问目标标识
	 * @param target（必须是唯一）
	 */
	public void setTarget(String target) {
		if (target != null && !"".equals(target) && !this.target.equals(target)) {
			this.target = target;
		}
	}

}
