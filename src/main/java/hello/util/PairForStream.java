package hello.util;

import java.util.Objects;

public class PairForStream<F, S> {
	
	private F first;
	
	private S second;
	
	public PairForStream(F one, S two) {
		this.first = one;
		this.second = two;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	};

	public F getFirst() {
		return first;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public S getSecond() {
		return second;
	}

	public void setSecond(S second) {
		this.second = second;
	}

}

