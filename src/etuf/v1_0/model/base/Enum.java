package etuf.v1_0.model.base;

/**
 * 通用枚举
 * 
 * @author lxd
 * 
 */
public class Enum {

	/**
	 * 业务操作类型
	 * @author lxd
	 * 
	 */
	public enum BussinessOperateType {
		Add(1), Update(2), Delete(3);

		private final int value;

		public int getOperateType() {
			return value;
		}

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		BussinessOperateType(int value) {
			this.value = value;
		}
	}

	/**
	 * 数据序列化方式枚举
	 * @author lxd
	 * 
	 */
	public enum DataFormat {
		JSON(1), XML(2);

		private final int value;

		public int getDataFormatType() {
			return value;
		}

		DataFormat(int value) {
			this.value = value;
		}

	}

	/**
	 * 返回值状态枚举
	 * @author lxd
	 * 
	 */
	public enum StatusType {
		Succeed(0), Error(1);

		private final int value;

		public int getStatus() {
			return value;
		}

		StatusType(int value) {
			this.value = value;
		}

	}

	/**
	 * 乘客类型枚举
	 * @author lxd
	 * 
	 */
	public enum PassengerType {
		成人(1), 儿童(2), 婴儿(3);

		private final int value;

		public int getPassengerType() {
			return value;
		}

		PassengerType(int value) {
			this.value = value;
		}

	}

	/**
	 * 性别枚举
	 * @author lxd
	 * 
	 */
	public enum Sex {
		未知(0), 男(1), 女(2), 保密(3);
		private final int value;

		public int getSex() {
			return value;
		}

		Sex(int value) {
			this.value = value;
		}

	}
	
	/**
	 * 航程类型枚举
	 * @author lxd
	 * 
	 */
	public enum FlyType {
		单程(1), 往返(2), 联程(3);
		private final int value;

		public int getFlyType() {
			return value;
		}

		FlyType(int value) {
			this.value = value;
		}

	}
	
	/**
	 * 客户端类型枚举
	 * @author lxd
	 * 
	 */
	public enum ClientType {
		IOS(1), Android(2), Wechat(3);
		private final int value;

		public int getClientType() {
			return value;
		}

		ClientType(int value) {
			this.value = value;
		}

	}
}
