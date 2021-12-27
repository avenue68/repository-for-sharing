package com.example.referenced;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public abstract class MyAbstractClass {
	private String value = "MyAbstractClass";
}
