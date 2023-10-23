package vazkii.quark.base.module.config;

import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class QuarkConfigBuilder implements IConfigBuilder {

	private final ForgeConfigSpec.Builder parent;
	private final IConfigCallback callback;
	
	private final Stack<String> layers = new Stack<>(); //Only for error reporting
	private String currComment = "";
	
	public QuarkConfigBuilder(ForgeConfigSpec.Builder parent, IConfigCallback callback) {
		this.parent = parent;
		this.callback = callback;
	}

	@Override
	public <T> ForgeConfigSpec configure(Function<IConfigBuilder, T> func) {
		return parent.configure(b -> func.apply(this)).getRight();
	}
	
	@Override
	public void push(String s, Object holderObject) {
		layers.push(s);
		parent.push(s);
		callback.push(s, currComment, holderObject);
		currComment = "";
	}

	@Override
	public void pop() {
		layers.pop();
		parent.pop();
		callback.pop();
	}

	@Override
	public void comment(String s) {
		currComment += s;
	}

	@Override
	public <T> ConfigValue<List<? extends T>> defineList(String name, List<? extends T> default_, Supplier<List<? extends T>> getter, Predicate<Object> predicate) {
		beforeDefine();
		ConfigValue<List<? extends T>> value = parent.defineList(name, default_, predicate);
		onDefine(value, default_, getter, predicate);
		return value;
	}

	@Override
	public <T> ConfigValue<T> defineObj(String name, T default_, Supplier<T> getter, Predicate<Object> predicate) {
		if(default_ == null)
			throw new RuntimeException("Can't define object " + name + " with null default @ " + layers.toString());
		
		beforeDefine();
		ConfigValue<T> value = parent.define(name, default_, predicate);
		onDefine(value, default_, getter, predicate);
		return value;
	}

	private void beforeDefine() {
		if(!currComment.isEmpty())
			parent.comment(currComment);
	}

	private <T> void onDefine(ConfigValue<T> value, T default_, Supplier<T> getter, Predicate<Object> predicate) {
		callback.addEntry(value, default_, getter, currComment, predicate);
		currComment = "";
	}

}
