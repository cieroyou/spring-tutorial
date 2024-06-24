package org.example.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
// @AllArgsConstructor
public class RequestData {
	private int intValue;
	private String stringValue;

	@Override
	public String toString() {
		return "RequestData{" +
			"intValue=" + intValue +
			", stringValue='" + stringValue + '\'' +
			'}';
	}
}
