package com.tj.cache;

/**
 * cas result
 */
public class CASResult <T>{
	private final long cas;
	private final T value;
	
	public CASResult(final long cas,final T value){
		this.cas = cas;
		this.value = value;
	}
	
	public long getCas() {
		return this.cas;
	}

	public T getValue() {
		return this.value;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.cas ^ (this.cas >>> 32));
		result = prime * result
				+ ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CASResult<?> other = (CASResult<?>) obj;
		if (this.cas != other.cas) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Result[cas=" + this.cas + ",value="
				+ this.value + "]";
	}
}
