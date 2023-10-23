package vazkii.quark.base.module.config;

public interface IConfigCallback {

	void push(String s, String comment, Object holderObject);
	void pop();

	final class Dummy implements IConfigCallback {

		@Override
		public void push(String s, String comment, Object holderObject) {
			// NO-OP
		}

		@Override
		public void pop() {
			// NO-OP
		}

	}

}
