package org.example.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData {
	private int intValue;

	@Override
	public String toString() {
		return "ResponseData{" +
			"intValue=" + intValue +
			'}';
	}
}
